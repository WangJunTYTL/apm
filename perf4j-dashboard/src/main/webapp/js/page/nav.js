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

    $(document).on('click', '.cluster_nav', function () {
        var name = $(this).html();
        window.location.href = "/?currentCluster=" + name;
    });

    $(document).on("click", ".top_nav_menu", function () {
        var method = $(this).attr("menu");
        window.location.href = "/?method=" + method + "&currentCluster=" + $("#currentCluster").html();
    })
});