<%-- 
    Document   : FoodView
    Created on : Feb 18, 2025, 9:54:23 AM
    Author     : admin
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Food Page</title>

        <!-- CSS chung -->
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!--        <link rel="stylesheet" href="./CSS/Style.css">-->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>

        <!--CSS rieng-->
        <link rel="stylesheet" href="./CSS/Login_CSS.css">
    </head>
    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="login-form">
                <h2>Login</h2>
                <form action="login-with-username" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="mb-3 position-relative">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                        <span toggle="#password" class="fa fa-fw fa-eye toggle-password position-absolute" style="top: 50%; right: 10px; cursor: pointer; transform: translateY(60%); color: #FC6E51;"></span>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Login</button>
                        <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/Restaurant_Online/GoogleLoginServlet&response_type=code&client_id=127240927862-ovg89s7bgqd52hppkv89dq7i1ch9oc9m.apps.googleusercontent.com&access_type=offline&approval_prompt=force">
                            Login with Google
                        </a>
                        <p class ="error"> ${messerror}</p>
                </div>
                <div class="form-text"> 
                    Don't have an account? <a href="register.jsp">Register Here</a>
                </div>
            </form>
        </div>

        <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.querySelector(".toggle-password").addEventListener("click", function () {
                const passwordInput = document.getElementById("password");
                const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
                passwordInput.setAttribute("type", type);
                this.classList.toggle("fa-eye-slash"); // Chuyển đổi biểu tượng mắt
            });
        </script>
    </body>
</html>
