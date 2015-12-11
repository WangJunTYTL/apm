<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/nav.jsp"></jsp:include>

<%--<script src="//cdn.bootcss.com/holder/2.8.2/holder.js"></script>--%>
<div class="row placeholders">
</div>
<div id="chart">
    <h5>无法获取数据</h5>
</div>


<data value="" style="display: none" id="runningInfo">${runningInfo}</data>
<data value="" style="display: none" id="clusterMap">${clusterMap}</data>
<data value="" style="display: none" id="currentCluster">${currentCluster}</data>
<data value="" style="display: none" id="refresh">${refresh}</data>
<%--<div class="col-xs-12"><data value="" style="" id="runningInfo">${runningInfo}</data></div>--%>
<script src="/js/index-graph.js"></script>
<script>
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
        $("#currentCluster" + currentCluster).attr("class", "active");
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

</script>


<jsp:include page="../../../../template/pageFooter.jsp"></jsp:include>

