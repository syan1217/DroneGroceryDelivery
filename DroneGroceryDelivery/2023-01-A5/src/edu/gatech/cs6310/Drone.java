package edu.gatech.cs6310;

import java.util.ArrayList;
import java.util.TreeMap;

//import java.util.TreeMap;
public class Drone {
    private String ID;
    private Store store;
    private int totalCap;
    private int remainCap;
    private int orders;
    private int tripLeft;
    private Boolean assigned;
    private Pilot pilot;
    private int lowRateStartHour=6 ;
    private int highRateStartHour=10;
    private int highRateEndHour=16;
    private int lowRateEndHour=19;
    private int lowChargeRate=10;
    private int highChargeRate=20;
    private int[] chargeRateArray = new int[24];
    private int batteryUseRate = 1; // per mile per weight unit
    private int currentBattery;
    private int maxBattery = 500;

    public Drone (Store store, String ID, int totalCap) {
        this.ID = ID;
        this.store = store;
        this.totalCap = totalCap;
        this.remainCap = totalCap;
        this.tripLeft = tripLeft;
        this.assigned = false;
        this.pilot = null;
        this.orders = 0;
        this.currentBattery = 50;
        updateChargeRateArray();
    }

    public Store getStore() {
        return this.store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
    public String getID() {
        return this.ID;
    }
    public void setName(String newID) {
        this.ID = newID;
    }


    public int getOrders() {
        return this.orders;
    }

    public int getTripLeft() {
        return this.tripLeft;
    }
    public Pilot getPilot(){
        return this.pilot;
    }
    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }
    public boolean assignedStatus() {
        if (this.pilot != null){
            return true;
        }
        return false;
    }
    public int getTotalCap() {
        return this.totalCap;
    }
    public int getRemainCap() {
        return this.remainCap;
    }

    public int getLowRateStartHour() { return this.lowRateStartHour; }
    public void setLowRateStartHour(int lowRateStartHour) { this.lowRateStartHour = lowRateStartHour;
        updateChargeRateArray(); }
    public int getHighRateStartHour() { return this.highRateStartHour; }
    public void setHighRateStartHour(int highRateStartHour) { this.highRateStartHour = highRateStartHour;
        updateChargeRateArray(); }
    public int getHighRateEndHour() { return this.highRateEndHour; }
    public void setHighRateEndHour(int highRateEndHour) { this.highRateEndHour = highRateEndHour;
        updateChargeRateArray(); }
    public int getLowRateEndHour() { return this.lowRateEndHour; }
    public void setLowRateEndHour(int lowRateEndHour) { this.lowRateEndHour = lowRateEndHour;
        updateChargeRateArray(); }
    public int getLowChargeRate() { return this.lowChargeRate; }
    public void setLowChargeRate(int lowChargeRate) { this.lowChargeRate = lowChargeRate;
        updateChargeRateArray(); }
    public int getHighChargeRate() { return this.highChargeRate; }
    public void setHighChargeRate(int highChargeRate) { this.highChargeRate = highChargeRate;
        updateChargeRateArray(); }
    public int getBatteryUseRate() { return this.batteryUseRate; }
    public void setBatteryUseRate (int newUseRate){ batteryUseRate = newUseRate; }
    public int getMaxBattery() { return this.maxBattery; }
    public void setMaxBattery(int newMaxBattery) {maxBattery = newMaxBattery; }
    public int[] getChargeRateArray() { return this.chargeRateArray; }
    private void updateChargeRateArray() {
        for (int i = 0; i < lowRateStartHour; i++) {
            chargeRateArray[i] = 0;
        }
        for (int i = lowRateStartHour; i < highRateStartHour; i++) {
            chargeRateArray[i] = lowChargeRate;
        }
        for (int i = highRateStartHour; i < highRateEndHour; i++) {
            chargeRateArray[i] = highChargeRate;
        }
        for (int i = highRateEndHour; i < lowRateEndHour; i++) {
            chargeRateArray[i] = lowChargeRate;
        }
        for (int i = lowRateEndHour; i < 24; i++) {
            chargeRateArray[i] = 0;
        }
    }

    public int chargeDrone(int startHour, int endHour, int dateChange) {
        int totalCharge = 0 ;
        int battAfterCharge = 0 ;
        if (dateChange > 1) {
            battAfterCharge = maxBattery;
        } else if (dateChange == 1) {
            for (int i = startHour; i < 24; i++) {
                totalCharge += chargeRateArray[i];
            }
            for (int i = 0; i < endHour; i++) {
                totalCharge += chargeRateArray[i];
            }
            battAfterCharge = this.currentBattery + totalCharge;
            if (battAfterCharge > maxBattery) {
                battAfterCharge = maxBattery;
            }
        } else if (dateChange == 0) {
            for (int i = startHour; i < endHour; i++){
                totalCharge += chargeRateArray[i];
            }
            battAfterCharge =  this.currentBattery + totalCharge;
            if (battAfterCharge > maxBattery) {
                battAfterCharge = maxBattery;
            }
        }
        return battAfterCharge;
    }


    public int batteryUse(int distance, int weight){
        int batteryUse = batteryUseRate * distance * weight;
        return batteryUse;
    }

    public void setCurrentBattery(int currentBattery) {
        this.currentBattery = currentBattery;
    }
    public int getCurrentBattery(){
        return this.currentBattery;
    }

}
