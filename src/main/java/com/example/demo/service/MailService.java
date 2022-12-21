package com.example.demo.service;

import com.example.demo.dto.mailDto.MailSendAccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final Logger logger= LoggerFactory.getLogger(MailService.class);
    @Autowired
    JavaMailSender emailSender;

    public String simpleMailSender(){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo("2312vjp@gmail.com");
        message.setSubject("test simple mail");
        message.setText("một ngày đẹp trời chiều thứ sáu - 29/07/2022(01-07 ÂL) ở văn phòng NCC");
        emailSender.send(message);
        return "done!!!";
    }

    public String attachmentMailSender(String pathTxt, String pathImg){
        MimeMessage mimeMessage= emailSender.createMimeMessage();
        boolean multipart= true;
        try {
            MimeMessageHelper helper= new MimeMessageHelper(mimeMessage, multipart);

            /*String[] friends= new String[2];
            friends[1]= "2312vjp@gmail.com";
            friends[0]= "atoan2312@gmail.com";
            helper.setTo(friends);*/

            helper.setTo("2312vjp@gmail.com");
            helper.setSubject("attachment mail");
            helper.setText("Đây là text của helper");
            helper.addAttachment("file.txt", new FileSystemResource(pathTxt));
            helper.addAttachment("ban co.jpg", new FileSystemResource(pathImg));
            emailSender.send(mimeMessage);
            return "done!!!";
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            return "error";
        }
    }
    public StringBuilder HtmlMailSender(){
        StringBuilder html= new StringBuilder("");
        html.append("<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>index page</title>" +
                "</head>");
        html.append("<body>");
        html.append("<h1>welcome to page</h1>");
        html.append("<a href=\"www.facebook.com\"><button>facebook</button></a><br>");
        html.append("</body>");
        String htmlString= html.toString();
        MimeMessage mimeMessage= emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper= new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessage.setText("moi dung khong co gi, cho vui thoi, mail html testl");
            mimeMessage.setSubject("mail gui tu manage employee");
            mimeMessage.setContent(htmlString, "text/html");
            helper.setTo("2312vjp@gmail.com");
            helper.setFrom("a.k.aMsThibar");
            this.emailSender.send(mimeMessage);
            return html;
        } catch (MessagingException e) {
            logger.error(e.toString());
            return new StringBuilder("failed");
        }
    }
    public void SendAccount(MailSendAccountDto content) {
        MimeMessage message= emailSender.createMimeMessage();
        StringBuilder html= new StringBuilder("");
        html.append("<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>index page</title>" +
                "</head>");
        html.append("<body><table><tr><th>Họ tên</th><th>");
        html.append(content.getName());
        html.append("</th></tr><tr><td>tài khoản</td><td>");
        html.append(content.getUsername());
        html.append("</td></tr><tr><td>Mật khẩu</td><td>");
        html.append(content.getPassword());
        html.append("</td></tr><tr><td>code checkin</td><td>");
        html.append(content.getCode());
        html.append("</td></tr></table></div><div><a href=\"");
        html.append(content.getLink());
        html.append("\"></a></body>");
        try {
            MimeMessageHelper helper= new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom("admin");
            helper.setTo(content.getMail());
            helper.setSubject("Tài khoản cho nhân viên mới");
            helper.setFrom("Một Người Bạn");
            message.setContent(html.toString(), "text/html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        emailSender.send(message);
    }
    public void notifyCheckout(String mail){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject("THÔNG BÁO CHECKOUT");
        message.setText("đã 6 giờ tối rồi\nhình như bạn đã quên checkout hôm nay, mau chóng checkout để cuối tháng có lương nhé");
        emailSender.send(message);
    }

}
