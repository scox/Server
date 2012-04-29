
package com.museum.javabeans;

import com.museum.server.Server;

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
    private String assignedIP;
    private int assignedPort;

    public int getAssignedPort() {
        return assignedPort;
    }

    public void setAssignedPort(int assignedPort) {
        this.assignedPort = assignedPort;
    }

    
    
    public String getAssignedIP() {
        return assignedIP;
    }

    public void setAssignedIP(String assignedIP) {
        this.assignedIP = assignedIP;
    }
    
    

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
    
    
    
   public synchronized void setIP(){
   
       this.assignedIP = Server.getNewIP();
       System.out.println(this.assignedIP);
        
    }
   
      public synchronized void setPort(){
        
         
 
          this.assignedPort = Server.getNewPort();
        
    }
    
    
    
}
