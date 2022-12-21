package com.example.demo.controllers;

import com.example.demo.dto.userDto.UserViewDto;
import com.example.demo.dto.userDto.UserUpdateDto;
import com.example.demo.models.ResponseObject;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/Admin")
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/All")
    ResponseEntity<ResponseObject> findAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", userService.findAll(), "find All user successfully")
        );
    }
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable("id") int id){
        UserViewDto foundUser= userService.findById(id);
        if(foundUser!=null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", foundUser, "Query successfully!!!")
            );
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "Cannot find user with id"+id));
        }
    }
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> addUser(@RequestBody UserUpdateDto newUser){
        logger.info(newUser.toString());
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        if(userService.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseObject("Error", null, "found user with username: " + newUser.getUsername())
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", userService.save(newUser), "Insert User successfully")
        );

    }
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable("id") int id){
        if(userService.existsById(id)){
            userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", null, "delete user successfully")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Error", null, "not found user with id: "+id)
        );
    }
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable("id") int id){
        if(userService.existsById(id)){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", null, userService.updateUser(userUpdateDto, id))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Error", null, "not found user with id: "+id)
        );
    }
    @GetMapping("/findHQL")
    ResponseEntity<ResponseObject> findHQL(){
        ArrayList<UserViewDto> list= userService.findHQL();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", list, "truy van thanh cong")
        );
    }

    @GetMapping("/search/{name}")
    ResponseEntity<ResponseObject> findAllByName(@PathVariable("name") String name){
        logger.info("name: "+name);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", userService.searchUsersByName(name), "done!!!")
        );
    }
    @GetMapping("/Error")
    ResponseEntity<?> findUsersErrorOfMonth(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsersErrorOfMonth());
    }
    @GetMapping("/Error/{month}")
    ResponseEntity<?> findUsersErrorOfMonth(@PathVariable("month") String month){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsersErrorOfMonth(month));
    }
    @GetMapping("/TimesheetBetweenTwoDates")
    ResponseEntity<?> findUsersTimesheetBetweenTwoDates(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsersTimesheetBetweenTwoDates());
    }
    @GetMapping("/TimesheetBetweenTwoDates/{start}/{end}")
    ResponseEntity<?> findUsersTimesheetBetweenTwoDates(@PathVariable("start") String start, @PathVariable("end") String end){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUsersTimesheetBetweenTwoDates(start, end));
    }

}
