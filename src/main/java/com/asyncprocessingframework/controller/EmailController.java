package com.asyncprocessingframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asyncprocessingframework.component.EmailProcessor;
import com.asyncprocessingframework.dto.EmailRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

	@Autowired
    private  EmailProcessor emailProcessor;

    @PostMapping("/send")
    public String sendEmail(
            @RequestBody EmailRequest request) {

        emailProcessor.sendEmail(request);

        return "Email Request Accepted";
    }
}
