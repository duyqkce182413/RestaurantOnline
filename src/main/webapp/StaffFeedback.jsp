<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Staff Feedback Management</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    <h1>Feedback Management</h1>

                <c:if test="${param.message != null}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>

                <form method="GET" action="filterStaffFeedbacks" class="mb-4">
                    <div class="input-group">
                        <select name="rating" class="form-select">
                            <option value="">All Ratings</option>
                            <option value="5" ${param.rating == '5' ? 'selected' : ''}>5 Stars</option>
                            <option value="4" ${param.rating == '4' ? 'selected' : ''}>4 Stars</option>
                            <option value="3" ${param.rating == '3' ? 'selected' : ''}>3 Stars</option>
                            <option value="2" ${param.rating == '2' ? 'selected' : ''}>2 Stars</option>
                            <option value="1" ${param.rating == '1' ? 'selected' : ''}>1 Star</option>
                        </select>
                        <button type="submit" class="btn btn-secondary">Filter</button>
                    </div>
                </form>

                <!-- Feedback Table -->
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Feedback ID</th>
                            <th>Customer</th>
                            <th>Food</th>
                            <th>Rating</th>
                            <th>Comment</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="feedback" items="${listStaffFeedbacks}">
                            <tr>
                                <td>${feedback.feedbackID}</td>
                                <td>${feedback.user.fullName}</td>
                                <td>${feedback.food.foodName}</td>
                                <td>${feedback.rating} ★</td>
                                <td>${feedback.comment}</td>
                                <td>${feedback.createdAt}</td>
                                <td>
                                    <!-- Sử dụng thẻ <a> để chuyển hướng đến trang chi tiết -->
                                    <a href="viewFeedbackDetails?id=${feedback.feedbackID}" class="btn btn-primary btn-sm">
                                        <i class="fas fa-eye"></i> View
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>
