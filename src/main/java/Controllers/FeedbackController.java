package Controllers;

import DAO.FeedbackDAO;
import DAO.FoodDAO;
import DAO.OrderDAO;
import Models.Feedback;
import Models.FeedbackReply;
import Models.Food;
import Models.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

public class FeedbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        String action = request.getServletPath();

        switch (action) {
            case "/submitFeedback":
                submitFeedback(request, response);
                break;
            case "/replyFeedback":
                replyFeedback(request, response);
                break;
            case "/editFeedback":
                editFeedback(request, response);
                break;
            case "/deleteFeedback":
                deleteFeedback(request, response);
                break;
            default:
                throw new AssertionError();
        }
    }

    // Gui Danh Gia
    private void submitFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        String foodIdStr = request.getParameter("foodId");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        if (foodIdStr == null || foodIdStr.trim().isEmpty() || ratingStr == null || ratingStr.trim().isEmpty()) {
            response.sendRedirect("view-food-detail?error=missing_parameters&foodID=" + foodIdStr);
            return;
        }

        int foodID;
        int rating;

        try {
            foodID = Integer.parseInt(foodIdStr);
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("view-food-detail?error=invalid_number&foodID=" + foodIdStr);
            return;
        }

        // Kiểm tra xem rating có hợp lệ không (từ 1 đến 5)
        if (rating < 1 || rating > 5) {
            response.sendRedirect("view-food-detail?error=invalid_rating&foodID=" + foodID);
            return;
        }

        // Kiểm tra xem người dùng có mua món ăn này hay không
        OrderDAO orderDAO = new OrderDAO();
        System.out.println(user.getUserID() + " " + foodID);
        boolean hasOrdered = orderDAO.isFoodPurchasedByUser(foodID, user.getUserID()); // Lấy userID từ session
        System.out.println(hasOrdered);
        if (!hasOrdered) {
            response.sendRedirect("view-food-detail?error=no_purchase&foodID=" + foodID);
            return;
        }

        FoodDAO foodDAO = new FoodDAO();
        Food food = foodDAO.getProductBYID(String.valueOf(foodID));

        if (food == null) {
            response.sendRedirect("view-food-detail?error=food_not_found&foodID=" + foodIdStr);
            return;
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setFood(food);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(new Date());

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        boolean success = feedbackDAO.addFeedback(feedback);

        if (success) {
            response.sendRedirect("view-food-detail?foodID=" + foodID + "&success=true");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + foodID + "&error=true");
        }
    }

    // Phan hoi cho moi nguoi
    private void replyFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        String feedbackIdStr = request.getParameter("feedbackId");
        String replyText = request.getParameter("replyText");

        if (feedbackIdStr == null || feedbackIdStr.trim().isEmpty() || replyText == null || replyText.trim().isEmpty()) {
            response.sendRedirect("view-food-detail?error=missing_parameters&feedbackID=" + feedbackIdStr);
            return;
        }

        int feedbackID;

        try {
            feedbackID = Integer.parseInt(feedbackIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("view-food-detail?error=invalid_number&feedbackID=" + feedbackIdStr);
            return;
        }

        // Kiểm tra xem feedback có tồn tại hay không
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback feedback = feedbackDAO.getFeedbackById(feedbackID);

        if (feedback == null) {
            response.sendRedirect("view-food-detail?error=feedback_not_found&feedbackID=" + feedbackIdStr);
            return;
        }

        // Kiểm tra xem người dùng có quyền trả lời feedback hay không (ví dụ: staff, admin hoặc người dùng có liên quan)
        if (user.getUserID() != feedback.getUser().getUserID() && !user.getRole().equals("Admin") && !user.getRole().equals("Staff")) {
            response.sendRedirect("view-food-detail?error=no_permission&feedbackID=" + feedbackIdStr);
            return;
        }

        // Tạo và lưu phản hồi
        FeedbackReply reply = new FeedbackReply();
        reply.setFeedback(feedback);
        reply.setUser(user);  // Sửa tại đây để truyền đối tượng User vào
        reply.setReplyText(replyText);
        reply.setReplyAt(new Date());

        FeedbackDAO replyDAO = new FeedbackDAO();
        boolean success = replyDAO.addReply(reply);

        // Kiểm tra kết quả và redirect
        if (success) {
            response.sendRedirect("view-food-detail?foodID=" + feedback.getFood().getFoodID() + "&success=true");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + feedback.getFood().getFoodID() + "&error=true");
        }
    }

    private void editFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("LoginView.jsp");
            return;
        }

        // Lấy thông tin từ request
        String feedbackIdStr = request.getParameter("feedbackID");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // Kiểm tra tham số
        if (feedbackIdStr == null || feedbackIdStr.trim().isEmpty()
                || ratingStr == null || ratingStr.trim().isEmpty()
                || comment == null || comment.trim().isEmpty()) {
            response.sendRedirect("view-food-detail?error=missing_parameters&foodID=" + request.getParameter("foodID"));
            return;
        }

        int feedbackID;
        int rating;

        try {
            feedbackID = Integer.parseInt(feedbackIdStr);
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("view-food-detail?error=invalid_number&foodID=" + request.getParameter("foodID"));
            return;
        }

        // Kiểm tra xem feedback có tồn tại và thuộc về người dùng hiện tại không
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback existingFeedback = feedbackDAO.getFeedbackById(feedbackID);

        if (existingFeedback == null || existingFeedback.getUser().getUserID() != user.getUserID()) {
            response.sendRedirect("view-food-detail?error=feedback_not_found_or_unauthorized&foodID=" + request.getParameter("foodID"));
            return;
        }

        // Cập nhật thông tin feedback
        existingFeedback.setRating(rating);
        existingFeedback.setComment(comment);

        // Thực hiện cập nhật feedback
        boolean success = feedbackDAO.updateFeedback(existingFeedback);

        if (success) {
            response.sendRedirect("view-food-detail?foodID=" + existingFeedback.getFood().getFoodID() + "&success=true");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + existingFeedback.getFood().getFoodID() + "&error=true");
        }
    }

    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int feedbackID = Integer.parseInt(request.getParameter("feedbackId"));
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        boolean isDeleted = feedbackDAO.deleteFeedback(feedbackID);

        System.out.println("Feedback ID to delete: " + feedbackID);
        if (isDeleted) {
            response.sendRedirect("view-food-detail?foodID=" + request.getParameter("foodID") + "&success=true");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + request.getParameter("foodID") + "&error=true");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý feedback của khách hàng";
    }
}
