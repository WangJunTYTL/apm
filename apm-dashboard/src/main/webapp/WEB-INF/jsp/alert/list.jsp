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

<!-- Content Wrapper. Contains page content -->
<div id="main-content">
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Alert List
                <small>display all alert info</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Alert</a></li>
                <li class="active">List</li>
            </ol>
        </section>

        <section class="content">
            <!-- /.row -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover">
                                <tr>
                                    <th>ID</th>
                                    <th>Service</th>
                                    <th>Tag</th>
                                    <th>Term</th>
                                    <th>Interval</th>
                                    <th>Status</th>
                                    <th>Operation</th>
                                </tr>
                                <c:forEach items="${alertList}" var="alert">
                                    <tr>
                                        <td>${alert.id}</td>
                                        <td>${alert.service}</td>
                                        <td>${alert.tag}</td>
                                        <td>${alert.term}</td>
                                        <td>${alert.interval}min</td>
                                        <td><span
                                                class="label ${alert.status == 0?"label-success":"label-danger"}">${alert.status == 0?"Running":"Stop"}</span>
                                        </td>
                                        <td><a class="text-green" href="/alert/build/update?id=${alert.id}">
                                            <i class="fa fa-edit"></i></a>&nbsp;&nbsp;&nbsp;
                                            <c:choose>
                                                <c:when test="${alert.status == 0}">
                                                    <a class="text-orange stopNode" href="javascript:void(0)" alertId="${alert.id}"><i
                                                            class="fa fa-pause"></i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="text-olive playNode" href="javascript:void(0)" alertId="${alert.id}"><i
                                                            class="fa fa-play"></i></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
            </div>

            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Add New Node</h4>
                        </div>
                        <div class="modal-body" id="myModalBody">
                            <form action="/conf/clsuter/add" method="post">
                                <div class="form-group">
                                    <label for="service">Term</label>
                                    <input type="text" class="form-control" id="service" placeholder="cluster">
                                    <input type="hidden" class="form-control" id="id" placeholder="cluster">
                                </div>
                                <div class="form-group">
                                    <label for="node">Sms Message</label>
                                    <input type="text" class="form-control" id="node" placeholder="node">
                                </div>
                                <div class="form-group">
                                    <label for="url">Url</label>
                                    <input type="text" class="form-control" id="url" placeholder="url">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-success" id="modalConfirmMenu">Submit</button>
                        </div>
                    </div>
                </div>
            </div>

        </section>
    </div>
</div>
<script src="/js/page/list.js"></script>
<jsp:include page="/template/02/pageFooter.jsp"></jsp:include>

