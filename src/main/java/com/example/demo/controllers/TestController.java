package com.example.demo.controllers;

import com.example.demo.dto.userDto.UserUpdateDto;
import com.example.demo.models.GooglePojo;
import com.example.demo.models.GoogleUtils;
import com.example.demo.models.ResponseObject;
import com.example.demo.service.TimesheetService;
import com.example.demo.service.UserService;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TimesheetService timesheetService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    UserService userService;
    @Autowired
    GoogleUtils googleUtils;
    private Logger logger= LoggerFactory.getLogger(TestController.class);
    @GetMapping("/updateCheckoutEarly")
    public String updateCheckoutEarly(){
        timesheetService.updateStatus();
        return "done!!!";
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserUpdateDto userUpdateDto){
        String encodedPassword= encoder.encode(userUpdateDto.getPassword());
        userUpdateDto.setPassword(encodedPassword);
        userService.save(userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("done!!!");
    }
    @RequestMapping("/loginWithGoogle")
    ResponseEntity<?> loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        logger.info("login with google:  run!!!");
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("error", null,"đăng nhập bằng google thất bại")
            );
        }
        String accessToken = googleUtils.getToken(code);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        logger.info(googlePojo.toString());
        String token= userService.loginWithEmail(googlePojo.getEmail());
        logger.info(token);
        return ResponseEntity.ok(token);
    }
    /*@GetMapping("/betweenDates")
    ResponseEntity<?> betweenDates(){
        return ResponseEntity.status(HttpStatus.OK).body(timesheetService.findTimesheetBetweenTwoDates());
    }*/

}
