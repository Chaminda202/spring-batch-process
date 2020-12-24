package com.spring.batch.service;

import com.spring.batch.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {
    private static double CURRENCY_RATE = 1.345;
    @Override
    public Employee process(Employee employee) throws Exception {
        employee.setSalary(employee.getSalary() * CURRENCY_RATE);
        return employee;
    }
}
