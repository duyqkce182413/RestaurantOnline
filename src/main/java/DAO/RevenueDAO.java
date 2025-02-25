/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Revenue;
import Utils.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class RevenueDAO extends DBContext {

    public List<Revenue> getRevenueByDateRange(java.util.Date startDate, java.util.Date endDate) {
        List<Revenue> revenueList = new ArrayList<>();
        String sql = "SELECT\n"
                + "    CAST(OrderDate AS DATE) AS order_date,\n"
                + "    SUM(TotalAmount) AS daily_revenue\n"
                + "FROM Orders\n"
                + "WHERE CAST(OrderDate AS DATE) BETWEEN ? AND ?\n"
                + "AND Status = N'Hoàn thành'\n"
                + "GROUP BY CAST(OrderDate AS DATE)\n"
                + "ORDER BY order_date;";

        try ( PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                revenueList.add(new Revenue(
                        rs.getDate("order_date"),
                        rs.getDouble("daily_revenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueList;
    }
}
