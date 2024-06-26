package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.Command;

public class CommandDto {
	private Long id;
	private String deviceModelNumber;
	private String baseCommand;
	private String description;
	private Long vechile_id;
	private String imeiNumber;
	private String endCommand;
	private String commandDetalis;
	private String command;
	private String vechileNumber;

	private Date createdOn;

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceModelNumber() {
		return deviceModelNumber;
	}

	public void setDeviceModelNumber(String deviceModelNumber) {
		this.deviceModelNumber = deviceModelNumber;
	}

	public String getBaseCommand() {
		return baseCommand;
	}

	public void setBaseCommand(String baseCommand) {
		this.baseCommand = baseCommand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVechile_id() {
		return vechile_id;
	}

	public void setVechile_id(Long vechile_id) {
		this.vechile_id = vechile_id;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public String getEndCommand() {
		return endCommand;
	}

	public void setEndCommand(String endCommand) {
		this.endCommand = endCommand;
	}

	public String getCommandDetalis() {
		return commandDetalis;
	}

	public void setCommandDetalis(String commandDetalis) {
		this.commandDetalis = commandDetalis;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getVechileNumber() {
		return vechileNumber;
	}

	public void setVechileNumber(String vechileNumber) {
		this.vechileNumber = vechileNumber;
	}

	public static CommandDto entityToDto(Command command) {
		CommandDto dto = new CommandDto();
		dto.setId(command.getId());
		dto.setDeviceModelNumber(command.getDevicModelNumber());
		dto.setDescription(command.getDescription());
		dto.setCommandDetalis(command.getCommandDetail());
		dto.setBaseCommand(command.getBaseCommand());
		dto.setCommand(command.getCommand());
		dto.setCreatedOn(command.getCreatedOn());
		dto.setVechile_id(command.getVechileId());
		dto.setEndCommand(command.getEndCommand());
		return dto;
	}

	public static Command dtoToEntity(CommandDto dto) {
		Command command = new Command();
		command.setId(dto.getId());
		command.setDevicModelNumber(dto.getDeviceModelNumber());
		command.setDescription(dto.getDescription());
		command.setImeiNumber(dto.getImeiNumber());
		command.setVechileId(dto.getVechile_id());
		command.setCommandDetail(dto.getCommandDetalis());
		command.setCommand(dto.getCommand());
		command.setBaseCommand(dto.getBaseCommand());
		command.setEndCommand(dto.getEndCommand());
		command.setVehicleNumber(dto.getVechileNumber());
		return command;
	}
}
