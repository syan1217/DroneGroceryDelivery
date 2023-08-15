package edu.gatech.cs6310;
import java.util.TreeMap;

public class Pilot {
    private String account;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String taxID;
    private String licenseID;
    private int level;
    private Boolean assigned;
    //private Drone drone;


    // constructor
    public Pilot(String account, String firstName, String lastName, String phoneNumber, String taxID, String licenseID, int level) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.taxID = taxID;
        this.licenseID = licenseID;
        this.level = level;
        //this.assigned = false; // remove
//        this.drone = null;
//        DeliveryService.pilotsList.put(this.account, this);
//        DeliveryService.licenseIDList.add(this.licenseID);
    }

    //getter setter
    public String getAccount() {
        return this.account;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public String getTaxID() {
        return this.taxID;
    }

    public String getLicenseID() {
        return this.licenseID;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void levelUp() {
        this.level++;
    }

//    public Drone getDrone() {
//        return drone;
//    }
//
//    public void setDrone(Drone drone) {
//        this.drone = drone;
//    }

    // remove:
//    public boolean assignedStatus() {
//        if (this.drone != null) {
//            this.assigned = true;
//        }
//        return this.assigned;
//    }




}