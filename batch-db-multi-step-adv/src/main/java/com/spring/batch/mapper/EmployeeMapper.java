package com.spring.batch.mapper;

import com.spring.batch.model.entity.EmployeeDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeMapper implements RowMapper<EmployeeDTO> {

    @Override
    public EmployeeDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        EmployeeDTO  employeeDTO = EmployeeDTO.builder()
                .name(resultSet.getString("name"))
                .age(resultSet.getInt("age"))
                .occupation(resultSet.getString("occupation"))
                .basic(resultSet.getDouble("basic"))
                .allowance(resultSet.getDouble("allowance"))
                .tax(resultSet.getDouble("tax"))
                .build();
        return employeeDTO;
    }
}
