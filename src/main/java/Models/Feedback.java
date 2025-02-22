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
public class Feedback {
    private int feedbackID;
    private int userID;
    private int foodID;
    private int orderID;
    private int rating;
    private String comment;
    private Date createdAt;

    public Feedback() {
    }

    public Feedback(int feedbackID, int userID, int foodID, int orderID, int rating, String comment, Date createdAt) {
        this.feedbackID = feedbackID;
        this.userID = userID;
        this.foodID = foodID;
        this.orderID = orderID;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Feedback{" + "feedbackID=" + feedbackID + ", userID=" + userID + ", foodID=" + foodID + ", orderID=" + orderID + ", rating=" + rating + ", comment=" + comment + ", createdAt=" + createdAt + '}';
    }
    
}
