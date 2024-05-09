package com.java.marketplace.services;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.OrderItemDTO;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.OrderItem;
import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.entities.enums.OrderStatus;
import com.java.marketplace.entities.pk.OrderItemPK;
import com.java.marketplace.repositories.OrderItemRepository;
import com.java.marketplace.repositories.ProductOfferingRepository;
import com.java.marketplace.services.exceptions.GeneralIllegalStateException;
import com.java.marketplace.services.exceptions.GeneralPatchException;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.OrderAlreadyClosedException;
import com.java.marketplace.services.exceptions.OrderStatusException;
import com.java.marketplace.services.exceptions.ProductCurrentlyUnavailableForSaleException;
import com.java.marketplace.services.exceptions.ProductOutOfStockException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class OrderItemService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductOfferingRepository productOfferingRepository;

	@Autowired
	private OrderItemRepository repository;

	public List<OrderItem> findAll() {
		return repository.findAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderItemRepository.findById(#id).get().getOrder().getCustomer().getUser().getEmail()")
	public OrderItem findById(OrderItemPK id) {
		Optional<OrderItem> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(OrderItem.class, id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @orderItemRepository.findById(#id).get().getOrder().getCustomer().getUser().getEmail()")
	public OrderItem getReferenceById(OrderItemPK id) {
		OrderItem obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(OrderItem.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(OrderItem.class, id);
		}
	}

	private OrderItemPK createOrderItemId(Long order_id, Long product_offering_id) {
//		throw exception if id not found
		Order order = orderService.findById(order_id);
		ProductOffering productOffering = findProductById(product_offering_id);
		OrderItemPK id = new OrderItemPK();
		id.setOrder(order);
		id.setProductOffering(productOffering);
		return id;
	}

	public OrderItem insert(Long order_id, Long product_offering_id, OrderItemDTO objDTO) {
		OrderItem obj = new OrderItem();
		obj.setDiscount(objDTO.getDiscount());
		obj.setQuantity(objDTO.getQuantity());
		checkProductAvailability(product_offering_id, obj);
		obj.setTotalPriceItem();
		checkOrderStatus(order_id, obj);
		OrderItem entity = repository.save(obj);
		return entity;
	}

	public void delete(Long order_id, Long product_offering_id) {
		OrderItemPK id = createOrderItemId(order_id, product_offering_id);
		deleteData(id);
	}
	
	public void delete(OrderItemPK id) {
		deleteData(id);
	}

	private void deleteData(OrderItemPK id) {
		getReferenceById(id);
		if (id.getOrder().getOrderStatus() == OrderStatus.OPEN) {
//			delete order as well
			if (id.getOrder().getProductOfferings().size() == 1) {
				orderService.delete(id.getOrder().getId());
			}
//			delete just the item
			else {
				repository.deleteById(id);
			}
		} else {
			throw new OrderStatusException("Items can only be deleted from an order if the order is in an open state.");
		}
	}

	public OrderItem update(Long order_id, Long product_offering_id, OrderItemDTO objDTO) {
		OrderItemPK id = createOrderItemId(order_id, product_offering_id);
		OrderItem entity = repository.getReferenceById(id);
		checkOrderStatus(order_id);
		checkProductAvailability(product_offering_id, objDTO);
		updateData(entity, objDTO);
		entity.setTotalPriceItem();
		entity = repository.save(entity);
		return entity;
	}

	private void updateData(OrderItem entity, OrderItemDTO objDTO) {
		entity.setDiscount(objDTO.getDiscount());
		entity.setQuantity(objDTO.getQuantity());
	}

	public OrderItem partialUpdate(Long order_id, Long product_offering_id, Map<String, Object> obj) {
		OrderItemPK id = createOrderItemId(order_id, product_offering_id);
		OrderItem entity = getReferenceById(id);
		checkOrderStatus(order_id, entity);
		checkProductAvailability(product_offering_id, entity);
		partialUpdateData(entity, obj);
		entity.setTotalPriceItem();
		repository.save(entity);
		return entity;
	}

	private void partialUpdateData(OrderItem entity, Map<String, Object> obj) {
		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			String field = entry.getKey();
			Object value = entry.getValue();

			if (field.equals("totalPrice")) {
				throw new GeneralPatchException("Modifying the totalPrice field is not allowed.");
			} else if (value != null && !value.equals("")) {
//				 apply the update to the corresponding field of the resource
				try {
					BeanUtils.setProperty(entity, field, value);
				} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
					throw new GeneralIllegalStateException("Illegal field.");
				}
			} else {
				throw new IllegalFieldException("Field value is mandatory");
			}
		}
	}

	private Order checkOrderStatus(Long order_id, OrderItem obj) {
//		throw exception if id not found
		Order order = orderService.findById(order_id);
		// check if order is still open
		if (order.getOrderStatus() == OrderStatus.OPEN) {
			obj.setOrder(order);
			return order;
		} else {
			throw new OrderAlreadyClosedException();
		}
	}

	private Order checkOrderStatus(Long order_id) {
//		throw exception if id not found
		Order order = orderService.findById(order_id);
		// check if order is still open
		if (order.getOrderStatus() == OrderStatus.OPEN) {
			return order;
		} else {
			throw new OrderAlreadyClosedException();
		}
	}

	private void checkProductAvailability(Long product_offering_id, OrderItem obj) {
//		throw exception if id not found
		ProductOffering productOffering = findProductById(product_offering_id);
		// check if productOffering is available and in stock
		if (productOffering.getSellIndicator()) {
			if (obj.getQuantity() <= productOffering.getStock())
				obj.setProductOffering(productOffering);
			else {
				throw new ProductOutOfStockException();
			}
		} else {
			throw new ProductCurrentlyUnavailableForSaleException();
		}
	}

	private Boolean checkProductAvailability(Long product_offering_id, OrderItemDTO obj) {
//		throw exception if id not found
		ProductOffering productOffering = findProductById(product_offering_id);
		// check if productOffering is available and in stock
		if (productOffering.getSellIndicator()) {
			if (obj.getQuantity() <= productOffering.getStock())
				return true;
			else {
				throw new ProductOutOfStockException();
			}
		} else {
			throw new ProductCurrentlyUnavailableForSaleException();
		}
	}

	private ProductOffering findProductById(Long id) {
		Optional<ProductOffering> obj = productOfferingRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Order.class, id));
	}
}
