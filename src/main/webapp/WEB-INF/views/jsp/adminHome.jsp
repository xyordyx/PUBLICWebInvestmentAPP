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
    <title>GMP SYSTEM - ADMIN</title>
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
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/adminHome">GMP SYSTEM</a>
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
<div id="content" class="center-block">
    <div class="row">
        <div class="col-md-12 ">
            <div class="row mb-4">
                <div class="col-md-12">
                    <div class="card redial-border-light redial-shadow mb-4">
                        <div class="card-body">
                            <h6 class=" pl-3 redial-relative"><a href="/adminUserRegistration" class="btn btn-dropbox">Create User</a></h6>
                            <table class="table table-hover mb-0 redial-font-weight-500 table-responsive d-md-table">
                                <thead class="redial-dark">
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">Email</th>
                                    <th scope="col">First Name</th>
                                    <th scope="col">Last Name</th>
                                    <th scope="col">Role</th>
                                    <th scope="col">Email <br>
                                        Confirmed</th>
                                    <th scope="col">Blocked</th>
                                    <th scope="col"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="user" items="${users}">
                                    <url var="updateLink" value="updateUser">
                                        <param name="userId" value="${tempUsers.id}" />
                                    </url>
                                    <tr>
                                        <th scope="row">${user.id}</th>
                                        <td>${user.email}</td>
                                        <td>${user.firstName}</td>
                                        <td>${user.lastName}</td>
                                        <td>
                                        <c:forEach var="role" items="${user.roles}">
                                            ${role.name}<br>
                                        </c:forEach>
                                            </td>
                                        <td>${user.enabled}</td>
                                        <td>${user.blocked}</td>
                                        <td>
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Options</button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="/confirm?id=${user.id}"
                                                       onclick="if (!(confirm('Are you sure you want to confirm customer email?'))) return false">
                                                        Confirm EMail</a>
                                                    <a class="dropdown-item" href="/unblock?id=${user.id}"
                                                       onclick="if (!(confirm('Are you sure you want to unblock this customer?'))) return false">
                                                        Unblock</a>
                                                    <a class="dropdown-item" href="/block?id=${user.id}"
                                                       onclick="if (!(confirm('Are you sure you want to block this customer?'))) return false">
                                                        Block</a>
                                                    <a class="dropdown-item" href="/edit?id=${user.id}">Edit</a>
                                                    <a class="dropdown-item" href="/resetPassword?id=${user.id}"
                                                       onclick="if (!(confirm('Are you sure you want to reset this customer password?'))) return false">
                                                        Reset pswd</a>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End main-content-->
<%
    if ("true".equals(request.getParameter("saved"))) {
%>
<script>
    window.onload = function () {
        toastr.success('User created successfully');
    };
</script>
<%}else if("true".equals(request.getParameter("updated"))){%>
<script>
    window.onload = function () {
        toastr.success('User updated successfully');
    };
</script>
<%}%>

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->


<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>