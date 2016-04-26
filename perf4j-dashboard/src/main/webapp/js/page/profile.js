$(function () {
    $("#modalConfirmMenu").click(function () {
        var cluster = $("#cluster").val();
        var node = $("#node").val();
        var url = $("#url").val();
        $.post("/conf/cluster/add", {clusterName: cluster, nodeName: node, url: url}, function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                alert("加入成功！");
            } else {
                alert("加入失败！！！");
            }
        });
    });

    var loadClusterListPanel = function () {
        $.post("/conf/cluster", function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                var nodes = JSON.parse(result.result);
                for (var key in nodes) {
                    var panel = $('<div class="col-lg-2">');
                    var body = $('<div class="panel-body">');
                    panel.append($('<div class="panel panel-info">').append($('<div class="panel-heading">').html(key)).append(body));
                    var table = $('<table class="table">');
                    for (var i = 0; i < nodes[key].length; i++) {
                        var node = nodes[key][i];
                        var tr = $("<tr>")
                        tr.append($('<td>').append($('<a>').attr("class", "btn").attr("href", "/?currentCluster=" + node['node_name']).html(node['node_name'])));
                        tr.append($('<td>').append($('<a>').attr("class", "btn removeNodeMenu").attr("href", "javascript:void(0)").attr("clusterName", node['cluster_name']).attr("nodeName", node['node_name']).html("删除")));
                        table.append(tr);
                    }
                    body.append(table);
                    $("#clusterPanel").append(panel);
                }
            } else {
                alert("获取机器列表失败！！！");
            }
        });
    }
    loadClusterListPanel();
    $(document).on('click','.removeNodeMenu',function () {
        var cluster = $(this).attr("clusterName");
        var node = $(this).attr("nodeName");
        var menu = $(this);
        $.post("/conf/cluster/delete",{clusterName:cluster,nodeName:node},function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                alert("删除成功！");
                menu.parent().parent().remove();
            } else {
                alert("删除失败！！！");
            }
        });
    })
});