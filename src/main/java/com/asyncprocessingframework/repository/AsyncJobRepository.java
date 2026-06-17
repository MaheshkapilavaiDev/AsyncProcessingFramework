package com.asyncprocessingframework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;

@Repository
public interface AsyncJobRepository extends JpaRepository<AsyncJob, Long>{

	List<AsyncJob> findByStatus(JobStatus running);

}
