package com.watsoo.dms.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LoginRequest {

	@NotNull(message = "username can not be blank")
	@NotBlank(message = "username can not be blank")
	@Email
	private String email;

	@NotNull(message = "password can not be blank")
	@NotBlank(message = "password can not be blank")
	private String password;

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
