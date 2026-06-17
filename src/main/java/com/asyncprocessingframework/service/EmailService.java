package com.asyncprocessingframework.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asyncprocessingframework.component.EmailProcessor;
import com.asyncprocessingframework.dto.EmailRequest;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.enums.JobType;
import com.asyncprocessingframework.repository.AsyncJobRepository;

@Service
public class EmailService {
	
	@Autowired
	private  AsyncJobRepository jobRepository;
	
	@Autowired
    private  EmailProcessor emailProcessor;

    public Long sendEmail(
            EmailRequest request) {

        AsyncJob job = new AsyncJob();

        job.setJobType(
                JobType.EMAIL_NOTIFICATION);

        job.setStatus(JobStatus.PENDING);

        job.setStartTime(
                LocalDateTime.now());

        jobRepository.save(job);

        emailProcessor.sendEmail(
                request,
                job.getJobId());

        return job.getJobId();
    }
}

