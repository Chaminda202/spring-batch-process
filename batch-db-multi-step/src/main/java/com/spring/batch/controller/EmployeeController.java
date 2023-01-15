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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

        // checkSum
        try {
            String inputPath = "/";
            String outputPath = "/";
            final String inputFile = BatchUtils
                    .calculateCheckSumMD5(inputPath);
            final String outputFile = BatchUtils
                    .calculateCheckSumMD5(outputPath);
            System.out.println(inputFile.equals(outputFile));
            boolean memoryMapped = BatchUtils.areFilesIdenticalMemoryMapped(inputPath, outputPath);
            System.out.println(memoryMapped);
            String checksum1 = BatchUtils.checksum(inputPath);
            String checksum2 = BatchUtils.checksum(outputPath);
            System.out.println("Input File CheckSum " + checksum1 + " Output File CheckSum " + checksum2
            + " " + checksum1.equals(checksum2));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("Batch is finished");
    }
}
