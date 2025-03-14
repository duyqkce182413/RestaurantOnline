package Models;

import java.util.Date;

/**
 *
 * @author admin
 */
public class OrderApproval {

    private int approvalID;
    private Order order;  // Đổi tên từ orderID thành order (Order object)
    private User approvedBy;
    private Date approvedAt;

    public OrderApproval() {
    }

    public OrderApproval(int approvalID, Order order, User approvedBy, Date approvedAt) {
        this.approvalID = approvalID;
        this.order = order;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    public int getApprovalID() {
        return approvalID;
    }

    public void setApprovalID(int approvalID) {
        this.approvalID = approvalID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        return "OrderApproval{" + "approvalID=" + approvalID + ", order=" + order + ", approvedBy=" + approvedBy + ", approvedAt=" + approvedAt + '}';
    }
}
