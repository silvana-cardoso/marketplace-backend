package com.java.marketplace.services;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.marketplace.dto.UserInsertDTO;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.User;
import com.java.marketplace.repositories.UserRepository;
import com.java.marketplace.services.exceptions.GeneralPatchException;
import com.java.marketplace.services.exceptions.IllegalFieldException;
import com.java.marketplace.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService{
//	validate email format passed through a patch request
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository repository;

	/*
	 * security implementation
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User entity = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Non-Existent Login"));
		return org.springframework.security.core.userdetails.User.builder().username(entity.getEmail())
				.password(entity.getPassword()).roles(entity.getRole().getAuthority().toString()).build();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<User> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userRepository.findByEmail(#email).get().getEmail()")
	public User findByEmail(String email) {
		Optional<User> obj = repository.findByEmail(email);
		return obj.orElseThrow(() -> new ResourceNotFoundException(User.class, email));
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
	public User findById(Long id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(User.class, id));
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
	public User getReferenceById(Long id) {
		User obj = repository.getReferenceById(id);
//		throw exception if id not found or obj is deleted
		try {
			if (obj.getDeleted() == true) {
				throw new ResourceNotFoundException(User.class, id);
			}
			return obj;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(User.class, id);
		}
	}
	
	public User insert(UserInsertDTO obj) {
		User user = new User();
		user.setEmail(obj.getEmail());
		user.setPassword(obj.getPassword());
		user.setRole(roleService.findById((long) 2));
//		save user
		return repository.save(user);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') && authentication.principal == @userRepository.findById(1L).get().getEmail()")
	public User insertAdmin(UserInsertDTO obj) {
		User user = new User();
		user.setEmail(obj.getEmail());
		user.setPassword(obj.getPassword());
		user.setRole(roleService.findById((long) 1));
//		save user
		return repository.save(user);
	}

	@PreAuthorize("authentication.principal == @userRepository.findById(1L).get().getEmail() || authentication.principal == @userRepository.findById(#id).get().getEmail()")
	public void delete(Long id) {
		getReferenceById(id);
		repository.deleteById(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
	public User update(Long id, UserInsertDTO obj) {
		User entity = getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	private void updateData(User entity, UserInsertDTO obj) {
		entity.setEmail(obj.getEmail());
		entity.setPassword(obj.getPassword());
//		update customer password as well
		Customer customer = entity.getCustomer();
		if (customer != null) {
			customer.setPassword(obj.getPassword());
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') || authentication.principal == @userRepository.findById(#id).get().getEmail()")
	public User partialUpdate(Long id, Map<String, String> obj) {
		User entity = getReferenceById(id);
		partialUpdateData(entity, obj);
		return repository.save(entity);
	}

	private void partialUpdateData(User entity, Map<String, String> obj) {
		for (Map.Entry<String, String> entry : obj.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();

			// update email
			if (field.equals("email") && value != null) {
				isValidEmail(value);
				entity.setEmail(value);
				// update password
			} else if (field.equals("password") && value != null) {
				if (value.length() >= 8 && value.length() <= 16) {
					entity.setPassword(value);
//					update customer password as well
					Customer customer = entity.getCustomer();
					if (customer != null) {
						customer.setPassword(value);
					}
					
				} else {
					throw new IllegalFieldException(
							"Password must be equal to or greater than 8 characters and less than 16 characters");
				}
			} else {
				throw new IllegalFieldException(field + " value is mandatory");
			}
		}
	}

//	public User insert(Long role_id, User obj) {
//		obj.setId(null);
//		obj.setRole(roleService.findById(role_id));
////		save user
//		return repository.save(obj);
//	}

	private void isValidEmail(String email) {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalFieldException("Email must have a valid format");
		}
	}
}
