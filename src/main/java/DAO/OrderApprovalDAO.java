/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

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
        String sql = "SELECT oa.ApprovalID, oa.OrderID, oa.ApprovedBy, u.UserID, u.FullName, oa.ApprovedAt "
                + "FROM OrderApproval oa "
                + "LEFT JOIN Users u ON oa.ApprovedBy = u.UserID ";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderApproval approval = new OrderApproval();
                User staff = new User();

                staff.setUserID(rs.getInt("userID"));  // Lấy ID của nhân viên duyệt
                staff.setFullName(rs.getString("fullName")); // Lấy tên nhân viên
                approval.setApprovalID(rs.getInt("ApprovalID"));
                approval.setOrderID(rs.getInt("OrderID"));
                approval.setApprovedBy(staff); // Gán đối tượng User
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
