package com.watsoo.dms.dto;

import java.time.LocalDateTime;

import com.watsoo.dms.entity.CommandSendDetails;
import com.watsoo.dms.enums.CommandStatus;

public class CommanddetalisSendDto {

	private Long id;
	private Long deviceId;
	private String imeiNumber;
	private Long eventId;
	private String eventType;
	private Long positionId;
	private String evidenceFiles;
	private String baseCommand;
	private String command;
	
	private String useCommand;
	
	private String commandResponse;
	private String status; // assuming CommandStatus is an enum, we use String here
	private String vehicleStatus;
	private Integer noOfFileReq;
	private Integer noOfFileUploaded;
	private LocalDateTime createOn;
	private LocalDateTime reCallOn;
	private Integer reCallCount;
	private String endCommand;
	private Long vechileId;
	private String description;

	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getEvidenceFiles() {
		return evidenceFiles;
	}

	public void setEvidenceFiles(String evidenceFiles) {
		this.evidenceFiles = evidenceFiles;
	}

	public String getBaseCommand() {
		return baseCommand;
	}

	public void setBaseCommand(String baseCommand) {
		this.baseCommand = baseCommand;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommandResponse() {
		return commandResponse;
	}

	public void setCommandResponse(String commandResponse) {
		this.commandResponse = commandResponse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Integer getNoOfFileReq() {
		return noOfFileReq;
	}

	public void setNoOfFileReq(Integer noOfFileReq) {
		this.noOfFileReq = noOfFileReq;
	}

	public Integer getNoOfFileUploaded() {
		return noOfFileUploaded;
	}

	public void setNoOfFileUploaded(Integer noOfFileUploaded) {
		this.noOfFileUploaded = noOfFileUploaded;
	}

	public LocalDateTime getCreateOn() {
		return createOn;
	}

	public void setCreateOn(LocalDateTime createOn) {
		this.createOn = createOn;
	}

	public LocalDateTime getReCallOn() {
		return reCallOn;
	}

	public void setReCallOn(LocalDateTime reCallOn) {
		this.reCallOn = reCallOn;
	}

	public Integer getReCallCount() {
		return reCallCount;
	}

	public void setReCallCount(Integer reCallCount) {
		this.reCallCount = reCallCount;
	}

	public String getEndCommand() {
		return endCommand;
	}

	public void setEndCommand(String endCommand) {
		this.endCommand = endCommand;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVechileId() {
		return vechileId;
	}

	public void setVechileId(Long vechileId) {
		this.vechileId = vechileId;
	}

	public CommanddetalisSendDto() {
		super();
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

	public static CommanddetalisSendDto entityToDto(CommandSendDetails commandSendDetails) {
		CommanddetalisSendDto dto = new CommanddetalisSendDto();
		dto.setId(commandSendDetails.getId());
		dto.setDeviceId(commandSendDetails.getDeviceId());
		dto.setImeiNumber(commandSendDetails.getImeiNumber());
		dto.setEventId(commandSendDetails.getEventId());
		dto.setEventType(commandSendDetails.getEventType());
		dto.setPositionId(commandSendDetails.getPositionId());
		dto.setEvidenceFiles(commandSendDetails.getEvidenceFiles()); // Note the spelling change here
		dto.setBaseCommand(commandSendDetails.getBaseCommand());
		dto.setCommand(commandSendDetails.getCommand());
		dto.setCommandResponse(commandSendDetails.getCommandRespone()); // Note the spelling change here
//	        dto.setStatus(commandSendDetails.getStatus().name());
		dto.setVehicleStatus(commandSendDetails.getVehicleStatus());
		dto.setNoOfFileReq(commandSendDetails.getNoOfFileReq());
		dto.setNoOfFileUploaded(commandSendDetails.getNoOfFileUploaded());
		dto.setCreateOn(commandSendDetails.getCreateOn());
		dto.setReCallOn(commandSendDetails.getReCallOn());
		dto.setReCallCount(commandSendDetails.getReCallCount());
		return dto;
	}

	public static CommandSendDetails dtoToEntity(CommanddetalisSendDto dto) {
		CommandSendDetails entity = new CommandSendDetails();
		entity.setId(dto.getId());
		entity.setDeviceId(dto.getDeviceId());
		entity.setImeiNumber(dto.getImeiNumber());
		entity.setEventId(dto.getEventId());
		entity.setEventType(dto.getEventType());
		entity.setPositionId(dto.getPositionId());
		entity.setEvidenceFiles(dto.getEvidenceFiles()); // Note the spelling change here
		entity.setBaseCommand(dto.getBaseCommand());
		entity.setCommand(dto.getCommand());
		entity.setCommandRespone(dto.getCommandResponse()); // Note the spelling change here
		entity.setStatus(CommandStatus.valueOf(dto.getStatus())); // Assuming CommandStatus is an enum
		entity.setVehicleStatus(dto.getVehicleStatus());
		entity.setNoOfFileReq(dto.getNoOfFileReq());
		entity.setNoOfFileUploaded(dto.getNoOfFileUploaded());
		entity.setCreateOn(dto.getCreateOn());
		entity.setReCallOn(dto.getReCallOn());
		entity.setReCallCount(dto.getReCallCount());
		return entity;
	}

}
