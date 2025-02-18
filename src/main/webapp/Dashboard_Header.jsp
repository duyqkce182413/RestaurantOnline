<%-- 
    Document   : header
    Created on : Oct 17, 2024, 8:06:54 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Navbar -->
<nav class="navbar mb-5">
    <div class="d-flex justify-content-end align-items-center w-100">
        <a href="home" class="btn btn-secondary me-3">Home</a>
        <i class="fa-solid fa-user me-2"><c:if test="${sessionScope.user != null}">
            <span class="username-display me-5">${user.username}</span>
        </c:if></i>
        
    </div>
</nav>
