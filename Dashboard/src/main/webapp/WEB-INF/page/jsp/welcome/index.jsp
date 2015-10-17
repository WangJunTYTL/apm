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
<script src="/js/holder.js"></script>
<script src="/js/Chart.js"></script>
<h1 class="page-header">Dashboard</h1>

<div class="row placeholders">
    <c:forEach items="${focusedTaskData}" var="task">
        <div class="col-xs-4 col-md-3 col-lg-2 placeholder">
            <c:choose>
            <c:when test="${task.remain < 666}">
                <img data-src="holder.js/66x66?auto=yes&theme=vine&text=${task.remain}" class="img-responsive"
            </c:when>
            <c:otherwise>
            <img data-src="holder.js/66x66?auto=yes&theme=lava&text=${task.remain}" class="img-responsive"
            </c:otherwise>
            </c:choose>
                 alt="Generic placeholder thumbnail">
            <h5>${task.id}</h5>
            <span class="text-muted"></span>

            <div id="chartGraph_${task.id}" style="display: none">${task.chartDataJson}</div>
        </div>
    </c:forEach>

    <c:forEach items="${firstFlexibleTaskData}" var="task">
        <div class="col-xs-4 col-md-3 col-lg-2 placeholder">
            <c:choose>
            <c:when test="${task.remain < 666}">
                <img data-src="holder.js/66x66?auto=yes&theme=vine&text=${task.remain}" class="img-responsive"
            </c:when>
            <c:otherwise>
            <img data-src="holder.js/66x66?auto=yes&theme=lava&text=${task.remain}" class="img-responsive"
            </c:otherwise>
            </c:choose>
                 alt="Generic placeholder thumbnail">
            <h5>${task.id}</h5>
            <span class="text-muted"></span>

            <div id="chartGraph_${task.id}" style="display: none">${task.chartDataJson}</div>
        </div>
    </c:forEach>
</div>

<h2 class="sub-header">TPS</h2>

<div class="container-fluid" id="graph_canvas">

    <%--<div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart2" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart3" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart4" height="400"></canvas>
        </div>
    </div>--%>
</div>

<h2 class="sub-header">Tasks Analysis</h2>

<div class="table-responsive">
    <table class="table table-striped" id="flexible-task">
        <thead>
        <tr>
            <th>批次</th>
            <%--<th>描述</th>--%>
            <th>积压数</th>
            <th>提交数</th>
            <th>实时生产速率</th>
            <th>实时消费速率</th>
            <th>开始时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${focusedTaskData}" var="task">

            <tr>
                <td>${task.id}</td>
                <%--<td>${task.desc}</td>--%>
                <td>${task.remain}</td>
                <td>${task.total}</td>
                <td>${task.produceRate}/s</td>
                <td>${task.consumeRate}/s</td>
                <td>${task.createTime}</td>
            </tr>
        </c:forEach>
        <c:forEach items="${firstFlexibleTaskData}" var="task">

            <tr>
                <td>${task.id}</td>
                <%--<td>${task.desc}</td>--%>
                <td>${task.remain}</td>
                <td>${task.total}</td>
                <td>${task.produceRate}/s</td>
                <td>${task.consumeRate}/s</td>
                <td>${task.createTime}</td>
            </tr>
        </c:forEach>
        <c:forEach items="${secondFlexibleTaskBeanSet}" var="task">
            <tr>
                <td>${task.id}</td>
                <%--<td>${task.desc}</td>--%>
                <td>${task.remain}</td>
                <td>${task.total}</td>
                <td>${task.produceRate}/s</td>
                <td>${task.consumeRate}/s</td>
                <td>${task.createTime}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<data value="" style="display: none" id="clusterMap">${clusterMap}</data>
<data value="" style="display: none" id="currentCluster">${currentCluster}</data>
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

        $("#currentCluster" + currentCluster).attr("class", "active");
    });

    // timing refresh page
    $(function () {
        function refresh() {
            window.location.reload();
        }

        setInterval(refresh, ${refresh} * 1000
        )
        ;

    });

</script>

<jsp:include page="../../../../template/pageFooter.jsp"></jsp:include>

