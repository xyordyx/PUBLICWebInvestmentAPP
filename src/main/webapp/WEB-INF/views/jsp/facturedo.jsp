<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HM Viking APP - Facturedo</title>
    <link rel="icon" href="${contextPath}/resources/s2/dist/images/favicon.ico" />

    <!--Plugin CSS-->
    <link href="${contextPath}/resources/s2/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="${contextPath}/resources/s2/dist/css/main.min.css" rel="stylesheet">
    <script src="${contextPath}/resources/s2/dist/js/jquery-3.5.1.min.js"></script>
    <script src="${contextPath}/resources/s2/dist/js/analytics.js"></script>
</head>
<body>


<!-- NAV BAR-->
<div id="header-fix" class="header py-3 py-lg-2 fixed-top">
    <div class="container-fluid">
        <div class="row">
            <div class="col-12 col-lg-3 col-xl-2 align-self-center">
                <div class="site-logo">
                    <a class="navbar-brand text-warning" title="Designed by GMP" data-placement="bottom" data-toggle="tooltip" href="/landing">
                        <img src="${contextPath}/resources/s2/dist/images/HMViking.png" width="50" height="50" alt="" class="img-fluid"/> HM Viking</a>
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
                                        </div>
                                    </div>
                                </a>
                                <ul class="dropdown-menu border-bottom-0 rounded-0 py-0">
                                    <li><a class="dropdown-item py-2" href="/userUpdate"><i class="fa fa-cog pr-2"></i> User Profile</a></li>
                                    <li><a class="dropdown-item py-2" href="/factUserProfile"><i class="lnr lnr-location pr-2"></i> Facturedo Profile</a></li>
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
                <li class="active"><a href="/finsmart" class="text-warning"><i class="fa fa-sheqel pr-2"></i> Facturedo</a></li>
                <li><a href="#" data-toggle="modal" data-target="#actualInvestments"><i class="icofont icofont-pie pr-1"></i>
                    Investments in Progress</a></li>
                <li><a href="#" data-toggle="modal" data-target="#finalizedInv"><i class="fa fa-calendar-check-o pr-1"></i>
                    Completed Investments</a></li>
                <li><a href="#" data-toggle="modal" data-target="#latestInv"><i class="fa fa-window-restore pr-1"></i>
                    Latest Investments</a></li>
            </ul>
        </div>
    </nav>
    <!--END SIDEBAR-->

    <div id="content" class="center-block">
        <div class="row">
            <div class="col-sm-12">
                <!--INVESMENT FORM-->
                <img class="py-2" src="${contextPath}/resources/s2/dist/images/logo-facturedo.png" height="95"  alt="" />
                <div class="row">
                    <div class="col-12 col-md-12 mb-4 col-xl-10 center-block">
                        <div class="card redial-border-light redial-bg-secondry-light redial-shadow mb-4">
                            <div class="card-body">
                                    <form id="bookForm" name="bookForm" action="facturedo" method="POST" class="form-horizontal">
                                        <%
                                            if ("true".equals(request.getParameter("error"))) {
                                        %>
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <strong>There was an error at the investment form. </strong> Please complete all the fields.
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <%
                                            }
                                        %>
                                        <div class="form-group" data-book-index="0">
                                            <div class="query">
                                            <a class="redial-dark d-block p-3 border redial-border-light">
                                                <div class="col-xs-4">
                                                    <div class="input-group form-group">
                                                        <span class="input-group-addon"><i class="fa fa-clone"></i></span>
                                                        <input type="text" class="form-control test" id="invoiceNumber" name="invoiceNumber" placeholder="Invoice ID" required/>
                                                </div>
                                                </div>
                                                <div class="col-xs-4">
                                                        <div class="input-group form-group">
                                                            <span class="input-group-addon"><i class="fa fa-money"></i></span>
                                                            <input type="number" class="form-control" id="amount" name="amount"
                                                                   placeholder="Amount" onkeyup="autoSum();" required/>
                                                    </div>
                                                </div>
                                                <div class="col-xs-2">
                                                    <select class="bg-warning text-dark form-group" onchange="autoSum();" id="currency" name="currency" >
                                                        <option value="PEN">Soles (PEN)</option>
                                                        <option value="USD" >Dólares (USD)</option>
                                                    </select>
                                                </div>
                                                <div class="col-xs-1">
                                                    <button type="button" class="btn btn-primary addButton"><i class="fa fa-plus"></i></button>
                                                </div>
                                            </a>
                                            </div>
                                        </div>

                                        <!-- The template for adding new field -->
                                        <div class="form-group hide" id="bookTemplate">
                                            <div class="query">
                                            <a class="redial-dark d-block p-3 border redial-border-light">
                                                <div class="col-xs-4 col-xs-offset-1">
                                                    <div class="input-group form-group">
                                                        <span class="input-group-addon"><i class="fa fa-clone"></i></span>
                                                        <input type="text" class="form-control" name="invoiceNumber" placeholder="Invoice ID" />
                                                    </div>
                                                </div>
                                                <div class="col-xs-4">
                                                    <div class="input-group form-group">
                                                        <span class="input-group-addon"><i class="fa fa-money"></i></span>
                                                            <input type="number" class="form-control" name="amount"
                                                                   placeholder="Amount" onkeyup="autoSum();"/>
                                                    </div>
                                                </div>
                                                <div class="col-xs-2">
                                                    <select class="bg-warning text-dark form-group" onchange="autoSum();" name="currency">
                                                        <option value="pen">Soles (PEN)</option>
                                                        <option value="usd">Dólares (USD)</option>
                                                    </select>
                                                </div>
                                                <div class="col-xs-1">
                                                    <button type="button" class="btn btn-primary removeButton"><i class="fa fa-minus"></i></button>
                                                    <button type="button" class="btn btn-primary addButton"><i class="fa fa-plus"></i></button>
                                                </div>
                                            </a>
                                            </div>
                                        </div>

                                        <label><h7><strong>Time of execution</strong></h7></label>
                                        <div class="form-group form-check form-check-inline">
                                            <input onclick="check(this);" class="radioCheck" value="none" name="scheduledTime" type="checkbox" id="now" checked>
                                            <label for="now" class="redial-dark mb-0">Now!</label>
                                        </div>
                                        <div class="form-group form-check form-check-inline">
                                            <input onclick="check(this);" class="radioCheck" value="10:00" name="scheduledTime" type="checkbox" id="10">
                                            <label for="10" class="redial-dark mb-0">10:00</label>
                                        </div>
                                        <div class="form-group form-check form-check-inline">
                                            <input onclick="check(this);" class="radioCheck" value="13:00" name="scheduledTime" type="checkbox" id="13">
                                            <label for="13" class="redial-dark mb-0">13:00</label>
                                        </div>
                                        <div class="form-group form-check form-check-inline">
                                            <input onclick="check(this);" class="radioCheck" value="16:00" name="scheduledTime" type="checkbox" id="16">
                                            <label for="16" class="redial-dark mb-0">16:00</label>
                                        </div>
                                        <div class="form-group form-check form-check-inline">
                                            <input onclick="check(this);" class="radioCheck" value="" type="checkbox" id="custom">
                                            <label for="custom" class="redial-dark mb-0">Custom</label>
                                        </div>

                                        <div class="form-group form-check form-check-inline" id="customField" style="display:none">
                                            <div class="input-group form-group">
                                                <span class="input-group-addon"><i class="fa fa-calendar-times-o"></i></span>
                                                <input type="text" data-masked="" name="scheduledTime" data-inputmask="'mask': '99:99'" id="customTime" placeholder="Custom Time" class="form-control">
                                            </div>
                                        </div>

                                        <div class="form-group form-check form-check-inline">
                                            <div class="input-group form-group">
                                                <span class="input-group-addon"><i class="fa fa-clock-o"></i></span>
                                                <input type="text" data-masked="" name="timeRequest" data-inputmask="'mask': '9.99'" id="timeRequest" placeholder="Time Request" class="form-control">
                                            </div>
                                        </div>
                                        <button type="submit" class="btn btn-block p-2 btn-outline-warning shadowed"><i class="fa fa-sheqel pr-2"></i>Invest!</button>
                                    </form>
                            </div>
                        </div>
                    </div>
                </div>
                <!--END INVESMENT FORM-->

                <!-- DASHBOARD KPIs-->
                <div class="row mb-xl-4 mb-0 justify-content-center  ">
                    <div class="col-12 col-sm-6 col-xl-2 mb-4 mb-xl-0">
                        <div class="card redial-bg-dark redial-border-link redial-shadow text-white">
                            <div class="card-body">
                                <div class="media d-block d-sm-flex text-center text-sm-left">
                                    <div class="media-body">
                                        <div class="fact-boxPEN text-center ">
                                            <p class="mb-2">Available to Invest</p>
                                            <h2 id="balancePEN"
                                                class="counter_number mb-1 redial-font-weight-400 text-warning">${balancePEN}
                                            </h2>
                                        </div>
                                        <div class="fact-boxUSD text-center">
                                            <h2 id="balanceUSD"
                                                class="counter_number mb-1 redial-font-weight-400 text-success">${balanceUSD} </h2>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-2 mb-4 mb-xl-0">
                        <div class="card  redial-shadow redial-bg-fb text-white">
                            <div class="card-body">
                                <div class="media d-block d-sm-flex text-center text-sm-left">
                                    <div class="media-body">
                                        <div class="fact-boxPEN text-center">
                                            <p class="mb-2">Invested in progress</p>
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalInvestedPEN} </h2>
                                        </div>
                                        <div class="fact-boxUSD text-center">
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalInvestedUSD} </h2>
                                        </div>
                                        <div id="accordion3" role="tablist">
                                            <div class="mb-2 text-center">
                                                <a class="redial-dark p-4" data-toggle="collapse" href="#collapse3" aria-expanded="true" aria-controls="collapse1">
                                                    <i class="fa fa-plus-square pr-2"></i>
                                                </a>
                                                <div id="collapse3" class="collapse" role="tabpanel" data-parent="#accordion3">
                                                    <div class="redial-divider"></div>
                                                    <div class="fact-boxPEN text-center">
                                                        <p class="mb-2">Total in progress *</p>
                                                        <h4 class="counter_number mb-1 redial-font-weight-400 text-white">${totalInvested}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-2 mb-4 mb-xl-0">
                        <div class="card redial-bg-success redial-border-success redial-shadow text-white">
                            <div class="card-body">
                                <div class="media d-block d-sm-flex text-center text-sm-left">
                                    <div class="media-body">
                                        <div class="fact-boxPEN text-center">
                                            <p class="mb-2">Total Profit</p>
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalProfitPEN} </h2>
                                        </div>
                                        <div class="fact-boxUSD text-center">
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalProfitUSD} </h2>
                                        </div>
                                        <div id="accordion4" role="tablist">
                                            <div class="mb-2 text-center">
                                                <a class="redial-dark" data-toggle="collapse" href="#collapse4" aria-expanded="true" aria-controls="collapse1">
                                                    <i class="fa fa-plus-square pr-2"></i>
                                                </a>
                                                <div id="collapse4" class="collapse" role="tabpanel" data-parent="#accordion4">
                                                    <div class="redial-divider"></div>
                                                    <div class="fact-boxPEN text-center">
                                                        <p class="mb-2">Accumulated Profit *</p>
                                                        <h4 class="counter_number mb-1 redial-font-weight-400 text-white">${totalProfit}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-2 mb-4 mb-xl-0">
                        <div class="card redial-bg-goog redial-border-twi redial-shadow text-white">
                            <div class="card-body">
                                <div class="media d-block d-sm-flex text-center text-sm-left">
                                    <div class="media-body">
                                        <div class="fact-boxPEN text-center">
                                            <p class="mb-2">Total Transferred</p>
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalDepositsPEN} </h2>
                                        </div>
                                        <div class="fact-boxUSD text-center">
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${totalDepositsUSD} </h2>
                                        </div>
                                        <div id="accordion5" role="tablist">
                                            <div class="mb-2 text-center">
                                                <a class="redial-dark" data-toggle="collapse" href="#collapse5" aria-expanded="true" aria-controls="collapse1">
                                                    <i class="fa fa-plus-square pr-2"></i>
                                                </a>
                                                <div id="collapse5" class="collapse" role="tabpanel" data-parent="#accordion5">
                                                    <div class="redial-divider"></div>
                                                    <div class="fact-boxPEN text-center">
                                                        <p class="mb-2">Total Transferred *</p>
                                                        <h4 class="counter_number mb-1 redial-font-weight-400 text-white">${totalDeposits}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12 col-sm-6 col-xl-2 mb-4 mb-xl-0">
                        <div class="card bg-secondary redial-shadow text-white">
                            <div class="card-body">
                                <div class="media d-block d-sm-flex text-center text-sm-left">
                                    <div class="media-body">
                                        <div class="fact-boxPEN text-center">
                                            <p class="mb-2">Profit Forecast</p>
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${expectedProfitPEN} </h2>
                                        </div>
                                        <div class="fact-boxUSD text-center">
                                            <h2 class="counter_number mb-1 redial-font-weight-400 text-white">${expectedProfitUSD} </h2>
                                        </div>
                                        <div id="accordion6" role="tablist">
                                            <div class="mb-2 text-center">
                                                <a class="redial-dark" data-toggle="collapse" href="#collapse6" aria-expanded="true" aria-controls="collapse1">
                                                    <i class="fa fa-plus-square pr-2"></i>
                                                </a>
                                                <div id="collapse6" class="collapse" role="tabpanel" data-parent="#accordion6">
                                                    <div class="redial-divider"></div>
                                                    <div class="fact-boxPEN text-center">
                                                        <p class="mb-2">Total Forecasted *</p>
                                                        <h4 class="counter_number mb-1 redial-font-weight-400 text-white">${expectedProfit}</h4>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--END DASHBOARD KPIS-->
            </div>
            <!--END INVESTMENT FORM-->
        </div>
    </div>


