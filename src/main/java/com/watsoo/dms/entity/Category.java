	package com.watsoo.dms.entity;
	
	import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
	
	@Entity
	@Table(name = "category")
	public class Category {
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private String name;
	    
	    @Column(name = "created_on")
	    private Date createdOn;
	    
	    @Column(name = "updated_on")
	    private Date updatedOn;
	    
	    @Column(name = "created_by")
	    private String createdBy;
	    
	    @Column(name = "min")
	    private Integer min;
	    
	    @Column(name = "max")
	    private Integer max;
	    
	    @Column(name = "color_code")
	    private String colorCode;
	
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
	    
	
	  
	}
