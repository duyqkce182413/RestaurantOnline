/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Address;
import Models.Order;
import Models.OrderApproval;
import Models.User;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderApprovalDAO extends DBContext {

    public List<OrderApproval> getAllApprovals() {
        List<OrderApproval> approvals = new ArrayList<>();
        String sql = "SELECT oa.ApprovalID, oa.OrderID, oa.ApprovedBy, \n"
                + "       u.UserID, u.FullName, oa.ApprovedAt, \n"
                + "       o.TotalAmount, o.UserID AS BuyerID, \n"
                + "       a.Name AS AddressName, a.AddressLine, a.City, \n"
                + "       buyer.PhoneNumber AS BuyerPhone\n"
                + "FROM OrderApproval oa\n"
                + "LEFT JOIN Orders o ON oa.OrderID = o.OrderID\n"
                + "LEFT JOIN Users u ON oa.ApprovedBy = u.UserID\n"
                + "LEFT JOIN Address a ON o.AddressID = a.AddressID\n"
                + "LEFT JOIN Users buyer ON o.UserID = buyer.UserID;";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderApproval approval = new OrderApproval();
                User staff = new User();
                User buyer = new User(); // Người mua
                Order order = new Order();
                Address address = new Address(); // Địa chỉ giao hàng

                staff.setUserID(rs.getInt("UserID"));
                staff.setFullName(rs.getString("FullName"));

                buyer.setUserID(rs.getInt("BuyerID"));
                buyer.setPhoneNumber(rs.getString("BuyerPhone"));

                order.setOrderID(rs.getInt("OrderID"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setUser(buyer); // Gán người mua vào đơn hàng

                address.setName(rs.getString("AddressName"));
                address.setAddressLine(rs.getString("AddressLine"));
                address.setCity(rs.getString("City"));

                order.setAddress(address); // Gán địa chỉ vào đơn hàng

                approval.setApprovalID(rs.getInt("ApprovalID"));
                approval.setOrder(order);
                approval.setApprovedBy(staff);
                approval.setApprovedAt(rs.getTimestamp("ApprovedAt"));

                approvals.add(approval);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return approvals;
    }

    public Integer getApprovedByStaffId(int orderId) {
        Integer approvedBy = null;
        String sql = "SELECT TOP 1 ApprovedBy FROM OrderApproval WHERE OrderID = ? ORDER BY ApprovedAt DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                approvedBy = rs.getInt("ApprovedBy");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return approvedBy;
    }

    public static void main(String[] args) {
        OrderApprovalDAO orderApprovalDAO = new OrderApprovalDAO();

        List<OrderApproval> orders = orderApprovalDAO.getAllApprovals();

        for (OrderApproval order : orders) {
            System.out.println(order);
        }
    }
}
