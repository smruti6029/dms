package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.CommandSendTrail;

public class CommandSendTrailDto {

	private Long id;

	private UserDto userDto;

	private String command;

	private Long vechileId;

	private Date createdOn;


	private String useCommand;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Long getVechileId() {
		return vechileId;
	}

	public void setVechileId(Long vechileId) {
		this.vechileId = vechileId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	public String getUseCommand() {
		return useCommand;
	}

	public void setUseCommand(String useCommand) {
		this.useCommand = useCommand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CommandSendTrailDto() {

	}

	public static CommandSendTrailDto entityToDto(CommandSendTrail commandSendTrail) {

		CommandSendTrailDto obj = new CommandSendTrailDto();
		obj.setId(commandSendTrail.getId());
		obj.setCreatedOn(commandSendTrail.getCreatedOn());
		obj.setVechileId(commandSendTrail.getVechileId());
		obj.setUseCommand(commandSendTrail.getUseCommand());
		obj.setDescription(commandSendTrail.getDescription());
		obj.setCommand(commandSendTrail.getCommand());
		return obj;

	}

}
