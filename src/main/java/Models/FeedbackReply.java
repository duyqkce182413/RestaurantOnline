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
    private int feedbackID;
    private int staffID;
    private String replyText;
    private Date replyAt;

    public FeedbackReply() {
    }

    public FeedbackReply(int replyID, int feedbackID, int staffID, String replyText, Date replyAt) {
        this.replyID = replyID;
        this.feedbackID = feedbackID;
        this.staffID = staffID;
        this.replyText = replyText;
        this.replyAt = replyAt;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
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
        return "FeedbackReply{" + "replyID=" + replyID + ", feedbackID=" + feedbackID + ", staffID=" + staffID + ", replyText=" + replyText + ", replyAt=" + replyAt + '}';
    }
    
}
