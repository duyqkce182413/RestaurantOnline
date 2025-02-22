/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Models.Address;
import Models.Food;
import Models.Order;
import Models.OrderDetail;
import Models.User;
import Utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

    // Xu ly chuc nang thanh toan
    public int createOrder(Order order) {
        String query = "INSERT INTO Orders (UserID, AddressID, OrderDate, TotalAmount, Status, PaymentMethod, Notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Kiểm tra nếu `user` và `address` là null
            if (order.getUser() == null || order.getAddress() == null) {
                throw new IllegalArgumentException("User or Address in order is null");
            }

            // Kiểm tra nếu `user` và `address` có ID hợp lệ
            if (order.getUser().getUserID() == 0 || order.getAddress().getAddressID() == 0) {
                throw new IllegalArgumentException("User ID or Address ID is invalid");
            }

            // Thiết lập các giá trị cho câu lệnh SQL
            ps.setInt(1, order.getUser().getUserID());         // user_id
            ps.setInt(2, order.getAddress().getAddressID());      // address_id
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));  // order_date
            ps.setDouble(4, order.getTotalAmount());       // total_amount
            ps.setString(5, order.getStatus());            // status
            ps.setString(6, order.getPaymentMethod());     // payment_method

            // Thực hiện truy vấn
            int affectedRows = ps.executeUpdate();

            // Kiểm tra nếu truy vấn không thực hiện thành công
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            // Lấy `orderId` được tạo tự động
            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về `orderId`
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;  // Trả về 0 nếu không thành công
        }
    }

    // Them san pham vao 
    public void createOrderItems(List<OrderDetail> items, int orderId) {
        String query = "INSERT INTO OrderDetails (OrderID, FoodID, Quantity, Price) VALUES (?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            for (OrderDetail item : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getFoodID().getFoodID());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();  // Chạy lệnh batch để thêm các sản phẩm vào Order_Items
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách đơn hàng của người dùng
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username, a.* FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "JOIN Address a ON o.AddressID = a.AddressID "
                + "WHERE o.UserID = ? ORDER BY o.OrderDate DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderDetail(getOrderItemsByOrderId(order.getOrderID())); // Lấy chi tiết đơn hàng
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Lấy các sản phẩm trong một đơn hàng
    public List<OrderDetail> getOrderItemsByOrderId(int orderId) {
        List<OrderDetail> items = new ArrayList<>();
        String query = "SELECT od.*, f.FoodName, f.Price FROM OrderDetails od "
                + "JOIN Foods f ON od.FoodID = f.FoodID "
                + "WHERE od.OrderID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(extractOrderItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateProductQuantity(List<OrderDetail> items) {
        String query = "UPDATE Foods SET Quantity = Quantity - ? WHERE FoodID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            for (OrderDetail item : items) {
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getFoodID().getFoodID());
                ps.addBatch();
            }
            ps.executeBatch();  // Thực thi batch để cập nhật số lượng tất cả sản phẩm trong danh sách
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lay order bang ID 
    public Order getOrderById(int orderId) {
        Order order = null;
        String query = "SELECT o.OrderID, o.OrderDate, o.Status, o.TotalAmount, o.PaymentMethod, "
                + "u.UserID, u.Username, "
                + "addr.AddressID, addr.Name, addr.AddressLine, addr.City, addr.PhoneNumber "
                + "FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "LEFT JOIN Address addr ON o.AddressID = addr.AddressID "
                + // Dùng LEFT JOIN để tránh lỗi NULL
                "WHERE o.OrderID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = extractOrderFromResultSet(rs);
                // Lấy danh sách sản phẩm của đơn hàng
                order.setOrderDetail(getOrderItemsByOrderId(orderId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return order;
    }

    // Phương thức hỗ trợ để lấy Order từ ResultSet
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderID(rs.getInt("OrderID"));

        // Tạo đối tượng User
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        order.setUser(user);

        // Kiểm tra xem AddressID có NULL không trước khi tạo Address
        Address address = null;
        int addressID = rs.getInt("AddressID");
        if (!rs.wasNull()) {  // Kiểm tra xem có NULL không
            address = new Address();
            address.setAddressID(addressID);
            address.setName(rs.getString("Name"));
            address.setAddressLine(rs.getString("AddressLine"));
            address.setCity(rs.getString("City"));
            address.setPhoneNumber(rs.getString("PhoneNumber"));
        }
        order.setAddress(address); // Đặt vào Order (có thể là null)

        // Các thông tin khác của Order
        order.setOrderDate(rs.getDate("OrderDate"));
        order.setTotalAmount(rs.getDouble("TotalAmount"));
        order.setStatus(rs.getString("Status"));
        order.setPaymentMethod(rs.getString("PaymentMethod"));

        return order;
    }

    // Phương thức hỗ trợ để lấy OrderItem từ ResultSet
    private OrderDetail extractOrderItemFromResultSet(ResultSet rs) throws SQLException {
        OrderDetail item = new OrderDetail();
        item.setOrderDetailID(rs.getInt("orderDetailID"));

        // Tạo đối tượng Food
        Food food = new Food();
        food.setFoodID(rs.getInt("foodID"));
        food.setFoodName(rs.getString("foodName"));
        food.setPrice(rs.getDouble("price")); // Sửa lại kiểu dữ liệu cho đúng
        item.setFoodID(food);

        // Lấy số lượng & giá từ ResultSet
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getDouble("price")); // Sửa kiểu dữ liệu

        return item;
    }

    // Xóa don hàng
    public boolean deleteOrder(int orderId) {
        // Đầu tiên, xóa các sản phẩm trong đơn hàng
        String deleteItemsQuery = "DELETE FROM OrderDetails WHERE OrderID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(deleteItemsQuery)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu không xóa được các sản phẩm
        }

        // Sau đó, xóa đơn hàng
        String deleteOrderQuery = "DELETE FROM Orders WHERE OrderID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(deleteOrderQuery)) {
            ps.setInt(1, orderId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu không xóa được đơn hàng
        }
    }

    /*
        ======================== Admin Orders  ========================
     */
    // Lấy tất cả đơn hàng theo ngày 
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username, addr.Name, addr.AddressLine, addr.City, addr.PhoneNumber FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "JOIN Address addr ON o.AddressID = addr.AddressID "
                + "ORDER BY o.OrderDate DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderDetail(getOrderItemsByOrderId(order.getOrderID()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Lấy đơn hàng theo trạng thái
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username, addr.Name, addr.AddressLine, addr.City, addr.PhoneNumber FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "JOIN Address addr ON o.AddressID = addr.AddressID "
                + "WHERE o.Status = ? ORDER BY o.OrderDate DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderDetail(getOrderItemsByOrderId(order.getOrderID()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Tìm kiem bang sdt
    public List<Order> getOrdersByPhoneNumber(String phoneNumber) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username, addr.* FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "JOIN Address addr ON o.AddressID = addr.AddressID "
                + "WHERE addr.PhoneNumber = ? ORDER BY o.OrderDate DESC";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderDetail(getOrderItemsByOrderId(order.getOrderID()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();

        Order order = orderDAO.getOrderById(4);

         System.out.println("Đang lấy Order ID: " + order.getOrderID());
        if (order != null) {
            System.out.println("Order lấy được: " + order.toString());
        } else {
            System.out.println("Không tìm thấy Order!");
        }
    }

}
