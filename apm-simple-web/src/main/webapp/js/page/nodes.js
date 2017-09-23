$(function () {
    var loadClusterListPanel = function () {
        $.get("/conf/cluster", function (data) {
            var result = data;
            if (result.code == 0) {
                var nodes = result.data;
                var flag = false;
                for (var key in nodes) {
                    var panel = $('#nodesPanel');
                    panel.append($('<h3>').html(key));
                    var p = $("<p>");
                    for (var i = 0; i < nodes[key].length; i++) {
                        var node = nodes[key][i];
                        p.append($('<a>').attr("class", "btn btn-success").attr("href", "/?method=now&currentCluster=" + node['node_name']).html(node['node_name']));
                    }
                    panel.append(p);
                    flag = true;
                }
                if (!flag) {
                    $('#nodesPanel').append("<p>Perf4j Dashboard仅是一款针对per4j性能数据渲染的WEB项目，它通过http方式拉取perf4j的性能数据并借助Baidu的EChart项目渲染出各种直观的性能图表。</p>")
                    $('#nodesPanel').append('<p>当前你还没有添加任何机器请先添加perf4j数据的接口，请添加机器</p>');
                }
            } else {
                alert("获取机器列表失败！！！");
            }
        });


    }
    loadClusterListPanel();

    $.post("/conf/links/get", function (data) {
        var result = data;
        if (result.code == 0) {
            var menus = result.data;
            for (var i = 0; i < menus.length; i++) {
                $("#quickMenu").append($("<a class='btn btn-success'>").attr("href", menus[i]['url']).html(menus[i]['name']));
            }
        }
    });

    $("#modalConfirmMenu").click(function () {
        var cluster = $("#cluster").val();
        var node = $("#node").val();
        var url = $("#url").val();
        $.post("/setting/node/add", {clusterName: cluster, nodeName: node, url: url}, function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                alert("加入成功！");
            } else {
                alert("加入失败！！！");
            }
        });
    });


    $("#modalConfirmMenu2").click(function () {
        var name = $("#menuName").val();
        var url = $("#menuUrl").val();
        $.post("/conf/links/add", {name: name, url: url, location: 0}, function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                alert("加入成功！");
            } else {
                alert("加入失败！！！");
            }
        });
    });

    $("#modalConfirmMenu3").click(function () {
        var type = $(":radio:checked").val();
        var cluster, node, link;
        if (type == 0) {
            cluster = $("#removeCluster").val();
            node = $("#removeNode").val();
        } else {
            link = $("#removeLink").val();
        }
        $.post("/conf/delete", {clusterName: cluster, nodeName: node, linkName: link, type: type}, function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                alert("删除" + result.data + "条记录");
            } else {
                alert("删除失败！！！");
            }
        });
    });
})