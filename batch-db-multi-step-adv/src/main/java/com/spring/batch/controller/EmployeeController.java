package com.spring.batch.controller;

import com.spring.batch.util.BatchUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.spring.batch.constant.JobConstant.EMPLOYEE_JOB;

@RestController
@RequestMapping(name = "employees")
//@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    @Autowired
    private JobLauncher jobLauncher;
    @Qualifier(value = EMPLOYEE_JOB)
    @Autowired
    private Job job;
    @Autowired
    private ExecutionContext executionContext;

    @GetMapping(name = "load")
    public void loadData() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(map);
        this.executionContext.putString("TEST", "Call to start the job");
        JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
        log.info("Batch is processing...");
        while (jobExecution.isRunning()){
            log.info("Batch processing is continue");
        }
        log.info("Batch is finished");
    }
}
