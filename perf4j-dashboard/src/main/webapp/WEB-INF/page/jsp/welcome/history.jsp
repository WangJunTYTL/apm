<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/12/11
  Time: 上午10:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/02/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/02/nav.jsp"></jsp:include>
<link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/echart/echarts.js"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            History
            <small>${currentCluster}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">History</li>
        </ol>
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="container-fluid">
                <div class="text-center col-lg-12">
                    <form class="form-inline">
                        <div class="form-group">
                            <label for="tag">Tag</label>
                            <select class="form-control" id="tag" name="tag">
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="exampleInputName2">From</label>
                            <input type="text" class="form-control datetimepicker" id="exampleInputName2" name="from"
                                   data-date-format="yyyy-mm-dd hh:ii" value="${from}">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail2">To</label>
                            <input type="text" class="form-control datetimepicker" id="exampleInputEmail2" name="to"
                                   data-date-format="yyyy-mm-dd hh:ii" value="${to}">
                        </div>
                        <input type="hidden" class="form-control" name="method" value="history">
                        <input type="hidden" class="form-control" name="currentCluster" value="${currentCluster}">
                        <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </div>
                <div class="col-lg-12" id="chart">

                </div>
            </div>
        </div>
        <data value="" style="display: none" id="runningInfo">${runningInfo}</data>
        <data value="" style="display: none" id="currentTag">${tag}</data>
    </section>
</div>
<script src="/js/history-graph.js"></script>
<script>
    $('.datetimepicker').datetimepicker({
        format: 'yyyy-mm-dd hh:ii'
    });
</script>
<jsp:include page="../../../../template/02/pageFooter.jsp"></jsp:include>

