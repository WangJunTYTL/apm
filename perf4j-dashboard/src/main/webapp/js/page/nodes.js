$(function () {
    var loadClusterListPanel = function () {
        $.post("/conf/cluster", function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                var nodes = JSON.parse(result.result);
                var flag = false;
                for (var key in nodes) {
                    var panel = $('#nodesPanel');
                    panel.append($('<h3>').html(key));
                    var table = $('<table class="table">');
                    var p = $("<p>");
                    for (var i = 0; i < nodes[key].length; i++) {
                        var node = nodes[key][i];
                        p.append($('<a>').attr("class", "btn btn-success").attr("href", "/?method=now&currentCluster=" + node['node_name']).html(node['node_name']));
                    }
                    panel.append(p);
                    flag = true;
                }
                if (!flag) {
                    $('#nodesPanel').append('<a class="text-danger" href="/profile">还没有任何机器，点击添加</a>');
                }
            } else {
                alert("获取机器列表失败！！！");
            }
        });
    }
    loadClusterListPanel();
})