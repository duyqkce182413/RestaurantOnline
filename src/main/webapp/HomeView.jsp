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
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

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
                <form action="search-food" method="GET">
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
                        <a href="all" class="col-md">Tất cả</a>
                    <c:forEach items="${requestScope.categories}" var="c">
                        <a href="all?categoryid=${c.categoryID}&index=${i}" class="col-md ${param.categoryID == c.categoryID ? 'active' : ''}">${c.categoryName}</a>
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
                                            ${item.getFood().foodName}
                                            <span class="minicart-quantity badge bg-primary rounded-pill">${item.getCart().quantity}</span>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <!-- Khai báo biến tổng giá -->
                                <c:set var="totalPrice" value="0" />

                                <!-- Duyệt qua từng item và tính tổng giá -->
                                <c:forEach items="${cartlists}" var="item">
                                    <c:set var="itemTotal" value="${item.getCart().quantity * item.getFood().price}" />
                                    <c:set var="totalPrice" value="${totalPrice + itemTotal}" />
                                </c:forEach>

                                <!-- Hiển thị tổng giá -->
                                <div class="cart-total mt-3">
                                    <strong>Tổng đơn:</strong> 
                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" />
                                    <span>VND</span>
                                </div>



                                <a href="view-cart" class="minicart-btn btn btn-secondary mt-3">Chỉnh sửa giỏ hàng</a>
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
                                <div class="col-sm-12 col-md-6 col-lg-3">
                                    <div class="card product-card">
                                        <a href="view-food-detail?foodID=${f.foodID}">
                                            <img src="${pageContext.request.contextPath}/image/${f.image}" class="card-img-top" alt="${f.foodName}">
                                        </a>
                                        <div class="card-body">
                                            <a style="text-decoration: none" href="view-food-detail?foodID=${f.foodID}">
                                                <h5 class="card-title">${f.foodName}</h5>
                                            </a>
                                            <p class="card-text">${f.price}</p>

                                            <button type="button" class="btn btn-primary" 
                                                    onclick="openAddToCartModal('${f.foodID}', '${f.foodName}', '${f.image}', '${f.price}', '${f.quantity}')">
                                                Thêm vào giỏ
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Modal Bootstrap -->
                        <div class="modal fade" id="addToCartModal" tabindex="-1" aria-labelledby="modalTitle" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="modalTitle">Thêm vào giỏ hàng</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="text-center">
                                            <img id="modalFoodImage" src="" alt="Food Image" class="img-fluid" style="max-height: 200px;">
                                        </div>
                                        <h5 id="modalFoodName" class="mt-3 text-center"></h5>
                                        <p id="modalFoodPrice" class="text-center"></p>
                                        <form action="add-to-cart" method="get">
                                            <input type="hidden" name="foodId" id="modalFoodID">

                                            <div class="mb-3">
                                                <label for="quantityInput" class="form-label">Số lượng:</label>
                                                <input type="number" name="quantity" id="quantityInput" class="form-control" value="1" min="1">
                                                <p>Còn lại: <span id="modalFoodQuantity">${f.quantity}</span></p>

                                            </div>
                                            <div class="text-center">
                                                <button type="submit" class="btn btn-success">Thêm vào giỏ</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
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


            <script>
                function openAddToCartModal(foodID, foodName, foodImage, foodPrice, foodQuantity) {
                    // Gán thông tin món ăn vào modal
                    document.getElementById('modalFoodID').value = foodID;
                    document.getElementById('modalFoodName').innerText = foodName;
                    document.getElementById('modalFoodImage').src = foodImage !== 'null' ? foodImage : 'default-image.jpg';
                    document.getElementById('modalFoodPrice').innerText = `Giá: ${foodPrice} VND`;
                    document.getElementById('modalFoodQuantity').innerText = foodQuantity; // Cập nhật số lượng còn lại
                    document.getElementById('quantityInput').max = foodQuantity; // Đặt max cho input số lượng

                    // Hiển thị modal
                    let myModal = new bootstrap.Modal(document.getElementById('addToCartModal'));
                    myModal.show();
                }
        </script>
    </body>
</html>
