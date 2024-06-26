package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.Remark;

public class RemarkDto {

	private Integer id;
	private String status;
	private Date createdOn;
	private Date updatedOn;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public static RemarkDto convertToDTO(Remark entity) {
		RemarkDto dto = new RemarkDto();
		dto.setId(entity.getId());
		dto.setStatus(entity.getStatus());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setUpdatedOn(entity.getUpdatedOn());
		return dto;
	}

}
