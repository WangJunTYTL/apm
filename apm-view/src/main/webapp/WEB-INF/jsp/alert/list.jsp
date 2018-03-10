<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="/template/01/pageHeader.jsp"></jsp:include>


<!-- Content Wrapper. Contains page content -->

        <div class="container">

            <div class="row starter-left">
                <form class="form-inline" method="get" action="/alert/list">
                    <div class="form-group">
                        <select class="form-control" name="service">
                            <option value="">选择服务</option>
                            <c:forEach items="${services}" var="entry">
                                <c:choose>
                                    <c:when test="${entry eq service}">
                                        <option value="${entry}" selected>${entry}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${entry}">${entry}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-success" href="/alert/list">预警列表</button>
                    <a class="btn btn-default" href="/alert/build">新建预警</a>
                </form>
            </div>
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
                                    <th>监控Tag</th>
                                    <th>预警规则</th>
                                    <th>预警间隔</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                <c:forEach items="${alertList}" var="alert">
                                    <tr>
                                        <td>${alert.tag}</td>
                                        <td>${alert.term}</td>
                                        <td>${alert.interval}min</td>
                                        <td><span
                                                class="label ${alert.status == 0?"label-success":"label-danger"}">${alert.status == 0?"Running":"Stop"}</span>
                                        </td>
                                        <td><a class="text-green" href="/alert/build/update?id=${alert.id}">
                                            编辑</a>&nbsp;&nbsp;&nbsp;
                                            <c:choose>
                                                <c:when test="${alert.status == 0}">
                                                    <a class="text-orange stopNode" href="javascript:void(0)" alertId="${alert.id}">关闭</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="text-olive playNode" href="javascript:void(0)" alertId="${alert.id}">打开</a>
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

        </div>
<script src="/js/page/list.js"></script>
<jsp:include page="/template/01/pageFooter.jsp"></jsp:include>

