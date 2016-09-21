<%@ page contentType="text/html; charset=utf-8" %>
<%--
  ~ Copyright (c) 2014.
  ~ Author WangJun
  ~ Email  wangjuntytl@163.com
  --%>
<%--页面头部基本内容--%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>APM | Dashboard</title>

    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.5 -->
    <link href="/css/bootstrap-3.5.css" rel="stylesheet">
    <!--  Font Awesome -->
    <link href="/css/font-awesome.min.css" rel="stylesheet">
    <!-- Ionicons -->
    <link rel="stylesheet" href="/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="/static/template02/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="/static/template02/skins/_all-skins.min.css">

    <!-- jQuery 2.2.0 -->
    <script src="/js/jquery-1.9.1.js"></script>
    <!-- jQuery UI 1.11.4 -->
    <script src="/js/jquery-ui.min.js"></script>
    <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
    <script>
        $.widget.bridge('uibutton', $.ui.button);
    </script>
    <!-- Bootstrap 3.3.6 -->
    <script src="/js/bootstrap-3.5.js"></script>
    <script src="/js/helper/Map.js"></script>
    <script src="/js/helper/js-cookie.js"></script>

</head>
<body class="hold-transition skin-green sidebar-mini"> <%-- sidebar-collapse--%>
<%--<body class="frame-transition skin-green sidebar-mini">--%>
<div class="wrapper">

    <header class="main-header">
        <!-- Logo -->
        <a href="/" class="logo">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><b>A</b>PM</span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>APM</b> Dashboard</span>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top">
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>

            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                </ul>
            </div>
        </nav>
    </header>