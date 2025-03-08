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
    private User user;
    private String name;
    private String addressLine;
    private String city;
    private String phoneNumber;
    private boolean isDefault;
    private int isDeleted;

    public Address() {
    }

    public Address(int addressID, User user, String name, String addressLine, String city, String phoneNumber, boolean isDefault, int isDeleted) {
        this.addressID = addressID;
        this.user = user;
        this.name = name;
        this.addressLine = addressLine;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
        this.isDeleted = isDeleted;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public int isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Address{" + "addressID=" + addressID + ", user=" + user + ", name=" + name + ", addressLine=" + addressLine + ", city=" + city + ", phoneNumber=" + phoneNumber + ", isDefault=" + isDefault + ", isDeleted=" + isDeleted + '}';
    }

       
}
