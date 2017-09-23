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

<div class="container-fluid">
    <div class="row starter-template">
        <form class="form-inline" method="post" action="/dashboard">
            <div class="form-group">
                <label>Service</label>
                <select class="form-control" id="service" name="service">
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
            <div class="form-group">
                <label>Node</label>
                <select class="form-control" id="node" name="node">
                    <option value="">选择服务节点</option>
                    <c:forEach items="${nodes}" var="entry">
                        <c:choose>
                            <c:when test="${entry eq node}">
                                <option value="${entry}" selected>${entry}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry}">${entry}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Tag</label>
                <select class="form-control" id="Tag" name="tag">
                    <option value="">选择监控指标</option>
                    <c:forEach items="${tags}" var="entry">
                        <c:choose>
                            <c:when test="${entry eq tag}">
                                <option value="${entry}" selected>${entry}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry}">${entry}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="from">From</label>
                <input type="text" class="form-control datetimepicker" id="from"
                       name="from"
                       data-date-format="yyyy-mm-dd hh:ii" value="${from}"
                       placeholder="Last 1 hours">
            </div>
            <div class="form-group">
                <label for="to">To</label>
                <input type="text" class="form-control datetimepicker" id="to"
                       name="to"
                       data-date-format="yyyy-mm-dd hh:ii" value="${to}" placeholder="Now">
            </div>
            <button type="button" class="btn btn-default" id="submit">Submit</button>
        </form>
    </div>
    <div id="typeContainer" class="text-left"></div>

    <hr>
    <div class="row">
        <!-- /.box-header -->
        <div class="box-body table-responsive no-padding" id="chart">

        </div>
        <!-- /.box-body -->
    </div>
</div>
<!-- /.container -->
<script src="/js/page/dashboard.js"></script>
<script>
    $(function () {

        // 服务节点指标选择
        $("#service").change(function () {
            var service = $(this).val();
            if (service == "") {
                alert("请选择需要查看的服务");
                exit(1);
            }
            $.post("/dashboard/service/node", {"service": service}, function (data) {
                if (data.code == 0) {
                    var nodes = data.data['nodes'];
                    var tags = data.data['tags'];
                    if (nodes.length == 0) {
                        alert("在该服务下没有发现可用节点")
                        exit(1);
                    }
                    $("#node").children().remove("option");
                    $("#Tag").children().remove("option");
                    for (var i = 0; i < nodes.length; i++) {
                        var option = $("<option>").attr("value", nodes[i]).html(nodes[i]);
                        $("#node").append(option);
                    }
                    for (var i = 0; i < tags.length; i++) {
                        var option = $("<option>").attr("value", tags[i]).html(tags[i]);
                        $("#Tag").append(option);
                    }
                } else {
                    alert(data.toString());
                }
            });
        });

        $("option[value='']").attr("selected", "true");
    });

    $('.datetimepicker').datetimepicker(
        {
            format: 'yyyy-mm-dd hh:ii',
            minView: 0,
            maxView: 3
        });

</script>
<jsp:include page="/template/01/pageFooter.jsp"></jsp:include>

