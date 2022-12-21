package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender= new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("2312vip@gmail.com");
        mailSender.setPassword("hjoskhdagrfcbvny");

        Properties prop= mailSender.getJavaMailProperties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.debug", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }
}
