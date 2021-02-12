<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.gmp.persistence.model.User" %>
<%@ page import="com.gmp.persistence.model.Role" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>GMP SYSTEM - Admin User Update</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

</head>
<body>

<!-- Navbar -->

<nav class="navbar navbar-expand-lg bg-transparent navbar-dark">
    <div class="container">
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/admin">FINSMART APP</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNowUIKitFree">
            <span class="navbar-toggler-icon"></span>
        </button>
        <security:authorize access="isAuthenticated()">
            <form id="logoutForm" method="POST" action="${contextPath}/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <div class="collapse navbar-collapse text-center justify-content-end" id="navbarNowUIKitFree">
                <ul class="navbar-nav">
                    <li><b class="dropdown-item py-2"><i class="fa fa-user pr-2"></i> <security:authentication property="principal.firstName"/>
                        <security:authentication property="principal.lastName"/></b></li>
                    <li><a class="dropdown-item py-2" onclick="document.forms['logoutForm'].submit()"><i class="fa fa-sign-out pr-2"></i> logout</a></li>
                </ul>
            </div>
        </security:authorize>
    </div>
</nav>
<br>

<div class="wrapper">
    <div id="content" class="center-block">
        <div class="row">
            <div class="col-sm-12">
                <!--INVESMENT FORM-->
                <div class="row">
                    <div class="col-12 col-md-12 mb-4 col-xl-8 center-block">
                        <div class="card redial-border-light redial-shadow mb-4">
                            <div class="card-body">
                                <h5 class="pl-3 redial-relative">USER UPDATE</h5>
                                <form:form method="POST" name="userRegistration" modelAttribute="user" class="form-signin">
                                    <spring:bind path="email">
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <form:input type="email" path="email" class="form-control"
                                                        placeholder="Email" required="required"></form:input>
                                            <form:errors path="email"></form:errors>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="firstName">
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <form:input type="text" path="firstName" class="form-control"
                                                        placeholder="First Name" required="required"></form:input>
                                            <form:errors path="firstName"></form:errors>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="lastName">
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <form:input type="text" path="lastName" class="form-control"
                                                        placeholder="Last Name" required="required"></form:input>
                                            <form:errors path="lastName"></form:errors>
                                        </div>
                                    </spring:bind>


                                    <spring:bind path="roles">
                                        <div class="col-12 col-sm-2 align-self-center">
                                            <label class="redial-font-weight-600 mb-3">ROLE</label>
                                        </div>
                                        <div class="form-group">
                                            <form:select  multiple="true" path="tempRol" class="form-control select2" >
                                                <optgroup label="Roles Available">
                                                    <c:forEach var="roles" items="${user.roles}">
                                                        <c:choose>
                                                            <c:when test="${roles.name == 'ROLE_ADMIN'}">
                                                                <form:option value="ROLE_ADMIN" selected="selected">Admin</form:option>
                                                            </c:when>
                                                            <c:when test="${roles.name == 'ROLE_USER'}">
                                                                <form:option value="ROLE_USER" selected="selected">User</form:option>
                                                            </c:when>
                                                            <c:when test="${roles.name == 'ROLE_FINSMART'}">
                                                                <form:option value="ROLE_FINSMART" selected="selected">Finsmart</form:option>
                                                            </c:when>
                                                            <c:when test="${roles.name == 'ROLE_FACTUREDO'}">
                                                                <form:option value="ROLE_FACTUREDO" selected="selected">Facturedo</form:option>
                                                            </c:when>
                                                        </c:choose>
                                                    </c:forEach>
                                                    <form:option value="ROLE_ADMIN">Admin</form:option>
                                                    <form:option value="ROLE_USER">User</form:option>
                                                    <form:option value="ROLE_FINSMART">Finsmart</form:option>
                                                    <form:option value="ROLE_FACTUREDO">Facturedo</form:option>
                                                </optgroup>
                                            </form:select>
                                            <form:errors path="tempRol"></form:errors>
                                        </div>
                                    </spring:bind>

                                    <spring:bind path="password">
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <form:input type="password" path="password" class="form-control"
                                                        placeholder="Password" required="required"></form:input>
                                            <form:errors path="password"></form:errors>
                                        </div>
                                    </spring:bind>
                                    <div class='btn-group mb-2'>
                                        <button type="submit" class="btn-primary btn btn-block rounded">Update</button>
                                    </div>
                                    <div class='btn-group mb-2'>
                                        <a href="/adminHome" class="btn-primary btn btn-block rounded">Close</a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
                <!--END INVESMENT FORM-->


        </div>
    </div>
</div>
<!-- End main-content-->

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->


<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>