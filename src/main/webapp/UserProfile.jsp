<%-- Document : account.jsp Created on : Oct 17, 2024, 1:40:00 PM Author : admin --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Account Information</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/HomeView.css">
        <script src="./JS/header-script.js"></script>

        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7f7f7;
            }

            /* Sidebar Customization */
            .col-md-3 ul {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                list-style-type: none;
            }

            .col-md-3 ul h3 {
                font-size: 20px;
                margin-bottom: 20px;
                font-weight: bold;
                color: #FC6E51;
            }

            .col-md-3 ul li {
                margin-bottom: 15px;
            }

            .col-md-3 ul li a {
                text-decoration: none;
                font-size: 16px;
                color: #333;
                transition: color 0.2s;
            }

            .col-md-3 ul li a:hover {
                color: #FC6E51;
            }

            /* Account Information Section */
            .account-info {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .account-info h3 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 30px;
                color: #333;
            }

            .account-info .info-item {
                margin-bottom: 20px;
            }

            .account-info label {
                font-weight: bold;
                margin-bottom: 5px;
                display: block;
                color: #555;
            }

            .account-info input[type="text"],
            .account-info input[type="email"],
            .account-info input[type="password"]{
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                margin-bottom: 10px;
            }

            .account-info .btn {
                background-color: #FC6E51;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .account-info .btn:hover {
                background-color: #F56B4C;
            }
        </style>
    </head>

    <body>
        <c:if test="${not empty sessionScope.errors}">
            <div class="alert alert-danger">
                <ul>
                    <c:forEach var="error" items="${sessionScope.errors}">
                        <li>${error}</li>
                        </c:forEach>
                </ul>
            </div>
            <c:remove var="errors" scope="session" />
        </c:if>

        <jsp:include page="Header.jsp"></jsp:include>
        <% 
String message = (String) session.getAttribute("updateMessage");
if (message != null) { 
        %>
        <div class="text-center" style="color: green; font-weight: bold;"><%= message %></div>
        <%
                session.removeAttribute("updateMessage"); // Xóa thông báo sau khi hiển thị
            }
        %>
        <!-- Info Account Section -->
        <div class="container my-5">
            <h3 class="text-center mb-5">Tài Khoản</h3>
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3">
                    <ul>
                        <li><a href="UserProfile.jsp">Thông tin tài khoản</a></li>
                        <li><a href="listAddress">Danh sách địa chỉ</a></li>
                        <li><a href="listOrders">Lịch sử mua hàng</a></li>
                        <li><a href="logout">Đăng xuất</a></li>
                    </ul>
                </div>


                <!-- Account Information -->
                <c:if test="${sessionScope.user != null}">
                    <div class="col-md-9">
                        <div class="account-info">
                            <h3>Thông tin tài khoản</h3>
                            <div class="row">
                                <div class="col-8">
                                    <div class="info-item">
                                        <label for="fullName">Họ và Tên:</label>
                                        <input type="text" id="fullName" value="${sessionScope.user.fullName}" readonly>
                                    </div>
                                    <div class="info-item">
                                        <label for="username">Tên đăng nhập:</label>
                                        <input type="text" id="username" value="${sessionScope.user.username}" readonly>
                                    </div>
                                </div>
                                <div class="col-4 d-flex flex-column align-items-center justify-content-center">
                                    <div class="info-item text-center">
                                        <label>Ảnh đại diện:</label><br>
                                        <c:choose>
                                            <c:when test="${sessionScope.user.avatar.startsWith('http')}">
                                                <!-- Nếu avatar là URL (ví dụ: ảnh từ Google) -->
                                                <img src="${sessionScope.user.avatar}" alt="Avatar" width="100" class="rounded-circle">
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Nếu avatar là ảnh lưu trong project -->
                                                <img src="${pageContext.request.contextPath}/image/${sessionScope.user.avatar}" alt="Avatar" width="100" class="rounded-circle">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                            </div>
                            <div class="info-item">
                                <label for="password">Mật khẩu:</label>
                                <input type="password" id="password" value="${sessionScope.user.passwordHash}" readonly>
                                <input type="checkbox" onclick="myFunction()"> Hiển thị mật khẩu
                            </div>
                            <div class="info-item">
                                <label for="email">Email:</label>
                                <input type="email" id="email" value="${sessionScope.user.email}" readonly>
                            </div>
                            <div class="info-item">
                                <label for="phoneNumber">Số điện thoại:</label>
                                <input type="text" id="phoneNumber" value="${sessionScope.user.phoneNumber}" readonly>
                            </div>
                            <div class="info-item">
                                <label for="dateOfBirth">Ngày sinh:</label>
                                <input type="text" id="dateOfBirth" value="${sessionScope.user.dateOfBirth}" readonly>
                            </div>
                            <div class="info-item">
                                <label for="gender">Giới tính:</label>
                                <input type="text" id="gender" value="${sessionScope.user.gender}" readonly>
                            </div>

                            <!-- Nút mở popup -->
                            <div class="info-item mx-auto">
                                <button class="btn" data-bs-toggle="modal" data-bs-target="#editAccountModal">Chỉnh sửa thông tin</button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Modal (Popup) -->
