var stompClient = null;
var i;
var invDisplay = "";

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/finSmart/investments', function (data) {
            console.log(data);
            processData(JSON.parse(data.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function processData(data) {
    if(data.investmentList.length !== 0){
        for (i = 0; i < data.investmentList.length; i++) {
            if(data.system == "HMFINSMART"){
                if(data.investmentList[i].status == "cancelled"){
                    document.getElementById('card_'+data.investmentList[i].invoiceNumber)
                        .setAttribute("class", "card redial-border-light redial-shadow redial-bg-goog");
                    document.getElementById('invID_'+data.investmentList[i].invoiceNumber)
                        .setAttribute("class", "mb-1 redial-font-weight-400 text-primary");
                    document.getElementById('currency_'+data.investmentList[i].invoiceNumber)
                        .setAttribute("class", "fact-box"+data.investmentList[i].currency+" text-center text-sm-right text-primary");
                    document.getElementById('amount_'+data.investmentList[i].invoiceNumber)
                        .setAttribute("class", "counter_number text-primary");

                    document.getElementById('status_'+data.investmentList[i].invoiceNumber)
                        .setAttribute("class", "badge p-2 badge-light text-danger");
                    document.getElementById('status_'+data.investmentList[i].invoiceNumber).innerHTML =
                        "NOT FOUND - CANCELLED";
                }else {
                    document.getElementById('customer_' + data.investmentList[i].invoiceNumber).innerHTML =
                        data.investmentList[i].opportunity.debtor.companyName;
                    if (data.investmentList[i].status == "inProgress") {
                        document.getElementById('card_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "card redial-border-light redial-shadow redial-bg-link");
                        document.getElementById('status_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "badge p-2 badge-success text-white");
                        document.getElementById('status_' + data.investmentList[i].invoiceNumber).innerHTML =
                            "In Progress";
                    }else if (data.investmentList[i].status == "scheduled") {
                        document.getElementById('card_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "card redial-border-light redial-shadow redial-bg-fb");
                        document.getElementById('status_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "badge p-2 badge-warning text-white");
                        document.getElementById('status_' + data.investmentList[i].invoiceNumber).innerHTML =
                            "Scheduled";
                    }else if(data.investmentList[i].status == "true"){
                        document.getElementById('card_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "card redial-border-light redial-shadow bg-success");
                        document.getElementById('status_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "badge p-2 badge-light text-success");
                        document.getElementById('customer_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "mb-2 text-warning lead");
                        if(data.investmentList[i].autoAdjusted == true){
                            document.getElementById('amount_'+data.investmentList[i].invoiceNumber).innerHTML =
                                data.investmentList[i].adjustedAmount;
                            invDisplay += "Invoice: <span class=\"text-warning\">"+data.investmentList[i].invoiceNumber
                                +"</span> Status: <span class=\"text-success\">Success </span> " +
                                "- Amount: <span class=\"text-info\"> AUTO adjusted from "+data.investmentList[i].amount+
                                " to "+data.investmentList[i].adjustedAmount+"</span><br>";
                            document.getElementById('status_' + data.investmentList[i].invoiceNumber).innerHTML =
                                "Completed with Amount Adjustment";
                        }else{
                            document.getElementById('status_' + data.investmentList[i].invoiceNumber).innerHTML =
                                "Completed Successfully";
                        }
                    }else if(data.investmentList[i].status == "false"){
                        document.getElementById('card_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "card redial-border-light redial-shadow redial-bg-goog");
                        document.getElementById('invID_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "mb-1 redial-font-weight-400 text-primary");
                        document.getElementById('currency_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "fact-box"+data.investmentList[i].currency+" text-center text-sm-right text-primary");
                        document.getElementById('amount_'+data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "counter_number text-primary");
                        document.getElementById('customer_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "mb-2 text-primary lead");

                        document.getElementById('status_' + data.investmentList[i].invoiceNumber).style.visibility =
                            "hidden";
                        document.getElementById('message_' + data.investmentList[i].invoiceNumber)
                            .setAttribute("class", "mb-2 text-white lead");
                        document.getElementById('message_'+data.investmentList[i].invoiceNumber).innerHTML =
                            data.investmentList[i].message;
                    }
                }
            }
        }
        if(data.transactionStatus === true){
            toastr.success('All Investments were processed!');
            document.getElementById("return").style.visibility = "visible";
            document.getElementById("cancelBtn").style.visibility = "hidden";
            disconnect();
        }
    }
}

function stopTransactions(){
    swal({
            title: "Do you want to cancel?",
            text: "All investments in progress will be lost",
            type: "warning",
            showCancelButton: true,
            confirmButtonClass: 'btn-primary',
            confirmButtonText: 'Yes, I want to cancel it!',
            cancelButtonText: "No",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                disconnect();
                $.ajax({
                    type : "GET",
                    url : "cancelTransactions.json"
                });
                swal("Investments successfully cancelled!" , "Going back to Invesment APP", "success", returnToApp());
            } else {
                swal("Investments are in progress!");
            }
        });
}

function returnToApp() {
    window.location=document.referrer;
}

function pullStatus(){
    $.ajax({
        url: 'getDataUX.json',
        type: 'GET',
        success: function(data) {
            if(data != null){
                processData(data);
            }
        },
        error: function(xhr){
            var errorMessage = xhr.status + ': ' + xhr.statusText
            console.log('Error - ' + errorMessage);
        }
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});