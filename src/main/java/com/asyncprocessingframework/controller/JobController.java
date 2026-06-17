package com.asyncprocessingframework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

	@Autowired
    private  AsyncJobRepository repository;

    @GetMapping("/{jobId}")
    public AsyncJob getStatus(
            @PathVariable Long jobId) {

        return repository.findById(jobId)
                .orElseThrow();
    }

    @GetMapping("/history")
    public List<AsyncJob> history() {

        return repository.findAll();
    }

    @GetMapping("/running")
    public List<AsyncJob> runningJobs() {

        return repository.findByStatus(
                JobStatus.RUNNING);
    }
}
