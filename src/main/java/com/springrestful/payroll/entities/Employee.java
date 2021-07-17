package com.springrestful.payroll.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {
	private @Id @GeneratedValue Long id;
	
	private String role;
	private String lastName;
	private String firstName;
	Employee (){};
	public Employee(Long id, String firstName,String lastName, String role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		
		this.role = role;
	}
	
	public String getName(){
		return this.firstName+" "+this.lastName;
	}
	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstName=parts[0];
		this.lastName=parts[1];
				
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	  public boolean equals(Object o) {

	    if (this == o)
	      return true;
	    if (!(o instanceof Employee))
	      return false;
	    Employee employee = (Employee) o;
	    return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
	    	&& Objects.equals(this.lastName, employee.lastName)	
	        && Objects.equals(this.role, employee.role);
	  }

	@Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.firstName,this.lastName, this.role);
	  }
	 @Override
	  public String toString() {
	    return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
	        + '\'' + ", role='" + this.role + '\'' + '}';
	  }
}
