package com.asyncprocessingframework.component;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.asyncprocessingframework.dto.EmailRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailProcessor {

    @Async("bankExecutor")
    public CompletableFuture<Void> sendEmail(
            EmailRequest request) {

        try {

            System.out.println(
                    "Sending Email To : "
                            + request.getEmail());

            Thread.sleep(5000);

            System.out.println(
                    "Email Sent Successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }
}
