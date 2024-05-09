package com.java.marketplace.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.java.marketplace.dto.UserDTO;
import com.java.marketplace.dto.UserInsertDTO;
import com.java.marketplace.services.UserService;

@RestController
@RequestMapping(value = "/signup")
public class SignUpResource {
	
	@Autowired
	private UserService service;
	
//	create new user with operator role
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO obj) {
		UserDTO objDTO = new UserDTO(service.insert(obj));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(objDTO);
	}
}
