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
public class FeedbackReply {
    private int replyID;
    private Feedback feedback;
    private User user;
    private String replyText;
    private Date replyAt;

    public FeedbackReply() {
    }

    public FeedbackReply(int replyID, Feedback feedback, User user, String replyText, Date replyAt) {
        this.replyID = replyID;
        this.feedback = feedback;
        this.user = user;
        this.replyText = replyText;
        this.replyAt = replyAt;
    }
    
    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public Date getReplyAt() {
        return replyAt;
    }

    public void setReplyAt(Date replyAt) {
        this.replyAt = replyAt;
    }

    @Override
    public String toString() {
        return "FeedbackReply{"
                + "replyID=" + replyID
                + ", userID=" + user.getUserID()
                + ", replyText='" + replyText + '\''
                + ", replyAt=" + replyAt
                + '}';
    }

}
