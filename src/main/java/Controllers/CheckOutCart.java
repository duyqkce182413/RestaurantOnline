package Controllers;

import DAO.AddressDAO;
import DAO.CartDAO;
import DAO.OrderDAO;
import Models.Address;
import Models.CartItem;
import Models.Order;
import Models.OrderDetail;
import Models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CheckOutCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin người dùng từ session (sử dụng đối tượng User từ Models)
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        // Lấy giỏ hàng của người dùng
        CartDAO cartDAO = new CartDAO();
        OrderDAO orderDAO = new OrderDAO();
        List<CartItem> cartItems = cartDAO.getcart(user.getUserID());

        if (cartItems.isEmpty()) {
            response.sendRedirect("view-cart?error=Cart is empty");
            return;
        }

        // Lấy địa chỉ mặc định của người dùng
        AddressDAO addressDAO = new AddressDAO();
        Address address = null;
        try {
            address = addressDAO.getDefaultAddressByUserId(user.getUserID());
        } catch (Exception ex) {
            Logger.getLogger(CheckOutCart.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (address == null) {
            response.sendRedirect("listAddress?error=address_not_found");
            return;
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(new Date());
        order.setStatus("Chưa xử lý");
        order.setTotalAmount(calculateTotal(cartItems));
        order.setPaymentMethod("Cash");

        int orderId = orderDAO.createOrder(order);
        if (orderId > 0) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderID(order);
                orderDetail.setFoodID(item.getFood());
                orderDetail.setQuantity(item.getCart().getQuantity());
                orderDetail.setPrice(item.getFood().getPrice());
                orderDetails.add(orderDetail);
            }

            // Cập nhật số lượng sản phẩm trong kho
            orderDAO.updateProductQuantity(orderDetails);
            // Thêm chi tiết đơn hàng
            orderDAO.createOrderItems(orderDetails, orderId);
            // Xóa giỏ hàng sau khi đặt hàng thành công
            cartDAO.clearCartByUserId(user.getUserID());
            // Encode thông báo tiếng Việt
            String message = URLEncoder.encode("Đơn hàng đã được đặt thành công!", StandardCharsets.UTF_8);
            response.sendRedirect("listOrders?message=" + message);
        } else {
            String errorMessage = URLEncoder.encode("Không thể đặt hàng!", StandardCharsets.UTF_8);
            response.sendRedirect("CartView.jsp?error=" + errorMessage);
        }
    }

    // Phương thức tính tổng tiền của giỏ hàng (sử dụng số lượng từ Cart và giá từ Food)
    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getCart().getQuantity() * item.getFood().getPrice();
        }
        return total;
    }

    @Override
    public String getServletInfo() {
        return "Checkout Cart Servlet";
    }
}
