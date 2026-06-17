package com.asyncprocessingframework.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.asyncprocessingframework.service.CustomerImportService;

@RestController
@RequestMapping("/api/customers")
public class CustomerImportController {

	@Autowired
	private CustomerImportService importService;

	@PostMapping("/import")
	public ResponseEntity<String> importCustomers(@RequestParam("file") MultipartFile file) throws IOException {

		Long jobId = importService.importCustomers(file);

		return ResponseEntity.ok("Customer import started. Job Id : " + jobId);
	}
}
