package Models;

import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class Order {
    private int orderID;
    private User user;
    private Address address;
    private Date orderDate;
    private String status;
    private double totalAmount;
    private String paymentMethod;
    private List<OrderDetail> orderDetail; // Sửa đúng chính tả

    public Order() {
    }

    public Order(int orderID, User user, Address address, Date orderDate, String status, double totalAmount, String paymentMethod, List<OrderDetail> orderDetail) {
        this.orderID = orderID;
        this.user = user;
        this.address = address;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderDetail = orderDetail;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "Order{" + 
                "orderID=" + orderID + 
                ", user=" + user + 
                ", address=" + address + 
                ", orderDate=" + orderDate + 
                ", status=" + status + 
                ", totalAmount=" + totalAmount + 
                ", paymentMethod=" + paymentMethod + 
                ", orderDetail=" + orderDetail + 
                '}';
    }
}
