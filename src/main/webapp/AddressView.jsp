<%-- 
    Document   : address_management
    Created on : Oct 17, 2024, 3:47:16 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page import="model.Address" %>--%>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông Tin Địa Chỉ</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <!--        <link rel="stylesheet" href="./CSS/home.css">-->
        <script src="./JS/header-script.js"></script>

        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7f7f7;
            }

            /* Navbar Customization */
            .navbar {
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .navbar-brand img {
                max-width: 150px;
            }

            .nav-link {
                font-size: 16px;
                font-weight: 500;
                color: #333;
            }

            .nav-link:hover {
                color: #FC6E51;
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

            /* Address Management Section */
            .address-card {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .address-card h5 {
                margin-bottom: 10px;
            }

            .address-card p {
                margin-bottom: 5px;
                font-size: 14px;
                color: #666;
            }

            .address-actions {
                display: flex;
                align-items: center;
            }

            .address-actions a {
                margin-right: 10px;
            }

            .btn-primary,
            .btn-danger,
            .btn-success {
                margin-right: 10px;
                background-color: #FC6E51;
                border-color: #FC6E51;
            }

            .btn-primary:hover,
            .btn-danger:hover,
            .btn-success:hover {
                background-color: #F56B4C;
                border-color: #F56B4C;
            }

            /* Default Address */
            .default-address {
                border: 2px solid #FC6E51;
                background-color: #fff3cd;
            }

            /* Form New Address */
            .form-control {
                margin-bottom: 10px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            /* Footer Customization */
            .footer {
                background-color: #333;
                color: white;
                padding: 20px 0;
                margin-top: 40px;
            }

            .footer p {
                margin: 0;
                font-size: 14px;
            }

            .footer strong {
                color: #FC6E51;
            }

            .content{
                margin: 100px auto;
            }
        </style>
        <script>
            function confirmDelete() {
                return confirm("Bạn có chắc chắn muốn xóa địa chỉ này không?");
            }
        </script>

    </head>

    <body>
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container content">
                <h3 class="text-center mb-4">Thông Tin Địa Chỉ</h3>

                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-md-3">
                        <ul>
                            <h3>Tài Khoản</h3>
                            <li><a href="UserProfile.jsp">Thông tin tài khoản</a></li>
                            <li><a href="listAddress">Danh sách địa chỉ</a></li>
                            <li><a href="listOrders">Lịch sử mua hàng</a></li>
                            <li><a href="logout">Đăng xuất</a></li>
                        </ul>
                    </div>

                    <!-- Address Cards -->
                    <div class="col-md-6">
                        <c:forEach var="address" items="${listAddresses}">
                            <div class="address-card ${address.isDefault() ? 'default-address' : ''}">
                                <div>
                                    <h5>${address.name} <c:if test="${address.isDefault()}">(Địa chỉ mặc định)</c:if></h5>
                                    <p>Địa chỉ: ${address.addressLine}, ${address.city}</p>
                                    <p>Số điện thoại: ${address.phoneNumber}</p>
                                    <c:if test="${!address.isDefault()}">
                                        <a href="setDefaultAddress?id=${address.addressID}" class="btn btn-success">Đặt Làm Địa Chỉ Mặc Định</a>
                                    </c:if>
                                </div>
                                <div class="address-actions">
                                    <a href="updateAddress?id=${address.addressID}" class="btn btn-primary">
                                        <i class="fa-solid fa-pen"></i> Sửa
                                    </a>
                                    <c:if test="${!address.isDefault()}">
                                        <a href="deleteAddress?id=${address.addressID}" class="btn btn-danger" onclick="return confirmDelete();">
                                            <i class="fa-solid fa-trash"></i> Xóa
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>


                <!-- New Address Form -->
                <div class="col-md-3">
                    <button class="btn btn-success mb-3" onclick="toggleAddressForm()">Nhập địa chỉ mới</button>
                    <div id="newAddressForm" style="display:none;">
                        <form action="insertAddress" method="POST">
                            <input type="text" class="form-control" placeholder="Họ và Tên" name="name" required>
                            <input type="text" class="form-control" placeholder="Địa chỉ" name="addressLine" required>
                            <input type="text" class="form-control" placeholder="Thành phố" name="city" required>
                            <input type="text" class="form-control" placeholder="Số điện thoại" name="phoneNumber" required>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="defaultAddress" name="is_default">
                                <label class="form-check-label" for="defaultAddress">Đặt làm địa chỉ mặc định</label>
                            </div>
                            <button type="submit" class="btn btn-success mt-3">Thêm Mới</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp"></jsp:include>

        <script>
            function toggleAddressForm() {
                var form = document.getElementById("newAddressForm");
                if (form.style.display === "none") {
                    form.style.display = "block";
                } else {
                    form.style.display = "none";
                }
            }

            function confirmDelete() {
                return confirm("Bạn có chắc chắn muốn xóa địa chỉ này không?");
            }
        </script>
    </body>

</html>
