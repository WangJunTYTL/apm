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
    <title>Pef4j | Dashboard</title>

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


    <%-- <!-- iCheck -->
     <link rel="stylesheet" href="plugins/iCheck/flat/blue.css">
     <!-- Morris chart -->
     <link rel="stylesheet" href="plugins/morris/morris.css">
     <!-- jvectormap -->
     <link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css">
     <!-- Date Picker -->
     <link rel="stylesheet" href="plugins/datepicker/datepicker3.css">
     <!-- Daterange picker -->
     <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker-bs3.css">
     <!-- bootstrap wysihtml5 - text editor -->
     <link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">

     <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
     <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
     <!--[if lt IE 9]>
     <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
     <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
     <![endif]-->--%>

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

</head>
<body class="hold-transition skin-green sidebar-mini sidebar-collapse">
<div class="wrapper">

    <header class="main-header">
        <!-- Logo -->
        <a href="/" class="logo">
            <!-- mini logo for sidebar mini 50x50 pixels -->
            <span class="logo-mini"><b>P</b>erf4j</span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>Perf4j</b> Dashboard</span>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top">
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>

            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="javascript:void(0)" class="top_nav_menu" menu="now">Now</a>
                    </li>
                    <li>
                        <a href="javascript:void(0)" class="top_nav_menu" menu="history">History</a>
                    </li>
                    <li>
                        <a href="javascript:void(0)" class="top_nav_menu" menu="jvm">JVM</a>
                    </li>


                    <li>
                        <a href="/profile">Profile</a>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            Refresh
                        </a>
                        <ul class="dropdown-menu">
                            <li class="refreshMenu">
                                <a href="#">
                                    5s
                                </a>
                            </li>
                            <li class="refreshMenu">
                                <a href="#">
                                    30s
                                </a>
                            </li>
                            <li class="refreshMenu active">
                                <a href="#">
                                    60s
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="https://github.com/WangJunTYTL/perf4j-zh" target="_blank">Help</a>
                    </li>
                    <!-- Control Sidebar Toggle Button -->
                    <li>
                        <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>