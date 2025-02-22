/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.FoodDAO;
import Models.Category;
import Models.Food;
import Models.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class FoodController extends HttpServlet {

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
            out.println("<title>Servlet FoodController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FoodController at " + request.getContextPath() + "</h1>");
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
            case "/all":
                getAllFoods(request, response);
                break;
            case "/view-food-detail":
                viewFoodDetail(request, response);
                break;
            default:
                getAllFoods(request, response);
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
        doGet(request, response);
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

    protected void getAllFoods(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String indexStr = request.getParameter("index");
        String categoryIdStr = request.getParameter("categoryid");
        Integer categoryid = null;

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            categoryid = Integer.parseInt(categoryIdStr);
        }

//        List<CartItem> list;
//        if (acc != null) {
//            CartDAO cartDAO = new CartDAO();
//            int id = acc.getId();
//            list = cartDAO.getcart(id);
//        } else {
//            list = new ArrayList<>(); // Nếu chưa đăng nhập, tạo danh sách giỏ hàng trống
//        }
//        request.setAttribute("cartlists", list);
        FoodDAO dao = new FoodDAO();
        List<Category> categories = dao.selectAllCategories();

        int index;
        if (indexStr == null || indexStr.isEmpty()) {
            index = 1; // Giá trị mặc định nếu index không có trong request
        } else {
            index = Integer.parseInt(indexStr);
        }

        // Gọi phương thức pagingProducts với categoryid
        List<Food> foods = dao.getAllFoods(index, categoryid);

        // Đếm số trang dựa trên categoryid
        int count;
        if (categoryid != null) {
            count = dao.countFoodByCategoryID(categoryid); // Gọi phương thức đếm sản phẩm theo categoryId
        } else {
            count = dao.countFood(); // Nếu không có categoryid thì đếm tất cả sản phẩm
        }

        int endPage = count / 12;
        if (count % 12 != 0) {
            endPage++;
        }

        // Đảm bảo cả hai danh sách đều được khởi tạo
        if (foods == null) {
            foods = new ArrayList<>(); // Tránh NullPointerException nếu không có sản phẩm
        }
        if (categories == null) {
            categories = new ArrayList<>(); // Tránh NullPointerException nếu không có danh mục
        }
        request.setAttribute("currentPage", index);
        request.setAttribute("endPage", endPage);
        request.setAttribute("foods", foods);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("HomeView.jsp").forward(request, response);
    }

    protected void viewFoodDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodID = request.getParameter("foodID");
        FoodDAO dao = new FoodDAO();
        
        Food food = dao.getProductBYID(foodID);
 
        request.setAttribute("food_detail", food);
        request.getRequestDispatcher("FoodDetailView.jsp").forward(request, response);
    }
}
