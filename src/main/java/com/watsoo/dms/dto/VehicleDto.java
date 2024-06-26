package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.Vehicle;

public class VehicleDto {
	private Long id;
	private String name;
	private String chassisNumber;
	private String color;
	private String engineNumber;
	private String vehicleNumber;
	private Integer deviceId;
	private Date createdOn;
	private String imeiNo;
	private String model;

	private Date updatedOn;

	private Boolean isActive;

	// Getters and Setters
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

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
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

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public static VehicleDto fromEntity(Vehicle vehicle) {
		VehicleDto dto = new VehicleDto();
		dto.setId(vehicle.getId());
		dto.setName(vehicle.getName());
		dto.setChassisNumber(vehicle.getChassisNumber());
		dto.setColor(vehicle.getColor());
		dto.setEngineNumber(vehicle.getEngineNumber());
		dto.setVehicleNumber(vehicle.getVehicleNumber());
		dto.setDeviceId(vehicle.getDeviceId());
		dto.setCreatedOn(vehicle.getCreatedOn());
		dto.setUpdatedOn(vehicle.getUpdatedOn());
		dto.setIsActive(vehicle.getIsActive());
		dto.setImeiNo(vehicle.getImeiNo());
		dto.setModel(vehicle.getModel());
		return dto;
	}
}
