package com.java.marketplace.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.AddressDTO;
import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.enums.AddressType;
import com.java.marketplace.repositories.AddressRepository;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	@Autowired
	private CustomerService customerService;

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @addressRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Address findById(Long id) {
		Optional<Address> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Address.class, id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @addressRepository.findById(#id).get().getCustomer().getUser().getEmail()")
	public Address getReferenceById(Long id) {
		Address obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(Address.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Address.class, id);
		}
	}

	private Address getReferenceByIdAddressAndCustomer(Long customer_id, Long address_id) {
		customerService.getReferenceById(customer_id);
		Address address = getReferenceById(address_id);
		return address;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#customer_id).get().getUser().getEmail()")
	public Address insert(Long customer_id, @Valid AddressDTO objDTO) {
//		throw exception if id not found
		Customer customer = customerService.getReferenceById(customer_id);

		Address obj = new Address();
		obj.setCustomer(customer);
		obj.setStreet(objDTO.getStreet());
		obj.setHouseNumber(objDTO.getHouseNumber());
		obj.setNeighborhood(objDTO.getNeighborhood());
		obj.setZipCode(objDTO.getZipCode());
		obj.setCountry(objDTO.getCountry());
		obj.setAddressType(objDTO.getAddressType());

		return repository.save(obj);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#customer_id).get().getUser().getEmail() && authentication.principal == @addressRepository.findById(#address_id).get().getCustomer().getUser().getEmail()")
	public void delete(Long customer_id, Long address_id) {
//		throw exception if id not found
		getReferenceByIdAddressAndCustomer(customer_id, address_id);
		repository.deleteById(address_id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#customer_id).get().getUser().getEmail() && authentication.principal == @addressRepository.findById(#address_id).get().getCustomer().getUser().getEmail()")
	public Address update(Long customer_id, Long address_id, AddressDTO obj) {
//		throw exception if id not found
		Address entity = getReferenceByIdAddressAndCustomer(customer_id, address_id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Address entity, AddressDTO obj) {
		entity.setStreet(obj.getStreet());
		entity.setHouseNumber(obj.getHouseNumber());
		entity.setNeighborhood(obj.getNeighborhood());
		entity.setZipCode(obj.getZipCode());
		entity.setCountry(obj.getCountry());
		entity.setAddressType(obj.getAddressType());
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#customer_id).get().getUser().getEmail() && authentication.principal == @addressRepository.findById(#address_id).get().getCustomer().getUser().getEmail()")
	public Address partialUpdate(Long customer_id, Long address_id, Map<String, Object> obj) {
//		throw exception if id not found
		Address entity = getReferenceByIdAddressAndCustomer(customer_id, address_id);
		
		partialUpdateData(entity, obj);
		return repository.save(entity);
	}
	
	private void partialUpdateData(Address entity, Map<String, Object> obj) {

		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			String field = entry.getKey();
			Object value = entry.getValue();

			if (field.equals("addressType")) {
				if (value instanceof String) {
					AddressType updatedAddressType = AddressType.valueOf((String) value);
					entity.setAddressType(updatedAddressType);
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
}
