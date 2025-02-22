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
        <link rel="stylesheet" href="./CSS/HomeView.css">
    </head>
    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container-fluid">
                <!-- Thanh tìm kiếm và menu trong cùng một form -->
                <form action="home-search" method="GET">
                    <div class="search-bar">
                        <div class="container">
                            <div class="d-flex justify-content-center">
                                <input type="text" name="search" class="form-control search-input" placeholder="Tìm món...">
                                <button type="submit" class="btn search-btn"><i class="fa-solid fa-search"></i></button>
                            </div>
                        </div>
                    </div>
                </form>

                <!-- Product Section + Cart -->
                <div class="container my-5">
                    <!-- Categories -->
                    <div class="menu-nav">
                        <a href="home" class="col-md">Tất cả</a>
                    <c:forEach items="${requestScope.categories}" var="c">
                        <a href="home?categoryid=${c.categoryID}&index=${i}" class="col-md ${param.categoryID == c.categoryID ? 'active' : ''}">${c.categoryName}</a>
                    </c:forEach>

                </div>
            </div>

            <div class="container-fluid main-content px-5">
                <div class="row">
                    <!-- Phần giỏ hàng -->
                    <div class="col-md-3">
                        <div class="cart-section">
                            <div class="cart-title">Giỏ hàng</div>
                            <c:if test="${not empty cartlists}">
                                <ul class="list-group">
                                    <c:forEach items="${cartlists}" var="item">
                                        <li class="list-group-item d-flex justify-content-between align-items-center">
                                            ${item.product.name} 
                                            <span class="minicart-quantity badge bg-primary rounded-pill">${item.quantity}</span>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <!-- Khai báo biến tổng giá -->
                                <c:set var="totalPrice" value="0" />

                                <!-- Duyệt qua từng item và tính tổng giá -->
                                <c:forEach items="${cartlists}" var="item">
                                    <c:set var="itemTotal" value="${item.quantity * item.product.price}" />
                                    <c:set var="totalPrice" value="${totalPrice + itemTotal}" />
                                </c:forEach>

                                <!-- Hiển thị tổng giá -->
                                <div class="cart-total mt-3">
                                    <strong>Tổng đơn:</strong> 
                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" />
                                    <span>VND</span>
                                </div>



                                <a href="cart" class="minicart-btn btn btn-secondary mt-3">Chỉnh sửa giỏ hàng</a>
                            </c:if>
                            <c:if test="${empty cartlists}">
                                <p>Giỏ hàng trống.</p>
                            </c:if>
                        </div>
                    </div>

                    <!-- Phần sản phẩm -->
                    <div class="col-md-9">
                        <div class="row g-4">
                            <c:forEach items="${requestScope.foods}" var="f">
                                <c:set var="quantity" value="1" /> <!-- Số lượng mặc định -->
                                <c:forEach items="${cartlists}" var="item">
                                    <c:if test="${item.product.id == f.id}">
                                        <c:set var="quantity" value="${item.quantity + 1}" /> <!-- Nếu đã có trong giỏ hàng, cộng thêm 1 -->
                                    </c:if>
                                </c:forEach>

                                <div class="col-sm-12 col-md-6 col-lg-3">
                                    <div class="card product-card">
                                        <a href="product-detail?pro_id=${f.foodID}">
                                            <img src="${f.image != null ? p.image : 'default-image.jpg'}" class="card-img-top" alt="${f.foodName}">
                                        </a>
                                        <div class="card-body">
                                            <a style="text-decoration: none" href="product-detail?pro_id=${f.foodID}">
                                                <h5 class="card-title">${f.foodName}</h5>
                                            </a>
                                            <p class="card-text">${f.price}</p>

                                            <!-- Thay đổi nút Đặt Món thành một form -->
                                            <form action="updateCart" method="post" style="display:inline;">
                                                <input type="hidden" name="productId" value="${f.foodID}">
                                                <input type="hidden" name="quantity" value="${quantity}"> <!-- Sử dụng số lượng đã tính -->

                                                <button type="submit" class="btn btn-primary">Đặt Món</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>


                        <div class="float-end">
                            <jsp:include page="pagination.jsp">
                                <jsp:param name="baseUrl" value="otherServlet" />
                            </jsp:include>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>
    </body>
</html>
