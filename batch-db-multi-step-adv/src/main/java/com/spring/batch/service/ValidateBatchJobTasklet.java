package com.spring.batch.service;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Entity;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.batch.constant.JobConstant.EMPLOYEE_JOB;

@Component
@AllArgsConstructor
public class ValidateBatchJobTasklet implements Tasklet {
    //private BatchJobExecutionRepository batchJobExecutionRepository;
    private JobExplorer jobExplorer;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//        List<BatchJobExecution> jobExecutionDetails = this.batchJobExecutionRepository.getBatchJobExecutionDetails();
//        for (BatchJobExecution execution : jobExecutionDetails) {
//            System.out.println(execution.getJobExecutionId());
//        }
//        List<String> jobNames = Arrays.asList(EMPLOYEE_JOB);
//        List<BatchJobExecution> jobExecutions = this.batchJobExecutionRepository
//                .findAllByBatchJobInstanceJobNameIn(jobNames);
//        for (BatchJobExecution execution : jobExecutions) {
//            System.out.println(execution.getBatchJobInstance().getJobName());
//        }

        List<JobInstance> instances = this.jobExplorer.findJobInstancesByJobName(EMPLOYEE_JOB, 0, Integer.MAX_VALUE);
        List<JobExecution> executions = instances.stream()
                .map(jobInstance -> this.jobExplorer.getJobExecutions(jobInstance))
                .flatMap(jes -> jes.stream())
                .sorted(Comparator.comparing(JobExecution::getLastUpdated).reversed())
                .collect(Collectors.toList());

        for (JobExecution jobExecution : executions) {
            System.out.println(jobExecution.getJobInstance().getJobName() + " " + jobExecution.getStatus());
        }
        return null;
    }
}
