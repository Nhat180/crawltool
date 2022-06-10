package com.netcompany.crawltool.controller;

import com.netcompany.crawltool.model.UserInfo;
import com.netcompany.crawltool.service.AuthService;
import com.netcompany.crawltool.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping
    public String hello() {
        return "Hello Crawl server";
    }


    @PostMapping
    public ResponseEntity<String> login (@RequestBody UserInfo userInfo) {
        if(authService.login(userInfo) != null) {
            return new ResponseEntity<>("Verification is successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Oops!", HttpStatus.BAD_REQUEST);
        }
    }
}
