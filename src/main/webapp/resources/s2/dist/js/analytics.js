var i;
var profit = ['Profit'];
var invested = ['Invested'];
var timeLine = ['x'];
var profitForecast = ['P.Expected'];

var profitWeek = ['Profit'];
var investedWeek = ['Invested'];
var timeLineWeek = ['x'];
var totalWeek = ['Total'];

function analyticStart(){
    generateWeeklyGraphic();
    generateHistoricGraphic();
}

function generateHistoricGraphic(){
    $.ajax({
        url: 'historicalData.json',
        type: 'GET',
        success: function(data) {
            for (i = 0; i < data.length; i++) {
                profit.push(data[i].profitAmount);
                invested.push(data[i].investedAmount);
                timeLine.push(data[i].year+"-"+data[i].month+"-"+1);
                profitForecast.push(data[i].paymentAmount);
            }
            c3.generate({
                bindto: '#historicGraphic',
                data: {
                    x: 'x',
                    columns: [
                        timeLine,
                        profit,
                        invested,
                        profitForecast
                    ]
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        tick: {
                            format: '%Y-%m'
                        }
                    }
                }
            });
        },
        error: function(xhr, status, error){
            var errorMessage = xhr.status + ': ' + xhr.statusText
            console.log('Error - ' + errorMessage);
        }
    });
}


function generateWeeklyGraphic(){
    $.ajax({
        url: 'weeklyData.json',
        type: 'GET',
        success: function(data) {
            for (i = 0; i < data.length; i++) {
                profitWeek.push(data[i].profitAmount);
                investedWeek.push(data[i].investedAmount);
                timeLineWeek.push(data[i].year+"-"+data[i].month+"-"+data[i].day);
                totalWeek.push(data[i].total);
            }
            console.log(timeLineWeek);
            c3.generate({
                bindto: '#weeklyGraphic',
                data: {
                    x: 'x',
                    columns: [
                        timeLineWeek,
                        profitWeek,
                        investedWeek,
                        totalWeek
                    ],
                    type: 'bar',
                    types: {
                        Total: 'line',
                    },
                    groups: [
                        ['Profit','Invested']
                    ]
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        tick: {
                            format: '%d-%m'
                        }
                    }
                }
            });
        },
        error: function(xhr, status, error){
            var errorMessage = xhr.status + ': ' + xhr.statusText
            console.log('Error - ' + errorMessage);
        }
    });
}