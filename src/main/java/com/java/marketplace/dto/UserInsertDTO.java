package com.java.marketplace.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.java.marketplace.entities.User;

public class UserInsertDTO {

//	the constrained fields must be not null and their trimmed length must be greater than zero.
	@NotBlank(message = "Email is mandatory")
//	email is case insensitive: Aron@Aron.com is valid
//	and must have .
	@Email(message = "Email must have a valid format", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String email;
	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, max = 16, message = "Password must be equal to or greater than 8 characters and less than 16 characters")
	private String password;

	public UserInsertDTO() {

	}

	public UserInsertDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public UserInsertDTO(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
