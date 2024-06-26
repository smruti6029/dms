package com.watsoo.dms.entity;

import java.util.Date;
import com.watsoo.dms.enums.EventType;
import javax.persistence.*;

@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String vehicleNo;

	private Long positionId;

	@Enumerated(EnumType.STRING)
	private EventType eventType;

	private Date eventTime;

	private Date eventServerCreateTime;

	private String evidencePhotos;

	private String evidenceVideos;

	private Long deviceId;

	@Column(name = "imei_no")
	private String imeiNo;

	@Column(name = "chassis_number")
	private String chassisNumber;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "driver_phone")
	private String driverPhone;

	@Column(name = "driver_name")
	private String driverName;

	@Column(name = "remark")
	private String remark;

	@Column(name = "dl_number")
	private String dlNo;

	@Transient
	private String registrationCertificate;

	@Column(name = "updated_on")
	private Date UpdatedOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "event_id")
	private Long eventId;

	@Column(name = "remark_id")
	private Integer remarkId;
	
	@Column(name = "driver_id")
	private Long driverId;
	
	@Column(name = "vehicle_id")
	private Long vehicleID;
	
	@Column(name = "speed")
	private Double speed;
	
	@Column(name = "ignition")
	private Boolean ignition;

	public Event() {
	}

	// Getters and Setters for all fields

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getEvidencePhotos() {
		return evidencePhotos;
	}

	public void setEvidencePhotos(String evidencePhotos) {
		this.evidencePhotos = evidencePhotos;
	}

	public String getEvidenceVideos() {
		return evidenceVideos;
	}

	public void setEvidenceVideos(String evidenceVideos) {
		this.evidenceVideos = evidenceVideos;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getEventServerCreateTime() {
		return eventServerCreateTime;
	}

	public void setEventServerCreateTime(Date eventServerCreateTime) {
		this.eventServerCreateTime = eventServerCreateTime;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdatedOn() {
		return UpdatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		UpdatedOn = updatedOn;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}

	public String getRegistrationCertificate() {
		return registrationCertificate;
	}

	public void setRegistrationCertificate(String registrationCertificate) {
		this.registrationCertificate = registrationCertificate;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Integer getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(Integer remarkId) {
		this.remarkId = remarkId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(Long vehicleID) {
		this.vehicleID = vehicleID;
	}
	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Boolean getIgnition() {
		return ignition;
	}

	public void setIgnition(Boolean ignition) {
		this.ignition = ignition;
	}

}
