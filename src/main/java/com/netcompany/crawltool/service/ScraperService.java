package com.netcompany.crawltool.service;

import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DataSnapshot;
import com.google.type.TimeOfDay;
import com.netcompany.crawltool.model.DailyMenu;
import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UserInfo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Array;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    public String scrape(UserInfo userInfo) {

        Menu menu = new Menu();
        
        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();

            driver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            List<WebElement> monDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[2]"));
            List<WebElement> tueDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[3]"));
            List<WebElement> wedDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[4]"));
            List<WebElement> thuDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[5]"));
            List<WebElement> friDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[6]"));



            String monDessert = monDishes.get(monDishes.size() - 1).getText();
            String monSoup = monDishes.get(monDishes.size() - 2).getText();
            String monSide = monDishes.get(monDishes.size() - 3).getText();
            String monMain = monDishes.get(monDishes.size() - 4).getText();

            String monDessertArr[] = monDessert.split("\\n+");
            String monSoupArr[] = monSoup.split("\\n+");
            String monSideArr[] = monSide.split("\\n+");
            String monMainArr[] = monMain.split("\\n+");

            Map<String, Object> mon = new HashMap<>();
            mon.put("dessert", Arrays.asList(monDessertArr));
            mon.put("soup", Arrays.asList(monSoupArr));
            mon.put("side", Arrays.asList(monSideArr));
            mon.put("main", Arrays.asList(monMainArr));
            dbFirestore.collection("lunch").document("mon").update(mon);

            String tueDessert = tueDishes.get(tueDishes.size() - 1).getText();
            String tueSoup = tueDishes.get(tueDishes.size() - 2).getText();
            String tueSide = tueDishes.get(tueDishes.size() - 3).getText();
            String tueMain = tueDishes.get(tueDishes.size() - 4).getText();

            String tueDessertArr[] = tueDessert.split("\\n+");
            String tueSoupArr[] = tueSoup.split("\\n+");
            String tueSideArr[] = tueSide.split("\\n+");
            String tueMainArr[] = tueMain.split("\\n+");

            Map<String, Object> tue = new HashMap<>();
            tue.put("dessert", Arrays.asList(tueDessertArr));
            tue.put("soup", Arrays.asList(tueSoupArr));
            tue.put("side", Arrays.asList(tueSideArr));
            tue.put("main", Arrays.asList(tueMainArr));
            dbFirestore.collection("lunch").document("tue").update(tue);

            String wedDessert = wedDishes.get(wedDishes.size() - 1).getText();
            String wedSoup = wedDishes.get(wedDishes.size() - 2).getText();
            String wedSide = wedDishes.get(wedDishes.size() - 3).getText();
            String wedMain = wedDishes.get(wedDishes.size() - 4).getText();

            String wedDessertArr[] = wedDessert.split("\\n+");
            String wedSoupArr[] = wedSoup.split("\\n+");
            String wedSideArr[] = wedSide.split("\\n+");
            String wedMainArr[] = wedMain.split("\\n+");

            Map<String, Object> wed = new HashMap<>();
            wed.put("dessert", Arrays.asList(wedDessertArr));
            wed.put("soup", Arrays.asList(wedSoupArr));
            wed.put("side", Arrays.asList(wedSideArr));
            wed.put("main", Arrays.asList(wedMainArr));
            dbFirestore.collection("lunch").document("wed").update(wed);

            String thuDessert = thuDishes.get(thuDishes.size() - 1).getText();
            String thuSoup = thuDishes.get(thuDishes.size() - 2).getText();
            String thuSide = thuDishes.get(thuDishes.size() - 3).getText();
            String thuMain = thuDishes.get(thuDishes.size() - 4).getText();

            String thuDessertArr[] = thuDessert.split("\\n+");
            String thuSoupArr[] = thuSoup.split("\\n+");
            String thuSideArr[] = thuSide.split("\\n+");
            String thuMainArr[] = thuMain.split("\\n+");

            Map<String, Object> thu = new HashMap<>();
            thu.put("dessert", Arrays.asList(thuDessertArr));
            thu.put("soup", Arrays.asList(thuSoupArr));
            thu.put("side", Arrays.asList(thuSideArr));
            thu.put("main", Arrays.asList(thuMainArr));
            dbFirestore.collection("lunch").document("thu").update(thu);

            String friDessert = friDishes.get(friDishes.size() - 1).getText();
            String friSoup = friDishes.get(friDishes.size() - 2).getText();
            String friSide = friDishes.get(friDishes.size() - 3).getText();
            String friMain = friDishes.get(friDishes.size() - 4).getText();

            String friDessertArr[] = friDessert.split("\\n+");
            String friSoupArr[] = friSoup.split("\\n+");
            String friSideArr[] = friSide.split("\\n+");
            String friMainArr[] = friMain.split("\\n+");

            Map<String, Object> fri = new HashMap<>();
            fri.put("dessert", Arrays.asList(friDessertArr));
            fri.put("soup", Arrays.asList(friSoupArr));
            fri.put("side", Arrays.asList(friSideArr));
            fri.put("main", Arrays.asList(friMainArr));
            dbFirestore.collection("lunch").document("fri").update(fri);

//            LocalDateTime now = LocalDateTime.now();
//
//            ApiFuture<DocumentSnapshot> doc = dbFirestore.collection("lunch").document("updateTime").get(FieldMask.of("satDate"));



//            driver.quit();
            // TimeUnit.SECONDS.sleep(5);

            return("OK");

        } catch (Exception e) {
            System.out.println("Oops! Something went wrong!");
            return null;
        }
    }
}