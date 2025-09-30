package com.pw.controller;

import com.pw.common.controller.BaseController;
import com.pw.common.controller.convertController;
import com.pw.common.utils.Result;
import com.pw.domain.EmailMessage;
import com.pw.service.impl.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "邮箱")
@RequestMapping("/emails")
public class EmailController extends BaseController implements convertController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Operation(summary = "发送邮件")
    public Result send(@RequestBody EmailMessage emailMessage) {
        emailService.sendEmail(emailMessage);
        return Result.ok();
    }
}
