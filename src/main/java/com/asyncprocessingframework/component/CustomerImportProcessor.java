package com.asyncprocessingframework.component;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;
import com.asyncprocessingframework.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerImportProcessor {

	@Autowired
    private  CustomerRepository customerRepository;
	
	@Autowired
    private  AsyncJobRepository jobRepository;

    @Async("bankExecutor")
    public CompletableFuture<Void> importCustomers(
            MultipartFile file,
            Long jobId) {

        AsyncJob job =
                jobRepository.findById(jobId)
                        .orElseThrow();

        try {

            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);

            // Parse CSV
            // Save Customers

            Thread.sleep(15000);

            job.setStatus(JobStatus.COMPLETED);
            job.setResultMessage(
                    "Customer Import Completed");

            jobRepository.save(job);

        } catch (Exception e) {

            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());

            jobRepository.save(job);
        }

        return CompletableFuture.completedFuture(null);
    }
}
