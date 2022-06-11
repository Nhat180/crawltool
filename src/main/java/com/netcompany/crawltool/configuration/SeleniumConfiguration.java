package com.netcompany.crawltool.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {
    @PostConstruct
    void postConstruct(){
//        System.setProperty("webdriver.chrome.driver", ".\\src\\chromedriver\\chromedriver.exe");
        WebDriverManager.chromedriver().setup();
    }

    @Bean
    public ChromeDriver driver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }
}
