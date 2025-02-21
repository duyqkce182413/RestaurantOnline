<%-- 
    Document   : food_detail
    Created on : Oct 17, 2024, 7:41:02 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi Tiết Món Ăn</title>
        <!-- Bootstrap -->
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>

        <style>
            /* General Body Styling */
            body {
                font-family: 'Arial', sans-serif;
                background-color: #fffdf8;
                color: #333;
            }

            mg-100px {
                margin: 100px;
            }


            /* Food Detail Page Styling */
            .food-detail-page {
                padding: 50px 0;
            }

            .food-img {
                max-width: 100%;
                border-radius: 15px;
                box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }

            .food-img:hover {
                transform: scale(1.05);
            }

            .food-name {
                font-size: 34px;
                font-weight: bold;
                color: #FC6E51;
                text-transform: uppercase;
            }

            .food-price {
                font-size: 26px;
                font-weight: bold;
                color: #d9534f;
                margin-top: 10px;
            }

            .food-desc {
                font-size: 16px;
                color: #777;
                line-height: 1.7;
                margin: 20px 0;
            }

            .quantity {
                width: 80px;
                text-align: center;
                border-radius: 8px;
                border: 1px solid #ddd;
                padding: 5px;
            }

            .btn-cart {
                background-color: #FF7F50;
                border: none;
                color: #fff;
                font-size: 16px;
                font-weight: bold;
                padding: 12px 25px;
                border-radius: 25px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                transition: background-color 0.3s ease, transform 0.2s ease;
            }

            .btn-cart:hover {
                background-color: #FF6347;
                transform: translateY(-2px);
            }

            /* Custom Back Button */
            .btn-back {
                background-color: #ddd;
                color: #333;
                font-size: 16px;
                padding: 10px 20px;
                border-radius: 25px;
                text-decoration: none;
                display: inline-block;
                transition: background-color 0.3s ease, color 0.3s ease;
            }

            .btn-back:hover {
                background-color: #bbb;
                color: #fff;
            }

            /* Footer Styling */
            .footer {
                background-color: #333;
                color: #f1f1f1;
                padding: 40px 0;
                font-size: 14px;
            }

            .footer strong {
                color: #FC6E51;
            }

            .footer p {
                margin: 0;
                font-size: 14px;
            }
            
        </style>
    </head>

    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container my-5">
                <div class="row">
                    <!-- Food Image -->
                    <div class="col-md-6">
                        <img src="${food_detail.image}" alt="Tên món ăn" class="food-img">
                </div>

                <!-- Food Details -->
                <div class="col-md-6 margin-left">
                    <h2 class="food-name">${food_detail.foodName}</h2>
                    <p class="food-price">${food_detail.price} VND</p>

                    <!-- Quantity and Add to Cart -->
                    <form action="ProductDetailController" method="post"> <!-- Đổi action thành 'cart' để gửi đến CartController -->
                        <div class="form-group">
                            <label for="quantity">Số Lượng:</label>
                            <input type="number" id="quantity" name="quantity" class="form-control quantity" min="1" value="1">
                        </div>

                        <input type="hidden" name="productId" value="${food_detail.foodID}"> <!-- ID của món ăn -->
                        <c:if test="${food_detail.quantity > 0}">
                            <p>Còn lại: ${food_detail.quantity}</p> 
                            <button type="submit" class="btn btn-cart mt-3 text-white">Thêm Vào Giỏ Hàng</button>
                        </c:if>
                        <c:if test="${food_detail.quantity <= 0}">
                            <p>Tình trạng: Hết hàng</p>
                        </c:if>

                        <a class="btn btn-cart mt-3 bg-gradient-secondary text-white" href="all">Trở lại</a>
                    </form>

                </div>
            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>


