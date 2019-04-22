package com.qf.utils;

import com.qf.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(Email email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(from,"英雄联盟官网");
            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setSubject(email.getTitle());
            mimeMessageHelper.setText(email.getContent(),true);
            mimeMessageHelper.setSentDate(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(mimeMessage);

    }
}
