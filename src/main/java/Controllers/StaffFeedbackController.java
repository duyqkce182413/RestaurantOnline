package Controllers;

import DAO.FeedbackDAO;
import Models.Feedback;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StaffFeedbackController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/listStaffFeedbacks":
                    listAllFeedbacks(request, response);
                    break;
                case "/filterStaffFeedbacks":
                    filterFeedbacks(request, response);
                    break;
                case "/viewFeedbackDetails": // Thêm action để xử lý chi tiết feedback
                    viewFeedbackDetails(request, response);
                    break;
                case "/deleteStaffFeedback": // Thêm action để xử lý chi tiết feedback
                    deleteFeedback(request, response);
                    break;
                default:
                    listAllFeedbacks(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        if ("/replyStaffFeedback".equals(action)) {
            replyFeedback(request, response);
        } 
    }

    private void listAllFeedbacks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Feedback> feedbacks;
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        feedbacks = feedbackDAO.getAllFeedbacks();

        request.setAttribute("listStaffFeedbacks", feedbacks);
        request.getRequestDispatcher("StaffFeedback.jsp").forward(request, response);
    }

    private void filterFeedbacks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ratingParam = request.getParameter("rating");
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        List<Feedback> feedbacks;

        if (ratingParam == null || ratingParam.isEmpty()) {
            // Nếu không có rating, lấy tất cả feedbacks
            feedbacks = feedbackDAO.getAllFeedbacks();
        } else {
            try {
                int rating = Integer.parseInt(ratingParam);
                feedbacks = feedbackDAO.getFeedbacksByRating(rating);
            } catch (NumberFormatException e) {
                // Nếu lỗi parse số, cũng lấy tất cả feedbacks
                feedbacks = feedbackDAO.getAllFeedbacks();
            }
        }

        request.setAttribute("listStaffFeedbacks", feedbacks);
        request.getRequestDispatcher("StaffFeedback.jsp").forward(request, response);
    }

    // Thêm hành động xử lý Feedback Detail
    private void viewFeedbackDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int feedbackID = Integer.parseInt(request.getParameter("id"));
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            Feedback feedback = feedbackDAO.getFeedbackById(feedbackID);  // Lấy feedback chi tiết

            if (feedback != null) {
                request.setAttribute("feedbackDetail", feedback);  // Đưa thông tin feedback vào request
                request.getRequestDispatcher("FeedbackDetail.jsp").forward(request, response);  // Chuyển sang trang FeedbackDetail.jsp
            } else {
                response.sendRedirect("listStaffFeedbacks?error=Feedback not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("listStaffFeedbacks?error=An error occurred");
        }
    }

    private void replyFeedback(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
        int staffID = Integer.parseInt(request.getParameter("staffID"));
        String replyText = request.getParameter("replyText");

        FeedbackDAO feedbackDAO = new FeedbackDAO();
        boolean isReplied = feedbackDAO.replyToFeedback(feedbackID, staffID, replyText);

        if (isReplied) {
            request.setAttribute("message", "Reply added successfully");
        } else {
            request.setAttribute("error", "Failed to add reply");
        }

        request.getRequestDispatcher("StaffFeedback.jsp").forward(request, response);
    }

    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int feedbackID = Integer.parseInt(request.getParameter("id"));
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        boolean isDeleted = feedbackDAO.deleteFeedback(feedbackID);

        if (isDeleted) {
            response.sendRedirect("listStaffFeedbacks?message=Feedback deleted successfully");
        } else {
            response.sendRedirect("listStaffFeedbacks?error=Feedback deletion failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý feedback cho nhân viên";
    }
}
