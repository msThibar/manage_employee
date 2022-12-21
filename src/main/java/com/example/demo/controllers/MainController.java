package com.example.demo.controllers;

import com.example.demo.dto.LoginDto;
import com.example.demo.models.GooglePojo;
import com.example.demo.models.GoogleUtils;
import com.example.demo.models.ResponseObject;
import com.example.demo.service.UserService;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    GoogleUtils googleUtils;
    private  final Logger logger= LoggerFactory.getLogger(MainController.class);
    @RequestMapping(value= {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model){
        LoginDto loginDto= new LoginDto();
        String message= "hello word";
        model.addAttribute("mess", message);
        model.addAttribute("loginDto", loginDto);
        return "index";
    }
    @RequestMapping(value= {"/login"}, method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value={"/login"}, method = RequestMethod.POST)
    public String login(HttpServletRequest request, Model model){
        LoginDto loginDto= new LoginDto(request.getParameter("username"), request.getParameter("password"));
        System.out.println(loginDto.toString());
        model.addAttribute("token", userService.login(loginDto));
        return "loginSuccessfull";
    }
    @RequestMapping("/loginWithGoogle")
    ResponseEntity<?> loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
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
    @RequestMapping("/logout")
    String logout(){
        return "login";
    }

    @RequestMapping(value = {"/error"}, method = RequestMethod.GET)
    public String ErrorPage(){
        return "error";
    }

}
