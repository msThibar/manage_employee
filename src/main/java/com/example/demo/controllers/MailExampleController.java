package com.example.demo.controllers;

import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
@RequestMapping("/mail")
public class MailExampleController {
    @Autowired
    public MailService mailService;

    @GetMapping("/sendSimpleEmail")
    public String sendSimpleEmail(){
        mailService.simpleMailSender();
        return "done!!!";
    }
    //gui mail co dinh kem file
    @GetMapping("/sendAttachmentMail")
    public String sendAttachmentMail(){
        String pathTxt= "E:\\admin\\Desktop\\database_manage_employee.txt";
        String pathImage= "E:\\admin\\Download\\ban_co.jpg";
        return mailService.attachmentMailSender(pathTxt, pathImage);
    }
    @GetMapping("/sendHtmlMail")
    public StringBuilder sendHtmlMail() {
        return mailService.HtmlMailSender();
    }
}
