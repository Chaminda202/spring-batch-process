package com.spring.batch.integration;

import com.spring.batch.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class EmployeeIntegrationConfig {
    private final AppConfig appConfig;
    private final Job employeeJob;
    private final JobRepository jobRepository;
    // private final TaskExecutor taskExecutor;

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReadingMessageSource(),
                sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
                .channel(directChannel())
                .handle(fileWritingMessageHandler())
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingGateway())
                .log()
                .get();
    }

    public FileReadingMessageSource fileReadingMessageSource() {
        FileReadingMessageSource readingMessageSource = new FileReadingMessageSource();
        readingMessageSource.setDirectory(new File(this.appConfig.getInputFilePath()));
        readingMessageSource.setFilter(new SimplePatternFileListFilter("*." + this.appConfig.getFileFormat()));
        return readingMessageSource;
    }

    public DirectChannel directChannel() {
        return new DirectChannel();
    }

    // Rename the file adding processing suffix
    public FileWritingMessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler writingMessageHandler = new FileWritingMessageHandler(new File(this.appConfig.getInputFilePath()));
        writingMessageHandler.setFileExistsMode(FileExistsMode.REPLACE);
        writingMessageHandler.setDeleteSourceFiles(Boolean.TRUE);
        // writingMessageHandler.setFileNameGenerator(new DefaultFileNameGenerator());
        writingMessageHandler.setFileNameGenerator(fileNameGenerator());
        writingMessageHandler.setRequiresReply(Boolean.FALSE);
        return writingMessageHandler;
    }

    public DefaultFileNameGenerator fileNameGenerator(){
        DefaultFileNameGenerator filenameGenerator = new DefaultFileNameGenerator();
        filenameGenerator.setExpression("payload.name + '.processing'");
        return filenameGenerator;
    }

    public FileMessageToJobRequest fileMessageToJobRequest() {
        FileMessageToJobRequest jobRequest = new FileMessageToJobRequest();
        jobRequest.setJob(employeeJob);
        jobRequest.setFileName("input.file.name");
        return jobRequest;
    }

    public JobLaunchingGateway jobLaunchingGateway() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        // simpleJobLauncher.setTaskExecutor(taskExecutor);
        // simpleJobLauncher.setTaskExecutor(new SyncTaskExecutor());
        return new JobLaunchingGateway(simpleJobLauncher);
    }
}