<div class="modal fade" id="editAccountModal" tabindex="-1" aria-labelledby="editAccountModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editAccountModalLabel">Chỉnh sửa thông tin</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="update-user" method="post">
                    <div class="mb-3">
                        <label for="editFullName" class="form-label">Họ và Tên:</label>
                        <input type="text" class="form-control" id="editFullName" name="fullName" value="${sessionScope.user.fullName}">
                    </div>
                    <div class="mb-3">
                        <label for="editPassword" class="form-label">Mật khẩu mới:</label>
                        <div class="input-group">
                            <input type="password" class="form-control" id="editPassword" name="password" required>
                            <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                <i class="fa fa-eye"></i>
                            </button>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Nhập lại mật khẩu:</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email:</label>
                        <input type="email" class="form-control" id="editEmail" name="email" value="${sessionScope.user.email}">
                    </div>
                    <div class="mb-3">
                        <label for="editPhoneNumber" class="form-label">Số điện thoại:</label>
                        <input type="text" class="form-control" id="editPhoneNumber" name="phoneNumber" value="${sessionScope.user.phoneNumber}">
                    </div>
                    <div class="mb-3">
                        <label for="editDateOfBirth" class="form-label">Ngày sinh:</label>
                        <input type="date" class="form-control" id="editDateOfBirth" name="dateOfBirth" value="${sessionScope.user.dateOfBirth}">
                    </div>
                    <div class="mb-3">
                        <label for="editGender" class="form-label">Giới tính:</label>
                        <select class="form-control" id="editGender" name="gender">
                            <option value="Male" ${sessionScope.user.gender == 'Male' ? 'selected' : ''}>Male</option>
                            <option value="Female" ${sessionScope.user.gender == 'Female' ? 'selected' : ''}>Female</option>
                            <option value="Other" ${sessionScope.user.gender == 'Other' ? 'selected' : ''}>Other</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>



<jsp:include page="Footer.jsp"></jsp:include>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
                                    function myFunction() {
                                        var x = document.getElementById("password");
                                        if (x.type === "password") {
                                            x.type = "text";
                                        } else {
                                            x.type = "password";
                                        }
                                    }

                                    document.getElementById('togglePassword').addEventListener('click', function () {
                                        let passwordField = document.getElementById('editPassword');
                                        let icon = this.querySelector('i');
                                        if (passwordField.type === 'password') {
                                            passwordField.type = 'text';
                                            icon.classList.remove('fa-eye');
                                            icon.classList.add('fa-eye-slash');
                                        } else {
                                            passwordField.type = 'password';
                                            icon.classList.remove('fa-eye-slash');
                                            icon.classList.add('fa-eye');
                                        }
                                    });

</script>
</body>

</html>