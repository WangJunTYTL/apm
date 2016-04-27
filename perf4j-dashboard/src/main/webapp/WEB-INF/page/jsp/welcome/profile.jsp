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
            Profile
            <small>${currentCluster}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">Profile</li>
        </ol>
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="container-fluid" id="clusterPanel">
                <div class="col-lg-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">快捷菜单</div>
                        <div class="panel-body">
                            <a href='javascript:void(0)' class='btn btn-primary' data-toggle='modal' data-target='#myModal'>添加机器</a>
                            <a href='/' class='btn btn-primary'>机器索引</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加机器</h4>
            </div>
            <div class="modal-body" id="myModalBody">
                <form action="/conf/clsuter/add" method="post">
                    <div class="form-group">
                        <label for="cluster">所属集群</label>
                        <input type="text" class="form-control" id="cluster" placeholder="cluster">
                    </div>
                    <div class="form-group">
                        <label for="node">节点名称</label>
                        <input type="text" class="form-control" id="node" placeholder="node">
                    </div>
                    <div class="form-group">
                        <label for="url">url</label>
                        <input type="text" class="form-control" id="url" placeholder="url">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="modalConfirmMenu">Submit</button>
            </div>
        </div>
    </div>
</div>
<script src="/js/page/profile.js"></script>
<jsp:include page="../../../../template/02/pageFooter.jsp"></jsp:include>

