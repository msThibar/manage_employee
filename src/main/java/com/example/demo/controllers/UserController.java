package com.example.demo.controllers;

import com.example.demo.Enum.Week;
import com.example.demo.models.ResponseObject;
import com.example.demo.service.TimesheetService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/User")
public class UserController {
    private final Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    TimesheetService timesheetService;
    @GetMapping("/profile")
    public ResponseEntity<ResponseObject> profile(){
        String username= getUsernameOfAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", userService.viewProfileByUsername(username), "correct!!!")
        );
    }
    @GetMapping("/Timesheet")
    public ResponseEntity<?> viewTimesheetOfMonth(){
        String username= getUsernameOfAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(timesheetService.findTimesheetErrorByUsername(username));
    }
    @GetMapping("/Timesheet/{month}")
    public ResponseEntity<?> viewTimesheetOfMonth(@PathVariable("month") String month){
        String username= getUsernameOfAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(timesheetService.findTimesheetErrorByUsernameAndMonth(month, username));
    }

    @GetMapping("/TimesheetBetweenDates/default")
    ResponseEntity<?> betweenDates(){
        String username= getUsernameOfAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(timesheetService.findTimesheetOfWeek(username));
    }
    @GetMapping("/TimesheetBetweenDates")
    ResponseEntity<?> betweenDates(@RequestParam("start") String start, @RequestParam("end") String end){
        logger.info(start+"____"+end);
        String username= getUsernameOfAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(timesheetService.findTimesheetBetweenTwoDates(start, end, username));
    }
    private String getUsernameOfAuthentication(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username= principal.toString();
        }
        return username;
    }
}
