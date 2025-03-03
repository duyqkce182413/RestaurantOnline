package Controllers;

import DAO.OrderDAO;
import Models.Order;
import Models.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class StaffOrderController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check login with role Staff
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null ||  ( !user.getRole().equalsIgnoreCase("Admin") && !user.getRole().equalsIgnoreCase("Staff"))) {
            response.sendRedirect("LoginView.jsp?error=You must be an admin to access this page.");
            return;
        }

        // Luôn cập nhật staffId mỗi khi đăng nhập
        session.setAttribute("staffId", user.getUserID());

        String action = request.getServletPath();

        try {
            switch (action) {
                case "/listAdminOrders":
                    listAllOrder(request, response);
                    break;
                case "/updateOrderStatus":
                    updateOrderStatus(request, response);
                    break;
                case "/viewOrderDetails":
                    viewOrderDetails(request, response);
                    break;
                case "/searchByPhone":
                    searchOrdersByPhone(request, response);
                    break;
                case "/deleteOrder":
                    deleteOrder(request, response);
                    break;
                default:
                    listAllOrder(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // Hiển thị danh sách tất cả đơn hàng
    private void listAllOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String statusFilter = request.getParameter("status");
        List<Order> orders;
        OrderDAO ordersDAO = new OrderDAO(); // Sửa lỗi tên biến

        if (statusFilter == null || statusFilter.isEmpty()) {
            orders = ordersDAO.getAllOrders();
        } else {
            orders = ordersDAO.getOrdersByStatus(statusFilter);
        }

        request.setAttribute("listOrders", orders);
        request.getRequestDispatcher("admin_orders.jsp").forward(request, response);
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String orderIdStr = request.getParameter("id");
            String staffIdStr = request.getParameter("staffId");

            // Kiểm tra null hoặc rỗng
            if (orderIdStr == null || orderIdStr.trim().isEmpty() || staffIdStr == null || staffIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Order ID hoặc Staff ID không hợp lệ.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            // Kiểm tra có phải số không
            if (!orderIdStr.matches("\\d+") || !staffIdStr.matches("\\d+")) {
                request.setAttribute("error", "Order ID hoặc Staff ID phải là số nguyên.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            int staffId = Integer.parseInt(staffIdStr);

            // Gọi DAO để cập nhật trạng thái
            OrderDAO ordersDAO = new OrderDAO();
            boolean isUpdated = ordersDAO.updateOrderStatus(orderId, "Hoàn thành", staffId);

            if (isUpdated) {
                Order order = ordersDAO.getOrderById(orderId);
                if (order != null) {
                    ordersDAO.updateProductQuantity(order.getOrderDetail());
                }
                request.setAttribute("message", "Status updated successfully and product quantities adjusted");
            } else {
                request.setAttribute("error", "Failed to update status");
            }

            request.getRequestDispatcher("listAdminOrders").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("listAdminOrders").forward(request, response);
        }
    }

    // Hiển thị chi tiết đơn hàng
    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        OrderDAO ordersDAO = new OrderDAO();
        Order order = ordersDAO.getOrderById(orderId);

        if (order != null) {
            request.setAttribute("order", order);
            request.getRequestDispatcher("admin_view_orders.jsp").forward(request, response);
        } else {
            response.sendRedirect("listAdminOrders?error=Order not found");
        }
    }

    // Tìm kiếm theo số điện thoại
    private void searchOrdersByPhone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneSearch = request.getParameter("phone");
        OrderDAO ordersDAO = new OrderDAO();
        List<Order> orders = ordersDAO.getOrdersByPhoneNumber(phoneSearch);

        request.setAttribute("listOrders", orders);
        request.getRequestDispatcher("admin_orders.jsp").forward(request, response);
    }

    // Xóa order
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        OrderDAO ordersDAO = new OrderDAO();
        boolean isDeleted = ordersDAO.deleteOrder(orderId);

        if (isDeleted) {
            response.sendRedirect("listAdminOrders?message=Order deleted successfully");
        } else {
            response.sendRedirect("listAdminOrders?error=Order deletion failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý đơn hàng cho nhân viên";
    }
}
