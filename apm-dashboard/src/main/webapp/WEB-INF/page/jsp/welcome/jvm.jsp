<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/02/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/02/nav.jsp"></jsp:include>
<link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/echart/echarts.js"></script>


<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            JVM
            <small>${currentCluster}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">JVM</li>
        </ol>
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="container-fluid">
                <div class="text-center col-lg-12">
                    <form class="form-inline">
                        <div class="form-group">
                            <label for="exampleInputName2">From</label>
                            <input type="text" class="form-control datetimepicker" id="exampleInputName2" name="from"
                                   data-date-format="yyyy-mm-dd hh:ii" value="${from}" placeholder="Last 2 hours">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail2">To</label>
                            <input type="text" class="form-control datetimepicker" id="exampleInputEmail2" name="to"
                                   data-date-format="yyyy-mm-dd hh:ii" value="${to}" placeholder="Now">
                        </div>
                        <input type="hidden" class="form-control" name="method" value="jvm">
                        <input type="hidden" class="form-control" name="currentCluster" value="${currentCluster}">
                        <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </div>
                <data value="" style="display: none" id="runningInfo">${runningInfo}</data>
            </div>
        </div>
        <div id="chart" class="row">
            <div class="col-lg-6">
                <div id="UsedHeap" style="height: 300px"></div>
            </div>
            <div class="col-lg-6">
                <div id="UsedNonHeap" style="height: 300px"></div>
            </div>
            <div class="col-lg-12">
                <div id="Thread" style="height: 300px;"></div>
            </div>
            <div class="col-lg-6">
                <div id="GCCount" style="height: 300px"></div>
            </div>
            <div class="col-lg-6">
                <div id="GCTime" style="height: 300px"></div>
            </div>
        </div>
        <div class="row">
            <div class="text-center text-muted" id="hostname"></div>
        </div>
    </section>
</div>

<script src="/js/jvm-graph.js"></script>
<script>
    $('.datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii',
        minView: 0,
        maxView: 3
    });
</script>
<jsp:include page="../../../../template/02/pageFooter.jsp"></jsp:include>

