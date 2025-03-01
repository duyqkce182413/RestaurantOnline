/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;

public class Revenue {
    private Date date;
    private double revenue;
    
    public Revenue(Date date, double revenue) {
        this.date = date;
        this.revenue = revenue;
    }
    
    // Getters and setters
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public double getRevenue() {
        return revenue;
    }
    
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
