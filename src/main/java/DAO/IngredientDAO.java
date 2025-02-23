package DAO;

import Models.Ingredient;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO extends DBContext {

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM Ingredients";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("IngredientID"),
                        rs.getString("IngredientName"),
                        rs.getInt("StockQuantity"),
                        rs.getString("Unit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public List<Ingredient> searchIngredients(String keyword) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM Ingredients WHERE IngredientName LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt("IngredientID"),
                        rs.getString("IngredientName"),
                        rs.getInt("StockQuantity"),
                        rs.getString("Unit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO Ingredients (IngredientName, StockQuantity, Unit) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ingredient.getIngredientName());
            pstmt.setInt(2, ingredient.getStockQuantity());
            pstmt.setString(3, ingredient.getUnit());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteIngredient(int ingredientId) {
        String sql = "DELETE FROM Ingredients WHERE IngredientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ingredientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIngredient(Ingredient ingredient) {
        String sql = "UPDATE Ingredients SET IngredientName = ?, StockQuantity = ?, Unit = ? WHERE IngredientID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ingredient.getIngredientName());
            pstmt.setInt(2, ingredient.getStockQuantity());
            pstmt.setString(3, ingredient.getUnit());
            pstmt.setInt(4, ingredient.getIngredientID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
