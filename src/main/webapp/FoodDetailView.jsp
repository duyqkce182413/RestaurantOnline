<%-- 
    Document   : food_detail
    Created on : Oct 17, 2024, 7:41:02 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <script src="./JS/header-script.js"></script>
        <link rel="stylesheet" href="./CSS/HomeView.css">

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
                transition: transform 0.3s ease, box-shadow 0.3s ease;
            }

            .food-img:hover {
                transform: scale(1.05);
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            }

            .food-name {
                font-size: 34px;
                font-weight: bold;
                color: #FC6E51;
                text-transform: uppercase;
                margin-bottom: 15px;
            }

            .food-price {
                font-size: 26px;
                font-weight: bold;
                color: #d9534f;
                margin-top: 10px;
                margin-bottom: 20px;
            }

            .food-description {
                font-size: 16px;
                color: #777;
                line-height: 1.7;
                margin: 20px 0;
                padding: 15px;
                background-color: #f9f9f9;
                border-radius: 10px;
                border: 1px solid #eee;
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

            /* Feedback */
            .feedback-section h3 {
                margin-top: 30px; /* Tạo khoảng cách với phần trên */
                font-size: 24px; /* Tăng kích thước chữ */
                font-weight: bold; /* Làm đậm */
                color: #ff9800; /* Màu cam nhẹ */
                text-transform: uppercase; /* Viết hoa chữ */
                letter-spacing: 1px; /* Giãn chữ một chút */
                border-bottom: 3px solid #ff9800; /* Gạch chân màu cam */
                display: inline-block; /* Gạch chân vừa đủ chữ */
                padding-bottom: 5px; /* Thêm khoảng cách dưới */
            }


            .star-rating {
                display: flex;
                gap: 5px;
                font-size: 24px;
                color: gray;
                cursor: pointer;
            }

            .star-rating .fas {
                color: gold;
            }

            .fas.checked {
                color: gold;
            }

            /* Edit Feedback */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.4);
                align-items: center;
                justify-content: center;
            }

            .modal-content {
                background-color: white;
                padding: 20px;
                width: 400px;
                margin: auto;
                border-radius: 10px;
                text-align: center;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }

            .modal-content h4 {
                font-size: 20px;
                font-weight: bold;
                color: #333;
                margin-bottom: 20px;
            }

            .modal-content .close {
                float: right;
                font-size: 24px;
                cursor: pointer;
                color: #333;
            }

            .modal-content .close:hover {
                color: #000;
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
                        <img src="${pageContext.request.contextPath}/image/${food_detail.image}" alt="Tên món ăn" class="food-img">
                </div>

                <!-- Food Details -->
                <div class="col-md-6 margin-left">
                    <h2 class="food-name">${food_detail.foodName}</h2>
                    <p class="food-price">${food_detail.price} VND</p>
                    <p class="food-description">Mô tả: ${food_detail.description} </p>

                    <!-- Quantity and Add to Cart -->
                    <form action="add-to-cart" method="get"> <!-- Đổi action thành 'cart' để gửi đến CartController -->
                        <div class="form-group">
                            <label for="quantity">Số Lượng:</label>
                            <input type="number" id="quantity" name="quantity" class="form-control quantity" min="1" value="1" max="${food_detail.quantity}">
                        </div>

                        <input type="hidden" name="foodId" value="${food_detail.foodID}"> <!-- ID của món ăn -->
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

                <%--  ================ Phần Feedback và Phản hồi ==================== --%>
                <div class="mt-5 feedback-section">
                    <!-- Form Add Feedback -->
                    <div class="mt-4">
                        <h4>Gửi đánh giá của bạn</h4>
                        <%-- Hiển thị thông báo khi đánh giá thành công hoặc có lỗi --%>
                        <c:if test="${param.success == 'comment_added'}">
                            <div class="alert alert-success">Bạn đã bình luận thành công!</div>
                        </c:if>

                        <c:if test="${param.error == 'already_reviewed'}">
                            <div class="alert alert-warning">Bạn đã đánh giá món ăn này rồi!</div>
                        </c:if>

                        <c:if test="${param.error == 'no_purchase'}">
                            <div class="alert alert-danger">Bạn cần mua món ăn này trước khi đánh giá!</div>
                        </c:if>

                        <c:if test="${param.error == 'invalid_rating'}">
                            <div class="alert alert-danger">Đánh giá không hợp lệ! Vui lòng chọn số sao từ 1 đến 5.</div>
                        </c:if>

                        <c:if test="${param.error == 'food_not_found'}">
                            <div class="alert alert-danger">Không tìm thấy món ăn!</div>
                        </c:if>

                        <form action="submitFeedback" method="POST" onsubmit="return validateFeedbackForm();">
                            <input type="hidden" name="foodId" value="${food_detail.foodID}">
                            <input type="hidden" id="ratingValue" name="rating" value="0"> <!-- Giá trị mặc định là 0 sao -->

                            <div class="mb-3">
                                <label class="form-label">Chọn số sao:</label>
                                <div class="star-rating">
                                    <i class="fa-star far" data-value="1"></i>
                                    <i class="fa-star far" data-value="2"></i>
                                    <i class="fa-star far" data-value="3"></i>
                                    <i class="fa-star far" data-value="4"></i>
                                    <i class="fa-star far" data-value="5"></i>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Bình luận:</label>
                                <textarea name="comment" class="form-control" rows="3" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
                        </form>
                    </div>

                    <!-- Hien thi danh gia va phan hoi -->
                    <h3 class="feedback-section">Đánh Giá Từ Khách Hàng</h3>
                    <c:forEach var="feedback" items="${feedbackList}">
                        <div class="border p-3 mb-3 rounded">
                            <!-- Hiển thị rating dưới dạng sao -->
                            <p><strong>Đánh giá:</strong> 
                                <c:forEach var="i" begin="1" end="5">
                                    <i class="fa fa-star text-warning ${feedback.rating >= i ? 'fas' : 'far'}"></i>
                                </c:forEach>
                            </p>
                            <p><strong>${feedback.user.fullName}</strong> - 
                                <!-- Định dạng ngày theo dd/MM/yy -->
                                <fmt:formatDate value="${feedback.createdAt}" pattern="dd/MM/yy" />
                            </p>
                            <p>${feedback.comment}</p>

                            <!-- Nếu người dùng là chủ sở hữu feedback sửa -->
                            <c:if test="${feedback.user.userID == sessionScope.user.userID}">

                                <button class="btn btn-sm btn-warning edit-feedback" 
                                        data-feedbackid="${feedback.feedbackID}" 
                                        data-rating="${feedback.rating}" 
                                        data-comment="${feedback.comment}">
                                    Sửa
                                </button>
                                <!-- Nút xóa feedback -->
                                <form action="deleteFeedback" method="POST" style="display: inline;">
                                    <input type="hidden" name="feedbackId" value="${feedback.feedbackID}">
                                    <input type="hidden" name="foodID" value="${food_detail.foodID}">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirmDelete();">Xóa</button>
                                </form>
                            </c:if>


                            <!-- Hiển thị phản hồi của shop nếu có -->
                            <c:if test="${not empty feedback.reply}">
                                <div class="ms-4 p-2 border-start border-warning">
                                    <c:forEach var="r" items="${feedback.reply}">

                                        <p>
                                            <strong>
                                                <c:choose>
                                                    <c:when test="${r.user.role == 'Admin' || r.user.role == 'Staff'}">
                                                        <span class="text-primary">Shop</span> <!-- Nếu phản hồi từ Admin hoặc Staff -->
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-success">${r.user.fullName}</span> <!-- Nếu phản hồi từ khách -->
                                                    </c:otherwise>
                                                </c:choose>
                                            </strong>: ${r.replyText}
                                            <br>
                                            <span class="text-muted">
                                                <fmt:formatDate value="${r.replyAt}" pattern="dd/MM/yy" />
                                            </span>
                                        </p>

                                        <!-- Chỉ hiển thị nút sửa nếu đó là phản hồi của người dùng hiện tại hoặc Admin/Staff -->
                                        <!-- Nút sửa và xóa phản hồi -->
                                        <c:if test="${sessionScope.user.userID == r.user.userID}">
                                            <button class="btn btn-sm btn-warning edit-reply" 
                                                    data-replyid="${r.replyID}" 
                                                    data-replytext="${r.replyText}">
                                                Sửa
                                            </button>
                                        </c:if>

                                        <c:if test="${sessionScope.user.role == 'Admin' or sessionScope.user.role == 'Staff' or sessionScope.user.userID == r.user.userID}">
                                            <form action="delete-feedback-reply" method="POST" style="display: inline;">
                                                <input type="hidden" name="replyID" value="${r.replyID}">
                                                <input type="hidden" name="foodID" value="${food_detail.foodID}">
                                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa phản hồi này không?');">Xóa</button>
                                            </form>
                                        </c:if>
                                    </c:forEach>

                                    <div class="feedback">
                                        <!-- Nút Trả lời -->
                                        <button class="btn btn-sm btn-primary reply-feedback" data-feedbackid="${feedback.feedbackID}">
                                            Trả lời
                                        </button>

                                        <form id="reply-form-${feedback.feedbackID}" class="reply-form" style="display:none;"  method="POST" action="replyFeedback">
                                            <textarea id="replyText-${feedback.feedbackID}" name="replyText" rows="4" class="form-control" placeholder="Nhập phản hồi của bạn..."></textarea>
                                            <button type="submit" class="btn btn-sm btn-success mt-2">Gửi phản hồi</button>
                                            <input type="hidden" name="feedbackId" value="${feedback.feedbackID}">
                                        </form>
                                    </div>
                                </div>
                            </c:if>

                            <!-- Hiển thị nút phản hồi nếu chưa có phản hồi -->
                            <c:if test="${empty feedback.reply}">
                                <button class="btn btn-sm btn-primary reply-feedback" data-feedbackid="${feedback.feedbackID}">
                                    Phản hồi
                                </button>
                            </c:if>
                            <!-- Luôn có form để JS tìm thấy -->
                            <form id="reply-form-${feedback.feedbackID}" class="reply-form" style="display:none;" method="POST" action="replyFeedback">
                                <textarea id="replyText-${feedback.feedbackID}" name="replyText" rows="4" class="form-control" placeholder="Nhập phản hồi của bạn..."></textarea>
                                <button type="submit" class="btn btn-sm btn-success mt-2">Gửi phản hồi</button>
                                <input type="hidden" name="feedbackId" value="${feedback.feedbackID}">
                            </form>
                        </div>  
                    </c:forEach>

                    <c:if test="${empty feedbackList}">
                        <p class="text-muted">Chưa có đánh giá nào cho món ăn này.</p>
                    </c:if>

                    <!-- Form chỉnh sửa đánh giá (ẩn mặc định) -->
                    <div id="editFeedbackModal" class="modal" style="display: none;">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <h4>Chỉnh Sửa Đánh Giá</h4>
                            <form id="editFeedbackForm" action="editFeedback" method="post">
                                <input type="hidden" name="feedbackID" id="editFeedbackID">
                                <label>Chọn số sao:</label>
                                <div class="star-rating" id="editStarRating">
                                    <i class="fa-star far" data-value="1"></i>
                                    <i class="fa-star far" data-value="2"></i>
                                    <i class="fa-star far" data-value="3"></i>
                                    <i class="fa-star far" data-value="4"></i>
                                    <i class="fa-star far" data-value="5"></i>
                                </div>
                                <input type="hidden" name="rating" id="editRatingValue">

                                <label>Bình luận:</label>
                                <textarea name="comment" id="editComment" class="form-control" required></textarea>

                                <button type="submit" class="btn btn-primary mt-2">Lưu</button>
                            </form>
                        </div>
                    </div>

                    <!-- Modal để sửa phản hồi -->
                    <div id="editReplyModal" class="modal" style="display: none;">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <h4>Chỉnh Sửa Phản Hồi</h4>
                            <form id="editReplyForm" action="edit-feedback-reply" method="post">
                                <input type="hidden" name="replyID" id="editReplyID">
                                <input type="hidden" name="foodId" value="${food_detail.foodID}">
                                <label>Nội dung phản hồi:</label>
                                <textarea name="replyText" id="editReplyText" class="form-control" required></textarea>
                                <button type="submit" class="btn btn-primary mt-2">Lưu</button>
                            </form>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                    // Chon SAO khi add feedback
                                                    document.addEventListener("DOMContentLoaded", function () {
                                                        const stars = document.querySelectorAll(".star-rating .fa-star");
                                                        const ratingValue = document.getElementById("ratingValue");

                                                        let selectedRating = 0; // Lưu giá trị rating đã chọn

                                                        // Xử lý hover
                                                        stars.forEach(star => {
                                                            star.addEventListener("mouseover", function () {
                                                                const value = this.getAttribute("data-value");

                                                                // Highlight tạm thời các sao khi hover
                                                                stars.forEach(s => s.classList.remove("fas", "checked"));
                                                                stars.forEach(s => s.classList.add("far"));

                                                                for (let i = 0; i < value; i++) {
                                                                    stars[i].classList.remove("far");
                                                                    stars[i].classList.add("fas", "checked");
                                                                }
                                                            });

                                                            // Khi rời chuột khỏi vùng sao, khôi phục trạng thái đã chọn
                                                            star.addEventListener("mouseout", function () {
                                                                stars.forEach(s => s.classList.remove("fas", "checked"));
                                                                stars.forEach(s => s.classList.add("far"));

                                                                for (let i = 0; i < selectedRating; i++) {
                                                                    stars[i].classList.remove("far");
                                                                    stars[i].classList.add("fas", "checked");
                                                                }
                                                            });

                                                            // Xử lý khi click vào sao
                                                            star.addEventListener("click", function () {
                                                                selectedRating = this.getAttribute("data-value");
                                                                ratingValue.value = selectedRating;

                                                                // Cập nhật trạng thái chính thức
                                                                stars.forEach(s => s.classList.remove("fas", "checked"));
                                                                stars.forEach(s => s.classList.add("far"));

                                                                for (let i = 0; i < selectedRating; i++) {
                                                                    stars[i].classList.remove("far");
                                                                    stars[i].classList.add("fas", "checked");
                                                                }
                                                            });
                                                        });
                                                    });
                                                    //Thong bao chua rating
                                                    function alidateFeedbackForm() {
                                                        const ratingValue = document.getElementById("ratingValue").value;
                                                        if (ratingValue == 0) {
                                                            alert("Vui lòng chọn số sao trước khi gửi đánh giá!");
                                                            return false; // Ngăn form gửi đi
                                                        }
                                                        return true; // Cho phép form gửi đi
                                                    }

                                                    document.addEventListener("DOMContentLoaded", function () {
                                                        var replyButtons = document.querySelectorAll(".reply-feedback");

                                                        replyButtons.forEach(function (button) {
                                                            button.addEventListener("click", function () {
                                                                var feedbackId = button.getAttribute("data-feedbackid");

                                                                console.log("🔍 Nút được nhấn:", button); // Xem nút có tồn tại không
                                                                console.log("🔍 Giá trị feedbackId nhận được:", feedbackId); // Kiểm tra giá trị

                                                                if (feedbackId) {
                                                                    toggleReplyForm(feedbackId);
                                                                } else {
                                                                    console.error("❌ Không lấy được feedbackID từ nút phản hồi.");
                                                                }
                                                            });
                                                        });
                                                    });

                                                    function toggleReplyForm(feedbackId) {
                                                        var form = document.getElementById("reply-form-" + feedbackId);
                                                        console.log("🔍 Tìm kiếm form ID: reply-form-" + feedbackId, form); // Kiểm tra form có tồn tại không

                                                        if (form) {
                                                            form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
                                                        } else {
                                                            console.error("❌ Không tìm thấy form phản hồi với ID:", feedbackId);
                                                        }
                                                    }




                                                    // Sua Feedback
                                                    document.addEventListener("DOMContentLoaded", function () {
                                                        const editButtons = document.querySelectorAll(".edit-feedback");
                                                        const modal = document.getElementById("editFeedbackModal");
                                                        const closeBtn = modal.querySelector(".close");
                                                        const editForm = document.getElementById("editFeedbackForm");
                                                        const editFeedbackID = document.getElementById("editFeedbackID");
                                                        const editComment = document.getElementById("editComment");
                                                        const editRatingValue = document.getElementById("editRatingValue");
                                                        const stars = document.querySelectorAll("#editStarRating .fa-star");

                                                        let selectedRating = 0;

                                                        // Xử lý khi click nút "Sửa"
                                                        editButtons.forEach(button => {
                                                            button.addEventListener("click", function () {
                                                                editFeedbackID.value = this.getAttribute("data-feedbackid");
                                                                editComment.value = this.getAttribute("data-comment");
                                                                selectedRating = this.getAttribute("data-rating");
                                                                editRatingValue.value = selectedRating;

                                                                // Cập nhật sao cho đúng với rating hiện tại
                                                                updateStarDisplay(selectedRating);

                                                                modal.style.display = "block";
                                                            });
                                                        });

                                                        // Đóng modal
                                                        closeBtn.addEventListener("click", function () {
                                                            modal.style.display = "none";
                                                        });

                                                        window.addEventListener("click", function (event) {
                                                            if (event.target === modal) {
                                                                modal.style.display = "none";
                                                            }
                                                        });

                                                        // Xử lý chọn sao trong form chỉnh sửa
                                                        stars.forEach(star => {
                                                            star.addEventListener("mouseover", function () {
                                                                const value = this.getAttribute("data-value");
                                                                updateStarDisplay(value);
                                                            });

                                                            star.addEventListener("mouseout", function () {
                                                                updateStarDisplay(selectedRating);
                                                            });

                                                            star.addEventListener("click", function () {
                                                                selectedRating = this.getAttribute("data-value");
                                                                editRatingValue.value = selectedRating;
                                                                updateStarDisplay(selectedRating);
                                                            });
                                                        });

                                                        function updateStarDisplay(value) {
                                                            stars.forEach(s => s.classList.remove("fas", "checked"));
                                                            stars.forEach(s => s.classList.add("far"));

                                                            for (let i = 0; i < value; i++) {
                                                                stars[i].classList.remove("far");
                                                                stars[i].classList.add("fas", "checked");
                                                            }
                                                        }
                                                    });

                                                    function confirmDelete() {
                                                        return confirm("Bạn có chắc chắn muốn xóa feedback này không?");
                                                    }

                                                    // Sua phan hoi (feedback reply)
                                                    document.addEventListener("DOMContentLoaded", function () {
                                                        const editReplyButtons = document.querySelectorAll(".edit-reply");
                                                        const deleteReplyButtons = document.querySelectorAll(".delete-reply");
                                                        const editReplyModal = document.getElementById("editReplyModal");
                                                        const closeEditReplyModal = editReplyModal.querySelector(".close");
                                                        const editReplyForm = document.getElementById("editReplyForm");
                                                        const editReplyID = document.getElementById("editReplyID");
                                                        const editReplyText = document.getElementById("editReplyText");

                                                        // Xử lý khi click nút "Sửa" phản hồi
                                                        editReplyButtons.forEach(button => {
                                                            button.addEventListener("click", function () {
                                                                editReplyID.value = this.getAttribute("data-replyid");
                                                                editReplyText.value = this.getAttribute("data-replytext");
                                                                editReplyModal.style.display = "block";
                                                            });
                                                        });

                                                        // Đóng modal sửa phản hồi
                                                        closeEditReplyModal.addEventListener("click", function () {
                                                            editReplyModal.style.display = "none";
                                                        });

                                                        window.addEventListener("click", function (event) {
                                                            if (event.target === editReplyModal) {
                                                                editReplyModal.style.display = "none";
                                                            }
                                                        });


                                                    });
        </script>
    </body>

</html>


