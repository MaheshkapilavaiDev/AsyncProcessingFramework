package com.asyncprocessingframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyncprocessingframework.service.StatementService;

@RestController
@RequestMapping("/api/statements")
public class StatementController {
	
	@Autowired
	private StatementService statementService;
	
	@PostMapping("/generate/{customerId}")
	public ResponseEntity<String> generateStatement(
	        @PathVariable Long customerId) {

	    Long jobId =
	      statementService.startStatementJob(customerId);

	    return  ResponseEntity.ok(
                "Statement generation started. Job Id : "
                        + jobId);
	}

}
