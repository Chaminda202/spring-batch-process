package com.spring.batch.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(name = "employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping(name = "load")
    public void loadData() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(map);

        JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
        log.info("Batch is processing...");
        while (jobExecution.isRunning()){
            log.info("Batch processing is continue");
        }
        log.info("Batch is finished");
    }
}
