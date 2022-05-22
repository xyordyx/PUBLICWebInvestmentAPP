<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>investmentAPP APP Analysis</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="${contextPath}/resources/s2/dist/js/jquery-3.5.1.min.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/analytics.js"></script>
</head>
<body onload="analyticStart()">


<!-- NAV BAR-->
<div id="header-fix" class="header py-3 py-lg-2 fixed-top">
    <div class="container-fluid">
        <div class="row">
            <div class="col-12 col-lg-3 col-xl-2 align-self-center">
                <div class="site-logo">
                    <a class="navbar-brand" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/investmentAPP">investmentAPP APP</a>
                </div>
                <div class="navbar-header">
                    <button type="button" id="sidebarCollapse" class="navbar-btn bg-transparent float-right">
                        <i class="glyphicon glyphicon-align-left"></i>
                        <span class="navbar-toggler-icon"></span>
                        <span class="navbar-toggler-icon"></span>
                        <span class="navbar-toggler-icon"></span>
                    </button>
                </div>
            </div>
            <div class="col-12 col-lg-3 align-self-center d-none d-lg-inline-block">
            </div>
            <security:authorize access="isAuthenticated()">
                <form id="logoutForm" method="POST" action="${contextPath}/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <div class="col-12 col-lg-6 col-xl-7 d-none d-lg-inline-block">
                    <nav class="navbar navbar-expand-lg p-0">
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
                                    <li><a class="dropdown-item py-2" href="/smartUserProfile"><i class="fa fa-cog pr-2"></i> Setting</a></li>
                                    <li><a class="dropdown-item py-2" onclick="document.forms['logoutForm'].submit()"><i class="fa fa-sign-out pr-2"></i> logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </nav>
                </div>
            </security:authorize>
        </div>
    </div>
</div>
<!-- End NAV BAR--->
<!-- Main-content Top bar-->
<div class="redial-relative mt-80 py-2">
</div>
<!-- End Main-content Top bar-->



<div class="wrapper">

    <!--SIDEBAR-->

    <nav id="sidebar" class="card redial-border-light redial-shadow mb-4">
        <div class="sidebar-scrollarea">
            <ul class="metismenu list-unstyled mb-0" id="menu">
                <li><a href="/investmentAPP"><i class="fa fa-rocket pr-2"></i> investmentAPP</a></li>
                <li><a href="#" data-toggle="modal" data-target="#actualInvestments"><i class="icofont icofont-pie pr-1"></i>
                    Investments in Progress</a></li>
                <li><a href="#" data-toggle="modal" data-target="#finalizedInv"><i class="fa fa-calendar-check-o pr-1"></i>
                    Completed Investments</a></li>
                <li><a href="#" data-toggle="modal" data-target="#latestInv"><i class="fa fa-window-restore pr-1"></i>
                    Latest Investments</a></li>
                <li class="active"><a href="/monthlyAnalysis" class="text-warning"><i class="fa fa-bar-chart pr-1"></i>
                    Monthly Analysis</a></li>
            </ul>
        </div>
    </nav>
    <!--END SIDEBAR-->

    <div id="content">
        <div class="row">
            <div class="col-12 col-sm-12">
                <div class="row mb-4">
                    <div class="col-12 col-md-12">
                        <div class="card redial-border-light redial-shadow mb-4">
                            <div class="card-body">
                                <h6 class="header-title pl-3 redial-relative">Monthly Analysis</h6>
                                <div id="historicGraphic"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-12">
                <div class="row mb-4">
                    <div class="col-12 col-md-12">
                        <div class="card redial-border-light redial-shadow mb-4">
                            <div class="card-body">
                                <h6 class="header-title pl-3 redial-relative">Weekly Analysis</h6>
                                <div id="weeklyGraphic"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- End main-content-->

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->

