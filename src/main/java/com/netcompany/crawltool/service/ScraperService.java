package com.netcompany.crawltool.service;

import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
// ref https://www.youtube.com/watch?v=PF0iyeDmu9E

@Service
@AllArgsConstructor
public class ScraperService {
//    private static final String URL = "https://pct:Nhatrangg@2022@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx";
    private final WebDriver driver; // sửa
    

//    @PostConstruct
//    void postConstruct() {
//        scrape();
//    }

    public Menu scrape(UserInfo userInfo) {

        Menu menu = new Menu();
        
        try {
            driver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            List<WebElement> monDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[2]"));
            List<WebElement> tueDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[3]"));
            List<WebElement> wedDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[4]"));
            List<WebElement> thuDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[5]"));
            List<WebElement> friDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[6]"));

            monDishes.forEach(word -> System.out.println(word.getText()));
            tueDishes.forEach(word -> System.out.println(word.getText()));
            wedDishes.forEach(word -> System.out.println(word.getText()));
            thuDishes.forEach(word -> System.out.println(word.getText()));
            friDishes.forEach(word -> System.out.println(word.getText()));



//            for (WebElement monDish : monDishes) {
//                menu.setMainDish(monDish.getText());
//            }

//            menu.setMainDish(monDishes.get(0).getText());
//
//            ArrayList<String> word = new ArrayList<>();
//
//            for (int i = 0; i < monDishes.size(); i++) {
//               word.add(monDishes.get(i).getText());
//            }
//
//            System.out.println(Arrays.toString(word.toArray()));

            driver.quit();
            // TimeUnit.SECONDS.sleep(5);

            return menu;

        } catch (Exception e) {
            System.out.println("Oops! Something went wrong!");
            return null;
        }
    }
}