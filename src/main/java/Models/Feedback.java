/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class Feedback {
    private int feedbackID;
    private User user;
    private Food food;
    private Order order;
    private int rating;
    private String comment;
    private Date createdAt;
    private  List<FeedbackReply> reply; // Thêm list reply cua khach hang vaf 

    public Feedback() {
    }

    public Feedback(int feedbackID, User user, Food food, Order order, int rating, String comment, Date createdAt) {
        this.feedbackID = feedbackID;
        this.user = user;
        this.food = food;
        this.order = order;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Feedback(int feedbackID, User user, Food food, Order order, int rating, String comment, Date createdAt, List<FeedbackReply> reply) {
        this.feedbackID = feedbackID;
        this.user = user;
        this.food = food;
        this.order = order;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.reply = reply;
    }
    
    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
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

    public List<FeedbackReply> getReply() {
        return reply;
    }

    public void setReply(List<FeedbackReply> reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Feedback{" + "feedbackID=" + feedbackID + ", user=" + user + ", food=" + food + ", order=" + order + ", rating=" + rating + ", comment=" + comment + ", createdAt=" + createdAt + ", reply=" + reply + '}';
    }
    
}
