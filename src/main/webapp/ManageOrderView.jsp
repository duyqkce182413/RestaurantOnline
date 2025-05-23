<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Orders Dashboard</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />

        <link rel="stylesheet" href="./CSS/Style.css" />

        <style>
            /* Sidebar Styles */
            .sidebar {
                width: 250px;
                background-color: #343a40;
                padding: 20px;
                height: 100%;
                position: fixed;
                top: 0;
                left: 0;
                color: white;
                display: flex;
                flex-direction: column;
                align-items: flex-start;
            }
            .sidebar h3 {
                margin-bottom: 30px;
                text-transform: uppercase;
                font-size: 18px;
            }

            .sidebar a {
                color: white;
                text-decoration: none;
                display: block;
                padding: 10px 15px;
                margin: 5px 0;
                width: 100%;
                text-align: left;
                border-radius: 5px;
            }

            .sidebar a:hover {
                background-color: #495057;
                transition: background-color 0.3s;
            }

            /* Main content */
            .content {
                margin-left: 250px; /* Space for the sidebar */
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
                float: left;
            }

            /* Prevent floating issues */
            .clearfix::after {
                content: "";
                display: table;
                clear: both;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Dashboard_Header.jsp"></jsp:include>

            <div class="wrapper clearfix">
                <!-- Sidebar -->
                <div class="sidebar">
                    <h3>Staff Dashboard</h3>
                    <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Manage Order </a>
                    <a href="listStaffFeedbacks"><i class="fas fa-comments"></i> Manage Feedback </a>
                </div>

                <!-- Main content -->
                <div class="content">
                    <h1>Order Management</h1>

                    <!-- Thông báo trạng thái -->
                <c:if test="${param.message != null}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>

                <!-- Form lọc đơn hàng theo số điện thoại  -->
                <form method="GET" action="searchByPhone" class="mb-3" onsubmit="return validatePhone()">
                    <div class="row">
                        <div class="col-md-6">
                            <input type="text" id="phone" name="phone" class="form-control" placeholder="Search by phone number" value="${param.phone}">
                            <small id="phoneError" class="text-danger"></small>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </div>
                    </div>
                </form>

                <!-- Form lọc đơn hàng theo trạng thái -->
                <form method="GET" action="listAdminOrders" class="mb-4">
                    <div class="input-group">
                        <select name="status" class="form-select">
                            <option value="">All</option>
                            <option value="Chưa xử lý" ${param.status == 'Chưa xử lý' ? 'selected' : ''}>Pending</option>
                            <option value="Đã tiếp nhận" ${param.status == 'Đã tiếp nhận' ? 'selected' : ''}>Received</option>
                            <option value="Đang chuẩn bị" ${param.status == 'Đang chuẩn bị' ? 'selected' : ''}>Preparing</option>
                            <option value="Đang giao" ${param.status == 'Đang giao' ? 'selected' : ''}>On Delivery</option>
                            <option value="Hoàn thành" ${param.status == 'Hoàn thành' ? 'selected' : ''}>Completed</option>
                            <option value="Đã hủy" ${param.status == 'Đã Hủy' ? 'selected' : ''}>Canceled</option>
                        </select>
                        <button type="submit" class="btn btn-secondary">Filter</button>
                    </div>
                </form>

                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <!-- Bảng hiển thị danh sách đơn hàng -->
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Order Date</th>
                            <th>Shipping address</th>
                            <th>Status</th>
                            <th>Total Amount</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${listOrders}">
                            <tr>
                                <td>${order.orderID}</td>
                                <td>${order.address.name}</td>
                                <td>${order.orderDate}</td>
                                <td>
                                    ${order.address.addressLine}, ${order.address.city} <br/> 
                                    Số điện thoại: ${order.address.phoneNumber} 
                                </td>
                                <td>
                                    <span class="badge
                                          <c:choose>
                                              <c:when test="${order.status.equals('Chưa xử lý')}">bg-warning</c:when>
                                              <c:when test="${order.status.equals('Đã tiếp nhận')}">bg-warning</c:when>
                                              <c:when test="${order.status.equals('Đang chuẩn bị')}">bg-info</c:when>
                                              <c:when test="${order.status.equals('Đang giao')}">bg-primary</c:when>
                                              <c:when test="${order.status.equals('Hoàn thành')}">bg-success</c:when>
                                              <c:when test="${order.status.equals('Đã hủy')}">bg-danger</c:when>
                                          </c:choose>">${order.status}
                                    </span>
                                </td>
                                <td>${order.totalAmount}</td>
                                <td>
                                    <a href="viewOrderDetails?id=${order.orderID}" class="btn btn-primary btn-sm">
                                        <i class="fas fa-eye"></i> View
                                    </a>
                                    <c:choose>
                                        <c:when test="${order.status == 'Chưa xử lý'}">
                                            <a href="updateOrderStatus?id=${order.orderID}&staffId=${sessionScope.staffId}&newStatus=Đã tiếp nhận" 
                                               class="btn btn-warning btn-sm">
                                                <i class="fas fa-check"></i> Received
                                            </a>
                                        </c:when>
                                        <c:when test="${order.status == 'Đã tiếp nhận'}">
                                            <a href="updateOrderStatus?id=${order.orderID}&staffId=${sessionScope.staffId}&newStatus=Đang chuẩn bị" 
                                               class="btn btn-info btn-sm">
                                                <i class="fas fa-box"></i> Preparing
                                            </a>
                                        </c:when>
                                        <c:when test="${order.status == 'Đang chuẩn bị'}">
                                            <a href="updateOrderStatus?id=${order.orderID}&staffId=${sessionScope.staffId}&newStatus=Đang giao" 
                                               class="btn btn-primary btn-sm">
                                                <i class="fas fa-truck"></i> On Delivery
                                            </a>
                                        </c:when>
                                        <c:when test="${order.status == 'Đang giao'}">
                                            <a href="updateOrderStatus?id=${order.orderID}&staffId=${sessionScope.staffId}&newStatus=Hoàn thành" 
                                               class="btn btn-success btn-sm">
                                                <i class="fas fa-check-circle"></i> Completed
                                            </a>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                    function validatePhone() {
                        let phoneInput = document.getElementById("phone");
                        let phoneError = document.getElementById("phoneError");
                        let phoneNumber = phoneInput.value.trim();
                        let phoneRegex = /^[0-9]{10,}$/; // Chỉ chứa số, ít nhất 10 số

                        if (phoneNumber === "") {
                            phoneError.textContent = "Vui lòng nhập số điện thoại.";
                            return false;
                        } else if (!phoneRegex.test(phoneNumber)) {
                            phoneError.textContent = "Số điện thoại không hợp lệ (chỉ nhập số, ít nhất 10 số).";
                            return false;
                        } else {
                            phoneError.textContent = ""; // Xóa lỗi nếu hợp lệ
                            return true;
                        }
                    }
        </script>
    </body>
</html>
