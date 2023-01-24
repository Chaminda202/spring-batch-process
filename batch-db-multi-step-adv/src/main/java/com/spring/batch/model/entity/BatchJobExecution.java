//package com.spring.batch.model.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "BATCH_JOB_EXECUTION")
//public class BatchJobExecution {
//    //    JOB_EXECUTION_ID BIGINT  PRIMARY KEY ,
//    @Id
//    @Column(name = "JOB_EXECUTION_ID")
//    private Integer jobExecutionId;
//    //    CREATE_TIME TIMESTAMP NOT NULL,
//    @Column(name = "CREATE_TIME")
//    private LocalDateTime createTime;
//    //    START_TIME TIMESTAMP DEFAULT NULL,
//    @Column(name = "START_TIME")
//    private LocalDateTime startTime;
//    //    END_TIME TIMESTAMP DEFAULT NULL,
//    @Column(name = "END_TIME")
//    private LocalDateTime endTime;
//    //    JOB_INSTANCE_ID BIGINT NOT NULL,
////    @Column(name = "JOB_INSTANCE_ID")
////    private Integer jobInstanceId;
//    //    STATUS VARCHAR(10),
//    @Column(name = "STATUS")
//    private String status;
//    //    VERSION BIGINT,
//    @Column(name = "VERSION")
//    private Integer version;
//    //    EXIT_CODE VARCHAR(20),
//    @Column(name = "EXIT_CODE")
//    private String exitCode;
//    //    EXIT_MESSAGE VARCHAR(2500),
//    @Column(name = "EXIT_MESSAGE")
//    private String exitMessage;
//    //    LAST_UPDATED TIMESTAMP,
//    @Column(name = "LAST_UPDATED")
//    private LocalDateTime lastUpdated;
//    @OneToOne
//    @JoinColumn(name = "JOB_INSTANCE_ID")
//    private BatchJobInstance batchJobInstance;
//}
