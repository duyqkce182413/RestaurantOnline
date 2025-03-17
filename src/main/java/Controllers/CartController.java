/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.CartDAO;
import DAO.FoodDAO;
import Models.CartItem;
import Models.Food;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author admin
 */
public class CartController extends HttpServlet {

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
            out.println("<title>Servlet CartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartController at " + request.getContextPath() + "</h1>");
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
        String action = request.getServletPath();
        switch (action) {
            case "/view-cart":
                getCart(request, response);
                break;
            case "/add-to-cart":
                addToCart(request, response);
                break;
            case "/update-cart":
                updateCart(request, response);
                break;
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    protected void getCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Nếu acc là null, chuyển hướng đến trang đăng nhập hoặc trang lỗi
            response.sendRedirect("LoginView.jsp"); // hoặc trang khác phù hợp
            return;
        }
        CartDAO cartDAO = new CartDAO();
        int id = user.getUserID();

        List<CartItem> cartItems = cartDAO.getcart(id);

        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("CartView.jsp").forward(request, response);
    }

    protected void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user == null) {
            // Nếu acc là null, chuyển hướng đến trang đăng nhập hoặc trang lỗi
            response.sendRedirect("LoginView.jsp"); // hoặc trang khác phù hợp
            return;
        }
        
        int userId = user.getUserID();
        int productId = Integer.parseInt(request.getParameter("foodId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        CartDAO dao = new CartDAO();

        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
        if (dao.isFoodInCart(userId, productId)) {
            // Nếu đã có, cập nhật số lượng bằng cách cộng thêm số lượng mới
            int quantityOfExistingItem = dao.getQuantityOfExistingCartItem(userId, productId);
            int newQuantity = quantityOfExistingItem + quantity;
            dao.updateCartItemQuantity(userId, productId, newQuantity);
        } else {
            // Nếu chưa có, thêm sản phẩm vào giỏ hàng
            dao.addCartItem(userId, productId, quantity);
        }

        getCart(request, response);

    }

    protected void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Nếu acc là null, chuyển hướng đến trang đăng nhập hoặc trang lỗi
            response.sendRedirect("LoginView.jsp"); // hoặc trang khác phù hợp
            return;
        }

        CartDAO dao = new CartDAO();
        int userId = user.getUserID();
        String[] foodIds = request.getParameterValues("foodId");
        String[] quantities = request.getParameterValues("quantity");
        String removeProductIdStr = request.getParameter("removeProductId");

        System.out.println(quantities);
        if (removeProductIdStr != null) {
            int removeProductId = Integer.parseInt(removeProductIdStr);
            dao.deleteCartItem(userId, removeProductId);
        } else if (foodIds != null && quantities != null) {
            // Thêm sản phẩm vào giỏ hàng hoặc cập nhật số lượng
            for (int i = 0; i < foodIds.length; i++) {
                int productId = Integer.parseInt(foodIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
                if (dao.isFoodInCart(userId, productId)) {

                    dao.updateCartItemQuantity(userId, productId, quantity);
                } else {
                    // Nếu chưa có, thêm sản phẩm vào giỏ hàng
                    dao.addCartItem(userId, productId, quantity);
                }
            }
        }
        getCart(request, response);
    }
}
