package com.spring.batch.service;

import com.spring.batch.model.entity.EmployeeDTO;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFlatFileItemWriter extends FlatFileItemWriter<EmployeeDTO> {
//    @Value("${generated.output.file_path}")
//    private String outPutPath;

    public EmployeeFlatFileItemWriter(@Value("${generated.output.file_path}") String outPutPath) {
        setResource(new FileSystemResource(outPutPath));
        setLineAggregator(getDelimitedLineAggregator());
        setHeaderCallback(writer -> writer.write("name,age,occupation,basic,allowance,tax"));
    }

    public DelimitedLineAggregator<EmployeeDTO> getDelimitedLineAggregator() {
        BeanWrapperFieldExtractor<EmployeeDTO> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] {"name", "age", "occupation", "basic", "allowance", "tax"});

        DelimitedLineAggregator<EmployeeDTO> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
        return delimitedLineAggregator;
    }
}
