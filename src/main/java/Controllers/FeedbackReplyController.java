/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.FeedbackDAO;
import DAO.FeedbackReplyDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author admin
 */
public class FeedbackReplyController extends HttpServlet {

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
            out.println("<title>Servlet FeedbackReplyController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackReplyController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);

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

        try {
            switch (action) {
                case "/edit-feedback-reply":
                    editFeedbackReply(request, response);
                    break;
                case "/delete-feedback-reply":
                    deleteFeedbackReply(request, response);
                    break;
                case "/staff-edit-feedback-reply":
                    staffEditFeedbackReply(request, response);
                    break;
                case "/staff-delete-feedback-reply":
                    staffDeleteFeedbackReply(request, response);
                    break;    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Feedback Reply Servlet";
    }

    protected void editFeedbackReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int replyID = Integer.parseInt(request.getParameter("replyID"));
        String replyText = request.getParameter("replyText");
        int foodId = Integer.parseInt(request.getParameter("foodId"));

        // Cập nhật phản hồi
        FeedbackReplyDAO replyDAO = new FeedbackReplyDAO();
        boolean isUpdated = replyDAO.updateFeedbackReply(replyID, replyText);

        if (isUpdated) {
            response.sendRedirect("view-food-detail?foodID=" + foodId); // Chuyển hướng về trang chi tiết
        } else {
            response.getWriter().write("Cập nhật phản hồi thất bại!");
        }
    }

    private void deleteFeedbackReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int replyID = Integer.parseInt(request.getParameter("replyID"));
        FeedbackReplyDAO feedbackReplyDAO = new FeedbackReplyDAO();
        boolean isDeleted = feedbackReplyDAO.deleteFeedbackReply(replyID);

        if (isDeleted) {
            response.sendRedirect("view-food-detail?foodID=" + request.getParameter("foodID") + "&success=true");
        } else {
            response.sendRedirect("view-food-detail?foodID=" + request.getParameter("foodID") + "&error=true");
        }
    }

    protected void staffEditFeedbackReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int replyID = Integer.parseInt(request.getParameter("replyID"));
        String replyText = request.getParameter("replyText");
        int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
        int staffID = Integer.parseInt(request.getParameter("staffID"));
        
        // Cập nhật phản hồi
        FeedbackReplyDAO replyDAO = new FeedbackReplyDAO();
        boolean isUpdated = replyDAO.updateFeedbackReply(replyID, replyText);

        if (isUpdated) {
            response.sendRedirect("viewFeedbackDetails?id=" + feedbackID + "&userId=" + staffID); // Chuyển hướng về trang chi tiết
        } else {
            response.getWriter().write("Cập nhật phản hồi thất bại!");
        }
    }
    
    private void staffDeleteFeedbackReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int replyID = Integer.parseInt(request.getParameter("replyID"));
        int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
        int staffID = Integer.parseInt(request.getParameter("staffID"));
        
        FeedbackReplyDAO feedbackReplyDAO = new FeedbackReplyDAO();
        boolean isDeleted = feedbackReplyDAO.deleteFeedbackReply(replyID);

        if (isDeleted) {
            response.sendRedirect("viewFeedbackDetails?id=" + feedbackID + "&userId=" + staffID); // Chuyển hướng về trang chi tiết
        } else {
            response.getWriter().write("Cập nhật phản hồi thất bại!");
        }
    }
}
