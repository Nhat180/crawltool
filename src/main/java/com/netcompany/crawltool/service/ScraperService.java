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
                    !crawlByDay(monDishes, "tue", dbFirestore) ||
                    !crawlByDay(monDishes, "wed", dbFirestore)||
                    !crawlByDay(monDishes, "thu", dbFirestore)||
                    !crawlByDay(monDishes, "fri", dbFirestore)){
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
            String dtbSatDate = documentSnapshot.get("satDate").toString();
            dtbSatDate = dtbSatDate.substring(0, 10) + " " + dtbSatDate.substring(11, 19);
            Timestamp sat = Timestamp.valueOf(dtbSatDate);
            Timestamp satNext = new Timestamp(sat.getTime() + 630000000);

            if(now.after(sat)) {
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

                dbFirestore.collection("lunch").document("updateTime").update("satDate", satNext);
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
                    if(dataArr[i].get(j).contains("/")){
                        if(!dataArr[i].get(j).endsWith("/")) {
                            String sub[] = dataArr[i].get(j).split("\\s*/\\s*");
                            System.out.println(sub[1]);
                            dataArr[i].set(j - clearPointer, sub[0] + " (" + sub[1] + ")");
                        }else{
                            String sub = dataArr[i].get(j).substring(0, dataArr[i].get(j).length() - 1);
                            if(j + 1 < dataArr[i].size()){
                                dataArr[i].set(j - clearPointer, sub + " (" + dataArr[i].get(j+1) + ")");
                                clearPointer += 1;
                                j++;
                            } else if (j + 1 == dataArr[i].size()) {
                                dataArr[i].set(j - clearPointer, sub);
                            }
                        }
                    }else if(dataArr[i].size() > j + 1){
                        dataArr[i].set(j - clearPointer, dataArr[i].get(j) + " (" + dataArr[i].get(j+1) + ")");
                        clearPointer += 1;
                        j += 1;
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

