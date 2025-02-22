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
                int quantity = rs.getInt("quantity");

                FoodList.add(new Food(id, name, price, catId, description, imageUrl, available, quantity));
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

    public Food getProductBYID(String id) {
        String sql = "select * from Foods\n"
                + "where FoodID = ?";
        try ( Connection connection = new DBContext().getConnection();  PreparedStatement prepareStatement = connection.prepareStatement(sql);) {
            prepareStatement.setString(1, id);
            ResultSet pr = prepareStatement.executeQuery();
            while (pr.next()) {
                return new Food(pr.getInt(1),
                        pr.getString(2),
                        pr.getDouble(3),
                        pr.getInt(4),
                        pr.getString(5),
                        pr.getString(6),
                        pr.getBoolean(7),
                        pr.getInt(8));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Food> selectProductByName(String nameInput) {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM Foods WHERE FoodName LIKE ?;";
        // Step 1: Establishing a Connection
        try ( Connection connection = getConnection(); // Step 2:Create a statement using connection object
                  PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, "%" + nameInput + "%");
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("FoodID");
                String name = rs.getString("FoodName");
                double price = rs.getDouble("Price");
                int categoryID = rs.getInt("CategoryID");
                String description = rs.getString("Description");
                String image = rs.getString("Image");
                Boolean available = rs.getBoolean("available");
                int quantity = rs.getInt("Quantity");
                foods.add(new Food(id, name, price, categoryID, description, image, true, quantity));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return foods;
    }
    
}
