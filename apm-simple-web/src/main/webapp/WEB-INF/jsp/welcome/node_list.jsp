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
                Node List
                <small>display all node info</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Setting</a></li>
                <li class="active">node</li>
            </ol>
        </section>

        <section class="content">
            <!-- /.row -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title"><a class="btn btn-success" id="insertNode">insert
                                new node</a></h3>
                            <div class="box-tools">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" name="table_search" class="form-control pull-right"
                                           placeholder="Search">
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <table class="table table-hover">
                                <tr>
                                    <th>ID</th>
                                    <th>ServiceName</th>
                                    <th>NodeName</th>
                                    <th>Status</th>
                                    <th>Url</th>
                                    <th>Operation</th>
                                </tr>
                                <c:forEach items="${nodes}" var="node">
                                    <tr>
                                        <td>${node.id}</td>
                                        <td>${node.serviceName}</td>
                                        <td>${node.nodeName}</td>
                                        <td><span class="label label-success">Running</span></td>
                                        <td>${node.url}</td>
                                        <td><a class="btn btn-success updateNode">udpate</a>&nbsp;&nbsp;&nbsp;<a class="btn btn-danger delNode">delete</a></td>
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
                                    <label for="service">ServiceName</label>
                                    <input type="text" class="form-control" id="service" placeholder="cluster">
                                    <input type="hidden" class="form-control" id="id" placeholder="cluster">
                                </div>
                                <div class="form-group">
                                    <label for="node">NodeName</label>
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
<script src="/js/page/node_list.js"></script>
<jsp:include page="/template/02/pageFooter.jsp"></jsp:include>

