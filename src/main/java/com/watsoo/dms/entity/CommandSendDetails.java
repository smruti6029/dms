package com.watsoo.dms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.watsoo.dms.enums.CommandStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "command_send_details")
public class CommandSendDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long deviceId;

	private String imeiNumber;

	private Long eventId;

	private String eventType;

	private Long positionId;

	@Column(name = "evidence_files")
	private String evidenceFiles;

	private String baseCommand;

	private String command;

	private String commandRespone;

	@Enumerated(EnumType.STRING)
	private CommandStatus status;

	private String vehicleStatus;

	private Integer noOfFileReq;

	private Integer noOfFileUploaded;

	@CreationTimestamp
	private LocalDateTime createOn;

	private LocalDateTime reCallOn;

	private Integer reCallCount;

	private String remarks;

	private LocalDateTime updatedOn;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getCommandRespone() {
		return commandRespone;
	}

	public void setCommandRespone(String commandRespone) {
		this.commandRespone = commandRespone;
	}

	public CommandStatus getStatus() {
		return status;
	}

	public void setStatus(CommandStatus status) {
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public CommandSendDetails() {
		super();

	}

}
