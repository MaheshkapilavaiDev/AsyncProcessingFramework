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

import com.asyncprocessingframework.entity.Account;
import com.asyncprocessingframework.entity.AsyncJob;
import com.asyncprocessingframework.entity.Customer;
import com.asyncprocessingframework.entity.Transaction;
import com.asyncprocessingframework.enums.JobStatus;
import com.asyncprocessingframework.repository.AccountRepository;
import com.asyncprocessingframework.repository.AsyncJobRepository;
import com.asyncprocessingframework.repository.CustomerRepository;
import com.asyncprocessingframework.repository.TransactionRepository;
import com.asyncprocessingframework.service.AuditService;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class StatementProcessor {

    @Autowired
    private AsyncJobRepository jobRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuditService auditService;

    @Value("${statement.storage.path}")
    private String folderPath;

    @Async("bankExecutor")
    public CompletableFuture<Void> generateStatement(Long customerId, Long jobId) {

        AsyncJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        try {

            //CANCEL CHECK 
            if (job.getStatus() == JobStatus.CANCELLED) {
                auditService.log(jobId, "STATEMENT", "CANCELLED", "Job cancelled before start");
                return CompletableFuture.completedFuture(null);
            }

            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);
            
            // this black is used to check CancelJob Api working or not
            
           /* try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }*/

            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Account account = accountRepository.findByCustomerCustomerId(customerId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            List<Transaction> transactions =
                    transactionRepository.findByAccountAccountId(account.getAccountId());

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath =
                    folderPath + "/customer_" + customerId + "_" + jobId + ".pdf";

            auditService.log(jobId, "STATEMENT", "STARTED", "Generation started");
            

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("BANK STATEMENT"));
            document.add(new Paragraph("Generated On : " + LocalDateTime.now()));
            document.add(new Paragraph("Customer Name : " + customer.getCustomerName()));
            document.add(new Paragraph("Email : " + customer.getEmail()));
            document.add(new Paragraph("Mobile : " + customer.getMobile()));
            document.add(new Paragraph("Account Number : " + account.getAccountNumber()));
            document.add(new Paragraph("Account Type : " + account.getAccountType()));
            document.add(new Paragraph("Balance : " + account.getBalance()));

            document.add(new Paragraph("------------ Transactions ------------"));

            for (Transaction tx : transactions) {

                //CANCEL CHECK
                AsyncJob latestJob = jobRepository.findById(jobId).orElseThrow();

                if (latestJob.getStatus() == JobStatus.CANCELLED) {
                    document.close();
                    auditService.log(jobId, "STATEMENT", "CANCELLED", "Job cancelled during execution");
                    return CompletableFuture.completedFuture(null);
                }

                document.add(new Paragraph(
                        tx.getTransactionDate() + " | " +
                        tx.getTransactionType() + " | " +
                        tx.getAmount()));
            }

            document.close();

            job.setStatus(JobStatus.COMPLETED);
            job.setResultMessage(filePath);
            job.setEndTime(LocalDateTime.now());
            jobRepository.save(job);

            auditService.log(jobId, "STATEMENT", "SUCCESS", filePath);

        } catch (Exception ex) {

            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            job.setEndTime(LocalDateTime.now());
            jobRepository.save(job);

            auditService.log(jobId, "STATEMENT", "FAILED", ex.getMessage());

            throw new RuntimeException("Statement generation failed", ex);
        }

        return CompletableFuture.completedFuture(null);
    }
}