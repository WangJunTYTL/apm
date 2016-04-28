$(function () {
    $.post("/conf/cluster", function (data) {
        var result = JSON.parse(data);
        var currentCluster = $("#currentCluster").html();
        var clusterNodes = JSON.parse(result.result);
        for (key in clusterNodes) {
            var menu = $('<li class="treeview">');
            menu.append($('<a href="#">').append($('<i class="fa fa-circle-o text-primary">')).append($('<span>').html(key)));
            var nodes = clusterNodes[key];
            var ul = $('<ul class="treeview-menu">');
            for (var i = 0; i < nodes.length; i++) {
                if (currentCluster == key) {
                    ul.append($("<li class='active'>").append($("<a class='cluster_nav' href='javascript:void(0)'>").attr("class", "active").append($('<i class="fa fa-circle-o"></i>')).html(nodes[i]['node_name'])));
                } else {
                    ul.append($("<li>").append($("<a class='cluster_nav' href='javascript:void(0)'>").append($('<i class="fa fa-circle-o"></i>')).html(nodes[i]['node_name'])));
                }
            }
            menu.append(ul);
            $("#cluster_list").append(menu);

        }
    });

    // 获取当前窗口url中param参数的值
    function get_param(param) {
        var query = location.search.substring(1).split('&');
        for (var i = 0; i < query.length; i++) {
            var kv = query[i].split('=');
            if (kv[0] == param) {
                return kv[1];
            }
        }
        return null;
    }

    // 设置当前窗口url中param的值
    function set_param(param, value) {
        var query = location.search.substring(1);
        var p = new RegExp("(^|&" + param + ")=[^&]*");
        if (p.test(query)) {
            query = query.replace(p, "$1=" + value);
            location.search = '?' + query;
        } else {
            if (query == '') {
                location.search = '?' + param + '=' + value;
            } else {
                location.search = '?' + query + '&' + param + '=' + value;
            }
        }
    }

    $(document).on('click', '.cluster_nav', function () {
        var url = window.location.href;
        var currentCluster = $(this).html();
        var method = get_param("method");
        if (method == null) {
            url = "/?method=now&currentCluster=" + currentCluster;
            window.location.href = url;
        } else {
            set_param("currentCluster", currentCluster);
        }
    });

    $(document).on("click", ".top_nav_menu", function () {
        var method = $(this).attr("menu");
        window.location.href = "/?method=" + method + "&currentCluster=" + $("#currentCluster").html();
    })
});