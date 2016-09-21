$(function () {
    var service = $("#service").val();
    if (service == "") {
        alert("No Available Service");
    } else {
        $.post("/dashboard/service/node", {"service": service}, function (data) {
            if (data.code == 0) {
                var tags = data.data;
                for (tag in tags) {
                    $("#tag").append($("<option>").html(tags[tag]).attr("value", tags[tag]));
                }
            } else {
                alert("No Available Tag");
            }
        });
    }
    $("#service").on("change", function () {
        var service = $("#service").val();
        $.post("/dashboard/service/node", {"service": service}, function (data) {
            if (data.code == 0) {
                $("#tag").children().remove();
                var tags = data.data;
                for (tag in tags) {
                    $("#tag").append($("<option>").html(tags[tag]).attr("value", tags[tag]));
                }
            } else {
                alert("No Available Tag");
            }
        });
    });

    $("#submit").click(function () {

        var service = $("#service").val();
        var tag = $("#tag").val();
        var term = $("#term").val();
        var interval = $("#interval").val();
        var smsMsg = $("#smsMsg").val();
        var subject = $("#subject").val();
        var mailMsg = $("#mailMsg").val();
        var receivers = [];
        $(":checkbox").each(function () {
            if ($(this).is(":checked")) {
                receivers.push($(this).attr("value"));
            }
        });

        $.post("/alert/build/post",
            {
                "service": service,
                "tag": tag,
                "term": term,
                "interval": interval,
                "smsMsg": smsMsg,
                "subject": subject,
                "mailMsg": mailMsg,
                "receivers": receivers
            },
            function (data) {
                alert(data.data);
            }
        );
    });
});
