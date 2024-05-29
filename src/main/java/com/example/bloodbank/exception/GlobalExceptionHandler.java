package com.example.bloodbank.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.bloodbank.utils.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidBloodGroupException.class)
	public ResponseEntity<ErrorResponse> handleInvalidBloodGroupException(InvalidBloodGroupException ex){
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,List<String>>> 
	handleValidationErrors(MethodArgumentNotValidException ex){
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage).collect(Collectors.toList());
		return new ResponseEntity<>(getErrorsMap(errors),HttpStatus.BAD_REQUEST);
	}
	
	private Map<String,List<String>> getErrorsMap(List<String> errors){
		Map<String,List<String>> errorResponse = new HashMap<>();
		errorResponse.put("errors",errors);
		return errorResponse;
	}
	
	
	 @ExceptionHandler(SQLIntegrityConstraintViolationException.class) public
	 ResponseEntity<ErrorResponse>
	  handleInvalidDonorId(SQLIntegrityConstraintViolationException ex) {
	  ErrorResponse errorResponse = new
	  ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Donor is not registered");
	  return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse); }
	 
	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundExeption(ValidationException ex){
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	//patch
	
	  @ExceptionHandler(ConstraintViolationException.class) public
	  ResponseEntity<Map<String,List<String>>> 
	  handleConstraintViolationException(ConstraintViolationException ex){
		  List<String> errors = ex.getConstraintViolations().stream().map(t -> t.getMessage()).collect(Collectors.toList());
		  ErrorResponse errorResponse = new
				  ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),ex.getLocalizedMessage());
		  return new ResponseEntity<>(getErrorsMap(errors),HttpStatus.BAD_REQUEST);
		  }
	 
	  @ExceptionHandler(InvalidDataException.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
		public ResponseEntity<ErrorResponse> handleInvalidDataExeption(InvalidDataException ex){
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
}
