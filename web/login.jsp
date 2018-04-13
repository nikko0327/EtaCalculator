<%--
  Created by IntelliJ IDEA.
  User: nlee
  Date: 8/9/16
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<!DOCTYPE html>
<html lang="en">
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
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css"/>

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

        /* Custom container */
        .container-narrow {
            margin: 0 auto;
            max-width: 700px;
        }

        .container-narrow > hr {
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
    </style>

    <link href="css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">

    <style type="text/css">
        .form-signin {
            max-width: 300px;
            padding: 19px 29px 29px;
            margin: 0 auto 20px;
            background-color: rgba(224, 217, 217, 0.8);
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            box-shadow: 0 1px 2px rgba(0, 0, 0, .05);

        }

        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }

        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }

        body {
            background: url('img/lt.jpeg') no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            background-size: cover;
            -o-background-size: cover;
        }

        .form-signin {

            left: 50%;
            top: 50%;
            position: absolute;
            -webkit-transform: translate3d(-50%, -50%, 0);
            -moz-transform: translate3d(-50%, -50%, 0);
            transform: translate3d(-50%, -50%, 0);
        }

        #sign_in {
            width: 227px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="masthead">
        <p class="pull-right"><img src="img/proofpoint_logo.png" alt="Proofpoint Logo"/></p>
        <%--<h3 class="muted">Drive Tracking Dashboard</h3>--%>
    </div>

    <%--<hr>--%>
    <div class="form">
        <form action="login" class="form-signin" method="post">
            <div class="control-group">
                <h4>ETA Calculator</h4>
                <label class="control-label" for="username">Username</label>
                <div class="controls">
                    <input type="text" id="username" placeholder="Username" autocomplete="off" required>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="password">Password</label>
                <div class="controls">
                    <input type="password" id="password" placeholder="Password" required>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary" id="sign_in">Sign in</button>
                </div>
            </div>
            <div id="spinner" hidden>
                <center><i class="icon-spinner icon-spin icon-3x"></i></center>
            </div>
            <p id="error-message" class="text-error" hidden><!--<b>Invalid login Credentials</b>--></p>
        </form>
    </div>
</div>
<!-- /container -->

<script language="JavaScript" type="text/javascript">
    window.history.forward(0);
</script>

<script src="js/jquery.js"></script>
<script src="js/bootstrap-transition.js"></script>
<script src="js/bootstrap-alert.js"></script>
<script src="js/bootstrap-modal.js"></script>
<script src="js/bootstrap-dropdown.js"></script>
<script src="js/bootstrap-scrollspy.js"></script>
<script src="js/bootstrap-tab.js"></script>
<script src="js/bootstrap-tooltip.js"></script>
<script src="js/bootstrap-popover.js"></script>
<script src="js/bootstrap-button.js"></script>
<script src="js/bootstrap-collapse.js"></script>
<script src="js/bootstrap-carousel.js"></script>
<script src="js/bootstrap-typeahead.js"></script>
<script src="js/bootbox.js"></script>

<script src="js/login_script.js"></script>
</body>
</html>
