$(function () {
    // load node list to nav menu
    $.post("/dashboard/node/list", function (data) {
        var result = data;
        var clusterNodes = result.data;
        for (key in clusterNodes) {
            var service_name = $("<li>");
            var service_name_a = $('<a href="#">').html('<i class="fa fa-circle-o"></i>' + key).attr({
                "id": key,
                "service": key
            }).addClass("cluster_nav left_nav_menu");
            service_name.append(service_name_a);
            $("#service_list").append(service_name);
        }
        fount_last_click();
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

    function fount_last_click() {
        var last_click = Cookies.get("apm.dashboard.last.click.menu");
        // alert(last_click);
        if (last_click != null && last_click !== undefined) {
            var parent = $("#" + last_click).parent();
            var i = 0;
            while (parent != null && parent.attr("class") != "sidebar-menu" && parent[0] !== undefined && i < 8) {
                i++;
                parent.addClass("active");
                // console.log(last_click+"_"+parent.attr("class"));
                parent = parent.parent();
            }
        }
    }

    $(document).on('click', '.cluster_nav', function () {
        var service = $(this).attr("service");
        url = "/dashboard?service=" + service;
        window.location.href = url;
    });

    /* $(document).on("click", ".top_nav_menu", function () {
     var method = $(this).attr("menu");
     var url = "/?method=" + method + "&node=" + $("#currentCluster").html();
     $("#main-content").load(url + " .content-wrapper");
     })*/

    $(document).on('click', '.left_nav_menu', function () {
        Cookies.set("apm.dashboard.last.click.menu", $(this).attr("id"), {path: "/"});
    });
});


