package com.springrestful.payroll.exception;

@SuppressWarnings("serial")
public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(Long id){
		super("Couldn't find employee "+id );
		
	}
	
}
