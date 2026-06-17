package com.asyncprocessingframework.service;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;

@Service
public class RetryService {

	/*@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public void executeWithRetry() {

		System.out.println("Executing operation...");

		throw new RuntimeException("Temporary Failure");
	}

	@Recover
	public void recover(Exception ex) {

		System.out.println("All retries exhausted");
	}*/
}
