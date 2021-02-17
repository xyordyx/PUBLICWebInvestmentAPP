<%@ page import="java.text.DecimalFormat" %>
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
    <title>Finsmart APP - Investmet Summary</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />
    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <!-- WEBSOCKET -->
    <script src="${contextPath}/resources/s2/dist/js/jquery-3.5.1.min.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/sockjs-0.3.4.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/stomp.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/finSmart.js"></script>

</head>
<body>

<!-- Navbar -->

<nav class="navbar navbar-expand-lg bg-transparent navbar-dark">
    <div class="container">
        <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/admin">FINSMART APP</a>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNowUIKitFree">
            <span class="navbar-toggler-icon"></span>
        </button>
    </div>
</nav>
<br>
<!-- DASHBOARD KPIs-->
<div class="wrapper">
    <div id="content" class="center-block">
        <div class="row center-block">
            <div class="col-sm-12">
                <div class="row justify-content-center">
                    <c:forEach var="invList" items="${investmentsList}">
                        <div class="col-12 col-sm-5 col-xl-3 mb-7 mb-xl-3 py-2">
                            <div class="card redial-border-light redial-shadow">
                                <div class="card-body">
                                    <h7 class="mb-1 redial-font-weight-400 text-p">Invoice ID </h7>
                                    <h5 class="mb-2 text-warning">${invList.invoiceNumber}</h5>
                                    <div class="media-body">
                                        <div class="fact-box${invList.currency} text-center text-sm-right">
                                            <h2 id="amount_${invList.invoiceNumber}" class="counter_number text-white">
                                                    ${invList.amount}
                                            </h2>
                                            <%
                                                if ("true".equals(request.getParameter("schedule"))) {
                                            %>
                                            <span id="status_${invList.invoiceNumber}"
                                                  class="badge p-2 badge-warning text-white"> Scheduled </span>
                                            <%
                                            }
                                            else {
                                            %>
                                            <span id="status_${invList.invoiceNumber}"
                                                  class="badge p-2 badge-info text-white"> In Progress </span>
                                            <%
                                                }
                                            %>
                                        </div>
                                        <div class="text-danger">
                                            <p id="message_${invList.invoiceNumber}" class="mb-2"></p>
                                        </div>
                                        <div class="text-success">
                                            <p id="customer_${invList.invoiceNumber}" class="mb-2"></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <button onclick="pullStatus()" type="button" class="btn btn-block btn-instagram"><i class='fa fa-refresh pr-2'></i> Refresh</button>
                    <div class="card-body">
                        <div class='btn-group mb-2 float-right'>
                            <button onclick="returnToApp()" class="btn btn-lg btn-block btn-outline-info" id="return" style="visibility:hidden;"> Back </button>
                        </div>
                        <div class='btn-group mb-2 float-left'>
                            <button onclick="stopTransactions()" class="btn btn-lg btn-block btn-outline-primary" id="cancelBtn"> Cancel </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End main-content-->

<!-- Top To Bottom-->
<a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
</div>
<!-- End Top To Bottom-->


<!-- jQuery -->

<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>