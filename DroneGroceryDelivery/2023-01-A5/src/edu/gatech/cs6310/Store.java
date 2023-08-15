package edu.gatech.cs6310;
import java.util.TreeMap;
public class Store {
    private int id;
    private String name;
    private int revenue;
    private double addressLat ;
    private double addressLng;


    // constructor
    public Store(String name, int revenue, double x, double y) {
        this.name = name;
        this.revenue = revenue;
        this.addressLat = x;
        this.addressLng = y;
    }

    //getter setter
    public String getName() {
        return this.name;
    }
    public void setName(String newName) {
        this.name = newName;
    }
    public int getRevenue() {
        return this.revenue;
    }
    public void setRevenue(int newRevenue) {
        this.revenue += newRevenue;
    }
    public double getLat(){
        return this.addressLat;
    }
    public double getLng(){
        return this.addressLng;
    }

}
