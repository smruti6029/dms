package com.watsoo.dms.dto;

public class DriverPerformanceDto {

	private String driverName;

	private String driverDlNumber;

	private String joinDate;

	private Integer perfomance;
	private CategoryDto categoryDto;
	private Integer previousRangeCount;
	private Integer currentRangeCount;
	private String driverPhone;
	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public Integer getPreviousRangeCount() {
		return previousRangeCount;
	}

	public void setPreviousRangeCount(Integer previousRangeCount) {
		this.previousRangeCount = previousRangeCount;
	}

	public Integer getCurrentRangeCount() {
		return currentRangeCount;
	}

	public void setCurrentRangeCount(Integer currentRangeCount) {
		this.currentRangeCount = currentRangeCount;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public Integer getPerfomance() {
		return perfomance;
	}

	public void setPerfomance(Integer perfomance) {
		this.perfomance = perfomance;
	}

	public CategoryDto getCategoryDto() {
		return categoryDto;
	}

	public void setCategoryDto(CategoryDto categoryDto) {
		this.categoryDto = categoryDto;
	}

	public String getDriverDlNumber() {
		return driverDlNumber;
	}

	public void setDriverDlNumber(String driverDlNumber) {
		this.driverDlNumber = driverDlNumber;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	

}
