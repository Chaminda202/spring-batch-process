package com.spring.batch.config;

import com.spring.batch.listener.EmployeeJobExecutionListener;
import com.spring.batch.model.entity.Employee;
import com.spring.batch.model.entity.EmployeeDTO;
import com.spring.batch.repository.EmployeeRepository;
import com.spring.batch.service.*;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.stream.IntStream;

import static com.spring.batch.constant.JobConstant.EMPLOYEE_JOB;
import static com.spring.batch.constant.JobConstant.VALIDATE_EMPLOYEE_JOB;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class EmployeeBatchConfig {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EmployeeRepository employeeRepository;
    private EmployeeWriter employeeWriter;
    private EmployeeJdbcCursorItemReader employeeJdbcCursorItemReader;
    private EmployeeFlatFileItemWriter employeeFlatFileItemWriter;
    private AppConfig appConfig;
    private EmployeeProcessor processor;
    private ValidateBatchJobTasklet validateBatchJobTasklet;

    @Bean
    @StepScope
    FlatFileItemReader<EmployeeDTO> fileItemReader(@Value("#{jobParameters['input.file.name']}") String fileName) {
        FlatFileItemReader<EmployeeDTO> flatFileItemReader = new FlatFileItemReader<>();
        // if file is defined inside the project
        // flatFileItemReader.setResource(new ClassPathResource("data/test.csv"));
        // flatFileItemReader.setResource(new FileSystemResource(this.appConfig.getInputFilePath()));
        flatFileItemReader.setResource(new FileSystemResource(fileName));
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
        // Skip fields when reading a file
        // String[] properties = new String[] {"X","age","occupation","X","allowance","tax"};

        String[] properties = new String[]{"name","age","occupation","basic","allowance","tax"};
        // lineTokenizer.setIncludedFields(new int[] {1, 2, 4, 5});
        lineTokenizer.setNames(properties);
        BeanWrapperFieldSetMapper<EmployeeDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(EmployeeDTO.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }

    @Bean
    public Step step1(ItemReader<EmployeeDTO> fileItemReader) {
        return stepBuilderFactory.get("csv-step").<EmployeeDTO, Employee>chunk(4)
                .reader(fileItemReader)
                .processor(processor)
                .writer(employeeWriter)
                //.taskExecutor(taskExecutor())
                .build();
    }

    @Bean(name = EMPLOYEE_JOB)
    public Job runJob(Step step1) {
        return jobBuilderFactory.get(EMPLOYEE_JOB)
                .start(step1)
                .next(generateCsvFile())
                //.listener(employeeJobExecutionListener())
                .listener(new EmployeeJobExecutionListener())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    @Bean
    public Step generateCsvFile() {
        return stepBuilderFactory.get("generateCsvFile")
                .<EmployeeDTO, EmployeeDTO>chunk(100)
                .reader(this.employeeJdbcCursorItemReader)
                .writer(this.employeeFlatFileItemWriter)
                .build();
    }

    @Bean
    public Step validateJob() {
        return stepBuilderFactory.get("validateJob")
                .tasklet(validateBatchJobTasklet)
                .build();
    }

    @Qualifier(value = VALIDATE_EMPLOYEE_JOB)
    @Bean
    public Job validateEmployeeJob() {
        return jobBuilderFactory.get(VALIDATE_EMPLOYEE_JOB)
                .start(validateJob())
                .build();
    }

//    @Bean
//    public EmployeeJobExecutionListener employeeJobExecutionListener() {
//        return new EmployeeJobExecutionListener();
//    }
}