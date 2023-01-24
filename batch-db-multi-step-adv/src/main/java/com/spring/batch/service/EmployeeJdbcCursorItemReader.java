package com.spring.batch.service;

import com.spring.batch.mapper.EmployeeMapper;
import com.spring.batch.model.entity.EmployeeDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class EmployeeJdbcCursorItemReader extends JdbcCursorItemReader<EmployeeDTO> implements ItemReader<EmployeeDTO> {
//    @Autowired
//    private DataSource dataSource;
//    @Autowired
//    private EmployeeMapper employeeMapper;

    public EmployeeJdbcCursorItemReader(@Autowired DataSource dataSource, @Autowired EmployeeMapper employeeMapper) {
        setDataSource(dataSource);
        setSql("SELECT e.name, e.age, e.occupation, s.basic, s.allowance, s.tax FROM employee e " +
                "JOIN salary s " +
                "On e.id = s.emp_id");
        setFetchSize(100);
        setRowMapper(employeeMapper);
    }
}
