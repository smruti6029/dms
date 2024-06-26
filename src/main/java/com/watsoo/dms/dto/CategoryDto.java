package com.watsoo.dms.dto;

import java.util.Date;

import com.watsoo.dms.entity.Category;

public class CategoryDto {

	private Long id;
	private String name;
	private Date createdOn;
	private Date updatedOn;
	private String createdBy;
	private Integer min;
	private Integer max;
	private String colorCode;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public CategoryDto() {
		super();
	}

	public static CategoryDto toDTO(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedOn(category.getCreatedOn());
        dto.setUpdatedOn(category.getUpdatedOn());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setMin(category.getMin());
        dto.setMax(category.getMax());
        dto.setColorCode(category.getColorCode());
        return dto;
    }

}
