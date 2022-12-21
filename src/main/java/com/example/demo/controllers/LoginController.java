package com.example.demo.controllers;

import com.example.demo.dto.LoginDto;
import com.example.demo.models.GoogleUtils;
import com.example.demo.models.ResponseObject;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    GoogleUtils googleUtils;

    private final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        /*String token= userService.login(loginDto);
        return ResponseEntity.ok(token);*/
        boolean isCorrect= encoder.matches(loginDto.getPassword(), userService.findPasswordByUsername(loginDto.getUsername()));
        if(isCorrect){
            String token= userService.login(loginDto);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("error", null,"username or password incorrect")
        );
    }

}
