package com.netcompany.crawltool.service;

import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.netcompany.crawltool.model.Menu;
import com.netcompany.crawltool.model.UpdateTime;
import com.netcompany.crawltool.model.UserInfo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
// ref https://www.youtube.com/watch?v=PF0iyeDmu9E

@Service
@AllArgsConstructor
public class ScraperService {
    //    private static final String URL = "https://pct:Nhatrangg@2022@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx";
    private WebDriver driver; // sửa


//    @PostConstruct
//    void postConstruct() {
//        scrape();
//    }

    public String scrape(UserInfo userInfo) {

        Menu menu = new Menu();

        try {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            int clearPointer = 0;

            driver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            List<WebElement> monDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[2]"));
            List<WebElement> tueDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[3]"));
            List<WebElement> wedDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[4]"));
            List<WebElement> thuDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[5]"));
            List<WebElement> friDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[6]"));



            Timestamp now = new Timestamp(System.currentTimeMillis());
            DocumentSnapshot documentSnapshot = dbFirestore.collection("lunch").document("updateTime").get().get();
            String dtbSatDate = documentSnapshot.get("satDate").toString();
            dtbSatDate = dtbSatDate.substring(0, 10) + " " + dtbSatDate.substring(11, 19);
            Timestamp sat = Timestamp.valueOf(dtbSatDate);
            System.out.println(System.currentTimeMillis());
            System.out.println(sat.getTime());
            Timestamp satNext = new Timestamp(sat.getTime() + 630000000);
            System.out.println(satNext);



            if(now.after(sat)) {
                dbFirestore.collection("lunch").document("updateTime").update("satDate", satNext);
                dbFirestore.collection("lunch").document("mon3").
                        update(dbFirestore.collection("lunch").document("mon2").get().get().getData());
                dbFirestore.collection("lunch").document("mon2").
                        update(dbFirestore.collection("lunch").document("mon1").get().get().getData());
                dbFirestore.collection("lunch").document("mon1").
                        update(dbFirestore.collection("lunch").document("mon").get().get().getData());


                dbFirestore.collection("lunch").document("tue3").
                        update(dbFirestore.collection("lunch").document("tue2").get().get().getData());
                dbFirestore.collection("lunch").document("tue2").
                        update(dbFirestore.collection("lunch").document("tue1").get().get().getData());
                dbFirestore.collection("lunch").document("tue1").
                        update(dbFirestore.collection("lunch").document("tue").get().get().getData());

                dbFirestore.collection("lunch").document("wed3").
                        update(dbFirestore.collection("lunch").document("wed2").get().get().getData());
                dbFirestore.collection("lunch").document("wed2").
                        update(dbFirestore.collection("lunch").document("wed1").get().get().getData());
                dbFirestore.collection("lunch").document("wed1").
                        update(dbFirestore.collection("lunch").document("wed").get().get().getData());

                dbFirestore.collection("lunch").document("thu3").
                        update(dbFirestore.collection("lunch").document("thu2").get().get().getData());
                dbFirestore.collection("lunch").document("thu2").
                        update(dbFirestore.collection("lunch").document("thu1").get().get().getData());
                dbFirestore.collection("lunch").document("thu1").
                        update(dbFirestore.collection("lunch").document("thu").get().get().getData());

                dbFirestore.collection("lunch").document("fri3").
                        update(dbFirestore.collection("lunch").document("fri2").get().get().getData());
                dbFirestore.collection("lunch").document("fri2").
                        update(dbFirestore.collection("lunch").document("fri1").get().get().getData());
                dbFirestore.collection("lunch").document("fri1").
                        update(dbFirestore.collection("lunch").document("fri").get().get().getData());
                }


            String monDessert = monDishes.get(monDishes.size() - 1).getText();
            String monSoup = monDishes.get(monDishes.size() - 2).getText();
            String monSide = monDishes.get(monDishes.size() - 3).getText();
            String monMain = monDishes.get(monDishes.size() - 4).getText();

            List<String> monDessertArr = Arrays.asList(monDessert.split("\\n+"));
            List<String> monSoupArr = Arrays.asList(monSoup.split("\\n+"));
            List<String> monSideArr = Arrays.asList(monSide.split("\\n+"));
            List<String> monMainArr = Arrays.asList(monMain.split("\\n+"));

            for(int i = 0; i < monDessertArr.size(); i ++){
                if(monDessertArr.get(i).contains("/")){
                    String sub[] = monDessertArr.get(i).split("\\s*/\\s*");
                    monDessertArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(monDessertArr.size() > 1){
                    monDessertArr.set(i - clearPointer, monDessertArr.get(i) + " (" + monDessertArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> monDessertDtb = monDessertArr.subList(0, monDessertArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < monSoupArr.size(); i ++){
                if(monSoupArr.get(i).contains("/")){
                    String sub[] = monSoupArr.get(i).split("\\s*/\\s*");
                    monSoupArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(monSoupArr.size() > 1){
                    monSoupArr.set(i - clearPointer, monSoupArr.get(i) + " (" + monSoupArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> monSoupDtb = monSoupArr.subList(0, monSoupArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < monSideArr.size(); i ++){
                if(monSideArr.get(i).contains("/")){
                    String sub[] = monSideArr.get(i).split("\\s*/\\s*");
                    monSideArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(monSideArr.size() > 1){
                    monSideArr.set(i - clearPointer, monSideArr.get(i) + " (" + monSideArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> monSideDtb = monSideArr.subList(0, monSideArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < monMainArr.size(); i ++){
                if(monMainArr.get(i).contains("/")){
                    String sub[] = monMainArr.get(i).split("\\s*/\\s*");
                    monMainArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(monMainArr.size() > 1){
                    monMainArr.set(i - clearPointer, monMainArr.get(i) + " (" + monMainArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> monMainDtb = monMainArr.subList(0, monMainArr.size() - clearPointer);
            clearPointer = 0;

            Map<String, Object> mon = new HashMap<>();
            mon.put("dessert", monDessertDtb);
            mon.put("soup", monSoupDtb);
            mon.put("side", monSideDtb);
            mon.put("main", monMainDtb);
            dbFirestore.collection("lunch").document("mon").update(mon);

            String tueDessert = tueDishes.get(tueDishes.size() - 1).getText();
            String tueSoup = tueDishes.get(tueDishes.size() - 2).getText();
            String tueSide = tueDishes.get(tueDishes.size() - 3).getText();
            String tueMain = tueDishes.get(tueDishes.size() - 4).getText();

            List<String> tueDessertArr = Arrays.asList(tueDessert.split("\\n+"));
            List<String> tueSoupArr = Arrays.asList(tueSoup.split("\\n+"));
            List<String> tueSideArr = Arrays.asList(tueSide.split("\\n+"));
            List<String> tueMainArr = Arrays.asList(tueMain.split("\\n+"));

            for(int i = 0; i < tueDessertArr.size(); i ++){
                if(tueDessertArr.get(i).contains("/")){
                    String sub[] = tueDessertArr.get(i).split("\\s*/\\s*");
                    tueDessertArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(tueDessertArr.size() > 1){
                    tueDessertArr.set(i - clearPointer, tueDessertArr.get(i) + " (" + tueDessertArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> tueDessertDtb = tueDessertArr.subList(0, tueDessertArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < tueSoupArr.size(); i ++){
                if(tueSoupArr.get(i).contains("/")){
                    String sub[] = tueSoupArr.get(i).split("\\s*/\\s*");
                    tueSoupArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(tueSoupArr.size() > 1){
                    tueSoupArr.set(i - clearPointer, tueSoupArr.get(i) + " (" + tueSoupArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> tueSoupDtb = tueSoupArr.subList(0, tueSoupArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < tueSideArr.size(); i ++){
                if(tueSideArr.get(i).contains("/")){
                    String sub[] = tueSideArr.get(i).split("\\s*/\\s*");
                    tueSideArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(tueSideArr.size() > 1){
                    tueSideArr.set(i - clearPointer, tueSideArr.get(i) + " (" + tueSideArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> tueSideDtb = tueSideArr.subList(0, tueSideArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < tueMainArr.size(); i ++){
                if(tueMainArr.get(i).contains("/")){
                    String sub[] = tueMainArr.get(i).split("\\s*/\\s*");
                    tueMainArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(tueMainArr.size() > 1){
                    tueMainArr.set(i - clearPointer, tueMainArr.get(i) + " (" + tueMainArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> tueMainDtb = tueMainArr.subList(0, tueMainArr.size() - clearPointer);
            clearPointer = 0;

            Map<String, Object> tue = new HashMap<>();
            tue.put("dessert", tueDessertDtb);
            tue.put("soup", tueSoupDtb);
            tue.put("side", tueSideDtb);
            tue.put("main", tueMainDtb);
            dbFirestore.collection("lunch").document("tue").update(tue);

            String wedDessert = wedDishes.get(wedDishes.size() - 1).getText();
            String wedSoup = wedDishes.get(wedDishes.size() - 2).getText();
            String wedSide = wedDishes.get(wedDishes.size() - 3).getText();
            String wedMain = wedDishes.get(wedDishes.size() - 4).getText();

            List<String> wedDessertArr = Arrays.asList(wedDessert.split("\\n+"));
            List<String> wedSoupArr = Arrays.asList(wedSoup.split("\\n+"));
            List<String> wedSideArr = Arrays.asList(wedSide.split("\\n+"));
            List<String> wedMainArr = Arrays.asList(wedMain.split("\\n+"));

            for(int i = 0; i < wedDessertArr.size(); i ++){
                if(wedDessertArr.get(i).contains("/")){
                    String sub[] = wedDessertArr.get(i).split("\\s*/\\s*");
                    wedDessertArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(monDessertArr.size() > 1){
                    wedDessertArr.set(i - clearPointer, wedDessertArr.get(i) + " (" + wedDessertArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> wedDessertDtb = wedDessertArr.subList(0, wedDessertArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < wedSoupArr.size(); i ++){
                if(wedSoupArr.get(i).contains("/")){
                    String sub[] = wedSoupArr.get(i).split("\\s*/\\s*");
                    wedSoupArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(wedSoupArr.size() > 1){
                    wedSoupArr.set(i - clearPointer, wedSoupArr.get(i) + " (" + wedSoupArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> wedSoupDtb = wedSoupArr.subList(0, wedSoupArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < wedSideArr.size(); i ++){
                if(wedSideArr.get(i).contains("/")){
                    String sub[] = wedSideArr.get(i).split("\\s*/\\s*");
                    wedSideArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(wedSideArr.size() > 1){
                    wedSideArr.set(i - clearPointer, wedSideArr.get(i) + " (" + wedSideArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> wedSideDtb = wedSideArr.subList(0, wedSideArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < wedMainArr.size(); i ++){
                if(wedMainArr.get(i).contains("/")){
                    String sub[] = wedMainArr.get(i).split("\\s*/\\s*");
                    wedMainArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(wedMainArr.size() > 1){
                    wedMainArr.set(i - clearPointer, wedMainArr.get(i) + " (" + wedMainArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> wedMainDtb = wedMainArr.subList(0, wedMainArr.size() - clearPointer);
            clearPointer = 0;

            Map<String, Object> wed = new HashMap<>();
            wed.put("dessert", wedDessertDtb);
            wed.put("soup", wedSoupDtb);
            wed.put("side", wedSideDtb);
            wed.put("main", wedMainDtb);
            dbFirestore.collection("lunch").document("wed").update(wed);

            String thuDessert = thuDishes.get(thuDishes.size() - 1).getText();
            String thuSoup = thuDishes.get(thuDishes.size() - 2).getText();
            String thuSide = thuDishes.get(thuDishes.size() - 3).getText();
            String thuMain = thuDishes.get(thuDishes.size() - 4).getText();

            List<String> thuDessertArr = Arrays.asList(thuDessert.split("\\n+"));
            List<String> thuSoupArr = Arrays.asList(thuSoup.split("\\n+"));
            List<String> thuSideArr = Arrays.asList(thuSide.split("\\n+"));
            List<String> thuMainArr = Arrays.asList(thuMain.split("\\n+"));

            for(int i = 0; i < thuDessertArr.size(); i ++){
                if(thuDessertArr.get(i).contains("/")){
                    String sub[] = thuDessertArr.get(i).split("\\s*/\\s*");
                    thuDessertArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(thuDessertArr.size() > 1){
                    thuDessertArr.set(i - clearPointer, thuDessertArr.get(i) + " (" + thuDessertArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> thuDessertDtb = thuDessertArr.subList(0, thuDessertArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < thuSoupArr.size(); i ++){
                if(thuSoupArr.get(i).contains("/")){
                    String sub[] = thuSoupArr.get(i).split("\\s*/\\s*");
                    thuSoupArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(thuSoupArr.size() > 1){
                    thuSoupArr.set(i - clearPointer, thuSoupArr.get(i) + " (" + thuSoupArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> thuSoupDtb = thuSoupArr.subList(0, thuSoupArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < thuSideArr.size(); i ++){
                if(thuSideArr.get(i).contains("/")){
                    String sub[] = thuSideArr.get(i).split("\\s*/\\s*");
                    thuSideArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(thuSideArr.size() > 1){
                    thuSideArr.set(i - clearPointer, thuSideArr.get(i) + " (" + thuSideArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> thuSideDtb = thuSideArr.subList(0, thuSideArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < thuMainArr.size(); i ++){
                if(thuMainArr.get(i).contains("/")){
                    String sub[] = thuMainArr.get(i).split("\\s*/\\s*");
                    thuMainArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(thuMainArr.size() > 1){
                    thuMainArr.set(i - clearPointer, thuMainArr.get(i) + " (" + thuMainArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> thuMainDtb = thuMainArr.subList(0, thuMainArr.size() - clearPointer);
            clearPointer = 0;

            Map<String, Object> thu = new HashMap<>();
            thu.put("dessert", thuDessertDtb);
            thu.put("soup", thuSoupDtb);
            thu.put("side", thuSideDtb);
            thu.put("main", thuMainDtb);
            dbFirestore.collection("lunch").document("thu").update(thu);

            String friDessert = friDishes.get(friDishes.size() - 1).getText();
            String friSoup = friDishes.get(friDishes.size() - 2).getText();
            String friSide = friDishes.get(friDishes.size() - 3).getText();
            String friMain = friDishes.get(friDishes.size() - 4).getText();

            List<String> friDessertArr = Arrays.asList(friDessert.split("\\n+"));
            List<String> friSoupArr = Arrays.asList(friSoup.split("\\n+"));
            List<String> friSideArr = Arrays.asList(friSide.split("\\n+"));
            List<String> friMainArr = Arrays.asList(friMain.split("\\n+"));

            for(int i = 0; i < friDessertArr.size(); i ++){
                if(friDessertArr.get(i).contains("/")){
                    String sub[] = friDessertArr.get(i).split("\\s*/\\s*");
                    friDessertArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(friDessertArr.size() > 1){
                    friDessertArr.set(i - clearPointer, friDessertArr.get(i) + " (" + friDessertArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> friDessertDtb = friDessertArr.subList(0, friDessertArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < friSoupArr.size(); i ++){
                if(friSoupArr.get(i).contains("/")){
                    String sub[] = friSoupArr.get(i).split("\\s*/\\s*");
                    friSoupArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(friSoupArr.size() > 1){
                    friSoupArr.set(i - clearPointer, friSoupArr.get(i) + " (" + friSoupArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> friSoupDtb = friSoupArr.subList(0, friSoupArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < friSideArr.size(); i ++){
                if(friSideArr.get(i).contains("/")){
                    String sub[] = friSideArr.get(i).split("\\s*/\\s*");
                    friSideArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(friSideArr.size() > 1){
                    friSideArr.set(i - clearPointer, friSideArr.get(i) + " (" + friSideArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> friSideDtb = friSideArr.subList(0, friSideArr.size() - clearPointer);
            clearPointer = 0;

            for(int i = 0; i < friMainArr.size(); i ++){
                if(friMainArr.get(i).contains("/")){
                    String sub[] = friMainArr.get(i).split("\\s*/\\s*");
                    friMainArr.set(i, sub[0] + " (" + sub[1] + ")");
                }else if(friMainArr.size() > 1){
                    friMainArr.set(i - clearPointer, friMainArr.get(i) + " (" + friMainArr.get(i+1) + ")");
                    clearPointer += 1;
                    i += 1;
                }
            }
            List<String> friMainDtb = friMainArr.subList(0, friMainArr.size() - clearPointer);
            clearPointer = 0;

            Map<String, Object> fri = new HashMap<>();
            fri.put("dessert", friDessertDtb);
            fri.put("soup", friSoupDtb);
            fri.put("side", friSideDtb);
            fri.put("main", friMainDtb);
            dbFirestore.collection("lunch").document("fri").update(fri);




            driver.quit();
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);

            // TimeUnit.SECONDS.sleep(5);

            return("OK");

        } catch (Exception e) {
            System.out.println("Oops! Something went wrong!");
            return null;
        }
    }
}

