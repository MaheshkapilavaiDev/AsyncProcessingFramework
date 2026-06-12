package com.asyncprocessingframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyncprocessingframework.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

	@Autowired
	private ReportService reportService;

	@PostMapping("/export")
	public ResponseEntity<String> exportReport() {

		Long jobId = reportService.startReportJob();

		return ResponseEntity.ok("Report Export Started. Job Id : " + jobId);
	}
}
