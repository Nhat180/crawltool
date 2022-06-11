package com.netcompany.crawltool.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {
//    @PostConstruct
//    void postConstruct(){
//        System.setProperty("webdriver.chrome.driver", "/app/.chromedriver/bin/chromedriver");
//        WebDriverManager.chromedriver().setup();
//    }

    @Bean
    public ChromeDriver driver(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
//        options.setBinary("/app/.apt/usr/bin/google-chrome");
        return new ChromeDriver(options);
    }
}
