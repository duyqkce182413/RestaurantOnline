package DAO;

import Models.Recipe;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO extends DBContext {

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        String query = "SELECT * FROM Recipes";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int foodId = rs.getInt("FoodID");
                int ingredientId = rs.getInt("IngredientID");
                double requiredQuantity = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble

                list.add(new Recipe(foodId, ingredientId, requiredQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Recipe> getRecipesByFoodId(int foodId) {
        List<Recipe> list = new ArrayList<>();
        String query = "SELECT * FROM Recipes WHERE FoodID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int ingredientId = rs.getInt("IngredientID");
                double requiredQuantity = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble

                list.add(new Recipe(foodId, ingredientId, requiredQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Recipe> getRecipesByFoodName(String foodName) {
        List<Recipe> list = new ArrayList<>();
        String query = "SELECT r.* FROM Recipes r "
                + "JOIN Foods f ON r.FoodID = f.FoodID "
                + "WHERE f.FoodName LIKE ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + foodName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int foodId = rs.getInt("FoodID");
                int ingredientId = rs.getInt("IngredientID");
                double requiredQuantity = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble

                list.add(new Recipe(foodId, ingredientId, requiredQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Recipe getRecipe(int foodId, int ingredientId) {
        String query = "SELECT * FROM Recipes WHERE FoodID = ? AND IngredientID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, foodId);
            ps.setInt(2, ingredientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double requiredQuantity = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble
                return new Recipe(foodId, ingredientId, requiredQuantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addRecipe(Recipe recipe) {
        String query = "INSERT INTO Recipes (FoodID, IngredientID, RequiredQuantity) VALUES (?, ?, ?)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, recipe.getFoodID());
            ps.setInt(2, recipe.getIngredientID());
            ps.setDouble(3, recipe.getRequiredQuantity()); // Thay setInt thành setDouble

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRecipe(Recipe recipe) {
        String query = "UPDATE Recipes SET RequiredQuantity = ? WHERE FoodID = ? AND IngredientID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDouble(1, recipe.getRequiredQuantity()); // Thay setInt thành setDouble
            ps.setInt(2, recipe.getFoodID());
            ps.setInt(3, recipe.getIngredientID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRecipe(int foodId, int ingredientId) {
        String query = "DELETE FROM Recipes WHERE FoodID = ? AND IngredientID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, foodId);
            ps.setInt(2, ingredientId);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected by delete: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFoodNameById(int foodId) {
        String query = "SELECT FoodName FROM Foods WHERE FoodID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("FoodName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getIngredientNameById(int ingredientId) {
        String query = "SELECT IngredientName, Unit FROM Ingredients WHERE IngredientID = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("IngredientName");
                String unit = rs.getString("Unit");
                return name + " (" + unit + ")";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Object[]> getRecipeDetailsForView() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT r.FoodID, r.IngredientID, r.RequiredQuantity, f.FoodName, i.IngredientName, i.Unit, f.Image "
                + "FROM Recipes r "
                + "JOIN Foods f ON r.FoodID = f.FoodID "
                + "JOIN Ingredients i ON r.IngredientID = i.IngredientID "
                + "ORDER BY f.FoodName, i.IngredientName";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("FoodID");
                row[1] = rs.getInt("IngredientID");
                row[2] = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble
                row[3] = rs.getString("FoodName");
                row[4] = rs.getString("IngredientName");
                row[5] = rs.getString("Unit");
                row[6] = rs.getString("Image");

                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getRecipeDetailsByFoodName(String foodName) {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT r.FoodID, r.IngredientID, r.RequiredQuantity, f.FoodName, i.IngredientName, i.Unit, f.Image "
                + "FROM Recipes r "
                + "JOIN Foods f ON r.FoodID = f.FoodID "
                + "JOIN Ingredients i ON r.IngredientID = i.IngredientID "
                + "WHERE f.FoodName LIKE ? "
                + "ORDER BY f.FoodName, i.IngredientName";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + foodName + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("FoodID");
                row[1] = rs.getInt("IngredientID");
                row[2] = rs.getDouble("RequiredQuantity"); // Thay getInt thành getDouble
                row[3] = rs.getString("FoodName");
                row[4] = rs.getString("IngredientName");
                row[5] = rs.getString("Unit");
                row[6] = rs.getString("Image");

                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getAllFoods() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT FoodID, FoodName FROM Foods ORDER BY FoodName";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getInt("FoodID");
                row[1] = rs.getString("FoodName");

                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getAllIngredients() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT IngredientID, IngredientName, Unit FROM Ingredients ORDER BY IngredientName";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("IngredientID");
                row[1] = rs.getString("IngredientName");
                row[2] = rs.getString("Unit");

                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
