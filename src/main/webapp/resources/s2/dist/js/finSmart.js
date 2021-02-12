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
            if(data.investmentList[i].status== "false" && data.investmentList[i].uistate == "true"){
                document.getElementById('status_'+data.investmentList[i].invoiceNumber)
                    .setAttribute("class", "badge p-2 badge-danger text-white");
                document.getElementById('status_'+data.investmentList[i].invoiceNumber).innerHTML =
                    "Investment Failed";
                document.getElementById('customer_'+data.investmentList[i].invoiceNumber)
                    .setAttribute("class", "text-danger");
                document.getElementById('customer_'+data.investmentList[i].invoiceNumber).innerHTML =
                    data.investmentList[i].message;
                invDisplay += "Invoice:  <span class=\"text-warning\">"+data.investmentList[i].invoiceNumber
                    +"</span> Status: <span class=\"text-danger\">"
                    +data.investmentList[i].status+"</span><br>";
            }else if(data.investmentList[i].status == "true" && data.investmentList[i].uistate == "true"){
                document.getElementById('status_'+data.investmentList[i].invoiceNumber)
                    .setAttribute("class", "badge p-2 badge-success text-white");
                document.getElementById('status_'+data.investmentList[i].invoiceNumber).innerHTML =
                    "Investment Successfully";
                if(data.system == "HMFINSMART"){
                    document.getElementById('customer_'+data.investmentList[i].invoiceNumber).innerHTML =
                        "Customer: "+data.investmentList[i].opportunity.debtor.companyName;
                }else{
                    document.getElementById('customer_'+data.investmentList[i].invoiceNumber).innerHTML =
                        "Customer: "+data.investmentList[i].results.debtor.official_name;
                }
                if(data.investmentList[i].autoAdjusted == true){
                    document.getElementById('amount_'+data.investmentList[i].invoiceNumber).innerHTML =
                        data.investmentList[i].adjustedAmount;
                    invDisplay += "Invoice: <span class=\"text-warning\">"+data.investmentList[i].invoiceNumber
                        +"</span> Status: <span class=\"text-success\">Success </span> " +
                        "- Amount: <span class=\"text-info\"> AUTO adjusted from "+data.investmentList[i].amount+
                        " to "+data.investmentList[i].adjustedAmount+"</span><br>";
                }else {
                    invDisplay += "Invoice: <span class=\"text-warning\">" + data.investmentList[i].invoiceNumber
                        + "</span> Status: <span class=\"text-success\">Success</span><br>";
                }
            }else if(data.investmentList[i].status == "inProgress"){
                document.getElementById('status_'+data.investmentList[i].invoiceNumber)
                    .setAttribute("class", "badge p-2 badge-info text-white");
                document.getElementById('status_'+data.investmentList[i].invoiceNumber).innerHTML =
                    "In Progress";
            }
        }
        if(data.transactionStatus === true){
            swal({
                title: "All Investments were processed!",
                text: invDisplay,
                html: true
            });
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