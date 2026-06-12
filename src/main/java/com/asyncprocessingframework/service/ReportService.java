package com.asyncprocessingframework.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asyncprocessingframework.component.ReportProcessor;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.enums.JobType;
import com.asyncprocessingframework.repository.AsyncJobRepository;

@Service
public class ReportService {

	@Autowired
	private  AsyncJobRepository jobRepository;
	
	@Autowired
    private  ReportProcessor reportProcessor;

    public Long startReportJob() {

        AsyncJob job = new AsyncJob();

        job.setJobType(JobType.REPORT_EXPORT);
        job.setStatus(JobStatus.PENDING);
        job.setStartTime(LocalDateTime.now());

        jobRepository.save(job);

        reportProcessor.exportReport(job.getJobId());

        return job.getJobId();
    }
}
