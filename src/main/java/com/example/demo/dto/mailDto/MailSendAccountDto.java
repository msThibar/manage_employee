package com.example.demo.dto.mailDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MailSendAccountDto {
    private String name;
    private String username;
    private String password;
    private String code;
    private String mail;
    private String link;
}
