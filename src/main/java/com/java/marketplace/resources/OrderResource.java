package com.java.marketplace.resources;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.java.marketplace.dto.OrderDTO;
import com.java.marketplace.dto.OrderFindByIdDTO;
import com.java.marketplace.dto.OrderInsertDTO;
import com.java.marketplace.dto.OrderItemDTO;
import com.java.marketplace.dto.ProductOfferingDTO;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.services.OrderItemService;
import com.java.marketplace.services.OrderService;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

	@Autowired
	private OrderService service;

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping
	public ResponseEntity<Page<OrderDTO>> findAll(
//		List<Order> list = service.findAll();
//		List<OrderDTO> listDto = list.stream().map(x -> new OrderDTO(x)).collect(Collectors.toList());
//		return ResponseEntity.ok().body(listDto);
		
		Pageable pageable,
		@RequestParam(required = false, defaultValue = "date") String sortField,
        @RequestParam(required = false, defaultValue = "desc") String sortOrder){
	//sort
	Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
	//pageable
	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	//request
	Page<Order> list = service.findAll(pageable);
//	Page<ProductOfferingDTO> listDto = list.stream().map(x -> new ProductOfferingDTO(x)).collect(Collectors.toList());
	Page<OrderDTO> dtoPage = list.map(new Function<Order, OrderDTO>() {
	    @Override
	    public OrderDTO apply(Order entity) {
	    	OrderDTO dto = new OrderDTO(entity);
	        return dto;
	    }
	});
	return ResponseEntity.ok().body(dtoPage);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderFindByIdDTO> findById(@PathVariable Long id) {
		OrderFindByIdDTO objDTO = new OrderFindByIdDTO(service.findById(id));
		return ResponseEntity.ok().body(objDTO);
	}

//	create new order
	@PostMapping
	public ResponseEntity<OrderDTO> insert(@RequestBody OrderInsertDTO obj) {
		OrderDTO objDTO = new OrderDTO(service.insert(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO obj) {
		OrderDTO objDTO = new OrderDTO(service.update(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> obj) {
		OrderDTO objDTO = new OrderDTO(service.partialUpdate(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}

	// set address delivery
	@PatchMapping(value = "/{order_id}/deliveryAddress/{address_id}")
	public ResponseEntity<OrderFindByIdDTO> addDeliveryAddress(@PathVariable Long order_id,
			@PathVariable Long address_id) {
		OrderFindByIdDTO objDTO = new OrderFindByIdDTO(service.addDeliveryAdrress(order_id, address_id));
		return ResponseEntity.ok().body(objDTO);
	}

//	create OrderItem for a existing order
	@PostMapping(value = "/{order_id}/productOfferings/{product_offering_id}")
	public ResponseEntity<OrderItemDTO> insert(@PathVariable Long order_id, @PathVariable Long product_offering_id,
			@Valid @RequestBody OrderItemDTO obj) {
		OrderItemDTO objDTO = new OrderItemDTO(orderItemService.insert(order_id, product_offering_id, obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{product_offering_id}")
				.buildAndExpand(product_offering_id).toUri();
		return ResponseEntity.created(uri).body(objDTO);

	}
	
	@DeleteMapping(value = "/{order_id}/productOfferings/{product_offering_id}")
	public ResponseEntity<Void> delete(@PathVariable Long order_id, @PathVariable Long product_offering_id) {
		orderItemService.delete(order_id, product_offering_id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{order_id}/productOfferings/{product_offering_id}")
	public ResponseEntity<OrderItemDTO> update(@PathVariable Long order_id, @PathVariable Long product_offering_id,
			@Valid @RequestBody OrderItemDTO obj) {
		OrderItemDTO objDTO = new OrderItemDTO(orderItemService.update(order_id, product_offering_id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
	
	@PatchMapping(value = "/{order_id}/productOfferings/{product_offering_id}")
	public ResponseEntity<OrderItemDTO> partialUpdate(@PathVariable Long order_id, @PathVariable Long product_offering_id,
			@RequestBody Map<String, Object> obj) {
		OrderItemDTO objDTO = new OrderItemDTO(orderItemService.partialUpdate(order_id, product_offering_id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
}
