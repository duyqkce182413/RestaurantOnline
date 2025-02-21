/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

/**
 *
 * @author admin
 */
public class Statistics {
    private int statID;
    private double revenue;
    private int totalOrders;
    private Date dateRecorded;
    private int orderID;
    private int userID;

    public Statistics() {
    }

    public Statistics(int statID, double revenue, int totalOrders, Date dateRecorded, int orderID, int userID) {
        this.statID = statID;
        this.revenue = revenue;
        this.totalOrders = totalOrders;
        this.dateRecorded = dateRecorded;
        this.orderID = orderID;
        this.userID = userID;
    }

    public int getStatID() {
        return statID;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Statistics{" + "statID=" + statID + ", revenue=" + revenue + ", totalOrders=" + totalOrders + ", dateRecorded=" + dateRecorded + ", orderID=" + orderID + ", userID=" + userID + '}';
    }

    
}
