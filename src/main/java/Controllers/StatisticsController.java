/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAO.RevenueDAO;
import Models.Revenue;
import Models.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class StatisticsController extends HttpServlet {

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
            out.println("<title>Servlet StatisticsController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StatisticsController at " + request.getContextPath() + "</h1>");
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
        // Kiểm tra nếu người dùng đã đăng nhập và là admin
//        HttpSession session = request.getSession();
//        User acc = (User) session.getAttribute("user");
//
//        if (user == null || !user.isAdmin()) {
//            response.sendRedirect("login.jsp?error=You must be an admin to access this page.");
//            return;
//        }

        try {
            // Get date range from parameters
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate;
            Date endDate;

            if (startDateStr != null && !startDateStr.isEmpty()
                    && endDateStr != null && !endDateStr.isEmpty()) {
                // Parse dates from parameters
                startDate = dateFormat.parse(startDateStr);
                endDate = dateFormat.parse(endDateStr);
            } else {
                // Default to last 30 days if no dates provided
                Calendar cal = Calendar.getInstance();
                endDate = cal.getTime();
                cal.add(Calendar.DATE, -30);
                startDate = cal.getTime();
            }

            RevenueDAO revenueDAO = new RevenueDAO();
            List<Revenue> revenueData = revenueDAO.getRevenueByDateRange(startDate, endDate);

            // Calculate total revenue
            double totalRevenue = 0;
            for (Revenue item : revenueData) {
                totalRevenue += item.getRevenue();
            }


            // Return JSON response
            if (request.getHeader("X-Requested-With") != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Create response object
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("revenueData", revenueData);
                responseData.put("totalRevenue", totalRevenue);

                Gson gson = new Gson();
                response.getWriter().write(gson.toJson(responseData));
            } else {
                // Forward to JSP
                request.setAttribute("revenueData", revenueData);
                request.setAttribute("totalRevenue", totalRevenue);
                request.setAttribute("startDate", dateFormat.format(startDate));
                request.setAttribute("endDate", dateFormat.format(endDate));
                request.getRequestDispatcher("ManageStatistic.jsp")
                        .forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
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

}
