package com.watsoo.dms.dto;

import org.springframework.stereotype.Component;

@Component
public class Response<T> {

	private int responseCode;
	private String message;
	private T data;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Response(String message,T data,int responseCode) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.data = data;
	}

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

}
