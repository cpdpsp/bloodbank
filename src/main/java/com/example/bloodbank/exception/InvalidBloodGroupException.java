package com.example.bloodbank.exception;

public class InvalidBloodGroupException extends RuntimeException {

	public InvalidBloodGroupException() {
		super("Please enter a valid blood group.");
	}
}
