package com.watsoo.dms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "command")
public class Command implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "device_model")
	private String devicModelNumber;

	@Column(name = "command")
	private String command;
	
	@Column(name = "base_command")
	private String baseCommand;
	
	@Column(name = "command_detalis")
	private String commandDetail;

	@Column(name = "description")
	private String description;

	@Column(name = "imei_number")
	private String imeiNumber;

	@Column(name = "vechile_id")
	private Long vechileId;

	@Column(name = "end_command")
	private String endCommand;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "vechile_number")
	private String vehicleNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDevicModelNumber() {
		return devicModelNumber;
	}

	public void setDevicModelNumber(String devicModelNumber) {
		this.devicModelNumber = devicModelNumber;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public Long getVechileId() {
		return vechileId;
	}

	public void setVechileId(Long vechileId) {
		this.vechileId = vechileId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEndCommand() {
		return endCommand;
	}

	public void setEndCommand(String endCommand) {
		this.endCommand = endCommand;
	}
	

	public String getBaseCommand() {
		return baseCommand;
	}

	public void setBaseCommand(String baseCommand) {
		this.baseCommand = baseCommand;
	}

	public String getCommandDetail() {
		return commandDetail;
	}

	public void setCommandDetail(String commandDetail) {
		this.commandDetail = commandDetail;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Command() {
		super();
	}

}
