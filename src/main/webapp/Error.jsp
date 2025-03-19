<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lỗi Xác Thực</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8d7da;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .error-container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 400px;
        }
        .error-icon {
            font-size: 50px;
            color: red;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <i class="fa fa-exclamation-triangle error-icon"></i>
        <h2 class="text-danger">Lỗi xác thực</h2>
        <p>
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "Token không hợp lệ hoặc đã hết hạn!" %>
        </p>
        <a href="ForgotPasswordView.jsp" class="btn btn-primary">Thử lại</a>
    </div>
</body>
</html>
