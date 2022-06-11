package com.netcompany.crawltool.service;

import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private WebDriver webDriver;

    public String login(UserInfo userInfo) {
        try {
            webDriver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            WebElement data = webDriver.findElement(By.xpath("/html/body/form/div[14]/div/div[2]/div[2]/div[3]/table/tbody/tr[2]/td/table/tbody/tr/td[1]/div/div[1]/div[1]/div[2]/div[1]/table/tbody/tr[1]/td[2]"));
            System.out.println(data.getText());
            webDriver.quit();
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless");
            webDriver = new ChromeDriver(options);
            return "Verify";
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
