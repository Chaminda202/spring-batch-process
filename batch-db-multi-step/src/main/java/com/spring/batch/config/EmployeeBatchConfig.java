package com.spring.batch.config;

import com.spring.batch.model.entity.Employee;
import com.spring.batch.model.entity.EmployeeDTO;
import com.spring.batch.repository.EmployeeRepository;
import com.spring.batch.service.EmployeeProcessor;
import com.spring.batch.service.EmployeeWriter;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class EmployeeBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private EmployeeRepository employeeRepository;

    private EmployeeWriter employeeWriter;

    private DataSource dataSource;

//    @Bean
//    public FlatFileItemReader<Employee> reader() {
//        return new FlatFileItemReaderBuilder<Employee>()
//                .name("EmployeeFileReader")
//                .resource(new ClassPathResource("employee.csv"))
//                .delimited()
//                .names(new String[]{"name", "age", "occupation", "salary"})
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
//                    setTargetType(Employee.class);
//                }})
//                .build();
//    }

    @Bean
    FlatFileItemReader<EmployeeDTO> fileItemReader() {
        FlatFileItemReader<EmployeeDTO> flatFileItemReader = new FlatFileItemReader<>();
        // flatFileItemReader.setResource(new ClassPathResource("employees.csv"));
        flatFileItemReader.setResource(new FileSystemResource("/Users/chamindasampath/interviews_exercise/input/test.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        flatFileItemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        return flatFileItemReader;
    }

    private LineMapper<EmployeeDTO> lineMapper() {
        DefaultLineMapper<EmployeeDTO> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"name","age","occupation","basic","allowance","tax"});

        BeanWrapperFieldSetMapper<EmployeeDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(EmployeeDTO.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public EmployeeProcessor processor() {
        return new EmployeeProcessor();
    }

    // Without creating item writer
    @Bean
    public RepositoryItemWriter<Employee> writer() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepository);
        writer.setMethodName("save");
        return writer;
    }



    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<EmployeeDTO, Employee>chunk(4)
                .reader(fileItemReader())
                .processor(processor())
                .writer(employeeWriter)
                //.taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("employeeJob")
                .flow(step1())
                .next(generateCsvFile())
                .end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }


    @Bean
    public JdbcCursorItemReader<EmployeeDTO> jdbcCursorItemReader(){
        JdbcCursorItemReader<EmployeeDTO> jdbcCursorItemReader =
                new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("SELECT e.name, e.age, e.occupation, s.basic, s.allowance, s.tax FROM employee e " +
                "JOIN salary s " +
                "On e.id = s.emp_id");
        jdbcCursorItemReader.setRowMapper(new RowMapper<EmployeeDTO>() {
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
        });
        return jdbcCursorItemReader;
    }

    @Bean
    public FlatFileItemWriter<EmployeeDTO> flatFileItemWriter(){
        FlatFileItemWriter<EmployeeDTO> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(new FileSystemResource("/Users/chamindasampath/interviews_exercise/output/test.csv"));
        DelimitedLineAggregator<EmployeeDTO> aggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<EmployeeDTO> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"name", "age", "occupation", "basic", "allowance", "tax"});
        aggregator.setFieldExtractor(extractor);
        flatFileItemWriter.setLineAggregator(aggregator);
        flatFileItemWriter.setHeaderCallback(writer -> writer.write("name,age,occupation,basic,allowance,tax"));
        /*flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<EmployeeDTO>() {{
                    setDelimiter(",");
                    setFieldExtractor(new BeanWrapperFieldExtractor<EmployeeDTO>()
                    {{
                        setNames(new String[] {"name", "age", "occupation", "basic", "allowance", "tax"});
                    }});
                }});
        flatFileItemWriter.setHeaderCallback(writer -> writer.write("name,age,occupation,basic,allowance,tax"));*/
        return flatFileItemWriter;
    }

    @Bean
    public Step generateCsvFile() {
        return stepBuilderFactory.get("generateCsvFile")
                .<EmployeeDTO, EmployeeDTO>chunk(100)
                .reader(jdbcCursorItemReader())
                .writer(flatFileItemWriter())
                .build();
    }
}