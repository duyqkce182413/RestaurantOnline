<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Order Approval</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
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
            .table-responsive {
                max-width: 100%;
                overflow-x: auto;
            }
            .table td, .table th {
                word-wrap: break-word;
                white-space: normal;
                min-width: 100px;
            }
            td {
                max-width: 200px;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Dashboard_Header.jsp"></jsp:include>
            <div class="wrapper clearfix row">
            <jsp:include page="Dashboard_Sidebar.jsp"></jsp:include>
                <div class="content">
                    <h1 class="text-center mb-4">Order Approval</h1>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover text-center align-middle">
                            <thead class="table-dark">
                                <tr>
                                    <th>Approval ID</th>
                                    <th>Người Duyệt</th>
                                    <th>Thông Tin Đơn Hàng</th>
                                    <th>Ngày Duyệt</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="approval" items="${approvals}">
                                <tr>
                                    <td>${approval.approvalID}</td>
                                    <td><strong>${approval.approvedBy.fullName}</strong></td>
                                    <td>
                                        <strong>Người Mua:</strong> ${approval.order.address.name} <br>
                                        <strong>SĐT:</strong> ${approval.order.user.phoneNumber} <br>
                                        <strong>Địa Chỉ:</strong> ${approval.order.address.addressLine}, ${approval.order.address.city} <br>
                                        <strong>Tổng Tiền:</strong> <fmt:formatNumber value="${approval.order.totalAmount}" type="currency" maxFractionDigits="0" currencySymbol="₫" />
                                    </td>
                                    <td><fmt:formatDate value="${approval.approvedAt}" pattern="dd/MM/yyyy HH:mm" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
