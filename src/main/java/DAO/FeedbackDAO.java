package DAO;

import Models.Feedback;
import Models.FeedbackReply;
import Models.User;
import Models.Food;
import Models.Order;
import Utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO extends DBContext {

    // Lấy danh sách tất cả phản hồi (có thông tin User, Food, Order, Reply)
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID, "
                + "fr.replyID, fr.staffID, fr.replyText, fr.replyAt "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));

                Food food = new Food();
                food.setFoodID(rs.getInt("foodID"));
                food.setFoodName(rs.getString("foodName"));

                Order order = new Order();
                order.setOrderID(rs.getInt("orderID"));

                Feedback feedback = new Feedback(
                        rs.getInt("feedbackID"),
                        user,
                        food,
                        order,
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("createdAt")
                );

                // Nếu có phản hồi từ nhân viên, thêm vào danh sách
                if (rs.getInt("replyID") != 0) {
                    FeedbackReply reply = new FeedbackReply(
                            rs.getInt("replyID"),
                            feedback,
                            rs.getInt("staffID"),
                            rs.getString("replyText"),
                            rs.getDate("replyAt")
                    );
                    feedback.setReply(reply);
                }
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Lọc phản hồi theo điểm đánh giá
    public List<Feedback> getFeedbacksByRating(int rating) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "WHERE f.rating = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, rating);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("fullName"));
                    user.setEmail(rs.getString("email"));

                    Food food = new Food();
                    food.setFoodID(rs.getInt("foodID"));
                    food.setFoodName(rs.getString("foodName"));

                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderID"));

                    Feedback feedback = new Feedback(
                            rs.getInt("feedbackID"),
                            user,
                            food,
                            order,
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getDate("createdAt")
                    );
                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    public Feedback getFeedbackById(int feedbackId) {
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID, "
                + "fr.replyID, fr.staffID, fr.replyText, fr.replyAt "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID "
                + "WHERE f.feedbackID = ?";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, feedbackId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("fullName"));
                    user.setEmail(rs.getString("email"));

                    Food food = new Food();
                    food.setFoodID(rs.getInt("foodID"));
                    food.setFoodName(rs.getString("foodName"));

                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderID"));

                    Feedback feedback = new Feedback(
                            rs.getInt("feedbackID"),
                            user,
                            food,
                            order,
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getDate("createdAt")
                    );

                    // Nếu có phản hồi từ nhân viên, thêm vào danh sách
                    if (rs.getInt("replyID") != 0) {
                        FeedbackReply reply = new FeedbackReply(
                                rs.getInt("replyID"),
                                feedback,
                                rs.getInt("staffID"),
                                rs.getString("replyText"),
                                rs.getDate("replyAt")
                        );
                        feedback.setReply(reply);
                    }
                    return feedback;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Trả lời phản hồi của khách hàng
    public boolean replyToFeedback(int feedbackID, int staffID, String replyText) {
        String query = "INSERT INTO FeedbackReplies (FeedbackID, StaffID, ReplyText, ReplyAt) "
                + "VALUES (?, ?, ?, GETDATE())";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, feedbackID);
            ps.setInt(2, staffID);
            ps.setString(3, replyText);
            return ps.executeUpdate() > 0; // Kiểm tra nếu câu lệnh thực thi thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa phản hồi (xóa luôn phản hồi của nhân viên nếu có)
    public boolean deleteFeedback(int feedbackID) {
        String deleteReplies = "DELETE FROM FeedbackReplies WHERE FeedbackID = ?";
        String deleteFeedback = "DELETE FROM Feedback WHERE FeedbackID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps1 = conn.prepareStatement(deleteReplies);  PreparedStatement ps2 = conn.prepareStatement(deleteFeedback)) {

            ps1.setInt(1, feedbackID);
            ps1.executeUpdate(); // Xóa phản hồi của nhân viên trước
            ps2.setInt(1, feedbackID);
            return ps2.executeUpdate() > 0; // Xóa phản hồi chính
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<FeedbackReply> getAllFeedbackReplies() {
        List<FeedbackReply> replies = new ArrayList<>();
        String query = "SELECT * FROM FeedbackReplies";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackID(rs.getInt("feedbackID"));

                FeedbackReply reply = new FeedbackReply(
                        rs.getInt("replyID"),
                        feedback,
                        rs.getInt("staffID"),
                        rs.getString("replyText"),
                        rs.getDate("replyAt")
                );
                replies.add(reply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return replies;
    }

    // =========== Customer ================
    public List<Feedback> getAllFeedbacksByFood(int foodId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID, "
                + "fr.replyID, fr.staffID, fr.replyText, fr.replyAt "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID "
                + "WHERE f.foodID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));

                Food food = new Food();
                food.setFoodID(rs.getInt("foodID"));
                food.setFoodName(rs.getString("foodName"));

                Order order = new Order();
                order.setOrderID(rs.getInt("orderID"));

                Feedback feedback = new Feedback(
                        rs.getInt("feedbackID"),
                        user,
                        food,
                        order,
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("createdAt")
                );

                if (rs.getInt("replyID") != 0) {
                    FeedbackReply reply = new FeedbackReply(
                            rs.getInt("replyID"),
                            feedback,
                            rs.getInt("staffID"),
                            rs.getString("replyText"),
                            rs.getDate("replyAt")
                    );
                    feedback.setReply(reply);
                }
                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Thêm feedback cho customer
    public boolean addFeedback(Feedback feedback) {
        String query = "INSERT INTO Feedback (UserID, FoodID, Rating, Comment, CreatedAt) "
                + "VALUES (?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, feedback.getUser().getUserID());
            ps.setInt(2, feedback.getFood().getFoodID());
            ps.setInt(3, feedback.getRating());
            ps.setString(4, feedback.getComment());
            ps.setDate(5, new java.sql.Date(feedback.getCreatedAt().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        List<Feedback> feedbackFood = feedbackDAO.getAllFeedbacksByFood(2);
       
        for (Feedback feedback : feedbackFood) {
            System.out.println(feedback);
        }
    }
}
