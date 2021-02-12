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
    <title>HM Viking APP - Facturedo Profile</title>
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
        <img src="${contextPath}/resources/s2/dist/images/HMViking.png" width="50" height="50" alt="" class="img-fluid"/> HM VIKING APP</a>
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
                                <security:authentication property="principal.lastName"/></p>
                            <small class="redial-primary-light font-weight-bold text-white"><security:authentication property="principal.roles[0].name"/></small>
                        </div>
                    </div>
                </a>
                <ul class="dropdown-menu border-bottom-0 rounded-0 py-0">
                    <li><a class="dropdown-item py-2" href="/userUpdate"><i class="fa fa-cog pr-2"></i> User Profile</a></li>
                    <li><a class="dropdown-item py-2" href="/smartUserProfile"><i class="lnr lnr-location pr-2"></i> Finsmart Profile</a></li>
                    <li><a class="dropdown-item py-2" onclick="document.forms['logoutForm'].submit()"><i class="fa fa-sign-out pr-2"></i> logout</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
<br>

<div class="wrapper">
    <div id="content" class="center-block">
        <div class="row">

            <div class="col-sm-12">
                <div class="row mb-4">
                    <div class="col-12 col-xl-6 center-block">
                        <div class="card redial-border-light redial-shadow mb-4">
                            <div class="card-body">
                                <form:form method="POST" name="factuProfile" modelAttribute="factUser" class="form-signin">
                                    <img src="${contextPath}/resources/s2/dist/images/logo-facturedo.svg" height="38"  alt="" >  Access</img>
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
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <span class="input-group-addon"><i class="fa fa-envelope-open pr-2"></i></span>
                                            <form:input type="email" path="email" class="form-control"
                                                        placeholder="Email" required="required"></form:input>
                                            <form:errors path="email"></form:errors>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="password">
                                        <div class="input-group form-group ${status.error ? 'has-error' : ''}">
                                            <span class="input-group-addon"><i class="fa fa-barcode pr-2"></i></span>
                                            <form:input type="password" path="password" class="form-control"
                                                        placeholder="Password" required="required"></form:input>
                                            <form:errors path="password"></form:errors>
                                        </div>
                                    </spring:bind>
                                    <div id="connection"></div>
                                    <div class="col-md-6 mb-3">
                                        <div class='btn-group mb-2'>
                                            <button id="test" onclick=connectionTest() class="btn btn-block btn-social btn-bitbucket text-white rounded">Test Connection</button>
                                        </div>
                                        <div class='btn-group mb-2'>
                                            <button id="save" type="submit" class="btn btn-block  btn-outline-success btn-bitbucket text-white btn-block rounded">Save</button>
                                        </div>
                                    </div>
                                    <div class="col-md-6 mb-3 text-right">
                                        <div class='btn-group mb-2'>
                                            <a href="/facturedo" class="btn-primary btn btn-block rounded">Close</a>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>

                <!--END INVESTMENT TABLE-->
            </div>
        </div>
    </div>
</div>
<!-- End main-content-->

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->

<%if("true".equals(request.getParameter("failed"))){%>
<script>
    window.onload = function () {
        toastr.error('Connection failed, please update your Facturedo credentials');
    };
</script>
<%}%>
<script>
    function connectionTest(){
        $("#test").prop('disabled',true);
        $("#save").prop('disabled',true);

        $.ajax({
            url: 'factuTestConnection',
            type: 'GET',
            data : $('form[name=factuProfile]').serialize(),
            success: function(data) {
                if(data == true){
                    document.getElementById('connection').innerHTML +=
                        '<div class="alert alert-primary alert-dismissible fade show" role="alert">\n' +
                        '<strong>Connection successfully!</strong>\n' +
                        '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                        '<span aria-hidden="true">&times;</span>\n' +
                        '</button>\n' +
                        '</div>';
                }else {
                    document.getElementById('connection').innerHTML +=
                        '<div class="alert alert-danger alert-dismissible fade show" role="alert">\n' +
                        '<strong>Connection Error</strong>, please check user and password and try again\n' +
                        '<button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                        '<span aria-hidden="true">&times;</span>\n' +
                        '</button>\n' +
                        '</div>';
                }
                $("#test").prop('disabled',false);
                $("#save").prop('disabled',false);
            }
        });
    }
</script>


<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>