package com.java.marketplace.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.RoleDTO;
import com.java.marketplace.entities.Role;
import com.java.marketplace.repositories.RoleRepository;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repository;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<Role> findAll(Pageable pageable){
		return repository.findAll(pageable);
	}
	
	public Role findById(Long id) {
		Optional<Role> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Role.class,id));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Role getReferenceById(Long id) {
		Role obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(Role.class, id);
			}
			return obj;
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Role.class, id);
		}	
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RoleDTO insert(RoleDTO obj) {
		Role entity = new Role();
		entity.setAuthority(obj.getAuthority());
		RoleDTO entityDTO = new RoleDTO(repository.save(entity));
		return entityDTO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RoleDTO update(Long id, RoleDTO obj) {
		try {
			Role entity = repository.getReferenceById(id);
			updateData(entity, obj);
			RoleDTO entityDTO = new RoleDTO(repository.save(entity));
			return entityDTO;
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Role.class, id);
		}
	}
	
	private void updateData(Role entity, RoleDTO obj) {
		entity.setAuthority(obj.getAuthority());
	}
}
