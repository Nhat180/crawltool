package com.netcompany.crawltool.controller;

import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import com.netcompany.crawltool.service.ScraperService;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    ScraperService scraperService;

    @PostMapping
    public ResponseEntity getMenu (@RequestBody UserInfo userInfo) {
        return scraperService.scrape(userInfo);
    }

//    @GetMapping
//    public void test (){
//        scraperService.write();
//        return;
//    }

}
