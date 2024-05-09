package com.java.marketplace.resources;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

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

import com.java.marketplace.dto.AddressDTO;
import com.java.marketplace.dto.CustomerDTO;
import com.java.marketplace.dto.CustomerFindByIdDTO;
import com.java.marketplace.dto.CustomerInsertDTO;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.services.AddressService;
import com.java.marketplace.services.CustomerService;

@RestController
@RequestMapping(value = "/customers")
public class CustomerResource {

	@Autowired
	private CustomerService service;

	@Autowired
	private AddressService addressService;

	@GetMapping
	public ResponseEntity<Page<CustomerDTO>> findAll(
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "id") String sortField,
	        @RequestParam(required = false, defaultValue = "asc") String sortOrder){
//		List<Customer> list = service.findAll();
//		List<CustomerDTO> listDto = list.stream().map(x -> new CustomerDTO(x)).collect(Collectors.toList());
//		return ResponseEntity.ok().body(listDto);
		
        Sort.Order firstField = new Sort.Order(Sort.Direction.fromString("desc"), "customerStatus");
    	Sort.Order lastField = new Sort.Order(Sort.Direction.fromString(sortOrder), sortField);
		//sort 
//	    		Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
		Sort sort = Sort.by(firstField, lastField);
		//pageable
    	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    	//request
		Page<Customer> list = service.findAll(pageable);
		Page<CustomerDTO> dtoPage = list.map(new Function<Customer, CustomerDTO>() {
		    @Override
		    public CustomerDTO apply(Customer entity) {
		    	CustomerDTO dto = new CustomerDTO(entity);
		        return dto;
		    }
		});
		return ResponseEntity.ok().body(dtoPage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerFindByIdDTO> findById(@PathVariable Long id) {
		CustomerFindByIdDTO obj = new CustomerFindByIdDTO(service.findById(id));
		return ResponseEntity.ok().body(obj);
	}
	
//	create new customer
	@PostMapping
	public ResponseEntity<CustomerDTO> insert(@Valid @RequestBody CustomerInsertDTO obj) {
		CustomerDTO objDTO = new CustomerDTO(service.insert(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerInsertDTO obj) {
		CustomerDTO objDTO = new CustomerDTO(service.update(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> partialUpdateAddress(@PathVariable Long id, @RequestBody Map<String, Object> obj) {
		CustomerDTO updatedObj = new CustomerDTO(service.partialUpdate(id, obj));
		return ResponseEntity.ok().body(updatedObj);
	}

//	get customer's addresses
	@GetMapping(value = "/{id}/addresses/{address_id}")
	public ResponseEntity<AddressDTO> findAddressById(@PathVariable Long address_id) {
		AddressDTO obj = new AddressDTO(addressService.findById(address_id));
		return ResponseEntity.ok().body(obj);
	}
	
//	insert new address to customer
	@PostMapping(value = "/{id}/addresses")
	public ResponseEntity<AddressDTO> insert(@PathVariable Long id, @Valid @RequestBody AddressDTO obj) {
		AddressDTO objDTO = new AddressDTO(addressService.insert(id, obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}/address").buildAndExpand(objDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}
	
//	delete address from customer
	@DeleteMapping(value = "/{id}/addresses/{address_id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable Long address_id) {
		addressService.delete(id, address_id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{id}/addresses/{address_id}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @PathVariable Long address_id,
			@Valid @RequestBody AddressDTO obj) {
		obj = new AddressDTO(addressService.update(id, address_id, obj));
		return ResponseEntity.ok().body(obj);
	}

	@PatchMapping(value = "/{id}/addresses/{address_id}")
	public ResponseEntity<AddressDTO> partialUpdateAddress(@PathVariable Long id, @PathVariable Long address_id,
			@RequestBody Map<String, Object> obj) {
		AddressDTO updatedObj = new AddressDTO(addressService.partialUpdate(id, address_id, obj));
		return ResponseEntity.ok().body(updatedObj);
	}
}
