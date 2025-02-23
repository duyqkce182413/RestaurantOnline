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
                    <a href="listFeedbacks"><i class="fas fa-comments"></i> Manage Feedback</a>
                </div>
                <div class="content">
                    <h1>Feedback Management</h1>
                <c:if test="${param.message != null}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>
                <form method="GET" action="filterFeedbacks" class="mb-4">
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
                        <c:forEach var="feedback" items="${listFeedbacks}">
                            <tr>
                                <td>${feedback.feedbackID}</td>
                                <td>${feedback.user.fullName}</td>
                                <td>${feedback.food.foodName}</td>
                                <td>${feedback.rating} ★</td>
                                <td>${feedback.comment}</td>
                                <td>${feedback.createdAt}</td>
                                <td>
                                    <button type="button" class="btn btn-primary btn-sm" 
                                            data-bs-toggle="modal" 
                                            data-bs-target="#viewFeedbackModal" 
                                            onclick="loadFeedback(${feedback.feedbackID}, '${feedback.comment}', ${feedback.rating})">
                                        <i class="fas fa-eye"></i> View
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="viewFeedbackModal" tabindex="-1" aria-labelledby="feedbackModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="feedbackModalLabel">View Feedback</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Comment:</strong> <span id="feedbackComment"></span></p>
                        <p><strong>Rating:</strong> <span id="feedbackRating"></span></p>

                        <!-- Reply Textarea -->
                        <div class="mb-3">
                            <label for="replyText" class="form-label">Reply:</label>
                            <textarea class="form-control" id="replyText" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-success" onclick="sendReply()">Reply</button>
                        <button type="button" class="btn btn-danger" onclick="deleteFeedback()">Delete</button>
                    </div>
                </div>
            </div>
        </div>
        <script>
            let currentFeedbackId = null;

            function loadFeedback(feedbackID, comment, rating) {
                currentFeedbackId = feedbackID; // Lưu ID feedback hiện tại
                document.getElementById("feedbackComment").innerText = comment;
                document.getElementById("feedbackRating").innerText = rating;
            }

            function sendReply() {
                let replyText = document.getElementById("replyText").value;
                if (!replyText.trim()) {
                    alert("Reply cannot be empty!");
                    return;
                }

                fetch("replyFeedback", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "feedbackID=" + currentFeedbackId + "&replyText=" + encodeURIComponent(replyText)
                }).then(response => response.text())
                        .then(data => {
                            alert("Reply sent successfully!");
                            document.getElementById("replyText").value = "";
                            location.reload(); // Reload page
                        }).catch(error => console.error(error));
            }

            function deleteFeedback() {
                if (!confirm("Are you sure you want to delete this feedback?"))
                    return;

                fetch("deleteFeedback", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: `feedbackID=${currentFeedbackId}`
                }).then(response => response.text())
                        .then(data => {
                            alert("Feedback deleted successfully!");
                            location.reload(); // Reload page
                        }).catch(error => console.error(error));
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
