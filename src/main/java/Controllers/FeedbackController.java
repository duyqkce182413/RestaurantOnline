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
            response.sendRedirect("view-food-detail?error=feedback_missing_parameters&foodID=" + foodIdStr);
            return;
        }

        int foodID, rating;
        try {
            foodID = Integer.parseInt(foodIdStr);
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("view-food-detail?error=feedback_invalid_number&foodID=" + foodIdStr);
            return;
        }

        if (rating < 1 || rating > 5) {
            response.sendRedirect("view-food-detail?error=feedback_invalid_rating&foodID=" + foodID);
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        if (!orderDAO.isFoodPurchasedByUser(foodID, user.getUserID())) {
            response.sendRedirect("view-food-detail?error=feedback_no_purchase&foodID=" + foodID);
            return;
        }

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        if (feedbackDAO.hasUserReviewedFood(user.getUserID(), foodID)) {
            response.sendRedirect("view-food-detail?error=feedback_already_reviewed&foodID=" + foodID);
            return;
        }

        FoodDAO foodDAO = new FoodDAO();
        Food food = foodDAO.getProductBYID(String.valueOf(foodID));
        if (food == null) {
            response.sendRedirect("view-food-detail?error=feedback_food_not_found&foodID=" + foodIdStr);
            return;
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setFood(food);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(new Date());

        boolean success = feedbackDAO.addFeedback(feedback);
        if (success) {
            response.sendRedirect("view-food-detail?foodID=" + foodID + "&success=feedback_added");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + foodID + "&error=feedback_failed");
        }
    }

    // Phản hồi cho mọi người
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

        if (feedbackIdStr == null || feedbackIdStr.trim().isEmpty()) {
            response.sendRedirect("view-food-detail?error=reply_missing_feedback_id");
            return;
        }
        if (replyText == null || replyText.trim().isEmpty()) {
            response.sendRedirect("view-food-detail?error=reply_empty_reply_text&feedbackID=" + feedbackIdStr);
            return;
        }

        int feedbackID;
        try {
            feedbackID = Integer.parseInt(feedbackIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("view-food-detail?error=reply_invalid_number&feedbackID=" + feedbackIdStr);
            return;
        }

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        Feedback feedback = feedbackDAO.getFeedbackById(feedbackID);

        if (feedback == null) {
            response.sendRedirect("view-food-detail?error=reply_feedback_not_found&feedbackID=" + feedbackIdStr);
            return;
        }

        if (feedback.getFood() == null) {
            response.sendRedirect("view-food-detail?error=reply_food_not_found&feedbackID=" + feedbackIdStr);
            return;
        }

        // Chỉ cho phép trả lời nếu:
        // 1. Là Admin hoặc Staff
        // 2. Hoặc là người viết feedback
        // 3. Hoặc đã mua món ăn
        if (!user.getRole().equals("Admin") && !user.getRole().equals("Staff")) {
            // Không phải Admin hoặc Staff
            boolean isOwner = user.getUserID() == feedback.getUser().getUserID();
            OrderDAO orderDAO = new OrderDAO();
            boolean hasPurchased = orderDAO.isFoodPurchasedByUser(feedback.getFood().getFoodID(), user.getUserID());

            System.out.println("UserID: " + user.getUserID() + ", FeedbackOwnerID: " + feedback.getUser().getUserID());
            System.out.println("isOwner = " + isOwner + ", hasPurchased = " + hasPurchased);

            // Kiểm tra quyền trả lời phản hồi
            // Chỉ cho phép trả lời nếu là chủ sở hữu hoặc đã mua món ăn
            if (!isOwner && !hasPurchased) {
                response.sendRedirect("view-food-detail?foodID=" + feedback.getFood().getFoodID() + "&error=reply_no_purchase_permission&feedbackID=" + feedbackIdStr);
                return;
            }
        }

        // Tạo và lưu phản hồi
        FeedbackReply reply = new FeedbackReply();
        reply.setFeedback(feedback);
        reply.setUser(user);
        reply.setReplyText(replyText);
        reply.setReplyAt(new Date());

        FeedbackDAO replyDAO = new FeedbackDAO();
        boolean success = replyDAO.addReply(reply);

        if (success) {
            response.sendRedirect("view-food-detail?foodID=" + feedback.getFood().getFoodID() + "&success=reply_added");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + feedback.getFood().getFoodID() + "&error=reply_failed");
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
