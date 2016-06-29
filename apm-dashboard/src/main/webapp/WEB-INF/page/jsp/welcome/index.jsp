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


<script src="/js/echart/echarts.js"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Dashboard
            <small>${currentCluster}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">Dashboard</li>
        </ol>
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="container-fluid">
                <div id="chart">
                    <h5>无法获取数据</h5>
                </div>
            </div>
        </div>
        <data value="" style="display: none" id="runningInfo">${runningInfo}</data>
        <data value="" style="display: none" id="clusterMap">${clusterMap}</data>
        <data value="" style="display: none" id="refresh">${refresh}</data>
        <script src="/js/index-graph.js"></script>

    </section>
</div>
<jsp:include page="../../../../template/02/pageFooter.jsp"></jsp:include>

