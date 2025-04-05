/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.CartDAO;
import DAO.FeedbackDAO;
import DAO.FoodDAO;
import Models.CartItem;
import Models.Category;
import Models.Feedback;
import Models.Food;
import Models.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            case "/search-food":
                getFoodByName(request, response);
                break;
            case "/manage-foods":
                manageFoods(request, response);
                break;
            case "/delete-food":
                deleteFood(request, response);
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
        String action = request.getServletPath();
        switch (action) {
            case "/add-food":
                addFood(request, response);
                break;
            case "/edit-food":
                editFood(request, response);
                break;
            default:
                doGet(request, response);
                break;
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

        CartDAO cartDAO = new CartDAO();
        List<CartItem> cartItems;
        if (user != null) {
            int id = user.getUserID();
            cartItems = cartDAO.getcart(id);
        } else {
            cartItems = new ArrayList<>();
        }
        request.setAttribute("cartlists", cartItems);

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        session.setAttribute("user", user);

        String foodID = request.getParameter("foodID");

        if (foodID != null && !foodID.trim().isEmpty()) {
            FoodDAO foodDAO = new FoodDAO();
            FeedbackDAO feedbackDAO = new FeedbackDAO();

            // Lấy thông tin món ăn
            Food food = foodDAO.getProductBYID(foodID);

            // Lấy danh sách feedback cho món ăn
            List<Feedback> feedbackList = feedbackDAO.getAllFeedbacksByFoodId(Integer.parseInt(foodID));

            // Kiểm tra xem có phải yêu cầu JSON không
            String format = request.getParameter("format");
            if ("json".equals(format)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Tạo đối tượng JSON chứa cả food và feedbackList
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("food", food);
                responseData.put("feedbacks", feedbackList);

                // Chuyển đổi sang JSON bằng Gson
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(responseData);

                response.getWriter().write(jsonResponse);
            } else {
                // Đặt dữ liệu vào request
                request.setAttribute("food_detail", food);
                request.setAttribute("feedbackList", feedbackList);

                // Chuyển hướng đến trang FoodDetailView.jsp
                request.getRequestDispatcher("FoodDetailView.jsp").forward(request, response);
            }
        } else {
            // Nếu không có foodID, chuyển hướng về trang lỗi hoặc danh sách món ăn
            response.sendRedirect("FoodDetailView.jsp");
        }
    }

    protected void getFoodByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        CartDAO cartDAO = new CartDAO();
        List<CartItem> cartItems;
        if (user != null) {
            int id = user.getUserID();
            cartItems = cartDAO.getcart(id);
        } else {
            cartItems = new ArrayList<>();
        }

        for (CartItem cartItem : cartItems) {
            System.out.println(cartItem);
        }
        request.setAttribute("cartlists", cartItems);

        String nameInput = request.getParameter("search");
        FoodDAO dao = new FoodDAO();
        List<Food> foods = dao.selectProductByName(nameInput);
        List<Category> categories = dao.selectAllCategories();
        request.setAttribute("foods", foods);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("HomeView.jsp").forward(request, response);
    }

    protected void manageFoods(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchName = request.getParameter("search");
        String categoryIdStr = request.getParameter("categoryid");
        Integer categoryid = null;

        if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
            categoryid = Integer.parseInt(categoryIdStr);
        }

        FoodDAO dao = new FoodDAO();
        List<Category> categories = dao.selectAllCategories();
        List<Food> foods;

        if (searchName != null && !searchName.isEmpty()) {
            foods = dao.selectProductByName(searchName);
        } else {
            foods = dao.getAllFoods(1, categoryid);
        }

        request.setAttribute("foods", foods);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("ManageFoodView.jsp").forward(request, response);
    }

    protected void addFood(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String foodName = request.getParameter("foodName");
            double price = Double.parseDouble(request.getParameter("price"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            String image = request.getParameter("image");
            boolean available = Boolean.parseBoolean(request.getParameter("available"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Food newFood = new Food();
            newFood.setFoodName(foodName);
            newFood.setPrice(price);
            newFood.setCategoryID(categoryId);
            newFood.setDescription(description);
            newFood.setImage(image);
            newFood.setAvailable(available);
            newFood.setQuantity(quantity);

            FoodDAO dao = new FoodDAO();
            dao.addFood(newFood);
            response.sendRedirect("manage-foods");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-foods?error=true");
        }
    }

    protected void deleteFood(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("id"));
            FoodDAO dao = new FoodDAO();
            dao.deleteFood(foodId);
            response.sendRedirect("manage-foods");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-foods");
        }
    }

    protected void editFood(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int foodId = Integer.parseInt(request.getParameter("foodId"));
            String foodName = request.getParameter("foodName");
            double price = Double.parseDouble(request.getParameter("price"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");
            String image = request.getParameter("image");
            boolean available = Boolean.parseBoolean(request.getParameter("available"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Food food = new Food(foodId, foodName, price, categoryId, description,
                    image, available, quantity);
            FoodDAO dao = new FoodDAO();
            List<String> errorMessages = new ArrayList<>();
            boolean updated = dao.updateFood(food, errorMessages);

            if (updated) {
                response.sendRedirect("manage-foods");
            } else {
                if (!errorMessages.isEmpty()) {
                    String errorMessage;
                    if (errorMessages.size() == 1) {
                        errorMessage = "Not enough stock for: " + errorMessages.get(0);
                    } else {
                        errorMessage = "Not enough stock for the following ingredients: " + String.join(", ", errorMessages);
                    }
                    request.setAttribute("error", errorMessage);
                } else {
                    request.setAttribute("error", "Failed to update food due to an unknown error.");
                }
                request.setAttribute("food", food);
                manageFoods(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-foods?error=true");
        }
    }
}
