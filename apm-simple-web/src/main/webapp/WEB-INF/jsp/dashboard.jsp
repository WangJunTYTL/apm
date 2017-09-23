<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="/template/02/pageHeader.jsp"></jsp:include>
<jsp:include page="/template/02/nav.jsp"></jsp:include>


<script src="/js/echart/echarts.js"></script>
<link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/date-util.js"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Dashboard
            <small> ${service}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
            <li><a href="#">Dashboard</a></li>
        </ol>
    </section>

    <section class="content">
        <!-- /.row -->
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-header">
                        <h3 class="box-title">
                            <form class="form-inline">
                                <div class="form-group">
                                    <label for="tag">Host</label>
                                    <select class="form-control" id="hostname" name="node">
                                        <c:forEach items="${nodes}" var="node">
                                            <option>${node}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="tag">Tag</label>
                                    <select class="form-control" id="tag" name="tag">
                                        <c:forEach items="${tags}" var="tag">
                                            <option>${tag}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputName2">From</label>
                                    <input type="text" class="form-control datetimepicker" id="exampleInputName2" name="from"
                                           data-date-format="yyyy-mm-dd hh:ii" value="${from}" placeholder="Last 2 hours">
                                    <input type="hidden" name="service" value="${service}">
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputEmail2">To</label>
                                    <input type="text" class="form-control datetimepicker" id="exampleInputEmail2" name="to"
                                           data-date-format="yyyy-mm-dd hh:ii" value="${to}" placeholder="Now">
                                </div>
                                <button type="button" class="btn btn-default" id="submit">Submit</button>
                            </form>
                        </h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body table-responsive no-padding" id="chart">

                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
        </div>
    </section>
</div>
<script src="/js/page/dashboard.js"></script>
<script>
    $(function () {
        $('.datetimepicker').datetimepicker({
            format: 'yyyy-mm-dd hh:ii',
            minView: 0,
            maxView:3
        });
    })
</script>
<jsp:include page="/template/02/pageFooter.jsp"></jsp:include>

