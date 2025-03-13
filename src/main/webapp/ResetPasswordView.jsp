<%-- 
    Document   : ResetPasswordView
    Created on : [Ngày tạo]
    Author     : [Tác giả]
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/HomeView.css">
        <link rel="stylesheet" href="./CSS/Login_CSS.css">
    </head>
    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

        <div class="login-form">
            <h2>Đặt lại mật khẩu</h2>

            <!-- Hiển thị thông báo -->
            <% if (request.getAttribute("message") != null) { %>
                <div class="alert alert-success text-center" role="alert">
                    <%= request.getAttribute("message") %>
                </div>
                <div class="d-grid gap-2">
                    <a href="LoginView.jsp" class="btn btn-success">Đi đến đăng nhập</a>
                </div>
            <% } else { %>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger text-center" role="alert">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form action="reset-password" method="post">
                    <input type="hidden" name="token" value="${param.token}">
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">Mật khẩu mới</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" required>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Đặt lại mật khẩu</button>
                    </div>
                </form>
            <% } %>
        </div>

        <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
