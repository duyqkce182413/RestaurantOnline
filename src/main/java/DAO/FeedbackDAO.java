package DAO;

import Models.Feedback;
import Models.FeedbackReply;
import Models.User;
import Models.Food;
import Models.Order;
import Utils.DBContext;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class FeedbackDAO extends DBContext {

    // ====================== MANAGE FEEDBACK FOR ADMIN AND STAFF =======================
    // Lấy danh sách tất cả phản hồi (có thông tin User, Food, Order, Reply)
    public List<Feedback> getAllFeedbacks() {
        Map<Integer, Feedback> feedbackMap = new HashMap<>();
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID, "
                + "fr.replyID, fr.userID, fr.replyText, fr.replyAt "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int feedbackID = rs.getInt("feedbackID");

                // Nếu feedback chưa tồn tại trong Map, tạo mới
                Feedback feedback = feedbackMap.get(feedbackID);
                if (feedback == null) {
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

                    feedback = new Feedback(
                            feedbackID,
                            user,
                            food,
                            order,
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getDate("createdAt"),
                            new ArrayList<FeedbackReply>() // Khởi tạo danh sách phản hồi
                    );

                    feedbackMap.put(feedbackID, feedback);
                }

                // Kiểm tra xem có phản hồi không
                int replyID = rs.getInt("replyID");
                if (!rs.wasNull()) { // Nếu replyID không phải NULL
                    User user = new User();
                    FeedbackReply reply = new FeedbackReply(
                            replyID,
                            feedback,
                            user,
                            rs.getString("replyText"),
                            rs.getDate("replyAt")
                    );

                    feedback.getReply().add(reply);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(feedbackMap.values());  // Trả về danh sách feedback đã nhóm phản hồi
    }

    // Lọc phản hồi theo điểm đánh giá
    public List<Feedback> getFeedbacksByRating(int rating) {
        Map<Integer, Feedback> feedbackMap = new HashMap<>();
        String query = "SELECT f.feedbackID, f.userID, f.foodID, f.orderID, f.rating, f.comment, f.createdAt, "
                + "u.username, u.email, u.fullName, "
                + "fd.foodName, "
                + "o.orderID, "
                + "fr.replyID, fr.userID, fr.replyText, fr.replyAt "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "JOIN Orders o ON f.orderID = o.orderID "
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID "
                + "WHERE f.rating = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, rating);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int feedbackID = rs.getInt("feedbackID");

                    // Nếu feedback chưa có trong Map, tạo mới
                    Feedback feedback = feedbackMap.get(feedbackID);
                    if (feedback == null) {
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

                        feedback = new Feedback(
                                feedbackID,
                                user,
                                food,
                                order,
                                rs.getInt("rating"),
                                rs.getString("comment"),
                                rs.getDate("createdAt"),
                                new ArrayList<FeedbackReply>() // Khởi tạo danh sách phản hồi
                        );

                        feedbackMap.put(feedbackID, feedback);
                    }

                    // Kiểm tra xem có phản hồi không
                    int replyID = rs.getInt("replyID");
                    if (!rs.wasNull()) { // Nếu replyID không NULL
                        User replyUser = new User(); // Tạo User mới cho người trả lời
                        replyUser.setUserID(replyID);
                        FeedbackReply reply = new FeedbackReply(
                                replyID,
                                feedback,
                                replyUser, // Dùng userID thay vì userID
                                rs.getString("replyText"),
                                rs.getDate("replyAt")
                        );

                        feedback.getReply().add(reply);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(feedbackMap.values()); // Trả về danh sách feedback đã nhóm phản hồi
    }

    public Feedback getFeedbackById(int feedbackId) {
        String feedbackQuery = "SELECT f.feedbackID, f.userID AS feedbackUserID, f.foodID, f.orderID, "
                + "f.rating, f.comment, f.createdAt, "
                + "u.username AS feedbackUsername, u.email AS feedbackEmail, u.fullName AS feedbackFullName, "
                + "u.role AS feedbackRole, fd.foodName, o.orderID "
                + "FROM Feedback f "
                + "JOIN Users u ON f.userID = u.userID "
                + "JOIN Foods fd ON f.foodID = fd.foodID "
                + "LEFT JOIN Orders o ON f.orderID = o.orderID "
                + "WHERE f.feedbackID = ?";

        String replyQuery = "SELECT fr.replyID, fr.feedbackID, fr.userID AS replyUserID, "
                + "ur.username AS replyUsername, ur.email AS replyEmail, ur.fullName AS replyFullName, "
                + "ur.role AS replyRole, fr.replyText, fr.replyAt "
                + "FROM FeedbackReplies fr "
                + "LEFT JOIN Users ur ON fr.userID = ur.userID "
                + "WHERE fr.feedbackID = ? "
                + "ORDER BY fr.replyAt";

        try ( Connection conn = getConnection();  PreparedStatement ps1 = conn.prepareStatement(feedbackQuery);  PreparedStatement ps2 = conn.prepareStatement(replyQuery)) {

            // Truy vấn Feedback chính
            ps1.setInt(1, feedbackId);
            ResultSet rs1 = ps1.executeQuery();

            Feedback feedback = null;
            if (rs1.next()) {
                User feedbackUser = new User(
                        rs1.getInt("feedbackUserID"),
                        rs1.getString("feedbackUsername"),
                        rs1.getString("feedbackFullName"),
                        rs1.getString("feedbackEmail"),
                        rs1.getString("feedbackRole") // Lấy role của người phản hồi
                );

                Food food = new Food(rs1.getInt("foodID"), rs1.getString("foodName"));

                Order order = null;
                int orderID = rs1.getInt("orderID");
                if (!rs1.wasNull()) {
                    order = new Order(orderID);
                }

                feedback = new Feedback(
                        rs1.getInt("feedbackID"),
                        feedbackUser,
                        food,
                        order,
                        rs1.getInt("rating"),
                        rs1.getString("comment"),
                        rs1.getDate("createdAt"),
                        new ArrayList<FeedbackReply>()
                );
            }

            if (feedback == null) {
                return null; // Không có Feedback nào tồn tại với ID này
            }

            // Truy vấn danh sách phản hồi
            ps2.setInt(1, feedbackId);
            ResultSet rs2 = ps2.executeQuery();

            List<FeedbackReply> replyList = new ArrayList<>();
            while (rs2.next()) {
                User replyUser = new User(
                        rs2.getInt("replyUserID"),
                        rs2.getString("replyUsername"),
                        rs2.getString("replyFullName"),
                        rs2.getString("replyEmail"),
                        rs2.getString("replyRole") // Lấy role của người trả lời
                );

                FeedbackReply reply = new FeedbackReply(
                        rs2.getInt("replyID"),
                        feedback,
                        replyUser,
                        rs2.getString("replyText"),
                        rs2.getDate("replyAt")
                );
                replyList.add(reply);
            }

            feedback.setReply(replyList);
            return feedback;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Trả lời phản hồi của khách hàng
    public boolean replyToFeedback(int feedbackID, int userID, String replyText) {
        // Kiểm tra feedback có tồn tại không
        String checkQuery = "SELECT COUNT(*) FROM Feedback WHERE feedbackID = ?";
        String insertQuery = "INSERT INTO FeedbackReplies (feedbackID, userID, replyText, replyAt) VALUES (?, ?, ?, GETDATE())";

        try ( Connection conn = getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkQuery)) {

            checkPs.setInt(1, feedbackID);
            try ( ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("FeedbackID không tồn tại!");
                    return false;
                }
            }

            // Nếu tồn tại, tiến hành thêm phản hồi
            try ( PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                insertPs.setInt(1, feedbackID);
                insertPs.setInt(2, userID);
                insertPs.setString(3, replyText);
                return insertPs.executeUpdate() > 0;
            }

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

    // ======================== MANAGE FEEDBACK FOR CUSTOMER ===================================
    public List<Feedback> getAllFeedbacksByFoodId(int foodId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT f.feedbackID, f.userID AS feedbackUserID, f.foodID, f.orderID, \n"
                + "       f.rating, f.comment, f.createdAt, \n"
                + "       u.username AS feedbackUsername, u.email AS feedbackEmail, u.fullName AS feedbackFullName, u.role AS feedbackRole, \n"
                + "       fd.foodName, \n"
                + "       o.orderID, \n"
                + "       fr.replyID, fr.userID AS replyUserID, \n"
                + "       ur.username AS replyUsername, ur.email AS replyEmail, ur.fullName AS replyFullName, ur.role AS replyRole, \n"
                + "       fr.replyText, fr.replyAt \n"
                + "FROM Feedback f \n"
                + "JOIN Users u ON f.userID = u.userID \n"
                + "JOIN Foods fd ON f.foodID = fd.foodID \n"
                + "LEFT JOIN Orders o ON f.orderID = o.orderID \n"
                + "LEFT JOIN FeedbackReplies fr ON f.feedbackID = fr.feedbackID \n"
                + "LEFT JOIN Users ur ON fr.userID = ur.userID \n"
                + "WHERE f.foodID = ? \n"
                + "ORDER BY f.feedbackID, fr.replyAt;";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();

            Map<Integer, Feedback> feedbackMap = new HashMap<>();

            while (rs.next()) {
                int feedbackID = rs.getInt("feedbackID");

                Feedback feedback = feedbackMap.get(feedbackID);
                if (feedback == null) {
                    User feedbackUser = new User(
                            rs.getInt("feedbackUserID"),
                            rs.getString("feedbackUsername"),
                            rs.getString("feedbackFullName"),
                            rs.getString("feedbackEmail"),
                            rs.getString("feedbackRole") // Thêm role cho người phản hồi
                    );

                    Food food = new Food(rs.getInt("foodID"), rs.getString("foodName"));

                    Order order = null;
                    int orderID = rs.getInt("orderID");
                    if (!rs.wasNull()) {
                        order = new Order(orderID);
                    }

                    feedback = new Feedback(
                            feedbackID,
                            feedbackUser,
                            food,
                            order,
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getDate("createdAt")
                    );
                    feedback.setReply(new ArrayList<FeedbackReply>());
                    feedbackMap.put(feedbackID, feedback);
                }

                // Xử lý phản hồi
                int replyID = rs.getInt("replyID");
                if (!rs.wasNull()) {
                    User replyUser = new User(
                            rs.getInt("replyUserID"),
                            rs.getString("replyUsername"),
                            rs.getString("replyFullName"),
                            rs.getString("replyEmail"),
                            rs.getString("replyRole") // Thêm role cho người phản hồi
                    );

                    FeedbackReply reply = new FeedbackReply(
                            replyID,
                            feedback,
                            replyUser,
                            rs.getString("replyText"),
                            rs.getDate("replyAt")
                    );
                    feedback.getReply().add(reply);
                }
            }

            feedbacks.addAll(feedbackMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    // Thêm feedback cho customer
    public boolean addFeedback(Feedback feedback) {
        String query = "INSERT INTO Feedback (UserID, FoodID, Rating, Comment, CreatedAt, OrderID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, feedback.getUser().getUserID());
            ps.setInt(2, feedback.getFood().getFoodID());
            ps.setInt(3, feedback.getRating());
            ps.setString(4, feedback.getComment());
            ps.setDate(5, new java.sql.Date(feedback.getCreatedAt().getTime()));

            // Lấy OrderID của đơn hàng mà feedback này thuộc về
            OrderDAO orderDAO = new OrderDAO();
            int orderID = orderDAO.getOrderIDForFoodAndUser(feedback.getFood().getFoodID(), feedback.getUser().getUserID());

            if (orderID > 0) {
                ps.setInt(6, orderID);  // Thêm OrderID vào câu truy vấn
                return ps.executeUpdate() > 0;
            } else {
                System.out.println("Không tìm thấy đơn hàng phù hợp.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm reply cho feedback
    public boolean addReply(FeedbackReply reply) {
        String query = "INSERT INTO FeedbackReplies (feedbackID, userID, replyText, replyAt) VALUES (?, ?, ?, ?)";
        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reply.getFeedback().getFeedbackID()); // feedbackID
            ps.setInt(2, reply.getUser().getUserID()); // userID, sử dụng getUser().getUserID()
            ps.setString(3, reply.getReplyText());  // replyText
            ps.setTimestamp(4, new Timestamp(reply.getReplyAt().getTime()));  // replyAt

            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Thêm phản hồi thất bại
    }

    public static void main(String[] args) {
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        List<Feedback> feedbackFood = feedbackDAO.getAllFeedbacks();

        System.out.println(feedbackFood);

        for (Feedback feedback : feedbackFood) {
            System.out.println(feedback);
        }
    }
}
