package edu.gatech.cs6310;

public class Customer {
    private String account;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int rating;
    private int credit;
    private int holdCredit;


    // constructor
    public Customer (String account, String firstName, String lastName, String phoneNumber, int rating, int credit) {
        this.account = account;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.credit = credit;
        this.holdCredit = 0;
        //DeliveryService.customersList.put(this.account, this);
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


    public int getRating() {
        return this.rating;
    }

    public void setRating(int newRating) {
        this.rating = newRating;
    }

    public int getCredit() {
        return this.credit;
    }
    public void updateCredit(int creditChange) {
        this.credit += creditChange;
    }
    public int getHoldCredit() {
        return this.holdCredit;
    }
    public void updateHoldCredit(int creditChange) {
        this.holdCredit += creditChange;
    }

}
