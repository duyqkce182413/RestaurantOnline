package Controllers;

import DAO.FeedbackDAO;
import Models.Feedback;
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
                case "/listFeedbacks":
                    listAllFeedbacks(request, response);
                    break;
                case "/filterFeedbacks":
                    filterFeedbacks(request, response);
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
        if ("/replyFeedback".equals(action)) {
            replyFeedback(request, response);
        } else if ("/deleteFeedback".equals(action)) {
            deleteFeedback(request, response);
        }
    }

    private void listAllFeedbacks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Feedback> feedbacks;
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        feedbacks = feedbackDAO.getAllFeedbacks();

        request.setAttribute("listFeedbacks", feedbacks);
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

        request.setAttribute("listFeedbacks", feedbacks);
        request.getRequestDispatcher("StaffFeedback.jsp").forward(request, response);
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
        int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        boolean isDeleted = feedbackDAO.deleteFeedback(feedbackID);

        if (isDeleted) {
            response.sendRedirect("listFeedbacks?message=Feedback deleted successfully");
        } else {
            response.sendRedirect("listFeedbacks?error=Feedback deletion failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý feedback cho nhân viên";
    }
}
