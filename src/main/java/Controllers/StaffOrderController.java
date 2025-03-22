package Controllers;

import DAO.OrderApprovalDAO;
import DAO.OrderDAO;
import DAO.UserDAO;
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

        if (user == null || (!user.getRole().equalsIgnoreCase("Admin") && !user.getRole().equalsIgnoreCase("Staff"))) {
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
        request.getRequestDispatcher("ManageOrderView.jsp").forward(request, response);
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String orderIdStr = request.getParameter("id");
            String staffIdStr = request.getParameter("staffId");
            String newStatus = request.getParameter("newStatus");

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

            OrderApprovalDAO orderApprovalDAO = new OrderApprovalDAO();

            // Kiểm tra nhân viên đã duyệt trước đó
            Integer previousStaffId = orderApprovalDAO.getApprovedByStaffId(orderId);
            if (previousStaffId != null && previousStaffId != staffId) {
                request.setAttribute("error", "Chỉ nhân viên đã duyệt trước đó mới có thể tiếp tục duyệt đơn.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            // Lấy trạng thái hiện tại của đơn hàng trước khi cập nhật
            OrderDAO ordersDAO = new OrderDAO();
            Order order = ordersDAO.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Không tìm thấy đơn hàng.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            String currentStatus = order.getStatus();

            // Gọi DAO để cập nhật trạng thái
            boolean isUpdated = ordersDAO.updateOrderStatus(orderId, newStatus, staffId);

            if (isUpdated) {
                // Kiểm tra nếu trạng thái chuyển từ "Đã tiếp nhận" sang "Đang chuẩn bị" thì cập nhật số lượng
                if ("Đã tiếp nhận".equalsIgnoreCase(currentStatus) && "Đang chuẩn bị".equalsIgnoreCase(newStatus)) {
                    ordersDAO.updateProductQuantity(order.getOrderDetail()); // Cập nhật số lượng sản phẩm
                }
                request.setAttribute("message", "Trạng thái đã được cập nhật thành công");
            } else {
                request.setAttribute("error", "Không cập nhật được trạng thái");
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
            request.getRequestDispatcher("OrderView.jsp").forward(request, response);
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
        request.getRequestDispatcher("ManageOrderView.jsp").forward(request, response);
    }

    // Xóa order
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String orderIdStr = request.getParameter("id");

            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Lỗi: Không tìm thấy ID đơn hàng.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            OrderDAO ordersDAO = new OrderDAO();
            Order order = ordersDAO.getOrderById(orderId);

            if (order == null) {
                request.setAttribute("error", "Lỗi: Đơn hàng không tồn tại.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            String currentStatus = order.getStatus();
            User user = (User) request.getSession().getAttribute("user");

            if (user == null) {
                request.setAttribute("error", "Bạn chưa đăng nhập.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            boolean isAdmin = "Admin".equalsIgnoreCase(user.getRole());
            int userId = user.getUserID(); // ID của người thực hiện thao tác

            OrderApprovalDAO orderApprovalDAO = new OrderApprovalDAO();
            Integer previousStaffId = orderApprovalDAO.getApprovedByStaffId(orderId);

            // Nếu không phải admin, kiểm tra xem nhân viên có phải là người đã duyệt trước đó không
            if (!isAdmin && previousStaffId != null && !previousStaffId.equals(userId)) {
                request.setAttribute("error", "Chỉ nhân viên đã duyệt trước đó mới có thể hủy đơn.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            // Nếu là Admin, cho phép hủy đơn hàng "Đang giao" bất kể ai đã duyệt trước đó
            if (isAdmin && "Đang giao".equalsIgnoreCase(currentStatus)) {
                boolean isUpdated = ordersDAO.updateOrderStatus(orderId, "Đã hủy", userId);
                request.setAttribute("message", isUpdated ? "Admin đã hủy đơn hàng thành công." : "Không thể hủy đơn hàng.");
                request.getRequestDispatcher("listAdminOrders").forward(request, response);
                return;
            }

            // Nhân viên hoặc Admin hủy đơn hàng ở trạng thái "Chưa xử lý" hoặc "Đã tiếp nhận"
            if ("Chưa xử lý".equalsIgnoreCase(currentStatus) || "Đã tiếp nhận".equalsIgnoreCase(currentStatus)) {
                boolean isUpdated = ordersDAO.updateOrderStatus(orderId, "Đã hủy", userId);
                request.setAttribute("message", isUpdated ? "Đơn hàng đã được hủy thành công." : "Hủy đơn hàng thất bại.");
            } else {
                request.setAttribute("error", "Không thể hủy đơn hàng ở trạng thái hiện tại.");
            }
            
            request.getRequestDispatcher("listAdminOrders").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Lỗi: ID đơn hàng không hợp lệ.");
            request.getRequestDispatcher("listAdminOrders").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("listAdminOrders").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý đơn hàng cho nhân viên";
    }
}
