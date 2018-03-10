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

<script src="/js/Chart.js"></script>
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
            <%--<div class="container-fluid">--%>
            <div class="col-lg-12">
                <canvas id="myChart" class="col-md-6" height="100"></canvas>
                <canvas id="myChart02" class="col-md-6" height="100"></canvas>
            </div>
            <canvas id="myChart03" class="col-lg-4" height="200"></canvas>
            <canvas id="myChart04" class="col-lg-4" height="200"></canvas>
            <canvas id="myChart05" class="col-lg-4" height="200"></canvas>


            <%--</div>--%>
        </div>
    </section>
</div>
<script>
    $(function () {
        var data = {
            labels: ["January", "February", "March", "April", "May", "June", "July"],
            datasets: [
                {
                    fillColor: "rgba(220,220,220,0.5)",
                    strokeColor: "rgba(220,220,220,1)",
                    pointColor: "rgba(220,220,220,1)",
                    pointStrokeColor: "#fff",
                    data: [65, 59, 90, 81, 56, 55, 40]
                },
                {
                    fillColor: "rgba(151,187,205,0.5)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    data: [28, 48, 40, 19, 96, 27, 100]
                }
            ]
        }
        var ctx = document.getElementById("myChart").getContext("2d");
        var ctx02 = document.getElementById("myChart02").getContext("2d");
        var ctx03 = document.getElementById("myChart03").getContext("2d");
        var ctx04 = document.getElementById("myChart04").getContext("2d");
        var ctx05 = document.getElementById("myChart05").getContext("2d");
        new Chart(ctx).Line(data);
        new Chart(ctx02).Line(data);
        new Chart(ctx03).Line(data);
        new Chart(ctx04).Line(data);
        new Chart(ctx05).Line(data);
    })
</script>
<jsp:include page="/template/02/pageFooter.jsp"></jsp:include>

