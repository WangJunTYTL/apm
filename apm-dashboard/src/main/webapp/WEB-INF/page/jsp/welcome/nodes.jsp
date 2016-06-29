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
    <%--<section class="content-header">--%>
    <%--<h1>--%>
    <%--Home--%>
    <%----%>
    <%--</h1>--%>
    <%--<ol class="breadcrumb">--%>
    <%--<li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>--%>
    <%--</ol>--%>
    <%--</section>--%>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="container-fluid">
                <div class="col-lg-12" id="nodesPanel">
                    <h3>Links</h3>
                    <p id="quickMenu">
                        <a class="btn btn-success" data-toggle='modal' data-target='#myModal'>添加机器</a>
                        <a class="btn btn-success" data-toggle='modal' data-target='#myModal2'>添加链接</a>
                        <a class="btn btn-success" data-toggle='modal' data-target='#myModal3'>移除节点或链接</a>
                    </p>
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
                <button type="button" class="btn btn-success" id="modalConfirmMenu">Submit</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">创建快捷链接</h4>
            </div>
            <div class="modal-body" id="myModalBody2">
                <form action="/conf/clsuter/add" method="post">
                    <div class="form-group">
                        <label for="cluster">名称</label>
                        <input type="text" class="form-control" id="menuName" placeholder="连接名称">
                    </div>
                    <div class="form-group">
                        <label for="url">url</label>
                        <input type="text" class="form-control" id="menuUrl" placeholder="url">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-success" id="modalConfirmMenu2">Submit</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModal3" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">移除节点或链接</h4>
            </div>
            <div class="modal-body">
                <form method="post">

                    <div class="radio">
                        <label>
                            <input type="radio" name="optionsRadios" id="optionsRadios02" value="0" checked>
                            移除下面节点
                        </label>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" id="removeCluster" placeholder="cluster">
                        <input type="text" class="form-control" id="removeNode" placeholder="node">
                    </div>
                    <div class="radio">
                        <label>
                            <input type="radio" name="optionsRadios" id="optionsRadios03" value="1">
                            移除下面链接
                        </label>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" id="removeLink" placeholder="link">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-success" id="modalConfirmMenu3">Submit</button>
            </div>
        </div>
    </div>
</div>

<script src="/js/page/nodes.js"></script>
<jsp:include page="../../../../template/02/pageFooter.jsp"></jsp:include>

