package com.asyncprocessingframework.component;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.entity.Transaction;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AsyncJobRepository;
import com.asyncprocessingframework.repository.TransactionRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportProcessor {

	@Autowired
	private AsyncJobRepository jobRepository;
	
	@Autowired
	private  TransactionRepository transactionRepository;
	
	 @Value("${report.storage.path}")
	    private String folderPath;

	@Async("bankExecutor")
	public CompletableFuture<Void> exportReport(Long jobId) {

		AsyncJob job = jobRepository.findById(jobId).orElseThrow(()->
        new RuntimeException("Job not found"));

		try {

			job.setStatus(JobStatus.RUNNING);
			jobRepository.save(job);
			
			 List<Transaction> transactions =
	                    transactionRepository.findAll();

			    File folder = new File(folderPath);
			    
			    if (!folder.exists()) {
	                folder.mkdirs();
	            }
			    
			    String filePath =
	                    folderPath +
	                    "/DailyTransactionReport_"
	                    + jobId
	                    + ".pdf";

			    Document document = new Document();

	            PdfWriter.getInstance(
	                    document,
	                    new FileOutputStream(filePath));

	            document.open();

	            document.add(
	                    new Paragraph(
	                            "BANK TRANSACTION REPORT"));

	            document.add(
	                    new Paragraph(
	                            "Generated On : "
	                            + LocalDateTime.now()));

	            document.add(
	                    new Paragraph(
	                            "-----------------------------------"));

	            double totalCredits = 0;
	            double totalDebits = 0;
	            
	            for (Transaction tx : transactions) {

	                document.add(
	                        new Paragraph(
	                                tx.getTransactionDate()
	                                + " | "
	                                + tx.getTransactionType()
	                                + " | "
	                                + tx.getAmount()));

	                if ("CREDIT".equalsIgnoreCase(
	                        tx.getTransactionType())) {

	                    totalCredits += tx.getAmount();
	                }

	                if ("DEBIT".equalsIgnoreCase(
	                        tx.getTransactionType())) {

	                    totalDebits += tx.getAmount();
	                }
	            }
	            
	            document.add(
	                    new Paragraph(
	                            "-----------------------------------"));

	            document.add(
	                    new Paragraph(
	                            "Total Credits : "
	                            + totalCredits));

	            document.add(
	                    new Paragraph(
	                            "Total Debits : "
	                            + totalDebits));

	            document.close();
	            job.setStatus(JobStatus.COMPLETED);
	            job.setResultMessage(filePath);
	            job.setEndTime(LocalDateTime.now());

	            jobRepository.save(job);

		} catch (Exception e) {

			job.setStatus(JobStatus.FAILED);
			job.setErrorMessage(e.getMessage());
			job.setEndTime(LocalDateTime.now());

			jobRepository.save(job);
		}

		return CompletableFuture.completedFuture(null);
	}
}
