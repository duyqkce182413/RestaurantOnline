<%-- 
    Document   : pagination.jsp
    Created on : Nov 3, 2024, 3:29:57 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="baseUrl" value="${param.baseUrl != null ? param.baseUrl : 'home'}" />
<nav aria-label="Page navigation example">
    <ul class="pagination">
        <!-- Nút Previous -->
        <li class="page-item <c:if test='${currentPage <= 1}'>disabled</c:if>'">
            <a class="page-link" href="<c:if test='${currentPage > 1}'>${baseUrl}?index=${currentPage - 1}</c:if>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>

            <!-- Số trang -->
        <c:forEach begin="1" end="${endPage}" var="i">
            <li class="page-item <c:if test='${i == currentPage}'>active</c:if>'">
                <a class="page-link" href="${baseUrl}?index=${i}">${i}</a>
            </li>
        </c:forEach>

        <!-- Nút Next -->
        <li class="page-item <c:if test='${currentPage >= endPage}'>disabled</c:if>'">
            <a class="page-link" href="<c:if test='${currentPage < endPage}'>${baseUrl}?index=${currentPage + 1}</c:if>" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">Next</span>
            </a>
        </li>
    </ul>
</nav>

