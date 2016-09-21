$(function () {
    $("#modalConfirmMenu").click(function () {
        var cluster = $("#service").val();
        var node = $("#node").val();
        var url = $("#url").val();
        var id = $("#id").val();
        $.post("/setting/node/add", {id: id, serviceName: cluster, nodeName: node, url: url}, function (data) {
            alert(data.data);
            window.location.reload();
        });
    });

});

$(function () {

    $("#insertNode").click(function () {
        $("#myModalLabel").html("Add New Node");
        $("#modalConfirmMenu").html("Submit");
        $("#id").val(0);
        $("#myModal").modal('show');
    });


    $(".updateNode").click(function () {
        var tr = $(this).parent().parent().children();
        var id = $(tr.get(0)).html();
        var service = $(tr.get(1)).html();
        var node = $(tr.get(2)).html();
        var url = $(tr.get(4)).html();

        $("#myModal").modal('show');
        $("#service").val(service);
        $("#node").val(node);
        $("#url").val(url);
        $("#id").val(id);

        $("#myModalLabel").html("Update The Node")
        $("#modalConfirmMenu").html("Update")
    });

    $(".delNode").click(function () {
        var tr = $(this).parent().parent().children();
        var id = $(tr.get(0)).html();
        $.post("/setting/node/del", {id: id}, function (data) {
            alert(data.data);
            window.location.reload();
        });
    });
});
