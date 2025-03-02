/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Cart;
import Models.CartItem;
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
public class CartDAO extends DBContext {

    public List<CartItem> getcart(int id) {
        String sql = "SELECT \n"
                + "    c.*, \n"
                + "    f.*\n"
                + "FROM Cart c\n"
                + "JOIN Foods f ON c.FoodID = f.FoodID\n"
                + "WHERE c.UserID = ?;";

        List<CartItem> list = new ArrayList<>();

        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Food food = new Food(rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9), rs.getString(10), rs.getBoolean(11), rs.getInt(12));
                Cart cart = new Cart(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                CartItem cartitem = new CartItem(cart, food);
                list.add(cartitem);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    // Phương thức kiểm tra sản phẩm đã tồn tại trong giỏ hàng
    public boolean isFoodInCart(int userId, int foodId) {
        String sql = "SELECT COUNT(*) FROM Cart WHERE UserID = ? AND FoodID = ?";
        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, foodId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu sản phẩm đã tồn tại
            }
        } catch (SQLException e) {
            System.out.println("Error checking if product is in cart: " + e);
        }
        return false;
    }

    public boolean addCartItem(int userId, int productId, int quantity) {
        String sql = "INSERT INTO Cart (UserID, FoodID, Quantity)\n"
                + "VALUES (?, ?, ?);";
        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            System.out.println("Error adding cart item: " + e);
            return false;
        }
    }

    public int getQuantityOfExistingCartItem(int userId, int productId) {
        String sql = "SELECT Quantity FROM Cart WHERE UserID = ? AND FoodID = ?";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1); // Trả về true nếu sản phẩm đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Trả về null nếu không tìm thấy sản phẩm
    }

    // Phương thức cập nhật số lượng sản phẩm trong giỏ hàng
    public boolean updateCartItemQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE Cart SET Quantity = ? WHERE UserID = ? AND FoodID = ?";
        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, quantity);  // Cộng thêm với số lượng hiện tại
            ps.setInt(2, userId);
            ps.setInt(3, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            System.out.println("Error updating cart item quantity: " + e);
            return false;
        }
    }

    public boolean deleteCartItem(int userId, int productId) {
        String sql = "DELETE FROM Cart WHERE UserID = ? AND FoodID = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error removing cart item: " + e);
            return false;
        }
    }

    // Xóa gio hàng khi user thanh toán thành công
    public void clearCartByUserId(int userId) {
        String query = "DELETE FROM Cart WHERE UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
