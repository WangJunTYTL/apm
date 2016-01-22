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

<div class="row placeholders">
    <div class="text-center">
        <form class="form-inline">
            <div class="form-group">
                <label for="exampleInputName2">From</label>
                <input type="text" class="form-control datetimepicker" id="exampleInputName2" name="from" data-date-format="yyyy-mm-dd hh:ii" value="${from}">
            </div>
            <div class="form-group">
                <label for="exampleInputEmail2">To</label>
                <input type="text" class="form-control datetimepicker" id="exampleInputEmail2" name="to" data-date-format="yyyy-mm-dd hh:ii" value="${to}">
            </div>
            <input type="hidden" class="form-control"  name="method" value="jvm" >
            <input type="hidden" class="form-control"  name="currentCluster" value="${currentCluster}" >
            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </div>
</div>


<div id="chart">
    <h3 class="page-header">JVM</h3>
    <div>
        <div class="col-lg-6">
            <%--<h5>Heap</h5>--%>
            <div id="UsedHeap" style="height: 300px"></div>
        </div>
        <div class="col-lg-6">

            <%--<h5>NonHeap</h5>--%>
            <div id="UsedNonHeap" style="height: 300px"></div>
        </div>
    </div>
    <div>
        <h3>Thread</h3>
        <div class="col-lg-12">
            <div id="Thread" style="height: 300px;"></div>
        </div>
    </div>
    <div>
        <h3 class="page-header">GC</h3>
        <div class="col-lg-6">
            <%--<h5>Count</h5>--%>
            <div id="GCCount" style="height: 300px"></div>
        </div>
        <div class="col-lg-6">
            <%--<h5>Time</h5>--%>
            <div id="GCTime" style="height: 300px"></div>
        </div>
    </div>

</div>
<div>
    <div class="text-center text-muted" id="hostname"></div>
</div>
<data value="" style="display: none" id="runningInfo">${runningInfo}</data>
<data value="" style="display: none" id="clusterMap">${clusterMap}</data>
<data value="" style="display: none" id="currentCluster">${currentCluster}</data>
<data value="" style="display: none" id="refresh">${refresh}</data>
<%--<div class="col-xs-12"><data value="" style="" id="runningInfo">${runningInfo}</data></div>--%>

<script src="/js/jvm-graph.js"></script>
<jsp:include page="../../../../template/pageFooter.jsp"></jsp:include>

