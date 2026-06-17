package com.asyncprocessingframework.component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.entity.Customer;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;
import com.asyncprocessingframework.repository.CustomerRepository;

@Component
public class CustomerImportProcessor {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AsyncJobRepository jobRepository;

	@Async("bankExecutor")
	public CompletableFuture<Void> importCustomers(byte[] fileData, Long jobId) {

		AsyncJob job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

		try {

			job.setStatus(JobStatus.RUNNING);
			jobRepository.save(job);

			BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileData)));

			String line;

			// Skip CSV Header
			reader.readLine();

			int importedCount = 0;

			while ((line = reader.readLine()) != null) {

				String[] data = line.split(",");

				if (data.length < 3) {
					continue;
				}

				Customer customer = new Customer();

				customer.setCustomerName(data[0].trim());
				customer.setEmail(data[1].trim());
				customer.setMobile(data[2].trim());

				customerRepository.save(customer);

				importedCount++;
			}

			reader.close();

			job.setStatus(JobStatus.COMPLETED);
			job.setResultMessage(importedCount + " customers imported");

			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);

		} catch (Exception ex) {

			ex.printStackTrace();

			job.setStatus(JobStatus.FAILED);
			job.setErrorMessage(ex.getMessage());
			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);
		}

		return CompletableFuture.completedFuture(null);
	}
}