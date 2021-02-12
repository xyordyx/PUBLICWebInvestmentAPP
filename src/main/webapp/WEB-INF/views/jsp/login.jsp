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
    <title>HM VIKING APP - Login</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="${contextPath}/resources/s2/dist/js/jquery-3.5.1.min.js"></script>
    <script>
        function validate() {
            if (document.f.username.value == "" && document.f.password.value == "") {
                toastr.error('Username/Email and password are required');
                document.f.username.focus();
                return false;
            }
            if (document.f.username.value == "") {
                toastr.error('Username/Email is required');
                document.f.username.focus();
                return false;
            }
            if (document.f.password.value == "") {
                toastr.error('Password is required');
                document.f.password.focus();
                return false;
            }
        }
    </script>
</head>
<body>

<!-- Navbar -->

<nav class="navbar navbar-expand-lg bg-transparent navbar-dark">
    <div class="container">
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="#">HM VIKING APP</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNowUIKitFree">
            <span class="navbar-toggler-icon"></span>
        </button>
    </div>
</nav>
<br>

<div class="wrapper">
    <div id="content" class="center-block">
        <div class="row">
            <div class="col-sm-12">
                <!--INVESMENT FORM-->
                <div class="wrapper">
                    <div class="w-100">
                        <div class="row d-flex justify-content-center  pt-5 mt-5">
                            <div class="col-12 col-xl-4">
                                <div class="card">
                                    <div class="card-body text-center">
                                        <h4 class="mb-0 redial-font-weight-400">Please Sign In</h4>
                                    </div>
                                    <div class="redial-divider"></div>
                                    <div class="card-body py-4 text-center">
                                        <img src="${contextPath}/resources/s2/dist/images/HMViking.png" width="50" height="50" alt="" class="img-fluid mb-4">
                                        <form name='f' action="login" method='POST' onsubmit="return validate();">
                                            <div class="form-group">
                                                <input type="text" class="form-control" name='username' value='' placeholder="E-Mail" />
                                            </div>
                                            <div class="form-group">
                                                <input type="password" class="form-control"  name='password' placeholder="password" />
                                            </div>
                                            <div class="form-group text-left">
                                                <input type="checkbox" id="remember-me" name="remember-me">
                                                <label for="remember-me">Remember Me</label>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-md redial-rounded-circle-50 btn-block">Login</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            <!--END INVESTMENT FORM-->
        </div>
    </div>
</div>
        <%
         if ("logOut".equals(request.getParameter("param"))) {
         %>
             <script>
                window.onload = function () {
                    toastr.success('Logged out successfully');
             };
            </script>
        <%}else if("true".equals(request.getParameter("error"))){%>
    <script>
        window.onload = function () {
            toastr.warning('Username or password are not valid');
        };
    </script>
    <%} else if("valid".equals(request.getParameter("param"))){%>
    <script>
        window.onload = function () {
            toastr.success('Account successfully created');
        };
    </script>
        <%} else if("expired".equals(request.getParameter("param"))){%>
    <script>
        window.onload = function () {
            toastr.error('Link has expired, please contact support to get a new registration link');
        };
    </script>
        <%} else if("invalidToken".equals(request.getParameter("param"))){%>
    <script>
        window.onload = function () {
            toastr.error('Link introduced is not valid, please contact support to get a new registration link');
        };
    </script>
        <%}%>
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