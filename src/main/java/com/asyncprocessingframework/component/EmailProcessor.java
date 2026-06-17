package com.asyncprocessingframework.component;


import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.asyncprocessingframework.dto.EmailRequest;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailProcessor {

	@Autowired
    private  JavaMailSender mailSender;
	
	@Autowired
    private  AsyncJobRepository jobRepository;

    @Async("bankExecutor")
    public CompletableFuture<Void> sendEmail(
            EmailRequest request,
            Long jobId) {

        AsyncJob job = jobRepository.findById(jobId)
                .orElseThrow();

        try {

            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);
            System.out.println(
                    "Sending email to: "
                    + request.getTo());

            Thread.sleep(5000);

            System.out.println(
                    "Email sent successfully");

           /* SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());

            mailSender.send(message);*/

            job.setStatus(JobStatus.COMPLETED);
            job.setResultMessage("Email sent successfully");
            job.setEndTime(LocalDateTime.now());

            jobRepository.save(job);

        } catch (Exception ex) {

            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            job.setEndTime(LocalDateTime.now());

            jobRepository.save(job);
        }

        return CompletableFuture.completedFuture(null);
    }
}