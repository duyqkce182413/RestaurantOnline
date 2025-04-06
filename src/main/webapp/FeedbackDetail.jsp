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
            /*Css cho modal*/
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
            }

            .close {
                float: right;
                font-size: 24px;
                cursor: pointer;
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

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success" style="width: 20%;">
                        <c:choose>
                            <c:when test="${param.success == 'delete_reply_success'}">
                                Reply deleted successfully!
                            </c:when>
                            <c:when test="${param.success == 'edit_reply_success'}">
                                Reply updated successfully!
                            </c:when>
                            <c:otherwise>
                                An error occurred!
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <!-- Thông báo thất bại khi cập nhật phản hồi -->
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger" style="width: 20%;">
                        <c:choose>
                            <c:when test="${param.error == 'delete_reply_failed'}">
                                Failed to delete reply!
                            </c:when>
                            <c:when test="${param.error == 'edit_reply_failed'}">
                                Failed to update reply!
                            </c:when>
                            <c:otherwise>
                                An error occurred!
                            </c:otherwise>
                        </c:choose>
                    </div>
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
                            <fmt:formatDate value="${feedbackDetail.createdAt}" pattern="dd-MM-yyyy" />
                        </label>
                    </div>

                    <!-- Kiểm tra nếu feedback đã có phản hồi -->
                    <div class="mb-3">
                        <c:if test="${not empty feedbackDetail.reply}">
                            <strong>Replies:</strong>
                            <ul>
                                <c:forEach var="r" items="${feedbackDetail.reply}">
                                    <li>
                                        <strong>${r.user.fullName}:</strong> ${r.replyText} 
                                        <br>
                                        Date: <fmt:formatDate value="${r.replyAt}" pattern="dd-MM-yyyy" />
                                        <!-- Nút sửa và xóa phản hồi -->
                                        <c:if test="${sessionScope.user.userID == r.user.userID}">
                                            <button class="btn btn-sm btn-warning edit-reply" 
                                                    data-replyid="${r.replyID}" 
                                                    data-replytext="${r.replyText}">
                                                Edit
                                            </button>
                                        </c:if>

                                        <c:if test="${sessionScope.user.role == 'Admin' or sessionScope.user.role == 'Staff' or sessionScope.user.userID == r.user.userID}">
                                            <form action="staff-delete-feedback-reply" method="POST" style="display: inline;">
                                                <input type="hidden" name="replyID" value="${r.replyID}">
                                                <input type="hidden" name="foodID" value="${food_detail.foodID}">
                                                <input type="hidden" name="feedbackID" value="${feedbackDetail.feedbackID}" />
                                                <input type="hidden" name="staffID" value="${sessionScope.staffId}" />
                                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this reply?');">Delete</button>
                                            </form>
                                        </c:if>
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

                <!-- Modal để sửa phản hồi -->
                <div id="editReplyModal" class="modal" style="display: none;">
                    <div class="modal-content">
                        <span class="close">&times;</span>
                        <h4>Edit Reply</h4>
                        <form id="editReplyForm" action="staff-edit-feedback-reply" method="post">
                            <input type="hidden" name="replyID" id="editReplyID">
                            <input type="hidden" name="feedbackID" value="${feedbackDetail.feedbackID}" />
                            <input type="hidden" name="staffID" value="${sessionScope.staffId}" />
                            <label>Replies content:</label>
                            <textarea name="replyText" id="editReplyText" class="form-control" required></textarea>
                            <button type="submit" class="btn btn-primary mt-2">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                               function confirmDelete() {
                                   return confirm("Are you sure you want to delete this feedback?");
                               }

                               function validateReplyForm() {
                                   let replyText = document.getElementById("replyText").value.trim();
                                   if (replyText === "") {
                                       alert("Feedback cannot be empty!");
                                       return false;
                                   }
                                   return true;
                               }

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
