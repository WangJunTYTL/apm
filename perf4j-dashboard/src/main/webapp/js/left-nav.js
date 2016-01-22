// load left nav menu
$(function () {
    var clusterMap = JSON.parse($("#clusterMap").html());
    for (var key in clusterMap) {
        var leftNav = $("#left-nav");
        var li = $("<li>").attr("id", "currentCluster" + key);
        var a = $("<a>").html(key).attr("href", "?currentCluster=" + key);
        $(li).append(a);
        $(leftNav).append($(li));
        console.log("属性：" + key + ",值：" + clusterMap[key]);
    }

    var currentCluster = $("#currentCluster").html();
    $("#nav_history").attr("href","?currentCluster="+currentCluster+"&method=history");
    $("#nav_dashboard").attr("href","?currentCluster="+currentCluster);
    $("#nav_jvm").attr("href","?currentCluster="+currentCluster+"&method=jvm");
    $("#currentCluster" + currentCluster).attr("class", "active");

    $(".datetimepicker").datetimepicker();
});

// timing refresh page
$(function () {
    function refresh() {
        window.location.reload();
    }
    var refreshTime = $("#refresh").html();
    if (refreshTime == null) refreshTime = 30;
    setInterval(refresh, refreshTime * 1000);

});