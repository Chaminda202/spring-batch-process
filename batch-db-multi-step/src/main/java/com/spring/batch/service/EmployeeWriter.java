package com.spring.batch.service;

import com.spring.batch.model.entity.Employee;
import com.spring.batch.model.entity.Salary;
import com.spring.batch.repository.EmployeeRepository;
import com.spring.batch.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeWriter implements ItemWriter<Employee> {
    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    @Override
    public void write(List<? extends Employee> employees) throws Exception {
        System.out.println("Execute Thread " + Thread.currentThread().getName());
//        for (Employee em : employees) {
//            this.employeeRepository.save(em);
//            final Salary salary = em.getSalary();
//            salary.setEmployee(em);
//            this.salaryRepository.save(salary);
//        }
       this.employeeRepository.saveAll(employees);
    }
}
