package com.xst.bigwhite.controllers;

import java.security.Principal;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javassist.NotFoundException;

@RestController
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/")
    ResponseEntity<?> home(){
    	ResponseEntity<?> resp= new ResponseEntity<String>("hello world",HttpStatus.CREATED);
    	
        //throw new UserNotFoundException("aa");
    	return resp;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
      return user;
    }

    /*public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }*/
}
