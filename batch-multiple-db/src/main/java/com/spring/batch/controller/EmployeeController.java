package com.spring.batch.controller;

import com.spring.batch.employee.model.Employee;
import com.spring.batch.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @PostMapping
    public Employee saveEmployee(@RequestBody Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @GetMapping
    public List<Employee> retrieveAllEmployees() {
        return this.employeeRepository.findAll();
    }
}
