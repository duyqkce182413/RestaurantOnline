package Controllers;

import DAO.UserDAO;
import Utils.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");

        UserDAO userDAO = new UserDAO();
        boolean emailExists = userDAO.isEmailExists(email);

        if (emailExists) {
            // Tạo token và thời gian hết hạn
            String resetToken = UUID.randomUUID().toString();
            java.util.Date expiryDate = new java.util.Date(System.currentTimeMillis() + 60 * 60 * 1000); // 1 giờ sau

            // Lưu token và thời gian hết hạn vào database
            userDAO.updateResetPasswordToken(email, resetToken, new java.sql.Timestamp(expiryDate.getTime()));

            // Tạo liên kết đặt lại mật khẩu
            String resetLink = "http://localhost:8080/Restaurant_Online/reset-password?token=" + resetToken;

            // Gửi email chứa liên kết đặt lại mật khẩu
            try {
                EmailUtil.sendResetPasswordEmail(email, resetLink);
                request.setAttribute("message", "A password reset link has been sent to your email.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to send email. Please try again.");
            }
        } else {
            // Hiển thị thông báo lỗi
            request.setAttribute("error", "Email not found.");
        }

        request.getRequestDispatcher("ForgotPasswordView.jsp").forward(request, response);
    }
}