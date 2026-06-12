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
public class ReportProcessor {

	@Autowired
	private AsyncJobRepository jobRepository;

	@Async("bankExecutor")
	public CompletableFuture<Void> exportReport(Long jobId) {

		AsyncJob job = jobRepository.findById(jobId).orElseThrow();

		try {

			job.setStatus(JobStatus.RUNNING);
			jobRepository.save(job);

			// Generate Excel/PDF report
			Thread.sleep(15000);

			job.setStatus(JobStatus.COMPLETED);
			job.setResultMessage("DailyTransactionReport.xlsx");
			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);

		} catch (Exception e) {

			job.setStatus(JobStatus.FAILED);
			job.setErrorMessage(e.getMessage());

			jobRepository.save(job);
		}

		return CompletableFuture.completedFuture(null);
	}
}
