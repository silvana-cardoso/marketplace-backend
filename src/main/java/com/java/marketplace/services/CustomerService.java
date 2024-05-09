package com.java.marketplace.services;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.AddressDTO;
import com.java.marketplace.dto.CustomerInsertDTO;
import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.User;
import com.java.marketplace.entities.enums.CustomerType;
import com.java.marketplace.repositories.AddressRepository;
import com.java.marketplace.repositories.CustomerRepository;
import com.java.marketplace.services.exceptions.GeneralPatchException;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;
import com.java.marketplace.services.exceptions.UserAlreadyHasACustomerException;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<Customer> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
	public Customer findById(Long id) {
		Optional<Customer> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(Customer.class, id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")
	public Customer getReferenceById(Long id) {
		Customer obj = repository.getReferenceById(id);
//		trhow exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(Customer.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Customer.class, id);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userService.findById(#objDTO.getUserId()).getEmail()")	
	public Customer insert(CustomerInsertDTO objDTO) {
//		throw exception if id not found
		User user = userService.getReferenceById(objDTO.getUserId());
//		throw exception if user already has an customer
		checkIfUserHasACustomer(objDTO.getUserId(), user);
		Customer obj = setFields(user, objDTO);
		// set address if any
		if (objDTO.getAddresses() != null) {
			obj = insertWithAddress(obj,objDTO.getAddresses());
		} else {
			obj = repository.save(obj);
		}
		return obj;
	}

	private void checkIfUserHasACustomer(Long user_id, User user) {
//		throw exception if user already has an customer
		Customer customerUser = user.getCustomer();
		if (customerUser != null) {
			if (!Objects.isNull(customerUser.getDeleted())) {
				throw new UserAlreadyHasACustomerException("User with id " + user_id + " already has an associated customer.");
			}
		}
	}
	
	private Customer setFields(User user, CustomerInsertDTO objDTO) {
		Customer obj = new Customer();
		obj.setCustomerName(objDTO.getCustomerName());
		obj.setDocumentNumber(objDTO.getDocumentNumber());
		obj.setCustomerStatus(objDTO.getCustomerStatus());
		obj.setCustomerType(objDTO.getCustomerType());
		obj.setCreditScore(objDTO.getCreditScore());
//	shared password between user and customer
		obj.setPassword(user.getPassword());
		obj.setUser(user);
		return obj;
	}

	private Customer insertWithAddress(Customer obj, Set<@Valid AddressDTO> addressesDTO) {
		Set<Address> addresses = new HashSet<>();
		Address address =  new Address();
		for (AddressDTO addressDTO : addressesDTO) {
			address.setStreet(addressDTO.getStreet());
			address.setHouseNumber(addressDTO.getHouseNumber());
			address.setNeighborhood(addressDTO.getNeighborhood());
			address.setZipCode(addressDTO.getZipCode());
			address.setCountry(addressDTO.getCountry());
			address.setAddressType(addressDTO.getAddressType());
			address.setCustomer(obj);
			addresses.add(address);
		}
		obj = repository.save(obj);
		addressRepository.saveAll(addresses);
		return obj;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")	
	public void delete(Long id) {
//		throw exception if id not found
		getReferenceById(id);
		repository.deleteById(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")	
	public Customer update(Long id, CustomerInsertDTO obj) {
		Customer entity = getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(Customer entity, CustomerInsertDTO obj) {
		entity.setCustomerName(obj.getCustomerName());
		entity.setDocumentNumber(obj.getDocumentNumber());
		entity.setCustomerStatus(obj.getCustomerStatus());
		entity.setCustomerType(obj.getCustomerType());
		entity.setCreditScore(obj.getCreditScore());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @customerRepository.findById(#id).get().getUser().getEmail()")	
	public Customer partialUpdate(Long id, Map<String, Object> obj) {
//		throw exception if id not found
		Customer entity = getReferenceById(id);
		partialUpdateData(entity, obj);
		return repository.save(entity);
	}

	private void partialUpdateData(Customer entity, Map<String, Object> obj) {

		for (Map.Entry<String, Object> entry : obj.entrySet()) {
			String field = entry.getKey();
			Object value = entry.getValue();

			if (field.equals("password")) {
				throw new GeneralPatchException("Password field modification is restricted");
			}
//			if enum
			if (field.equals("customerType")) {
				if (value instanceof String) {
					CustomerType updatedCustomerType = CustomerType.valueOf((String) value);
					entity.setCustomerType(updatedCustomerType);
				}
				else {
					throw new IllegalFieldException("Illegal customer type.");
				}
			} else if (value != null && !value.equals("")){
//				 apply the update to the corresponding field of the resource
				try {
					BeanUtils.setProperty(entity, field, value);
				} catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
					throw new IllegalFieldException("Illegal field: " + field);
				}
			} else {
				throw new IllegalFieldException("Field value is mandatory");
			}
		}
	}
}
