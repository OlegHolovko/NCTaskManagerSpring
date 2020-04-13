var stompClient = null;

function connect() {
    if (!stompClient){
        var socket = new SockJS('/task-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/schedule/task', function (taskAlert) {
                showAlert(JSON.parse(taskAlert.body));
            });
        });
    }
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showAlert(message) {
    var i = 0;
    var alerts = '';
    $.each(message, function(index, value) {
        i++;
        if(i==1) var active = 'active';
        else var active = '';
        if (!$("#item"+index).length) {
            $("#ws-alerts").append(`
            <div id="item` + index + `" class="carousel-item ` + active + ` sufee-alert alert with-close alert-info alert-dismissible fade show mt-3">
                <span class="badge badge-pill badge-info">Внимание!</span>
                ` + value + `
                <!--<button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>-->
            </div>
                `);
        }

    });
    $('.carousel').carousel();
}

$(function () {
    disconnect();
    connect();
    if($("div").is("#ws-alerts")){
        var interval = setInterval(connect, 50000);
    }
});