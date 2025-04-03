/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.AddressDAO;
import DAO.CartDAO;
import DAO.OrderDAO;
import Models.Address;
import Models.CartItem;
import Models.Order;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class OrderController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/placeOrder":
                    orderConfirmation(request, response);
                    break;
                case "/listOrders":
                    listOrders(request, response);
                    break;
                case "/cancelOrder":
                    cancelOrder(request, response);
                    break;
                default:
                    listOrders(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void orderConfirmation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        List<CartItem> cartItems = cartDAO.getcart(user.getUserID());

        // Lấy địa chỉ mặc định của người dùng
        AddressDAO addressDAO = new AddressDAO();
        Address address = null;
        try {
            address = addressDAO.getDefaultAddressByUserId(user.getUserID());
        } catch (Exception ex) {
            Logger.getLogger(CheckOutCart.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (cartItems.isEmpty()) {
            response.sendRedirect("view-cart?error=Giỏ hàng trống!");
            return;
        }

        // Kiểm tra số lượng sản phẩm trong kho
        for (CartItem item : cartItems) {
            int availableQuantity = item.getFood().getQuantity(); // Giả sử có phương thức để lấy số lượng sản phẩm hiện có trong kho
            if (item.getCart().getQuantity() > availableQuantity) {
                String errorMessage = URLEncoder.encode("Số lượng trong kho không đủ để bạn đặt hàng cho sản phẩm " + item.getFood().getFoodName(), StandardCharsets.UTF_8);
                response.sendRedirect("view-cart?error=" + errorMessage);
                return;
            }
        }

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("address", address);
        request.getRequestDispatcher("OrderConfirmation.jsp").forward(request, response);
    }

    // Hiển thị danh sách đơn hàng
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        OrderDAO ordersDAO = new OrderDAO();
        List<Order> orders = ordersDAO.getOrdersByUserId(user.getUserID());
        request.setAttribute("listOrders", orders);
        request.getRequestDispatcher("orders.jsp").forward(request, response);
    }

    // Hủy đơn hàng
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int orderId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        OrderDAO ordersDAO = new OrderDAO();
        Order order = ordersDAO.getOrderById(orderId);

        if (order != null && ("Chưa xử lý".equalsIgnoreCase(order.getStatus()) || "Đã tiếp nhận".equalsIgnoreCase(order.getStatus()))) {
            // Cập nhật trạng thái đơn hàng thành "Đã hủy"
            boolean isUpdated = ordersDAO.updateOrderStatusCustomer(orderId, "Đã hủy", user.getUserID());
            String message = isUpdated ? "Đơn hàng đã được hủy thành công." : "Không thể hủy đơn hàng.";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            ordersDAO.updateProductQuantityForCancel(order.getOrderDetail());
            response.sendRedirect("listOrders?message=" + encodedMessage);
        } else {
            String encodedError = URLEncoder.encode("Không thể hủy đơn hàng.", StandardCharsets.UTF_8);
            response.sendRedirect("listOrders?error=" + encodedError);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
