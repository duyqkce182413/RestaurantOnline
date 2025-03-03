<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch Sử Mua Hàng</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>

        <style>
            /* Variables */
            :root {
                --primary-color: #FC6E51;
                --primary-dark: #e85d3f;
                --primary-light: rgba(253, 139, 115, 0.2); /* Hoặc rgba(253, 139, 115, 0.5) */
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

            /* Header Styles */
            .navbar {
                background-color: white;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .navbar-brand {
                color: var(--primary-color) !important;
                font-weight: bold;
                font-size: 1.5rem;
                border: 2px solid var(--primary-color);
                padding: 8px;
            }

            /* Order History Section */
            .order-history {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .order-history h2 {
                color: var(--primary-color);
                margin-bottom: 30px;
            }

            /* Table Styles */
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

            .btn-danger {
                background-color: var(--primary-dark);
                border: none;
                border-radius: 20px;
                padding: 8px 15px;
                color: white;
            }

            .btn-danger:hover {
                background-color: var(--primary-color);
                transform: translateY(-2px);
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .order-history {
                    padding: 15px;
                }

                th, td {
                    padding: 10px;
                }

                .btn-danger {
                    padding: 6px 10px;
                }
            }
        </style>
    </head>

    <body>
        <jsp:include page="Header.jsp"></jsp:include>
            <!-- Order History Section -->
            <div class="container my-5">
                <div class="row">
                    <!-- Order History -->
                    <div class="col-md-12">
                        <div class="order-history">
                            <h2>Lịch sử mua hàng</h2>
                        <c:if test="${param.message != null}">
                            <div class="alert alert-success">${param.message}</div>
                        </c:if>
                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger">${param.error}</div>
                        </c:if>

                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Mã đơn</th>
                                    <th>Ngày</th>
                                    <th>Trạng thái</th>
                                    <th>Thông tin sản phẩm</th>
                                    <th>Thông tin giao hàng</th>
                                    <th>Tổng số tiền</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listOrders}" var="order">
                                    <tr>
                                        <td>${order.orderID}</td>
                                        <td>${order.orderDate}</td>
                                        <td>${order.status}</td>
                                        <td>
                                            <ul>
                                                <c:forEach items="${order.orderDetail}" var="item">
                                                   <li>${item.foodID.foodName} - Số lượng: ${item.quantity} - Giá: ${item.price}</li>
                                                    </c:forEach>
                                            </ul>
                                        </td>
                                        <td>
                                            Tên: ${order.address.name} <br/> 
                                            ${order.address.addressLine} - ${order.address.city} <br/> 
                                            Số điện thoại: ${order.address.phoneNumber} 
                                        </td>
                                        <td>${order.totalAmount}</td>
                                        <td>
                                            <c:if test="${order.status.equals('Chưa xử lý') || order.status.equals('Đã tiếp nhận')}">
                                                <a href="cancelOrder?id=${order.orderID}" class="btn btn-danger"
                                                   onclick="return confirm('Bạn chắc chắn muốn hủy đơn hàng này?')">
                                                    Hủy Đơn Hàng
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