<!--SIDE BAR START-->
<!-- Actual investments -->
<div class="modal fade" id="actualInvestments" tabindex="-1" role="dialog" aria-labelledby="actualInvestments" aria-hidden="true">
    <div class="modal-dialog  modal-lg" role="document">
        <div class="modal-content redial-border-light">
            <div class="modal-header redial-border-light">
                <h5 class="modal-title pt-2" id="exampleModalLabel3">Latest Investments</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="false">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="col-12 col-sm-12">
                    <div class="row mb-4">
                        <div class="col-12 col-md-12">
                            <div class="card redial-border-light redial-shadow mb-4">
                                <div class="card-body">
                                    <table id="example" class="table table-striped" cellspacing="0" width="100%">
                                        <thead>
                                        <tr class="redial-bg-primary text-white">
                                            <th scope="col">Company</th>
                                            <th scope="col">Payment Date</th>
                                            <th scope="col">Creation Date</th>
                                            <th scope="col">Days Remaining</th>
                                            <th scope="col">Amount</th>
                                            <th scope="col">Profit</th>
                                            <th scope="col">Tea / Tem</th>
                                        </tr>
                                        </thead>
                                        <tfoot>
                                        <tr class="redial-bg-primary text-white">
                                            <th scope="col">Company</th>
                                            <th scope="col">Payment Date</th>
                                            <th scope="col">Creation Date</th>
                                            <th scope="col">Days Remaining</th>
                                            <th scope="col">Amount</th>
                                            <th scope="col">Profit</th>
                                            <th scope="col">Tea / Tem</th>
                                        </tr>
                                        </tfoot>
                                        <tbody>
                                        <c:forEach var="invest" items="${investments}">
                                            <tr>
                                                <c:choose>
                                                    <c:when test="${invest.value.invoiceAppend.toBeCollectedIn == 'En mora'}">
                                                        <td class="bg-danger text-white">${invest.value.invoice.debtor.companyName} - ${invest.value.invoice.debtor.companyRuc}</td>
                                                        <td class="bg-danger text-white"><fmt:formatDate value="${invest.value.invoiceAppend.paymentDate}" pattern="dd-MM-yyyy" /></td>
                                                        <td class="bg-danger text-white"><fmt:formatDate value="${invest.value.invoiceAppend.createdAt}" pattern="dd-MM-yyyy" /></td>
                                                        <td class="bg-danger text-white">-${invest.value.invoiceAppend.moraDays}</td>
                                                        <c:choose>
                                                            <c:when test="${invest.value.currency == 'pen'}">
                                                                <td class="bg-danger text-white">PEN ${invest.value.amount}</td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="bg-danger text-white">USD ${invest.value.amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <td class="bg-danger text-white">${invest.value.expectedProfit}</td>
                                                        <td class="bg-danger text-white">${invest.value.invoiceAppend.tea} /<br>${invest.value.invoiceAppend.tem}</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>${invest.value.invoice.debtor.companyName} - ${invest.value.invoice.debtor.companyRuc}</td>
                                                        <td><fmt:formatDate value="${invest.value.invoiceAppend.paymentDate}" pattern="dd-MM-yyyy" /></td>
                                                        <td><fmt:formatDate value="${invest.value.invoiceAppend.createdAt}" pattern="dd-MM-yyyy" /></td>
                                                        <td>${invest.value.invoiceAppend.toBeCollectedIn}</td>
                                                        <c:choose>
                                                            <c:when test="${invest.value.currency == 'pen'}">
                                                                <td class="text-warning">PEN ${invest.value.amount}</td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="text-success">USD ${invest.value.amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <td>${invest.value.expectedProfit}</td>
                                                        <td>${invest.value.invoiceAppend.tea} /<br>${invest.value.invoiceAppend.tem}</td>
                                                    </c:otherwise>
                                                </c:choose>
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
            <div class="modal-footer redial-border-light">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- End Actual investments -->


<!-- Finalized investments -->
<div class="modal fade" id="finalizedInv" tabindex="-1" role="dialog" aria-labelledby="finalizedInv" aria-hidden="true">
    <div class="modal-dialog  modal-lg" role="document">
        <div class="modal-content redial-border-light">
            <div class="modal-header redial-border-light">
                <h5 class="modal-title pt-2" id="exampleModalLabel2">Latest Investments</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="false">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="col-12 col-sm-12">
                    <div class="row mb-4">
                        <div class="col-12 col-md-12">
                            <div class="card redial-border-light redial-shadow mb-4">
                                <div class="card-body">
                                    <table id="example2" class="table table-striped table-bordered mb-0 redial-font-weight-500 d-md-table" cellspacing="0" width="100%">
                                        <thead>
                                        <tr class="redial-bg-secondry-light text-white">
                                            <th scope="col">Company</th>
                                            <th scope="col">Investment Date</th>
                                            <th scope="col">Fixed P. Date</th>
                                            <th scope="col">Actual P. Date</th>
                                            <th scope="col">Due Days</th>
                                            <th scope="col">Tem</th>
                                            <th scope="col">Amount</th>
                                        </tr>
                                        </thead>
                                        <tfoot>
                                        <tr class="redial-bg-secondry-light text-white">
                                            <th scope="col">Company</th>
                                            <th scope="col">Investment Date</th>
                                            <th scope="col">Fixed P. Date</th>
                                            <th scope="col">Actual P. Date</th>
                                            <th scope="col">Due Days</th>
                                            <th scope="col">Tem</th>
                                            <th scope="col">Amount</th>
                                        </tr>
                                        </tfoot>
                                        <tbody>
                                        <c:forEach var="invest" items="${finalizedInv}">
                                            <tr>
                                                <td>${invest.transactions.invoice.debtor.companyName} - ${invest.transactions.invoice.debtor.companyRuc}</td>
                                                <td><fmt:formatDate value="${invest.createdAt}" pattern="dd-MM-yyyy" /></td>
                                                <td><fmt:formatDate value="${invest.paymentDate}" pattern="dd-MM-yyyy" /></td>
                                                <td><fmt:formatDate value="${invest.actualPaymentDate}" pattern="dd-MM-yyyy" /></td>
                                                <td>${invest.pastDueDays}</td>
                                                <td>${invest.tem}</td>
                                                <td>${invest.transactions.amount} ${invest.currency}</td>
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
            <div class="modal-footer redial-border-light">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- End finalized investments -->


<!-- Latest Investments MODAL -->
<div class="modal fade" id="latestInv" tabindex="-1" role="dialog" aria-labelledby="latestInv" aria-hidden="true">
    <div class="modal-dialog  modal-lg" role="document">
        <div class="modal-content redial-border-light">
            <div class="modal-header redial-border-light">
                <h5 class="modal-title pt-2" id="exampleModalLabel">Latest Investments</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="false">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:forEach var="latestInv" items="${latestInvestments}">
                    <div class="col-12 col-sm-5 col-xl-3 mb-7 mb-xl-3 py-2">
                        <div class="card redial-border-light redial-shadow">
                            <div class="card-body">
                                <h7 class="mb-1 redial-font-weight-400 text-p">Invoice ID </h7>
                                <span class="badge badge-primary text-success pull-right"><fmt:formatDate value="${latestInv.createdAt}" pattern="dd-MM-yyyy" /></span>
                                <h5 class="mb-2 text-warning">${latestInv.invoice.physicalInvoices[0].code}</h5>
                                <div class="media-body">
                                    <div class="fact-box${latestInv.currency} text-center text-sm-right">
                                        <h2 id="amount_${latestInv.invoice.physicalInvoices[0].code}" class="counter_number text-white">
                                                ${latestInv.amount}
                                        </h2>
                                    </div>
                                    <div class="text-success">
                                        <p id="customer_${latestInv.invoice.physicalInvoices[0].code}" class="mb-2">
                                            <c:set var="str1" value="${latestInv.invoice.debtor.companyName}"/>
                                            <c:set var = "str2" value = "${fn:substring(str1, 0, 35)}" />
                                                ${str2}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="modal-footer redial-border-light">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!--End Latest Investments MODAL -->
<!--SIDE BAR END-->



<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</body>
</html>