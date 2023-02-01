package com.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static com.spring.batch.constant.JobConstant.EMPLOYEE_JOB;
import static com.spring.batch.constant.JobConstant.VALIDATE_EMPLOYEE_JOB;

@SpringBootApplication
public class BatchDbApplication {

	public static void main(String[] args) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		// ConfigurableApplicationContext ctx = SpringApplication.run(BatchDbApplication.class, args);
		ApplicationContext ctx = SpringApplication.run(BatchDbApplication.class, args);
//		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
//		JobParameters jobParameters = new JobParameters();
//
//		Job job1= (Job) ctx.getBean(EMPLOYEE_JOB);
//		Job job2= (Job) ctx.getBean(VALIDATE_EMPLOYEE_JOB);
//		jobLauncher.run(job1, jobParameters);
//		jobLauncher.run(job2, jobParameters);
	}
}
