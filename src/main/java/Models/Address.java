/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author admin
 */
public class Address {
    private int addressID;
    private int userID;
    private String name;
    private String addressLine;
    private String city;
    private String phoneNumber;
    private boolean isDefault;

    public Address() {
    }

    public Address(int addressID, int userID, String name, String addressLine, String city, String phoneNumber, boolean isDefault) {
        this.addressID = addressID;
        this.userID = userID;
        this.name = name;
        this.addressLine = addressLine;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Address{" + "addressID=" + addressID + ", userID=" + userID + ", name=" + name + ", addressLine=" + addressLine + ", city=" + city + ", phoneNumber=" + phoneNumber + ", isDefault=" + isDefault + '}';
    }
    
    
    
}
