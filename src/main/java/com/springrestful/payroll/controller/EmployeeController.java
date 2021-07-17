package com.springrestful.payroll.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.util.ReflectionUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.springrestful.payroll.entities.Employee;
import com.springrestful.payroll.exception.EmployeeNotFoundException;
import com.springrestful.payroll.repository.EmployeeModelAssembler;
import com.springrestful.payroll.repository.EmployeeRepos;

@RestController
public class EmployeeController {
	
	private final EmployeeRepos repository;
	private EmployeeModelAssembler assembler;

	public EmployeeController(EmployeeRepos repository, EmployeeModelAssembler assembler) {
		super();
		this.repository = repository;
		this.assembler= assembler;
	}
	
	@GetMapping(path="/employees")
	public
	CollectionModel<EntityModel<Employee>> all() {

		  List<EntityModel<Employee>> employees = repository.findAll().stream()
		      .map(assembler::toModel).collect(Collectors.toList());

		  return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
		}
	
	@PostMapping(path="/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		
	}
	
	@GetMapping(path="/employees/{id}")
	public
	EntityModel<Employee> one(@PathVariable Long id) {

		  Employee employee = repository.findById(id) //
		      .orElseThrow(() -> new EmployeeNotFoundException(id));

		  return assembler.toModel(employee);
		}
	
	@PutMapping(path="/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee,@PathVariable Long id) {
		Employee updatedEmployee= repository.findById(id)
				.map(employee ->{
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
				return repository.save(employee);
				}).
				orElseGet(()->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
							});
		EntityModel<Employee> entityModel=assembler.toModel(updatedEmployee);
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		
	}
	@DeleteMapping(path="/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping(path="/employees/{id}")
		ResponseEntity<?> patchEmployee(@PathVariable Long id, @RequestBody Map<Object, Object> fields){
			Optional<Employee> patchEmployee = repository.findById(id);
			if(patchEmployee.isPresent()) {
				fields.forEach((key, value)->{
					Field field=ReflectionUtils.findRequiredField(Employee.class, (String) key);
					field.setAccessible(true);
					ReflectionUtils.setField(field, patchEmployee.get(), value);
				});
				Employee updatedEmployee=repository.save(patchEmployee.get());
				EntityModel<Employee> entityModel=assembler.toModel(updatedEmployee);
				return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
			}
			
			return null;
		}
	
}
