<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Feedback Detail</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <style>
            .sidebar {
                width: 250px;
                background-color: #343a40;
                padding: 20px;
                height: 100%;
                position: fixed;
                top: 0;
                left: 0;
                color: white;
            }
            .sidebar a {
                color: white;
                text-decoration: none;
                display: block;
                padding: 10px;
                margin: 5px 0;
                border-radius: 5px;
            }
            .sidebar a:hover {
                background-color: #495057;
            }
            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
            }
        </style>
    </head>
    <body>

        <jsp:include page="Dashboard_Header.jsp"></jsp:include>

            <div class="wrapper clearfix">
                <div class="sidebar">
                    <h3>Staff Dashboard</h3>
                    <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Manage Order</a>
                    <a href="listStaffFeedbacks"><i class="fas fa-comments"></i> Manage Feedback</a>
                </div>

                <div class="content">
                    <h1>Feedback Detail</h1>

                    <!-- Hiển thị thông báo lỗi hoặc thành công -->
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>
                <c:if test="${not empty param.message}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>

                <!-- Kiểm tra feedbackDetail có tồn tại không -->
                <c:if test="${not empty feedbackDetail}">
                    <div class="mb-3">
                        <label><strong>Customer:</strong> ${feedbackDetail.user.fullName}</label>
                    </div>
                    <div class="mb-3">
                        <label><strong>Food:</strong> ${feedbackDetail.food.foodName}</label>
                    </div>
                    <div class="mb-3">
                        <label><strong>Rating:</strong> ${feedbackDetail.rating} ★</label>
                    </div>
                    <div class="mb-3">
                        <label><strong>Comment:</strong> ${feedbackDetail.comment}</label>
                    </div>
                    <div class="mb-3">
                        <label><strong>Date:</strong> 
                            <fmt:formatDate value="${feedbackDetail.createdAt}" pattern="dd-MM-yyyy HH:mm" />
                        </label>
                    </div>

                    <!-- Kiểm tra nếu feedback đã có phản hồi -->
                    <div class="mb-3">
                        <c:if test="${not empty feedbackDetail.reply}">
                            <strong>Staff Replies:</strong>
                            <ul>
                                <c:forEach var="r" items="${feedbackDetail.reply}">
                                    <li>
                                        <strong>${r.user.fullName}:</strong> ${r.replyText} 
                                        <br>
                                        <fmt:formatDate value="${r.replyAt}" pattern="dd-MM-yyyy HH:mm" />
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </div>

                    <!-- Form nhập phản hồi luôn hiển thị -->
                    <form action="replyStaffFeedback" method="POST" onsubmit="return validateReplyForm();">
                        <input type="hidden" name="feedbackID" value="${feedbackDetail.feedbackID}" />
                        <input type="hidden" name="staffID" value="${sessionScope.staffId}" />

                        <div class="mb-3">
                            <label for="replyText" class="form-label"><strong>Reply:</strong></label>
                            <textarea class="form-control" id="replyText" name="replyText" rows="3" required></textarea>
                        </div>

                        <button type="submit" class="btn btn-success">Reply</button>
                    </form>

                    <!-- Nút xóa feedback -->
                    <div class="d-flex gap-2 mt-3">
                        <a href="deleteStaffFeedback?id=${feedbackDetail.feedbackID}" 
                           class="btn btn-danger" 
                           onclick="return confirmDelete();">
                            Delete
                        </a>
                        <a href="listStaffFeedbacks" class="btn btn-secondary">Back to Feedbacks</a>
                    </div>
                </c:if>

                <!-- Nếu không có feedback -->
                <c:if test="${empty feedbackDetail}">
                    <div class="alert alert-warning">No feedback details found.</div>
                </c:if>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                               function confirmDelete() {
                                   return confirm("Bạn có chắc chắn muốn xóa feedback này không?");
                               }

                               function validateReplyForm() {
                                   let replyText = document.getElementById("replyText").value.trim();
                                   if (replyText === "") {
                                       alert("Phản hồi không được để trống!");
                                       return false;
                                   }
                                   return true;
                               }
        </script>
    </body>
</html>
