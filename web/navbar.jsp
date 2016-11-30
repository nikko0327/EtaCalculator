<%--
  Created by IntelliJ IDEA.
  User: nlee
  Date: 8/9/16
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <title>ETA Calculator</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <!--  <link href="css/datepicker.css" rel="stylesheet" type="text/css"> -->

    <!-- jquery datepicker ui css, used here for datepicker -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />

    <link href="css/font-awesome.css" rel="stylesheet">

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <!--[if IE 7]>
    <link rel="stylesheet" href="css/font-awesome-ie7.min.css">
    <![endif]-->

    <style type="text/css">
        body {
            padding-top: 20px;
            padding-bottom: 40px;
        }

        #search_options {
            text-align: center;
        }

        #search {
            margin: 10px;
        }

        #drive_table {
            width: 100%;
        }

        .controls p {
            margin: 5px;
        }

        /* Custom container */
        .container-narrow {
            margin: 0 auto;
            max-width: 700px;
        }

        .container-narrow>hr {
            margin: 30px 0;
        }

        /* Main marketing message and sign up button */
        .jumbotron {
            text-align: center;
        }

        .jumbotron h1 {
            font-size: 72px;
            line-height: 1;
        }

        .jumbotron .btn {
            font-size: 21px;
            padding: 14px 24px;
        }

        #chartdiv {
            width: 100%;
            height: 500px;
        }
    </style>

    <link href="css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
</head>

<%
    String userName = null;
    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username"))
                userName = cookie.getValue();
        }
    }

    if (userName == null)
        response.sendRedirect("/EtaCalculator");
%>

<body>
    <div class="container">
        <div class="masthead">
            <ul class="nav nav-pills pull-right">
                <li><a href="#"><i class="icon-user icon-white"></i>Welcome, <%= userName %></a></li>
                <li><a href="logout.jsp" value="Logout">Logout</a></li>
            </ul>
            <h3 class="muted">ETA Calculator  <img src="img/proof.png" alt="Proofpoint Logo" /></h3>
        </div>

        <div class="navbar navbar-inverse ">
            <div class="navbar-inner">
                <div class="container">
                    <button type="button" class="btn btn-navbar" data-toggle="collapse"
                            data-target=".nav-collapse">
                        <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                    </button>

                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class=""><a class="" href="currentProjects.jsp">Current Projects</a></li>
                            <li class=""><a class="" href="upcomingProjects.jsp">Upcoming Projects</a></li>
                            <li class=""><a class="" href="applianceAssignment.jsp">Appliance Assignments</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
