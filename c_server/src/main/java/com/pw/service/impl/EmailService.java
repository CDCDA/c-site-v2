package com.pw.service.impl;
/***
 * @author cyd
 * @date 2025/2/21 14:57
 * @description <>
 **/

import com.pw.domain.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private Environment env;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送简单邮件
     *
     * @param emailMessage 邮件信息
     */
    public void sendEmail(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username")); // 发件人邮箱
        message.setTo(emailMessage.getTo()); // 收件人邮箱
        message.setSubject(emailMessage.getSubject()); // 邮件主题
        message.setText(emailMessage.getText()); // 邮件内容
        javaMailSender.send(message);
        System.out.println("邮件发送成功！");
    }
}
