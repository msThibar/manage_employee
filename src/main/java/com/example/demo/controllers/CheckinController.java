package com.example.demo.controllers;

import com.example.demo.models.ResponseObject;
import com.example.demo.service.TimesheetService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckinController {
    private final Logger logger= LoggerFactory.getLogger(CheckinController.class);
    @Autowired
    TimesheetService timesheetService;
    @Autowired
    UserService userService;
    @PostMapping("/checkin")
    public ResponseEntity<ResponseObject> checkIn(@RequestBody String code){
        logger.info(code);
        if(!userService.existsByCode(code))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("not found", null, "mã code sai")
            );
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", timesheetService.checkin(code), "check in thành công")
        );
    }
}
