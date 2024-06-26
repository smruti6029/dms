package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.Driver;

public class DriverDto {

	private Long id;

	private String name;

	private String phoneNumber;

	private int age;

	private String dob;

	private Date updatedOn;

	private Boolean isActive;

	private String dlNumber;

	private String joinDate;

	private String imageUrl;

	private CategoryDto categoryDto;

	private Long totalEvent;

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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public DriverDto() {
		super();
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

	public String getDlNumber() {
		return dlNumber;
	}

	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CategoryDto getCategoryDto() {
		return categoryDto;
	}

	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}

	public Long getTotalEvent() {
		return totalEvent;
	}

	public void setTotalEvent(Long totalEvent) {
		this.totalEvent = totalEvent;
	}

	public static Driver convertDtoToEntity(DriverDto dto) {
		Driver driver = new Driver();
		driver.setName(dto.getName());
		driver.setPhoneNumber(dto.getPhoneNumber());
		driver.setAge(dto.getAge());
		driver.setDob(dto.getDob());
		return driver;
	}

	public static DriverDto convertEntityToDto(Driver driver) {
		DriverDto driverDto = new DriverDto();
		driverDto.setId(driver.getId());
		driverDto.setName(driver.getName());
		driverDto.setPhoneNumber(driver.getPhoneNumber());
		driverDto.setAge(driver.getAge());
		driverDto.setDob(driver.getDob());
		driverDto.setDlNumber(driver.getDlNumber());
		driverDto.setJoinDate(driver.getJoinDate());
		driverDto.setUpdatedOn(driver.getUpdatedOn());
		driverDto.setIsActive(driver.getIsActive());
		driverDto.setImageUrl(driver.getImageUrl());
		return driverDto;
	}

}
