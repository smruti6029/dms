package com.watsoo.dms.dto;

import java.util.Date;
import java.util.List;

import com.watsoo.dms.entity.Event;

public class EventDto {
	private Long id;
	private Long vehicleId;
	private VehicleDto vehicleDto;
	private Long positionId;
	private String eventType;
	private Date eventTime;
	private List<String> evidencePhotos;
	private List<String> evidenceVideos;
	private Date eventServerCreateTime;
	private Long deviceId;
	private Double longitude;
	private Double latitude;
	private DriverDto driverDto;

	private String driverPhone;
	private String driverName;
	private String vehicleNo;
	private String chassisNumber;
	private String imeiNo;
	private String dlNumber;
	
	private Long driverId;

	private Integer remarkId;


	private String remark;

	private Date updateOn;
	
	
	private Double speed;
	
	private Boolean ignition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public List<String> getEvidencePhotos() {
		return evidencePhotos;
	}

	public void setEvidencePhotos(List<String> evidencePhotos) {
		this.evidencePhotos = evidencePhotos;
	}

	public List<String> getEvidenceVideos() {
		return evidenceVideos;
	}

	public void setEvidenceVideos(List<String> evidenceVideos) {
		this.evidenceVideos = evidenceVideos;
	}

	public VehicleDto getVehicleDto() {
		return vehicleDto;
	}

	public void setVehicleDto(VehicleDto vehicleDto) {
		this.vehicleDto = vehicleDto;
	}

	public EventDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getEventServerCreateTime() {
		return eventServerCreateTime;
	}

	public void setEventServerCreateTime(Date eventServerCreateTime) {
		this.eventServerCreateTime = eventServerCreateTime;
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

	public DriverDto getDriverDto() {
		return driverDto;
	}

	public void setDriverDto(DriverDto driverDto) {
		this.driverDto = driverDto;
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

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getDlNumber() {
		return dlNumber;
	}

	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
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

	public static EventDto fromEntity(Event event) {

		if (event == null) {
			return null;
		}
		EventDto dto = new EventDto();
		dto.setId(event.getId());
		dto.setVehicleNo(event.getVehicleNo());
		dto.setPositionId(event.getPositionId());
		dto.setEventType(event.getEventType().toString());
		dto.setEventTime(event.getEventTime());
		dto.setEventServerCreateTime(event.getEventServerCreateTime());
		dto.setDeviceId(event.getDeviceId());
		dto.setImeiNo(event.getImeiNo());
		dto.setChassisNumber(event.getChassisNumber());
		dto.setLongitude(event.getLongitude());
		dto.setLatitude(event.getLatitude());
		dto.setDriverPhone(event.getDriverPhone());
		dto.setDriverName(event.getDriverName());
		dto.setUpdateOn(event.getUpdatedOn());
		dto.setRemark(event.getRemark());
		dto.setDlNumber(event.getDlNo());
		dto.setRemarkId(event.getRemarkId());
		dto.setIgnition(event.getIgnition());
		dto.setSpeed(event.getSpeed());
		dto.setDriverId(event.getDriverId());
		return dto;

	}

}
