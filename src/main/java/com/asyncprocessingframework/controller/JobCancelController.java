package com.asyncprocessingframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyncprocessingframework.service.JobCancelService;

@RestController
@RequestMapping("/api/jobs")
public class JobCancelController {

    @Autowired
    private JobCancelService jobService;

    @PostMapping("/cancel/{jobId}")
    public ResponseEntity<String> cancelJob(@PathVariable Long jobId) {

        jobService.cancelJob(jobId);

        return ResponseEntity.ok("Job cancellation requested: " + jobId);
    }
}
