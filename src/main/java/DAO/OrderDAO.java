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
        String query = "INSERT INTO Orders (UserID, AddressID, OrderDate, Status, TotalAmount, PaymentMethod)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

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
            ps.setInt(1, order.getUser().getUserID());
            ps.setInt(2, order.getAddress().getAddressID());
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(4, order.getStatus());
            ps.setDouble(5, order.getTotalAmount());
            ps.setString(6, order.getPaymentMethod());

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
            ps.executeBatch();  // Chạy lệnh batch để thêm các sản phẩm vào OrderDetail
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách đơn hàng của người dùng
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.Username, a.* FROM Orders o "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "LEFT JOIN Address a ON o.AddressID = a.AddressID "
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

    // Cập nhật trạng thái đơn hàng và ghi nhận thông tin nhân viên duyệt đơn hàng
    public boolean updateOrderStatus(int orderId, String newStatus, int staffId) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            // Lấy trạng thái hiện tại của đơn hàng
            Order order = getOrderById(orderId);
            if (order == null) {
                return false; // Không tìm thấy đơn hàng
            }
            String currentStatus = order.getStatus();

            // Cập nhật trạng thái đơn hàng
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Nếu đơn hàng đang giao mà bị hủy, ghi nhận admin đã hủy
                if ("Đang giao".equalsIgnoreCase(currentStatus) && "Đã hủy".equalsIgnoreCase(newStatus)) {
                    insertOrderApproval(orderId, staffId);
                } else {
                    // Nếu chưa được duyệt trước đó, ghi nhận nhân viên duyệt đơn
                    if (!isOrderApproved(orderId)) {
                        insertOrderApproval(orderId, staffId);
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem đơn hàng đã có nhân viên duyệt hay chưa
    private boolean isOrderApproved(int orderId) {
        String query = "SELECT COUNT(*) FROM OrderApproval WHERE OrderID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Đơn hàng đã có nhân viên duyệt
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Chưa có nhân viên nào duyệt đơn hàng
    }

    // Chèn thông tin nhân viên duyệt đơn hàng
    private void insertOrderApproval(int orderId, int staffId) {
        String query = "INSERT INTO OrderApproval (OrderID, ApprovedBy, ApprovedAt) VALUES (?, ?, GETDATE())";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, staffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thay doi trang thai don hang thanh Da Huy neu customer huy don hang
    public boolean updateOrderStatusCustomer(int orderId, String newStatus, int staffId) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            // Cập nhật trạng thái đơn hàng
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateProductQuantity(List<OrderDetail> items) {
        String query = "UPDATE Foods SET Quantity = Quantity - ? WHERE FoodID = ?";

        FoodDAO food = new FoodDAO();
        
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            // Kiểm tra số lượng sản phẩm trong kho trước khi giảm
            for (OrderDetail item : items) {
                // Lấy số lượng sản phẩm hiện tại trong kho
                int availableQuantity = food.getAvailableQuantity(item.getFoodID().getFoodID());

                // Kiểm tra nếu số lượng trong kho không đủ
                if (item.getQuantity() > availableQuantity) {
                    System.out.println("Không đủ số lượng cho sản phẩm " + item.getFoodID().getFoodID() + ". Số lượng trong kho hiện tại: " + availableQuantity);
                    // Thông báo cho người dùng về việc hết hàng
                    throw new SQLException("Không đủ số lượng sản phẩm. Sản phẩm " + item.getFoodID().getFoodID() + " chỉ còn " + availableQuantity + " sản phẩm.");
                }

                // Nếu số lượng đủ, trừ số lượng
                ps.setInt(1, item.getQuantity());
                ps.setInt(2, item.getFoodID().getFoodID());
                ps.addBatch();
            }

            ps.executeBatch(); // Thực thi batch để cập nhật số lượng tất cả sản phẩm trong danh sách

        } catch (SQLException e) {
            e.printStackTrace();
            // Thông báo lỗi cho người dùng khi không đủ số lượng
            System.out.println("Lỗi khi cập nhật số lượng sản phẩm: " + e.getMessage());
        }
    }

    public void updateProductQuantityForCancel(List<OrderDetail> items) {
        String query = "UPDATE Foods SET Quantity = Quantity + ? WHERE FoodID = ?";

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

    // ======== FeedBack For Customer ==========
    // Kiểm tra xem khách hàng đã mua món ăn này chưa
    public boolean isFoodPurchasedByUser(int foodId, int userId) {
        String query = "SELECT COUNT(*) "
                + "FROM Orders o "
                + "JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "JOIN Users u ON o.UserID = u.UserID "
                + "WHERE od.FoodID = ? "
                + "AND o.UserID = ? "
                + "AND o.Status = 'Hoàn thành'"; // Kiểm tra đơn hàng đã hoàn thành

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, foodId); // Tham số đầu tiên là foodId
            ps.setInt(2, userId); // Tham số thứ hai là userId
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Count of orders found: " + count); // Debugging output
                return count > 0; // Nếu có ít nhất 1 đơn hàng đã hoàn thành với món ăn này
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getOrderIDForFoodAndUser(int foodID, int userID) {
        String query = "SELECT o.OrderID "
                + "FROM Orders o "
                + "JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "WHERE od.FoodID = ? AND o.UserID = ? AND o.Status = 'Hoàn thành'";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, foodID);
            ps.setInt(2, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("OrderID");  // Trả về OrderID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Trả về -1 nếu không tìm thấy đơn hàng
    }

    // ========== END ===========
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
        // Tạo một đối tượng OrderDAO
        OrderDAO orderDAO = new OrderDAO();

        int foodId = 2;
        int userId = 1;

        // Kiểm tra xem người dùng đã mua món ăn này chưa
        boolean hasPurchased = orderDAO.isFoodPurchasedByUser(foodId, userId);

        // In kết quả ra màn hình
        if (hasPurchased) {
            System.out.println("Người dùng đã mua món ăn này.");
        } else {
            System.out.println("Người dùng chưa mua món ăn này.");
        }
    }

}
