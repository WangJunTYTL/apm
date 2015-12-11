<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/12/11
  Time: 上午10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/nav.jsp"></jsp:include>

<%--<script src="//cdn.bootcss.com/holder/2.8.2/holder.js"></script>--%>
<div class="row placeholders">
    <div class="text-center">
        <form class="form-inline">
            <div class="form-group">
                <label for="tag">Tag</label>
                <select class="form-control" id="tag" name="tag">
                </select>
            </div>
            <div class="form-group">
                <label for="exampleInputName2">From</label>
                <input type="text" class="form-control datetimepicker" id="exampleInputName2" name="from" data-date-format="yyyy-mm-dd hh:ii" value="${from}">
            </div>
            <div class="form-group">
                <label for="exampleInputEmail2">To</label>
                <input type="text" class="form-control datetimepicker" id="exampleInputEmail2" name="to" data-date-format="yyyy-mm-dd hh:ii" value="${to}">
            </div>
            <input type="hidden" class="form-control"  name="method" value="history" >
            <input type="hidden" class="form-control"  name="currentCluster" value="${currentCluster}" >
            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </div>
</div>

<div id="chart">
    <h5>无法获取数据</h5>
</div>


<data value="" style="display: none" id="runningInfo">${runningInfo}</data>
<data value="" style="display: none" id="clusterMap">${clusterMap}</data>
<data value="" style="display: none" id="currentCluster">${currentCluster}</data>
<data value="" style="display: none" id="currentTag">${tag}</data>

<script src="/js/history-graph.js"></script>

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
        $("#nav_history").attr("href", "?currentCluster=" + currentCluster + "&method=history");
        $("#nav_dashboard").attr("href", "?currentCluster=" + currentCluster);
        $("#currentCluster" + currentCluster).attr("class", "active");

        $(".datetimepicker").datetimepicker();
    });

</script>


<jsp:include page="../../../../template/pageFooter.jsp"></jsp:include>

