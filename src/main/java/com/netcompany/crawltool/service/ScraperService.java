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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity scrape(UserInfo userInfo) {

        Menu menu = new Menu();
        Boolean flag = true;

        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            driver = new ChromeDriver(options);

            Firestore dbFirestore = FirestoreClient.getFirestore();

            if(!updateOnTime(dbFirestore)){
                flag = false;
            }

            driver.get("https://" + userInfo.getUsername() + ":" + userInfo.getPassword() + "@goto.netcompany.com/cases/GTE676/NCVNOFF/default.aspx");
            List<WebElement> monDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[2]"));
            List<WebElement> tueDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[3]"));
            List<WebElement> wedDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[4]"));
            List<WebElement> thuDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[5]"));
            List<WebElement> friDishes = driver.findElements(By.xpath("//*[@id=\"WebPartWPQ6\"]/div[1]/table/tbody/tr/td[6]"));

            if(!crawlByDay(monDishes, "mon", dbFirestore) ||
                    !crawlByDay(tueDishes, "tue", dbFirestore) ||
                    !crawlByDay(wedDishes, "wed", dbFirestore)||
                    !crawlByDay(thuDishes, "thu", dbFirestore)||
                    !crawlByDay(friDishes, "fri", dbFirestore)){
                flag = false;
            }

            driver.quit();

//            return("OK");
            if(flag){
                return new ResponseEntity<>("Verification is successful!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Oops!", HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            System.out.println("Oops! Something went wrong!");
            driver.quit();
            return new ResponseEntity<>("Oops!", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean updateOnTime (Firestore dbFirestore){
        try{
            Timestamp now = new Timestamp(System.currentTimeMillis());
            DocumentSnapshot documentSnapshot = dbFirestore.collection("lunch").document("updateTime").get().get();
            String dtbUpdateDate = documentSnapshot.get("updateDate").toString();
            dtbUpdateDate = dtbUpdateDate.substring(0, 10) + " " + dtbUpdateDate.substring(11, 19);
            Timestamp updateDate = Timestamp.valueOf(dtbUpdateDate);

            dbFirestore.collection("lunch").document("updateTime").update("updatedTimeField", now);

            if(now.after(updateDate)) {
                String dtbSatDate = documentSnapshot.get("satDate").toString();
                dtbSatDate = dtbSatDate.substring(0, 10) + " " + dtbSatDate.substring(11, 19);
                Timestamp sat = Timestamp.valueOf(dtbSatDate);
                Timestamp satNext = new Timestamp(sat.getTime() + 604800000);
                Timestamp updateNext = new Timestamp(updateDate.getTime() + 604800000);

                dbFirestore.collection("lunch").document("mon").collection("history")
                        .document("monHistory3").update(dbFirestore.collection("lunch").document("mon")
                                .collection("history").document("monHistory2").get().get().getData());
                dbFirestore.collection("lunch").document("mon").collection("history")
                        .document("monHistory2").update(dbFirestore.collection("lunch").document("mon")
                                .collection("history").document("monHistory1").get().get().getData());
                dbFirestore.collection("lunch").document("mon").collection("history")
                        .document("monHistory1").update(dbFirestore.collection("lunch").document("mon")
                                .get().get().getData());
                dbFirestore.collection("lunch").document("mon").
                        update("timestamp", new Timestamp(satNext.getTime() - 432000000));


                dbFirestore.collection("lunch").document("tue").collection("history")
                        .document("tueHistory3").update(dbFirestore.collection("lunch").document("tue")
                                .collection("history").document("tueHistory2").get().get().getData());
                dbFirestore.collection("lunch").document("tue").collection("history")
                        .document("tueHistory2").update(dbFirestore.collection("lunch").document("tue")
                                .collection("history").document("tueHistory1").get().get().getData());
                dbFirestore.collection("lunch").document("tue").collection("history")
                        .document("tueHistory1").update(dbFirestore.collection("lunch").document("tue")
                                .get().get().getData());
                dbFirestore.collection("lunch").document("tue").
                        update("timestamp", new Timestamp(satNext.getTime() - 345600000));

                dbFirestore.collection("lunch").document("wed").collection("history")
                        .document("wedHistory3").update(dbFirestore.collection("lunch").document("wed")
                                .collection("history").document("wedHistory2").get().get().getData());
                dbFirestore.collection("lunch").document("wed").collection("history")
                        .document("wedHistory2").update(dbFirestore.collection("lunch").document("wed")
                                .collection("history").document("wedHistory1").get().get().getData());
                dbFirestore.collection("lunch").document("wed").collection("history")
                        .document("wedHistory1").update(dbFirestore.collection("lunch").document("wed")
                                .get().get().getData());
                dbFirestore.collection("lunch").document("wed").
                        update("timestamp", new Timestamp(satNext.getTime() - 259200000));

                dbFirestore.collection("lunch").document("thu").collection("history")
                        .document("thuHistory3").update(dbFirestore.collection("lunch").document("thu")
                                .collection("history").document("thuHistory2").get().get().getData());
                dbFirestore.collection("lunch").document("thu").collection("history")
                        .document("thuHistory2").update(dbFirestore.collection("lunch").document("thu")
                                .collection("history").document("thuHistory1").get().get().getData());
                dbFirestore.collection("lunch").document("thu").collection("history")
                        .document("thuHistory1").update(dbFirestore.collection("lunch").document("thu")
                                .get().get().getData());
                dbFirestore.collection("lunch").document("thu").
                        update("timestamp", new Timestamp(satNext.getTime() - 172800000));

                dbFirestore.collection("lunch").document("fri").collection("history")
                        .document("friHistory3").update(dbFirestore.collection("lunch").document("fri")
                                .collection("history").document("friHistory2").get().get().getData());
                dbFirestore.collection("lunch").document("fri").collection("history")
                        .document("friHistory2").update(dbFirestore.collection("lunch").document("fri")
                                .collection("history").document("friHistory1").get().get().getData());
                dbFirestore.collection("lunch").document("fri").collection("history")
                        .document("friHistory1").update(dbFirestore.collection("lunch").document("fri")
                                .get().get().getData());
                dbFirestore.collection("lunch").document("fri").
                        update("timestamp", new Timestamp(satNext.getTime() - 86400000));

                dbFirestore.collection("lunch").document("updateTime").update("satDate", satNext);
                dbFirestore.collection("lunch").document("updateTime").update("updateDate", updateNext);
            }
            return true;

        }catch (Exception e) {
            System.out.println("updateTime update failed");
            return false;
        }
    }

    private boolean crawlByDay (List<WebElement> webElement, String date, Firestore dbFirestore){
        int clearPointer = 0;
        String title[] = {"dessert", "soup", "side", "main"};
        String data[] = new String[4];
        List<String> dataArr[] = new List[4];
        List<String> dataUpload[] = new List[4];
        Map<String, Object> dateMenu = new HashMap<>();

        try{
            for (int i = 0; i < title.length; i++){
                data[i] = webElement.get(webElement.size() - 1 - i).getText();
                dataArr[i] = Arrays.asList(data[i].split("\\n+"));
                for(int j = 0; j < dataArr[i].size(); j ++){
                    dataArr[i].get(j).trim();
                    if(dataArr[i].get(j).contains("/")) {
                        if (!dataArr[i].get(j).endsWith("/")) {
                            String sub[] = dataArr[i].get(j).split("\\s*/\\s*");
                            System.out.println(sub[1]);
                            dataArr[i].set(j - clearPointer, sub[0] + " (" + sub[1] + ")");
                        } else {
                            String sub = dataArr[i].get(j).substring(0, dataArr[i].get(j).length() - 1);
                            if (j + 1 < dataArr[i].size()) {
                                dataArr[i].set(j - clearPointer, sub + " (" + dataArr[i].get(j + 1) + ")");
                                clearPointer += 1;
                                j++;
                            } else if (j + 1 == dataArr[i].size()) {
                                dataArr[i].set(j - clearPointer, sub);
                            }
                        }
                    }
                    else if(dataArr[i].size() > j + 1){
                        if(dataArr[i].size() > j + 2 && (dataArr[i].get(j+2).charAt(0) == ' ' || Character.isLowerCase(dataArr[i].get(j+2).charAt(0)))){
                            dataArr[i].set(j - clearPointer, dataArr[i].get(j) + " (" + dataArr[i].get(j+1) + " " + dataArr[i].get(j+2).trim() + ")");
                            clearPointer += 2;
                            j += 2;
                        }else {
                            dataArr[i].set(j - clearPointer, dataArr[i].get(j) + " (" + dataArr[i].get(j+1) + ")");
                            clearPointer += 1;
                            j += 1;
                        }
                    }
                }
                dataUpload[i] = dataArr[i].subList(0, dataArr[i].size() - clearPointer);
                clearPointer = 0;
                dateMenu.put(title[i], dataUpload[i]);
            }
            dbFirestore.collection("lunch").document(date).update(dateMenu);

            return true;
        }catch (Exception e) {
            System.out.println("Data crawl on " + date + " failed");
            return false;
        }
    }
}

