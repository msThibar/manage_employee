package com.example.demo.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {
    private String status;
    private Object data;
    private String message;
}
