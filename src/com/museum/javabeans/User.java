
package com.museum.javabeans;

/**
 *
 * @author Sam
 * Date: 14/02/2012
 * User.java: Reusable java bean containing user details
 */
public class User {
    
    private int pin;
    private int userID;
    private String level;
    private String language;
    private int memberPin;
    private String customerType;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public int getMemberPin() {
        return memberPin;
    }

    public void setMemberPin(int memberPin) {
        this.memberPin = memberPin;
    }
    
    

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    
    
}