package com.watsoo.dms.entity;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "credential_master")
public class CredentialMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "user")
	private User user;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@JsonIgnore
	@Transient
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = passwordEncoder.encode(password); // Encode the password before storing
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CredentialMaster(long id, User user, String email, String password,Date createdOn,
			Date updatedOn, Boolean isActive) {
		super();
		this.id = id;
		this.user = user;
		this.email = email;
		this.password = password;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.isActive = isActive;
	}

	public CredentialMaster() {
		super();
	}

	public boolean passwordMatches(String password) {
		return (passwordEncoder.matches(password, this.password));
	}

	public String passwordEncoder(String password) {
		return passwordEncoder.encode(password);
	}
}
