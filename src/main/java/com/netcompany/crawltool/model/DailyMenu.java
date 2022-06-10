package com.netcompany.crawltool.model;

public class DailyMenu {
    private String[] dessert;
    private String[] main;
    private String[] side;
    private String[] soup;


    public String[] getDessert() {
        return dessert;
    }

    public void setDessert(String[] dessert) {
        this.dessert = dessert;
    }

    public String[] getMain() {
        return main;
    }

    public void setMain(String[] main) {
        this.main = main;
    }

    public String[] getSide() {
        return side;
    }

    public void setSide(String[] side) {
        this.side = side;
    }

    public String[] getSoup() {
        return soup;
    }

    public void setSoup(String[] soup) {
        this.soup = soup;
    }
}
