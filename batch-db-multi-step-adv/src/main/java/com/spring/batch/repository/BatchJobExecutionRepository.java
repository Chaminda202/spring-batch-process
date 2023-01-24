//package com.spring.batch.repository;
//
//import com.spring.batch.model.entity.BatchJobExecution;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, Integer> {
//
////    @Query(name = "select bje.* " +
////            "FROM " +
////            "BATCH_JOB_EXECUTION bje inner join BATCH_JOB_INSTANCE ji " +
////            "on ji.JOB_INSTANCE_ID = bje.JOB_INSTANCE_ID order by bje.JOB_INSTANCE_ID desc", nativeQuery = true)
////    List<BatchJobExecution> getBatchJobExecutionDetails();
//
//    List<BatchJobExecution> findAllByBatchJobInstanceJobNameIn(List<String> jobNames);
//}
