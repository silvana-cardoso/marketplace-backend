package com.java.marketplace.resources;

import java.net.URI;
import java.util.Arrays;
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
import com.java.marketplace.dto.UserDTO;
import com.java.marketplace.dto.UserFindByIdDTO;
import com.java.marketplace.dto.UserInsertDTO;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.User;
import com.java.marketplace.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(
//			){
//		List<User> list = service.findAll();
//		List<UserDTO> listDto = list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
//		return ResponseEntity.ok().body(listDto);
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "id") String sortField,
	        @RequestParam(required = false, defaultValue = "asc") String sortOrder){
		
		Sort.Order firstField = new Sort.Order(Sort.Direction.fromString("asc"), "role");
    	Sort.Order lastField = new Sort.Order(Sort.Direction.fromString(sortOrder), sortField);
		//sort
		Sort sort = Sort.by(firstField, lastField);
		//pageable
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		//request
		Page<User> list = service.findAll(pageable);
//		Page<ProductOfferingDTO> listDto = list.stream().map(x -> new ProductOfferingDTO(x)).collect(Collectors.toList());
		Page<UserDTO> dtoPage = list.map(new Function<User, UserDTO>() {
		    @Override
		    public UserDTO apply(User entity) {
		    	UserDTO dto = new UserDTO(entity);
		        return dto;
		    }
		});
		return ResponseEntity.ok().body(dtoPage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserFindByIdDTO> findById(@PathVariable Long id){
		User obj = service.findById(id);
		UserFindByIdDTO objDto = new UserFindByIdDTO(obj);
		return ResponseEntity.ok().body(objDto);
	}
	
	@GetMapping(value = "/login/{email}")
	public ResponseEntity<UserFindByIdDTO> findByEmail(@PathVariable String email){
		User obj = service.findByEmail(email);
//		List<Long> ids = Arrays.asList(obj.getId(), obj.getCustomer().getId());
		UserFindByIdDTO objDto = new UserFindByIdDTO(obj);
		return ResponseEntity.ok().body(objDto);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO obj) {
//		every user created through /users is set as Admin
		UserDTO objDTO = new UserDTO(service.insertAdmin(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserInsertDTO obj) {
		UserDTO objDTO = new UserDTO(service.update(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<UserDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, String> obj) {
		UserDTO objDTO = new UserDTO(service.partialUpdate(id, obj));
		return ResponseEntity.ok().body(objDTO);
	}
}
