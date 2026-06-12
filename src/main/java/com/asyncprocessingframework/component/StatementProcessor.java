package com.asyncprocessingframework.component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatementProcessor {

	@Autowired
	private  AsyncJobRepository jobRepository;

	@Async("bankExecutor")
	public CompletableFuture<Void> generateStatement(Long customerId, Long jobId) {

		AsyncJob job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

		try {

			job.setStatus(JobStatus.RUNNING);
			jobRepository.save(job);

			System.out.println("Generating statement for Customer : " + customerId);

			// Simulate statement generation
			Thread.sleep(10000);

			// Example PDF path
			String pdfPath = "statements/customer_" + customerId + ".pdf";

			job.setStatus(JobStatus.COMPLETED);
			job.setResultMessage("Statement Generated : " + pdfPath);
			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);

			System.out.println("Statement generated successfully");

		} catch (Exception ex) {

			job.setStatus(JobStatus.FAILED);
			job.setErrorMessage(ex.getMessage());
			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);

			throw new RuntimeException(ex);
		}

		return CompletableFuture.completedFuture(null);
	}
}
