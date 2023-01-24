package com.spring.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class EmployeeJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Employee Job start Job Name {} Start Time {}", jobExecution.getJobInstance().getJobName(),
                jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Employee Job run successfully Job Name {} End Time{}", jobExecution.getJobInstance().getJobName(),
                jobExecution.getEndTime());
            long totalTime = (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
            log.info("Execution time {} ms", totalTime);
        }
    }
}
