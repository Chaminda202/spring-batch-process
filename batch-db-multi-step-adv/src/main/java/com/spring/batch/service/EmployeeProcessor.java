package com.spring.batch.service;

import com.spring.batch.model.entity.Employee;
import com.spring.batch.model.entity.EmployeeDTO;
import com.spring.batch.model.entity.Salary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeProcessor implements ItemProcessor<EmployeeDTO, Employee> {
    private final ExecutionContext executionContext;
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
        log.info("Print Execution Context Key TEST Value {}", this.executionContext.get("TEST"));
        return employee;
    }
}
