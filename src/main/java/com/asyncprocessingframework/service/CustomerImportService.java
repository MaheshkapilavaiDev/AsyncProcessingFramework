package com.asyncprocessingframework.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.asyncprocessingframework.component.CustomerImportProcessor;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.enums.JobType;
import com.asyncprocessingframework.repository.AsyncJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerImportService {

	@Autowired
    private  AsyncJobRepository jobRepository;
	
	@Autowired
    private  CustomerImportProcessor importProcessor;

    public Long importCustomers(
            MultipartFile file) throws IOException {
    	
    	byte[] fileData = file.getBytes();

        AsyncJob job = new AsyncJob();

        job.setJobType(
                JobType.CUSTOMER_IMPORT);

        job.setStatus(JobStatus.PENDING);

        job.setStartTime(
                LocalDateTime.now());

        jobRepository.save(job);
        

        importProcessor.importCustomers(
                fileData,
                job.getJobId());

        return job.getJobId();
    }
}
