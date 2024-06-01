package com.example.bloodbank.exception;

public class ErrorResponse {

	private int statusCode;
	private String message;

	public ErrorResponse(int code, String message) {
		statusCode = code;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

}
