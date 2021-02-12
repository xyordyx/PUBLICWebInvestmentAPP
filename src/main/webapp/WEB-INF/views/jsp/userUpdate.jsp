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
    <title>GMP SYSTEM - User Update</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

</head>
<body>

<!-- Navbar -->

<!-- NAV BAR-->
<nav class="navbar navbar-expand-lg bg-transparent navbar-dark">
    <div class="container">
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/finsmart">HM Viking APP</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNowUIKitFree">
            <span class="navbar-toggler-icon"></span>
        </button>
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <ul class="navbar-nav notification ml-auto d-inline-flex">
            <li class="nav-item dropdown user-profile align-self-center">
                <a  class="nav-link" data-toggle="dropdown" aria-expanded="false">
                    <span class="float-right pl-3 text-white"><i class="fa fa-angle-down"></i></span>
                    <div class="media">
                        <div class="media-body align-self-center">
                            <p class="mb-2 text-white text-uppercase font-weight-bold"><security:authentication property="principal.firstName"/>
                                <security:authentication property="principal.lastName"/></p></div>
                    </div>
                </a>
                <ul class="dropdown-menu border-bottom-0 rounded-0 py-0">
                    <li><a class="dropdown-item py-2" href="/userUpdate"><i class="fa fa-cog pr-2"></i> User Profile</a></li>
                    <li><a class="dropdown-item py-2" onclick="document.forms['logoutForm'].submit()"><i class="fa fa-sign-out pr-2"></i> logout</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
<!-- End NAV BAR--->
<br>

<div class="wrapper">
    <div id="content" class="center-block">
        <div class="row">
            <div class="col-sm-12">
                <!--PROFILE-->
                <div class="row">
                    <div class="col-12 col-md-12 mb-4 col-xl-8 center-block">
                        <div class="card redial-border-light redial-shadow mb-4">
                            <div class="card-body">
                                <h5 class="pl-3 redial-relative">User Profile</h5>
                                <form:form method="POST" name="userRegistration" modelAttribute="user" class="form-signin">
                                <%
                                    if ("true".equals(request.getParameter("saved"))) {
                                %>
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    <strong>Finsmart Profile was saved succesfully!</strong>
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <%
                                }else if("false".equals(request.getParameter("saved"))){
                                %>
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <strong>Connection Error</strong>, please check user and password and try again
                                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <%
                                    }
                                %>
                                <spring:bind path="email">
                                    <label class="redial-font-weight-600">Email</label>
                                    <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                        <form:input type="email" path="email" class="form-control"
                                                    placeholder="Email" required="required" readonly="true"></form:input>
                                        <form:errors path="email"></form:errors>
                                    </div>
                                </spring:bind>
                                <spring:bind path="firstName">
                                    <label class="redial-font-weight-600">First Name</label>
                                    <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                        <form:input type="text" path="firstName" class="form-control"
                                                    placeholder="First Name" required="required"></form:input>
                                        <form:errors path="firstName"></form:errors>
                                    </div>
                                </spring:bind>
                                <spring:bind path="lastName">
                                    <label class="redial-font-weight-600">Last Name</label>
                                    <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                        <form:input type="text" path="lastName" class="form-control"
                                                    placeholder="Last Name" required="required"></form:input>
                                        <form:errors path="lastName"></form:errors>
                                    </div>
                                </spring:bind>

                                <spring:bind path="password">
                                    <label class="redial-font-weight-600">Password</label>
                                    <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                        <form:input id="password" type="password" path="password" class="form-control"
                                                    placeholder="Password" required="required"></form:input>
                                        <form:errors path="password"></form:errors>
                                    </div>
                                </spring:bind>
                                <spring:bind path="repassword">
                                    <label class="redial-font-weight-600">Confirm Password</label>
                                    <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                        <form:input id="confirm_password" type="password" path="repassword" class="form-control"
                                                    placeholder="Confirm Password" required="required"></form:input>
                                        <form:errors path="repassword"></form:errors>
                                    </div>
                                </spring:bind>
                                <span id='message'></span><br>
                                <div class='btn-group mb-2'>
                                    <button id="save" type="submit" class="btn-warning btn btn-block rounded">Save</button>
                                </div>
                                <div class='btn-group mb-2'>
                                    </form:form>
                                    <a class="btn-instagram btn btn-block rounded" href="/landing">Return</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--END PROFILE-->
            </div>
        </div>
    </div>
</div>
<!-- End main-content-->

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->

<script>
    $('#password, #confirm_password').on('keyup', function () {
        if($('#password').val() == ''){
            $('#message').clear();
        }
        if ($('#password').val() == $('#confirm_password').val()) {
            $('#message').html('Passwords are ok').css('color', 'green');
            $("#save").prop('disabled',false);
        } else {
            $('#message').html('Passwords are not identical').css('color', 'red');
            $("#save").prop('disabled', true);
        }
    });
</script>

<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</div>
</body>
</html>