<!-- End main-content-->

<!-- Top To Bottom--> <a href="#" class="scrollup text-center redial-bg-primary redial-rounded-circle-50 ">
    <h4 class="text-white mb-0"><i class="icofont icofont-long-arrow-up"></i></h4>
</a>
<!-- End Top To Bottom-->

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
                                            <c:forEach var="invest" items="${factuInvestments}">
                                                <tr>
                                                    <td>${invest.debtor.official_name} - ${invest.businessRel.debtor_ruc}</td>
                                                    <td><fmt:formatDate value="${invest.payment_date}" pattern="dd-MM-yyyy" /></td>
                                                    <td><fmt:formatDate value="${invest.investment_date}" pattern="dd-MM-yyyy" /></td>
                                                    <td>${invest.toBeCollectedIn}</td>
                                                    <c:choose>
                                                        <c:when test="${invest.operation.currency == 'PEN'}">
                                                            <td class="text-warning">PEN ${invest.amount}</td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td class="text-success">USD ${invest.amount}</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${invest.profit}" /></td>
                                                    <td>${invest.discount_rate}</td>
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
                                                <th scope="col">Fixed P. Date</th>
                                                <th scope="col">Actual P. Date</th>
                                                <th scope="col">Past Due Days</th>
                                                <th scope="col">Investment Date</th>
                                                <th scope="col">Tem</th>
                                            </tr>
                                            </thead>
                                            <tfoot>
                                            <tr class="redial-bg-secondry-light text-white">
                                                <th scope="col">Company</th>
                                                <th scope="col">Fixed P. Date</th>
                                                <th scope="col">Actual P. Date</th>
                                                <th scope="col">Past Due Days</th>
                                                <th scope="col">Investment Date</th>
                                                <th scope="col">Tem</th>
                                            </tr>
                                            </tfoot>
                                            <tbody>
                                            <c:forEach var="invest" items="${factuFinalizedInv.results}">
                                                <tr>
                                                    <td>${invest.debtor.official_name} - ${invest.businessRel.debtor_ruc}</td>
                                                    <td>NA</td>
                                                    <td><fmt:formatDate value="${invest.payment_date}" pattern="dd-MM-yyyy" /></td>
                                                    <td>NA</td>
                                                    <td><fmt:formatDate value="${invest.investment_date}" pattern="dd-MM-yyyy" /></td>
                                                    <td>${invest.discount_rate}</td>
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
                                <span class="badge badge-primary text-success pull-right">${latestInv.dateTime}</span>
                                <h5 class="mb-2 text-warning">${latestInv.invoiceNumber}</h5>
                                <div class="media-body">
                                    <div class="fact-box${latestInv.currency} text-center text-sm-right">
                                        <h2 id="amount_${latestInv.invoiceNumber}" class="counter_number text-white">
                                                ${latestInv.amount}
                                        </h2>
                                    </div>
                                    <c:if test="${latestInv.status == true}">
                                        <c:choose>
                                            <c:when test="${latestInv.autoAdjusted == true}">
                                                <span id="status_${latestInv.invoiceNumber}"
                                                      class="badge p-2 badge-success text-white">Investment Successfully
                                                </span>
                                                <div class="text-success">
                                                    <p id="customer_${latestInv.invoiceNumber}" class="mb-2">
                                                            ${latestInv.razonSocial}
                                                    </p>
                                                </div>
                                                <div class="text-info">
                                                    <p id="message_${latestInv.invoiceNumber}" class="mb-2">
                                                        Amount: was AUTO adjusted from ${latestInv.currency} ${latestInv.amount} to
                                                            ${latestInv.currency} ${latestInv.adjustedAmount}
                                                    </p>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span id="status_${latestInv.invoiceNumber}"
                                                      class="badge p-2 badge-success text-white">Investment Successfully
                                                </span>
                                                <div class="text-success">
                                                    <p id="customer_${latestInv.invoiceNumber}" class="mb-2">
                                                            ${latestInv.razonSocial}
                                                    </p>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${latestInv.status == false}">
                                        <span id="status_${latestInv.invoiceNumber}"
                                              class="badge p-2 badge-danger text-white">Error
                                        </span>
                                        <div class="text-danger">
                                            <p id="message_${latestInv.invoiceNumber}" class="mb-2">
                                                    ${latestInv.message}
                                            </p>
                                        </div>
                                    </c:if>
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
<!--ROI Calculator-->
<div class="l_c_h ">
    <div class="c_h">
        <div class="left_c">
            <div class="left right_c left_icons">
                <a href="#" class="mini text-danger" style="font-size:23px;">+</a>
            </div>
            <div class="left center_icons"><!--center_icons-->
                ROI Calculator
            </div><!--end center_icons-->
        </div>
        <div class="clear"></div>
    </div>
    <div class="chat_container" style="display: none;">
            <div class="card-body">
                <form>
                    <div class="form-group">
                        <label class="redial-font-weight-400">Monthy Rate %</label>
                        <input id="rate" type="text" class="form-control" placeholder="Rate %" onkeyup="convert();"/>
                    </div>
                    <div class="form-group">
                        <label class="redial-font-weight-400">Days</label>
                        <input id="days" type="text" class="form-control" placeholder="Days" onkeyup="convert();"/>
                    </div>
                    <div class="form-group">
                        <label class="redial-font-weight-400">Amount to Invest</label>
                        <input id="amountInv" type="text" class="form-control" placeholder="Amount" onkeyup="convert();"/>
                    </div>
                    <div class="form-group">
                        <label class="redial-font-weight-600">ROI</label>
                        <input id="roi" type="text" class="form-control text-capitalize" placeholder="ROI" readonly disabled/>
                    </div>
                </form>
            </div>
        </div>

