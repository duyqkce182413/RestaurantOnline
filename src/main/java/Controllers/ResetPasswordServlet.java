package Controllers;

import DAO.UserDAO;
import Utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        UserDAO userDAO = new UserDAO();
        if (userDAO.isResetTokenValid(token)) {
            request.setAttribute("token", token);
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Invalid or expired token.");
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword.equals(confirmPassword)) {
            UserDAO userDAO = new UserDAO();

            if (userDAO.isResetTokenValid(token)) {
                String hashedPassword = PasswordUtil.hashPassword(newPassword);
                userDAO.updatePasswordByToken(token, hashedPassword);
                request.setAttribute("message", "Đặt lại mật khẩu thành công.");
            } else {
                
                request.setAttribute("error", "Mã token không hợp lệ hoặc đã hết hạn.");
            }
        } else {
            request.setAttribute("error", "Mật khẩu không khớp.");
        }

        request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
    }
}

