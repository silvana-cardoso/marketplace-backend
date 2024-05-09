package com.java.marketplace.services;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.OrderDTO;
import com.java.marketplace.dto.OrderInsertDTO;
import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.OrderItem;
import com.java.marketplace.entities.enums.OrderStatus;
import com.java.marketplace.repositories.OrderRepository;
import com.java.marketplace.repositories.ProductOfferingRepository;
import com.java.marketplace.services.exceptions.GeneralIllegalStateException;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.OrderStatusException;
import com.java.marketplace.services.exceptions.ProductOutOfStockException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private OrderRepository repository;

	@Autowired
	private ProductOfferingRepository productOfferingRepository;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<Order> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Order findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Order.class, id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Order getReferenceById(Long id) {
		Order obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(Order.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Order.class, id);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#objDTO.getCustomerId()).get().getUser().getEmail()")
	public Order insert(OrderInsertDTO objDTO) {
//		throw exception if id not found
		Customer customer = customerService.findById(objDTO.getCustomerId());
		Order obj = new Order();
		obj.setOrderStatus(OrderStatus.OPEN);
		obj.setCustomer(customer);
		if (objDTO.getDate() == null) {
			obj.setDate(Instant.now());
		}
		else {
			obj.setDate(objDTO.getDate());
		}
		obj.setTotal(0.0);
		return repository.save(obj);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public void delete(Long id) {
//		throw exception if id not found
		Order entity = getReferenceById(id);
		updateOrderTotalPrice(entity);
		repository.deleteById(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Order update(Long id, OrderDTO objDTO) {
		Order entity = repository.getReferenceById(id);
		if (entity.getOrderStatus() != OrderStatus.OPEN) {
			throw new OrderStatusException(
					"The put request for the order is not available due to its current status.");
		}
		else {
			updateData(entity, objDTO);
		}
		return repository.save(entity);
	}

	private void updateData(Order entity, OrderDTO objDTO) {
		entity.setDate(objDTO.getDate());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Order partialUpdate(Long id, Map<String, Object> obj) {
		Order entity = repository.getReferenceById(id);
//		if (entity.getOrderStatus() != OrderStatus.OPEN) {
		if (entity.getOrderStatus() == OrderStatus.CANCELED) {
			throw new OrderStatusException(
					"The patch request for the order is not available due to its current status.");
		} else {
			partialUpdateData(entity, obj);
		}
		return repository.save(entity);
	}

	private void partialUpdateData(Order entity, Map<String, Object> obj) {
		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			String field = entry.getKey();
			Object value = entry.getValue();

			if (field.equals("orderStatus")) {
				updateOrderStatus(entity, value);
			} else if (value != null && !value.equals("")){
//			 apply the update to the corresponding field of the resource
				if (field.equals("date")) {
					entity.setDate(Instant.parse((String)value));
				}else {
					try {
						BeanUtils.setProperty(entity, field, value);
					} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
						throw new GeneralIllegalStateException("Illegal field.");
					}
				}
			} else {
				throw new IllegalFieldException("Field value is mandatory");
			}
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderRepository.findById(#order_id).get().getCustomer().getUser().getEmail() && authentication.principal == @addressRepository.findById(#address_id).get().getCustomer().getUser().getEmail()")
	public Order addDeliveryAdrress(Long order_id, Long address_id) {
		Address deliveryAddress = addressService.findById(address_id);
		Order entity = getReferenceById(order_id);
		if (entity.getOrderStatus() != OrderStatus.OPEN) {
			throw new OrderStatusException(
					"The put request for the order is not available due to its current status.");
		} else {
			entity.setDeliveryAddress(deliveryAddress);
		}
		return repository.save(entity);
	}

	private void updateOrderStatus(Order entity, Object value) {
//			reOPEN an order
		if (value.equals("OPEN") && entity.getOrderStatus() == OrderStatus.CLOSED) {
			entity.setOrderStatus(OrderStatus.OPEN);
			updateStock(entity);
			updateOrderTotalPrice(entity);
		} else if (value.equals("OPEN") && entity.getOrderStatus() != OrderStatus.CLOSED) {
			throw new OrderStatusException(
					"The opening operation for the order is not available due to its current status.");
//			close an order
		} else if (value.equals("CLOSED") && !entity.getProductOfferings().isEmpty()
				&& entity.getDeliveryAddress() != null && entity.getOrderStatus() == OrderStatus.OPEN) {
			entity.setOrderStatus(OrderStatus.CLOSED);
			updateStock(entity);
			updateOrderTotalPrice(entity);
		} else if (value.equals("CLOSED") && (entity.getProductOfferings().isEmpty()
				|| entity.getDeliveryAddress() == null || entity.getOrderStatus() != OrderStatus.OPEN)) {
			throw new OrderStatusException(
					"The order is either empty or lacks an associated delivery address or is not open.");
//			cancel an order
		} else if (value.equals("CANCELED") && entity.getOrderStatus() == OrderStatus.CLOSED) {
			entity.setOrderStatus(OrderStatus.CANCELED);
			updateStock(entity);
			updateOrderTotalPrice(entity);
		} else if (value.equals("CANCELED") && entity.getOrderStatus() != OrderStatus.CLOSED) {
			throw new OrderStatusException("Cancellation is only applicable to closed orders.");
		} else {
			throw new IllegalFieldException("Illegal order status.");

		}
	}

	private void updateStock(Order entity) {
		Integer updatedStock;
		for (OrderItem x : entity.getProductOfferings()) {
			if (entity.getOrderStatus() == OrderStatus.OPEN || entity.getOrderStatus() == OrderStatus.CANCELED) {
				updatedStock = x.getProductOffering().getStock() + x.getQuantity();
			}
			else {
				updatedStock = x.getProductOffering().getStock() - x.getQuantity();
			}
//			turn the product unavailable after closing the order
			if (updatedStock == 0) {
				x.getProductOffering().setSellIndicator(false);
			}
			if (updatedStock >= 0) {
				x.getProductOffering().setStock(updatedStock);
				productOfferingRepository.save(x.getProductOffering());
			}
			else {
				entity.setOrderStatus(OrderStatus.OPEN);
				throw new ProductOutOfStockException();
			}
		}
	}
	
	private void updateOrderTotalPrice(Order entity) {
		Double total = 0.0;
		for (OrderItem x : entity.getProductOfferings()) {
			total = total + x.getTotalPriceItem();
		}
		entity.setTotal(total);
	}
}