</div>
<!--END ROI Calculator-->

<script>
    function autoSum(){
        var totalPEN = parseInt(${balancePEN}, 10);
        var totalUSD = parseInt(${balanceUSD}, 10);
        var sumPEN = totalPEN;
        var sumUSD = totalUSD;


        $('div[class="query"]').each(function(index,item){
            var childAmount = item.children[0].children[1].children[0].children[1].value;
            var childCurency = item.children[0].children[2].children[0].options.selectedIndex;
            if (childAmount != null) {
                var number = +childAmount;
                if(childCurency == 0){
                    sumPEN = sumPEN - number;
                }else{
                    sumUSD = sumUSD - number;
                }
            }
        });
        document.getElementById('balancePEN').innerHTML = sumPEN;
        document.getElementById('balanceUSD').innerHTML = sumUSD;
    }

    function convert(){
        rate = document.getElementById('rate').value
        days = document.getElementById('days').value
        amountInv = document.getElementById('amountInv').value

        document.getElementById('roi').value = (((rate/30)*amountInv*days)*0.01).toFixed(2);

    }

    function check(input)
    {
        var checkboxes = document.getElementsByClassName("radioCheck");
        for(var i = 0; i < checkboxes.length; i++)
        {
            //uncheck all
            if(checkboxes[i].checked == true)
            {
                checkboxes[i].checked = false;
            }
        }

        //set checked of clicked object
        if(input.checked == true)
        {
            input.checked = false;
        }
        else
        {
            input.checked = true;
        }
        if(input.id === 'custom'){
            $("#customField").show();
        }else $("#customField").hide();
    }

    $(function(){
        $(".c_h").click(function(e) {
            if ($(".chat_container").is(":visible")) {
                $(".c_h .right_c .mini").text("+")
            } else {
                $(".c_h .right_c .mini").text("-")
            }
            $(".chat_container").slideToggle("slow");
            return false
        });
    });
</script>

<!-- jQuery -->
<script src="${contextPath}/resources/s2/dist/js/plugins.min.js"></script>
<script src="${contextPath}/resources/s2/dist/js/common.js"></script>
</div>
</body>
</html>