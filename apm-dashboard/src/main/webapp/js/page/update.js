$(function () {
    $("#submit").click(function () {

        var id = $("#id").val();
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
                "id": id,
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
