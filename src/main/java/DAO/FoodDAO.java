/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Category;
import Models.Food;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class FoodDAO extends DBContext {

    public List<Category> selectAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "Select * from Categories";
        try ( Connection connection = getConnection();  PreparedStatement prepareStatement = connection.prepareStatement(sql);) {
            ResultSet rs = prepareStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("CategoryID");
                String name = rs.getString("CategoryName");
                String description = rs.getString("Description");
                categories.add(new Category(id, name, description));
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return categories;
    }

    public List<Food> getAllFoods(int index, Integer categoryid) {
        String sql = "select * from Foods "
                + (categoryid != null ? "where CategoryID = ? " : "")
                + "order by FoodID "
                + "offset ? rows fetch next 12 rows only;";
        List<Food> FoodList = new ArrayList<>();
        try ( Connection connection = getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            if (categoryid != null) {
                ps.setInt(paramIndex++, categoryid);
            }
            ps.setInt(paramIndex, (index - 1) * 12);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("FoodID");
                String name = rs.getString("FoodName");
                double price = rs.getDouble("Price");
                int catId = rs.getInt("CategoryID");
                String description = rs.getString("Description");
                String imageUrl = rs.getString("image");
                boolean available = rs.getBoolean("available");

                FoodList.add(new Food(id, name, price, catId, description, imageUrl, available));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FoodList;
    }

    public int countFoodByCategoryID(Integer categoryId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Foods WHERE CategoryID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countFood() {
        String sql = "select count(*) from Foods";
        try ( Connection connection = getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
