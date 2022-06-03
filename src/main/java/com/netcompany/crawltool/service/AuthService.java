package com.netcompany.crawltool.service;

import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private final WebDriver webDriver;

    public String login(UserInfo userInfo) {
        try {
            webDriver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            List<WebElement> monDishes = webDriver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[2]"));
            monDishes.forEach(word -> System.out.println(word.getText()));
            System.out.println(monDishes.get(0).getText());
            webDriver.quit();
            return "Verify";
        } catch (Exception e) {
            System.out.println("Oops something went wrong");
            return null;
        }
    }
}
