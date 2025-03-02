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
public class OrderApproval {
    private int approvalID;
    private int orderID;
    private User approvedBy;
    private Date approvedAt;

    public OrderApproval() {
    }

    public OrderApproval(int approvalID, int orderID, User approvedBy, Date approvedAt) {
        this.approvalID = approvalID;
        this.orderID = orderID;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    public int getApprovalID() {
        return approvalID;
    }

    public void setApprovalID(int approvalID) {
        this.approvalID = approvalID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    @Override
    public String toString() {
        return "OrderApproval{" + "approvalID=" + approvalID + ", orderID=" + orderID + ", approvedBy=" + approvedBy + ", approvedAt=" + approvedAt + '}';
    }
}
