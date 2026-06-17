package com.asyncprocessingframework.service;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

//@Service
/*public class RetryService {

    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000))
    public void retryEmail() {

        System.out.println(
                "Retrying Email Service");

        throw new RuntimeException(
                "Mail Server Down");
    }

    @Recover
    public void recover(Exception ex) {

        System.out.println(
                "Recovery Method Executed");
    }
}*/
