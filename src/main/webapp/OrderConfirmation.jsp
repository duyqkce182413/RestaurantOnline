<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xác nhận đơn hàng</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>

        <style>
            /* Variables */
            :root {
                --primary-color: #FC6E51;
                --primary-dark: #e85d3f;
                --primary-light: rgba(253, 139, 115, 0.2);
                --text-color: #333333;
                --bg-light: #f8f9fa;
                --transition: all 0.3s ease;
            }

            /* Global Styles */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                color: var(--text-color);
                background-color: var(--bg-light);
            }

            .navbar {
                background-color: white;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .order-confirmation {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            h2 {
                color: var(--primary-color);
                margin-bottom: 30px;
            }

            .change-address {
                background-color: var(--primary-color);
                color: white;
                margin-bottom: 30px;
            }
            
            .change-address:hover {
                background-color: var(--primary-dark);
                color: white;
                transform: translateY(-2px);
            }
            
            table {
                width: 100%;
                margin-bottom: 20px;
                border-collapse: collapse;
            }

            th, td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background-color: var(--bg-light);
                color: var(--text-color);
            }

            tr:hover {
                background-color: var(--primary-light);
            }

            .btn-success {
                background-color: var(--primary-dark);
                border: none;
                border-radius: 20px;
                padding: 10px 15px;
                color: white;
                width: 100%;
            }

            .btn-success:hover {
                background-color: var(--primary-color);
                transform: translateY(-2px);
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .order-confirmation {
                    padding: 15px;
                }

                th, td {
                    padding: 10px;
                }

                .btn-success {
                    padding: 8px 12px;
                }
            }
        </style>
    </head>

    <body>
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container my-5 order-confirmation">
                <h2 class="text-center">Xác nhận đơn hàng</h2>

                <div class="row">
                    <!-- Thông tin địa chỉ giao hàng -->
                    <div class="col-md-6">
                        <div class="order-summary">
                            <h4>Địa chỉ giao hàng</h4>
                        <c:choose>
                            <c:when test="${not empty address}">
                                <p><strong>Họ tên:</strong> ${address.name}</p>
                                <p><strong>Địa chỉ:</strong> ${address.addressLine}, ${address.city}</p>
                                <p><strong>Số điện thoại:</strong> ${address.phoneNumber}</p>
                                <a href="listAddress" class="btn change-address">Thay đổi địa chỉ mặc định</a>
                            </c:when>
                            <c:otherwise>
                                <p class="text-danger">Không tìm thấy địa chỉ mặc định.</p>
                                <a href="listAddress" class="btn change-address">Mời bạn thêm địa chỉ mặc định</a>
                            </c:otherwise>
                        </c:choose>

                        <h4>Phương thức thanh toán</h4>
                        <p>Thanh toán khi nhận hàng</p>
                    </div>
                </div>

                <!-- Danh sách sản phẩm -->
                <div class="col-md-6">
                    <div class="order-summary">
                        <h4>Chi tiết đơn hàng</h4>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Tên món</th>
                                    <th>Số lượng</th>
                                    <th>Giá</th>
                                    <th>Tổng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="total" value="0" />
                                <c:forEach items="${cartItems}" var="item">
                                    <c:set var="itemTotal" value="${item.cart.quantity * item.food.price}" />
                                    <c:set var="total" value="${total + itemTotal}" />
                                    <tr>
                                        <td>${item.food.foodName}</td>
                                        <td>${item.cart.quantity}</td>
                                        <td>${item.food.price} VND</td>
                                        <td>${itemTotal} VND</td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td colspan="3"><strong>Tổng cộng:</strong></td>
                                    <td><strong>${total} VND</strong></td>
                                </tr>
                            </tbody>
                        </table>

                        <!-- Form xác nhận đặt hàng -->
                        <form action="checkoutcart" method="POST">
                            <button type="submit" class="btn btn-success">Xác nhận đặt hàng</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
