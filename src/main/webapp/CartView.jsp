<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%--<%@ page import="model.Product" %>--%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shopping Cart</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">

        <script src="./JS/header-script.js"></script>

        <style>
            /* Variables */
            :root {
                --primary-color: #FC6E51;
                --primary-dark: #e85d3f;
                --primary-light: rgba(253, 139, 115, 0.9); /* Đã sửa màu sáng hơn */
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
                font-size: 15px;
            }

            .product-name {
                font-size: 1.2rem; /* Điều chỉnh kích thước chữ */
                font-weight: bold; /* Làm đậm chữ */
                color: var(--primary-color); /* Sử dụng màu chính để tạo điểm nhấn */
                text-transform: capitalize; /* Viết hoa chữ cái đầu tiên của mỗi từ */
            }


            td img {
                width: 120px; /* Hoặc kích thước bạn muốn */
                height: 100px; /* Đảm bảo chiều cao cố định */
                object-fit: cover; /* Cắt hình ảnh theo tỉ lệ mà không làm biến dạng */
                border-radius: 8px; /* Thêm bo góc nếu cần */
            }


            th, td {
                padding: 15px;
                text-align: left; /* Căn trái nội dung trong ô */
                border-bottom: 1px solid #ddd;
                vertical-align: middle; /* Căn giữa theo trục y */
            }

            th {
                background-color: var(--bg-light);
                color: var(--text-color);
                font-size: larger;
            }

            /* Button Styles */
            .btn-danger {
                background-color: var(--primary-dark);
                border: none;
                border-radius: 20px;
                padding: 8px 15px;
                color: white;
                transition: background-color 0.3s;
            }

            .btn-danger:hover {
                background-color: var(--primary-color);
                transform: translateY(-2px);
            }

            /* Quantity Control Styles */
            .quantity-control {
                display: flex;
                align-items: center;
                gap: 10px; /* Khoảng cách giữa các nút và input */
            }

            .quantity-btn {
                background-color: var(--primary-color);
                border: none;
                color: white;
                border-radius: 5px;
                padding: 8px 12px;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .quantity-btn:hover {
                background-color: var(--primary-dark);
            }

            .quantity-input {
                width: 60px; /* Đặt chiều rộng cho input */
                text-align: center;
                border: 1px solid var(--primary-color);
                border-radius: 5px;
                padding: 5px;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .order-history {
                    padding: 15px;
                }

                th, td {
                    padding: 10px;
                    font-size: 12px; /* Đã điều chỉnh kích thước font trên thiết bị nhỏ */
                }

                .btn-danger {
                    padding: 6px 10px;
                }
            }


        </style>
    </head>
    <body>
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container my-5">
                <h3 class="text-center mb-4">Giỏ Hàng Của Bạn</h3>
                <div class="cart-table">
                    <form action="update-cart" method="get" id="cartForm">
                        <input type="hidden" name="action" value="update">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Sản Phẩm</th>
                                    <th>Hình Ảnh</th>
                                    <th>Số Lượng</th>
                                    <th>Giá</th>
                                    <th>Tổng</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>

                            <c:forEach items="${cartItems}" var="cartItems">

                                <tr>
                                    <td class="product-name" >${cartItems.getFood().foodName}</td>
<!--                                    <input type="hidden" name="foodId" value="${cartItems.getFood().foodID}">-->

                                    <td><img style="width: 100px" src="${pageContext.request.contextPath}/image/${cartItems.getFood().image}" alt="${cartItems.getFood().foodName}"/></td>
                                    <td>
                                        <div class="quantity-control">
                                            <button type="button" class="quantity-btn" onclick="decreaseQuantity(this)">-</button>

                                            <input type="number" name="quantity" class="quantity-input" 
                                                   value="${cartItems.getCart().quantity}" min="1"
                                                   data-price="${cartItems.getFood().price}"
                                                   onchange="updateRowTotal(this)">

                                            <button type="button" class="quantity-btn" onclick="increaseQuantity(this)">+</button>
                                        </div>
                                    </td>
                                    <td class="price">${cartItems.getFood().price}</td>
                                    <td class="row-total">${cartItems.getFood().price*cartItems.getCart().quantity}</td>
<!--                            <input type="hidden" name="foodId" value="${cartItems.getFood().foodID}">-->
                            <td>
                                <input type="hidden" name="foodId" value="${cartItems.getFood().foodID}">
                                <a href="javascript:void(0)" onclick="removeItem(this, ${cartItems.getFood().foodID})">
                                    <i class="fa-solid fa-trash"></i>
                                </a>

                            </td>
                            </tr>

                        </c:forEach>

                        </tbody>

                    </table>
                    <c:if test="${empty cartItems}">
                        <p class="cart-empty-message">Giỏ hàng trống</p>
                    </c:if>
                    <button style="border-radius: 20px" type="submit" class="btn btn-warning float-end">Cập nhật giỏ hàng</button>
                </form>

                <c:if test="${param.error == 'address_not_found'}">
                    <script>
                        alert("Địa chỉ mặc định không được tìm thấy. Vui lòng thêm địa chỉ mới.");
                    </script>
                </c:if>

                 <c:if test="${param.error == 'Cart is empty'}">
                    <script>
                        alert("Giỏ hàng không được trống");
                    </script>
                </c:if>   
                <div class="cart-summary">
                    <p><strong>Tổng số lượng:</strong> <span id="totalItems">0</span> sản phẩm</p>
                    <p><strong>Tổng số tiền:</strong> <span id="totalAmount">0</span> VND</p>
                </div>


                <div class="cart-actions">
                    <a href="all" class="btn btn-primary">Tiếp Tục Mua</a>
                    <form action="checkoutcart" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-success paybtn" style="border-radius: 20px">Thanh Toán</button>
                    </form>
                </div>

                <%-- Hiển thị thông báo lỗi nếu có --%>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

            </div>
        </div>

        <jsp:include page="Footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                        function decreaseQuantity(btn) {
                            const input = btn.nextElementSibling;
                            if (input.value > 1) {
                                input.value = parseInt(input.value) - 1;
                                updateRowTotal(input);
                            }
                        }

                        function increaseQuantity(btn) {
                            const input = btn.previousElementSibling;
                            input.value = parseInt(input.value) + 1;
                            updateRowTotal(input);
                        }

                        function updateRowTotal(input) {
                            const price = parseFloat(input.getAttribute('data-price'));
                            const quantity = parseInt(input.value);
                            const row = input.closest('tr');
                            const rowTotal = row.querySelector('.row-total');
                            const total = price * quantity;
                            rowTotal.textContent = total.toLocaleString('vi-VN') + ' VND';
                            updateCartTotals();
                        }

                        function updateCartTotals() {
                            let totalItems = 0;
                            let totalAmount = 0;

                            document.querySelectorAll('.quantity-input').forEach(input => {
                                const quantity = parseInt(input.value);
                                const price = parseFloat(input.getAttribute('data-price'));
                                totalItems += quantity;
                                totalAmount += quantity * price;
                            });

                            document.getElementById('totalItems').textContent = totalItems;
                            document.getElementById('totalAmount').textContent = totalAmount.toLocaleString('vi-VN');
                        }

                        // Initialize totals when page loads
                        document.addEventListener('DOMContentLoaded', function () {
                            document.querySelectorAll('.quantity-input').forEach(input => {
                                updateRowTotal(input);
                            });
                        });

                        function removeItem(link, productId) {
                            // Xác nhận trước khi xóa
                            if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")) {
                                const form = document.getElementById('cartForm');

                                // Tạo một input ẩn để gửi productId
                                const input = document.createElement('input');
                                input.type = 'hidden';
                                input.name = 'removeProductId'; // Tên tham số cho việc xóa sản phẩm
                                input.value = productId;
                                form.appendChild(input);

                                // Gửi form
                                form.submit();
                            }
                        }
        </script>
    </body>
</html>
