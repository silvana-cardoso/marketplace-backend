package com.java.marketplace.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.ProductOfferingDTO;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.OrderItem;
import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.entities.enums.POState;
import com.java.marketplace.repositories.ProductOfferingRepository;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class ProductOfferingService {

	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private ProductOfferingRepository repository;

	public Page<ProductOffering> findAll(Pageable pageable) {
		
		return repository.findAll(pageable);
	}

	//@PreAuthorize("hasRole('ROLE_ADMIN')"
	public ProductOffering findById(Long id) {
		Optional<ProductOffering> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Order.class, id));
	}
	
	public ProductOffering findByIdShort(Long id) {
		Optional<ProductOffering> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Order.class, id));
	}

	protected ProductOffering getReferenceById(Long id) {
		ProductOffering obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(ProductOffering.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(ProductOffering.class, id);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ProductOffering insert(ProductOfferingDTO obj) {
		ProductOffering entity = new ProductOffering();
		entity.setProductName(obj.getProductName());
		entity.setUnitPrice(obj.getUnitPrice());
		entity.setSellIndicator(obj.getSellIndicator());
		entity.setStock(obj.getStock());
		entity.setState(obj.getState());
		return repository.save(entity);
		}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Long id) {
//		throw exception if id not found
		ProductOffering product = getReferenceById(id);
		deleteOrderItems(product);
		repository.deleteById(id);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ProductOffering update(Long id, ProductOfferingDTO obj) {
		ProductOffering entity = getReferenceById(id);
		updateData(entity, obj);
		entity = repository.save(entity);
		if (!entity.getSellIndicator()) {
//			if product unavailable, delete order items
			deleteOrderItems(entity);
		}
		return entity;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ProductOffering partialUpdate(Long id, Map<String, Object> obj) {
//		throw exception if id not found
		ProductOffering entity = getReferenceById(id);
		partialUpdateData(entity, obj);
		entity = repository.save(entity);
		if (!entity.getSellIndicator()) {
//			if product unavailable, delete order items
			deleteOrderItems(entity);
		}
		return entity;
	}

	private void updateData(ProductOffering entity, ProductOfferingDTO obj) {
		entity.setProductName(obj.getProductName());
		entity.setUnitPrice(obj.getUnitPrice());
		entity.setSellIndicator(obj.getSellIndicator());
		entity.setStock(obj.getStock());
		entity.setState(obj.getState());
	}
	
	private void partialUpdateData(ProductOffering entity, Map<String, Object> obj) {

		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			String field = entry.getKey();
			Object value = entry.getValue();

			if (field.equals("state")) {
				if (value instanceof String) {
					POState updatedPOState = POState.valueOf((String) value);
					entity.setState(updatedPOState);
				}
			} else if (value != null && !value.equals("")){
				// Apply the update to the corresponding field of the resource
				try {
					BeanUtils.setProperty(entity, field, value);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// Handle any exception occurred while setting the property
					throw new IllegalFieldException("Illegal field: " + field);
				}
			} else {
				throw new IllegalFieldException("Field value is mandatory");
			}
		}
	}
	
	private void deleteOrderItems(ProductOffering entity) {
		Set<OrderItem> orderItems = entity.getorderItems();
//		delete order items
		for (OrderItem x : orderItems) {
			orderItemService.delete(x.getId());
		}
	}
}
