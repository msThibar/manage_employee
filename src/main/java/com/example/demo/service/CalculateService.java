package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class CalculateService {
    public long calMinute(String date1, String date2, SimpleDateFormat format) throws ParseException {
        return (format.parse(date1).getTime()-format.parse(date2).getTime())/1000/60;
    }
}
