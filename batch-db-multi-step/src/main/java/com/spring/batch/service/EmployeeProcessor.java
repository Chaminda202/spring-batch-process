package com.spring.batch.service;

import com.spring.batch.model.entity.Employee;
import com.spring.batch.model.entity.EmployeeDTO;
import com.spring.batch.model.entity.Salary;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProcessor implements ItemProcessor<EmployeeDTO, Employee> {
    @Override
    public Employee process(EmployeeDTO employeeDTO) {
        Salary salary = Salary.builder()
                .basic(employeeDTO.getBasic())
                .allowance(employeeDTO.getAllowance())
                .tax(employeeDTO.getTax())
                .build();

        Employee employee = Employee.builder()
                .age(employeeDTO.getAge())
                .name(employeeDTO.getName())
                .occupation(employeeDTO.getOccupation())
                .salary(salary)
                .build();
        salary.setEmployee(employee);
        return employee;
    }
}
