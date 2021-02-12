<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HM VIKING - Factoring</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="${contextPath}/resources/s2/dist/js/jquery-3.5.1.min.js"></script>
</head>
<body>

<!-- Navbar -->

<nav class="navbar navbar-expand-lg bg-transparent navbar-dark">
    <div class="container">
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="#">
            <img src="${contextPath}/resources/s2/dist/images/HMViking.png" width="50" height="50" alt="" class="img-fluid"/> HM VIKING APP</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNowUIKitFree">
            <span class="navbar-toggler-icon"></span>
        </button>
    </div>
    <div class="col-12 col-lg-6 col-xl-7 d-none d-lg-inline-block">
        <nav class="navbar navbar-expand-lg p-0">
            <ul class="navbar-nav notification ml-auto d-inline-flex">
                <form id="logoutForm" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <li><button class="dropdown-item py-2" onclick="document.forms['logoutForm'].submit()"><i class="fa fa-sign-out pr-2"></i> EXIT</button></li>                <li class="nav-item dropdown user-profile align-self-center">
            </ul>
        </nav>
    </div>
</nav>

<div id="content" class="center-block">
    <div class="row">
        <div class="col-sm-12">
            <br>
            <br>
            <br>
            <div class="row mb-xl-4 mb-0 justify-content-center ">
                <%
                    ArrayList<String> posts=(ArrayList<String>) request.getAttribute("roles");
                    for (String role: posts) {
                        if(role.equals("FINSMART_PRIVILEGE")){
                %>
                <div class="col-12 col-sm-6 col-xl-3 mb-4">
                    <div class="gallery card bg-white">
                        <a href="/finsmart"><img src="${contextPath}/resources/s2/dist/images/logo-finsmart2.png"  data-gallery="example-gallery" alt="" class="img-fluid rounded"></a>
                    </div>
                </div>
                <% } if(role.equals("FACTUREDO_PRIVILEGE")){ %>
                <div class="col-12 col-sm-6 col-xl-3 mb-4">
                    <div class="gallery card bg-white">
                        <a href="/facturedo"><img src="${contextPath}/resources/s2/dist/images/logo-facturedo.png"  data-gallery="example-gallery" alt="" class="img-fluid rounded"></a>
                    </div>
                </div>
                <%}
                    }%>
            </div>
        </div>
    </div>
    </div>
    <!-- End Top To Bottom-->

    <!-- jQuery -->
<script>
    $(document).ready(function () {
        //disable a normal button
        $("#test").attr("disabled", true);

    });
</script>
    <script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>