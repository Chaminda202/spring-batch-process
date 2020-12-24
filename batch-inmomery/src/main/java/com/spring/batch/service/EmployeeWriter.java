package com.spring.batch.service;

import com.spring.batch.model.Employee;
import com.spring.batch.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeWriter implements ItemWriter<Employee> {
    private final EmployeeRepository employeeRepository;
    @Override
    public void write(List<? extends Employee> employees) throws Exception {
        this.employeeRepository.saveAll(employees);
    }
}
