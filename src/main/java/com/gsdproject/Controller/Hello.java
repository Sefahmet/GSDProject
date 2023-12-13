package com.gsdproject.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Hello {
    @GetMapping("/")
    public ResponseEntity<String> helloVisiters(){
        String response = "To aplication click <a href=\"http://131.220.71.188:8080/masterproject/index.html\">" +
                "this address</a> and also you can change weights and coordinates. Make sure the coordinates must be in Bonn.";

        return new ResponseEntity(response, HttpStatus.OK);
    }

}