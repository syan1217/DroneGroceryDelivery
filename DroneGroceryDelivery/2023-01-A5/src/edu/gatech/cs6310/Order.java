package edu.gatech.cs6310;
import java.util.TreeMap;

public class Order {
    private Store store;
    private String ID;
    private Customer customer;
    private Drone drone;
    private int totalPrice;
    private int totalWeight;
    private double addressLat ;
    private double addressLng;
    private boolean onTime;


    public Order (Store store, String ID, Customer customer, Drone drone, double x, double y) {
        this.store = store;
        this.ID = ID;
        this.customer = customer;
        this.drone = drone;
        this.totalPrice = 0;
        this.totalWeight = 0;
        this.addressLat = x;
        this.addressLng = y;
        this.onTime = false;
    }

    public Store getStore() {
        return this.store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
    public Customer getCustomer() {
        return this.customer;
    }
    public Drone getDrone() {
        return this.drone;
    }
    public void setDrone(Drone drone) {
        this.drone = drone;
    }
    public double getLat(){
        return this.addressLat;
    }
    public double getLng(){
        return this.addressLng;
    }
    public void setOnTime (boolean onTime){
        this.onTime = onTime;
    }
    public boolean getOnTime(){
        return this.onTime;
    }
}
