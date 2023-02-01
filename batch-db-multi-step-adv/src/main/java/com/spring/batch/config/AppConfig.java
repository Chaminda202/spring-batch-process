package com.spring.batch.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
    @Value("${imported.input.file_path}")
    private String inputFilePath;
    @Value("${generated.output.file_path}")
    private String outputFilePath;
    @Value("${process.file.format}")
    private String fileFormat;
}
