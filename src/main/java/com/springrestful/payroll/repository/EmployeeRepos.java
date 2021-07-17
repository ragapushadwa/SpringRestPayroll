package com.springrestful.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springrestful.payroll.entities.Employee;

public interface EmployeeRepos extends JpaRepository<Employee, Long> {

}
