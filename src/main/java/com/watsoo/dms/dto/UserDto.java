package com.watsoo.dms.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.watsoo.dms.entity.User;

public class UserDto {
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String role;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdOn;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updatedOn;
	private Boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static User convertDtoToEntity(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
		return user;
	}

	public User convertDtoToEntity() {
		User user = new User();
		user.setId(this.id);
		user.setName(this.name);
		user.setEmail(this.email);
		user.setPhone(this.phone);
		user.setCreatedOn(this.createdOn);
		user.setUpdatedOn(this.updatedOn);
		user.setIsActive(this.isActive);
		return user;
	}

	public static UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setPhone(user.getPhone());
		userDto.setCreatedOn(user.getCreatedOn());
		userDto.setUpdatedOn(user.getUpdatedOn());
		userDto.setIsActive(user.getIsActive());
		userDto.setRole(user.getRole().name());
		return userDto;
	}

}
