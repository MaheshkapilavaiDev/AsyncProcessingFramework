package com.asyncprocessingframework.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asyncprocessingframework.entity.AuditLog;
import com.asyncprocessingframework.repository.AuditLogRepository;

@Service
public class AuditService {

	@Autowired
	private AuditLogRepository auditLogRepository;

	public void log(Long jobId, String action, String status, String message) {

		AuditLog log = new AuditLog();

		log.setJobId(jobId);
		log.setAction(action);
		log.setStatus(status);
		log.setMessage(message);
		log.setTimestamp(LocalDateTime.now());

		auditLogRepository.save(log);
	}
}
