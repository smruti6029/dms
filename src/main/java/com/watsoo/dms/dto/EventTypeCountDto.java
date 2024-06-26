package com.watsoo.dms.dto;

import java.util.HashMap;
import java.util.Map;

public class EventTypeCountDto {

	long yawningCount;
	long mobileUsageCount;
	long distractionCount;
	long smokingCount;
	long closeEyesCount;
	long noFaceCount;
	long lowHeadCount;
	long drinkingCount;
	long totalEventCount;
	int totalActiveVehicle ;
	int totalInActiveVehicle ;
	int tamperedDevices ;
	int countDefaulterDriver;
	Integer pendingRemark ;
	Integer actionTakenRemark ;
	int noActionTakenRemark;
	
	int vehiclesEngaged;
	
	int totalVehicle;
	
	Map<String, Integer> vehicleEventCountMap;
	
	

	public long getYawningCount() {
		return yawningCount;
	}

	public void setYawningCount(long yawningCount) {
		this.yawningCount = yawningCount;
	}

	public long getMobileUsageCount() {
		return mobileUsageCount;
	}

	public void setMobileUsageCount(long mobileUsageCount) {
		this.mobileUsageCount = mobileUsageCount;
	}

	public long getDistractionCount() {
		return distractionCount;
	}

	public void setDistractionCount(long distractionCount) {
		this.distractionCount = distractionCount;
	}

	public long getSmokingCount() {
		return smokingCount;
	}

	public void setSmokingCount(long smokingCount) {
		this.smokingCount = smokingCount;
	}

	public long getCloseEyesCount() {
		return closeEyesCount;
	}

	public void setCloseEyesCount(long closeEyesCount) {
		this.closeEyesCount = closeEyesCount;
	}

	public long getNoFaceCount() {
		return noFaceCount;
	}

	public void setNoFaceCount(long noFaceCount) {
		this.noFaceCount = noFaceCount;
	}

	public long getLowHeadCount() {
		return lowHeadCount;
	}

	public void setLowHeadCount(long lowHeadCount) {
		this.lowHeadCount = lowHeadCount;
	}

	public long getDrinkingCount() {
		return drinkingCount;
	}

	public void setDrinkingCount(long drinkingCount) {
		this.drinkingCount = drinkingCount;
	}

	public long getTotalEventCount() {
		return totalEventCount;
	}

	public void setTotalEventCount(long totalEventCount) {
		this.totalEventCount = totalEventCount;
	}

	public EventTypeCountDto() {
	}
	

	
	public int getTotalActiveVehicle() {
		return totalActiveVehicle;
	}

	public void setTotalActiveVehicle(int totalActiveVehicle) {
		this.totalActiveVehicle = totalActiveVehicle;
	}

	public int getTotalInActiveVehicle() {
		return totalInActiveVehicle;
	}

	public void setTotalInActiveVehicle(int totalInActiveVehicle) {
		this.totalInActiveVehicle = totalInActiveVehicle;
	}

	public int getTamperedDevices() {
		return tamperedDevices;
	}

	public void setTamperedDevices(int tamperedDevices) {
		this.tamperedDevices = tamperedDevices;
	}
	
	

	public int getCountDefaulterDriver() {
		return countDefaulterDriver;
	}

	public void setCountDefaulterDriver(int countDefaulterDriver) {
		this.countDefaulterDriver = countDefaulterDriver;
	}
	

	public Integer getPendingRemark() {
		return pendingRemark;
	}

	public void setPendingRemark(Integer pendingRemark) {
		this.pendingRemark = pendingRemark;
	}

	public Integer getActionTakenRemark() {
		return actionTakenRemark;
	}

	public void setActionTakenRemark(Integer actionTakenRemark) {
		this.actionTakenRemark = actionTakenRemark;
	}
	
	public Map<String, Integer> getVehicleEventCountMap() {
		return vehicleEventCountMap;
	}

	public void setVehicleEventCountMap(Map<String, Integer> vehicleEventCountMap) {
		this.vehicleEventCountMap = vehicleEventCountMap;
	}
	

	public int getVehiclesEngaged() {
		return vehiclesEngaged;
	}

	public void setVehiclesEngaged(int vehiclesEngaged) {
		this.vehiclesEngaged = vehiclesEngaged;
	}

	public int getNoActionTakenRemark() {
		return noActionTakenRemark;
	}

	public void setNoActionTakenRemark(int noActionTakenRemark) {
		this.noActionTakenRemark = noActionTakenRemark;
	}
	
	

	public int getTotalVehicle() {
		return totalVehicle;
	}

	public void setTotalVehicle(int totalVehicle) {
		this.totalVehicle = totalVehicle;
	}

	public EventTypeCountDto(long yawningCount, long mobileUsageCount, long distractionCount,
			long smokingCount, long closeEyesCount, long noFaceCount, long lowHeadCount, long drinkingCount,
			long totalEventCount) {
		this.yawningCount = yawningCount;
		
		this.mobileUsageCount = mobileUsageCount;
		this.distractionCount = distractionCount;
		this.smokingCount = smokingCount;
		this.closeEyesCount = closeEyesCount;
		this.noFaceCount = noFaceCount;
		this.lowHeadCount = lowHeadCount;
		this.drinkingCount = drinkingCount;
		this.totalEventCount = totalEventCount;
	}

}
