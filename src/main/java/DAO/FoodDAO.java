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

    // Thêm các phương thức mới cho admin
    public boolean addFood(Food food) {
        String query = "INSERT INTO Foods (FoodName, Price, CategoryID, Description, Image, Available, Quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, food.getFoodName());
            ps.setDouble(2, food.getPrice());
            ps.setInt(3, food.getCategoryID());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImage());
            ps.setBoolean(6, food.isAvailable());
            ps.setInt(7, food.getQuantity());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFood(int foodId) {
        String query = "DELETE FROM Foods WHERE FoodID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, foodId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sửa lại phương thức getAllFoods để hỗ trợ phân trang cho trang quản lý
    public List<Food> getAllFoodsForManage() {
        String query = "SELECT * FROM Foods ORDER BY FoodID";
        List<Food> foods = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Food food = new Food(
                        rs.getInt("FoodID"),
                        rs.getString("FoodName"),
                        rs.getDouble("Price"),
                        rs.getInt("CategoryID"),
                        rs.getString("Description"),
                        rs.getString("Image"),
                        rs.getBoolean("Available"),
                        rs.getInt("Quantity")
                );
                foods.add(food);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foods;
    }

    // Phương thức lấy số lượng sản phẩm hiện tại trong kho
    public int getAvailableQuantity(int foodID) throws SQLException {
        String query = "SELECT Quantity FROM Foods WHERE FoodID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, foodID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Quantity");
                }
                return 0; // Nếu không tìm thấy sản phẩm
            }
        }
    }

    public boolean checkIngredientAvailabilityForFood(int foodId, int newQuantity, int currentQuantity, List<String> missingIngredients) {
        String sql = "SELECT r.FoodID, r.IngredientID, r.RequiredQuantity, i.IngredientName, i.StockQuantity "
                + "FROM Recipes r "
                + "JOIN Ingredients i ON r.IngredientID = i.IngredientID "
                + "WHERE r.FoodID = ?";

        boolean isAvailable = true;

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();

            int quantityIncrease = newQuantity - currentQuantity;

            while (rs.next()) {
                int requiredQuantity = rs.getInt("RequiredQuantity");
                int stockQuantity = rs.getInt("StockQuantity");
                String ingredientName = rs.getString("IngredientName");
                int totalRequired = requiredQuantity * quantityIncrease;

                if (quantityIncrease > 0 && stockQuantity < totalRequired) {
                    int shortage = totalRequired - stockQuantity;
                    isAvailable = false;
                    missingIngredients.add(ingredientName + " (thiếu " + shortage + " " + getUnit(foodId, rs.getInt("IngredientID")) + ")");
                }
            }
            return isAvailable;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getUnit(int foodId, int ingredientId) {
        String sql = "SELECT Unit FROM Ingredients WHERE IngredientID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Unit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean updateStockQuantity(int foodId, int quantityIncrease) {
        String sql = "UPDATE Ingredients "
                + "SET StockQuantity = StockQuantity - (r.RequiredQuantity * ?) "
                + "FROM Ingredients i "
                + "JOIN Recipes r ON i.IngredientID = r.IngredientID "
                + "WHERE r.FoodID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityIncrease);  // Số lượng tăng thêm
            ps.setInt(2, foodId);            // FoodID để lấy công thức
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFood(Food food, List<String> errorMessages) {
        String query = "UPDATE Foods SET FoodName=?, Price=?, CategoryID=?, Description=?, "
                + "Image=?, Available=?, Quantity=? WHERE FoodID=?";
        Connection conn = null;
        try {
            conn = getConnection();

            Food currentFood = getProductBYID(String.valueOf(food.getFoodID()));
            int currentQuantity = currentFood.getQuantity();
            int newQuantity = food.getQuantity();
            int quantityIncrease = newQuantity - currentQuantity;

            if (quantityIncrease > 0) {
                List<String> missingIngredients = new ArrayList<>();
                if (!checkIngredientAvailabilityForFood(food.getFoodID(), newQuantity, currentQuantity, missingIngredients)) {
                    errorMessages.addAll(missingIngredients);
                    return false;
                }
            }

            boolean newAvailable = (newQuantity == 0) ? false : food.isAvailable();

            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, food.getFoodName());
            ps.setDouble(2, food.getPrice());
            ps.setInt(3, food.getCategoryID());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImage());
            ps.setBoolean(6, newAvailable);
            ps.setInt(7, newQuantity);
            ps.setInt(8, food.getFoodID());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0 && quantityIncrease > 0) {
                if (!updateStockQuantity(food.getFoodID(), quantityIncrease)) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
