//package com.spring.batch.model.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "BATCH_JOB_INSTANCE")
//public class BatchJobInstance {
//    //    JOB_INSTANCE_ID BIGINT  PRIMARY KEY ,
//    @Id
//    @Column(name = "JOB_INSTANCE_ID")
//    private Integer jobInstanceId;
//    //    JOB_NAME VARCHAR(100) NOT NULL ,
//    @Column(name = "JOB_NAME")
//    private String jobName;
//    //    JOB_KEY VARCHAR(32) NOT NULL
//    @Column(name = "JOB_KEY")
//    private String jobKey;
//    //    VERSION BIGINT,
//    @Column(name = "VERSION")
//    private Integer version;
//    @OneToOne(mappedBy = "batchJobInstance")
//    private BatchJobExecution batchJobExecution;
//}
