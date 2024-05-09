package com.java.marketplace.resources;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.java.marketplace.dto.RoleDTO;
import com.java.marketplace.dto.UserDTO;
import com.java.marketplace.entities.Role;
import com.java.marketplace.entities.User;
import com.java.marketplace.services.RoleService;

@RestController
@RequestMapping(value = "/roles")
public class RoleResource {
	
	@Autowired
	private RoleService service;
	
	@GetMapping
	public ResponseEntity<Page<RoleDTO>> findAll(
//			){
//		List<Role> list = service.findAll();
//		List<RoleDTO> listDto = list.stream().map(x -> new RoleDTO(x)).collect(Collectors.toList());
//		return ResponseEntity.ok().body(listDto);
			Pageable pageable,
			@RequestParam(required = false, defaultValue = "id") String sortField,
	        @RequestParam(required = false, defaultValue = "asc") String sortOrder){
	
		//sort
		Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortField);
		//pageable
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		//request
		Page<Role> list = service.findAll(pageable);
//		Page<ProductOfferingDTO> listDto = list.stream().map(x -> new ProductOfferingDTO(x)).collect(Collectors.toList());
		Page<RoleDTO> dtoPage = list.map(new Function<Role, RoleDTO>() {
		    @Override
		    public RoleDTO apply(Role entity) {
		    	RoleDTO dto = new RoleDTO(entity);
		        return dto;
		    }
		});
		return ResponseEntity.ok().body(dtoPage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> findById(@PathVariable Long id){
		Role obj = service.findById(id);
		RoleDTO objDTO = new RoleDTO(obj);
		return ResponseEntity.ok().body(objDTO);
	}
	
	@PostMapping
	public ResponseEntity<RoleDTO> insert(@RequestBody RoleDTO obj) {
		RoleDTO newObj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).body(newObj);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO obj) {
		RoleDTO newObj = service.update(id, obj);
		return ResponseEntity.ok().body(newObj);
	}
}
