<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  ~ Copyright (c) 2014.
  ~ Author WangJun
  ~ Email  wangjuntytl@163.com
  --%>
<%--页面头部基本内容--%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Apm simple web</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap-3.5.css" rel="stylesheet">
    <link href="/css/cover.css" rel="stylesheet">
    <script src="/js/jquery-1.9.1.js"></script>
    <script src="/js/bootstrap-3.5.js"></script>
    <script src="/js/echart/echarts.js"></script>

    <link rel="stylesheet" href="/css/bootstrap-datetimepicker.min.css">
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/monitor">APM<span class="small text-muted"></span></a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <c:choose>
                    <c:when test="${active eq 'Monitor'}">
                        <li class="active"><a href="/monitor">监控</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/monitor">监控</a></li>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${active eq 'Alert'}">
                        <li class="active"><a href="/alert/list">预警</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/alert/list">预警</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</nav>