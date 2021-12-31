package com.greatlearning.employee.resource;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class EmployeeResource {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/employee")
	public List<Employee> retrieveAllEmployee() {
		return employeeRepository.findAll();
	}

	@GetMapping("/employees/{id}")
	public Employee retrieveEmployee(@PathVariable long id) {
		Optional<Employee> employee = employeeRepository.findById(id);

		if (!employee.isPresent())
			throw new EmployeeNotFoundException("id-" + id);

		return employee.get();
	}

	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeRepository.deleteById(id);
	}

	@PostMapping("/employees")
	public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
		Employee savedEmployee = employeeRepository.save(employee);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedEmployee.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Object> updateEmployee(@RequestBody Employee employee, @PathVariable long id) {

		Optional<Employee> employeeOptional = employeeRepository.findById(id);

		if (!employeeOptional.isPresent())
			return ResponseEntity.notFound().build();

		employee.setId(id);
		
		employeeRepository.save(employee);

		return ResponseEntity.noContent().build();
	}
}