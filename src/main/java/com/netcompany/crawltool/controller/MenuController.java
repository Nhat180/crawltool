package com.netcompany.crawltool.controller;

import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import com.netcompany.crawltool.service.ScraperService;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    ScraperService scraperService;

    @PostMapping
    public ResponseEntity<String> getMenu (@RequestBody UserInfo userInfo) {
       if (scraperService.scrape(userInfo) != null) {
           return new ResponseEntity<>("Verification is successful! Please go back your Home Page to enjoy!", HttpStatus.OK);
       } else {
           return new ResponseEntity<>("Oops", HttpStatus.BAD_REQUEST);
       }
    }
}
