<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Liên hệ - Nhà hàng TheKoi</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!--        <link rel="stylesheet" href="./CSS/Style.css">-->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>

        <!--CSS rieng-->
        <link rel="stylesheet" href="./CSS/HomeView.css">
        <style>
            .contact-section {
                padding: 40px 0;
                background-color: #f9f9f9;
            }

            .contact-title {
                color: #FC6E51;
                font-weight: bold;
                text-align: center;
                margin-bottom: 20px;
            }

            .contact-info {
                font-size: 18px;
                color: #333;
                line-height: 1.6;
            }

            .contact-form {
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 8px;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .contact-form label {
                font-weight: bold;
            }

            .map-container {
                width: 100%;
                height: 100%;
                border-radius: 8px;
            }
            
            .btn-orange {
                background-color: orangered;
                color: white;
            }
            
        </style>
    </head>

    <body>
        <!-- Header -->
        <jsp:include page="Header.jsp"></jsp:include>

            <div class="container contact-section">
                <h1 class="contact-title">Liên hệ với TheKoi</h1>

                <div class="row">
                    <!-- Google Maps -->
                    <div class="col-md-6">
                        <iframe class="map-container"
                                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3929.317022919616!2d105.753978!3d10.0292817!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zMTDCsDAxJzQ1LjQiTiAxMDXCsDQ1JzE0LjMiRQ!5e0!3m2!1svi!2s!4v1640000000000" 
                                width="100%" height="400" style="border:0;" allowfullscreen="" loading="lazy">
                        </iframe>
                    </div>


                    <!-- Thông tin liên hệ & Form -->
                    <div class="col-md-6">
                        <div class="contact-info mb-4">
                            <h4><i class="fas fa-map-marker-alt"></i> Địa chỉ:</h4>
                            <p>123 Đường Nguyễn Văn Cừ, Phường Xuân Khánh, Quận Ninh Kiều, TP Cần Thơ</p>
                            <h4><i class="fas fa-phone"></i> Số điện thoại:</h4>
                            <p>+84 123 456 789</p>

                            <h4><i class="fas fa-envelope"></i> Email:</h4>
                            <p>contact@thekoi.com</p>
                        </div>

                        <!-- Form liên hệ -->
                        <div class="contact-form">
                            <form id="contactForm" action="ContactServlet" method="post" onsubmit="return validateForm()">
                                <div class="mb-3">
                                    <label for="name" class="form-label">Họ và tên</label>
                                    <input type="text" class="form-control" id="name" name="name">
                                    <small class="text-danger" id="nameError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" name="email">
                                    <small class="text-danger" id="emailError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="text" class="form-control" id="phone" name="phone">
                                    <small class="text-danger" id="phoneError"></small>
                                </div>
                                <div class="mb-3">
                                    <label for="message" class="form-label">Nội dung</label>
                                    <textarea class="form-control" id="message" name="message" rows="4"></textarea>
                                    <small class="text-danger" id="messageError"></small>
                                </div>
                                <button type="submit" class="btn btn-orange">Gửi</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                function validateForm() {
                                    let isValid = true;

                                    // Xóa thông báo lỗi cũ
                                    document.getElementById("nameError").innerText = "";
                                    document.getElementById("emailError").innerText = "";
                                    document.getElementById("phoneError").innerText = "";
                                    document.getElementById("messageError").innerText = "";

                                    // Kiểm tra Họ và tên
                                    let name = document.getElementById("name").value.trim();
                                    if (name.length < 3) {
                                        document.getElementById("nameError").innerText = "Họ và tên phải có ít nhất 3 ký tự.";
                                        isValid = false;
                                    }

                                    // Kiểm tra Email
                                    let email = document.getElementById("email").value.trim();
                                    let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                                    if (!emailPattern.test(email)) {
                                        document.getElementById("emailError").innerText = "Email không hợp lệ.";
                                        isValid = false;
                                    }

                                    // Kiểm tra Số điện thoại
                                    let phone = document.getElementById("phone").value.trim();
                                    let phonePattern = /^[0-9]{10}$/;
                                    if (!phonePattern.test(phone)) {
                                        document.getElementById("phoneError").innerText = "Số điện thoại phải từ 0 tới 10 chữ số.";
                                        isValid = false;
                                    }

                                    // Kiểm tra Nội dung
                                    let message = document.getElementById("message").value.trim();
                                    if (message.length < 10) {
                                        document.getElementById("messageError").innerText = "Nội dung phải có ít nhất 10 ký tự.";
                                        isValid = false;
                                    }

                                    return isValid; // Nếu có lỗi, form sẽ không gửi
                                }
        </script>


    </body>

</html>
