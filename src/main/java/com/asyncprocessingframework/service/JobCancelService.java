package com.asyncprocessingframework.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;

@Service
public class JobCancelService {

    @Autowired
    private AsyncJobRepository jobRepository;

    public void cancelJob(Long jobId) {

        AsyncJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() == JobStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed job");
        }

        job.setStatus(JobStatus.CANCELLED);
        job.setEndTime(LocalDateTime.now());

        jobRepository.save(job);
    }
}
