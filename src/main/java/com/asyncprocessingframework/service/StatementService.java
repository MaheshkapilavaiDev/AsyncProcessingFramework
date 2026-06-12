package com.asyncprocessingframework.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asyncprocessingframework.component.StatementProcessor;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.enums.JobType;
import com.asyncprocessingframework.repository.AsyncJobRepository;

@Service
public class StatementService {
	
	@Autowired
	private AsyncJobRepository jobRepository;
	
	@Autowired
	private StatementProcessor statementProcessor;
	
	public Long startStatementJob(Long customerId) {

	    AsyncJob job = new AsyncJob();

	    job.setJobType(
	            JobType.STATEMENT_GENERATION);

	    job.setStatus(JobStatus.PENDING);

	    job.setStartTime(LocalDateTime.now());

	    jobRepository.save(job);

	    statementProcessor
	            .generateStatement(
	                    customerId,
	                    job.getJobId());

	    return job.getJobId();
	}

}
