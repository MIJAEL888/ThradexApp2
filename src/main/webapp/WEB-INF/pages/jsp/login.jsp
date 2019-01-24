<%@ include file="/WEB-INF/pages/jsp/includes.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Login Page</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="" name="description">
    <meta content="" name="author">
    <link type="text/css" rel="stylesheet"
          href="//fonts.googleapis.com/css?family=Open+Sans:400italic,400,300,700">
    <link type="text/css" rel="stylesheet" href="//fonts.googleapis.com/css?family=Berkshire+Swash">

    <link type="text/css" rel="stylesheet" href="<spring:url value="/static/assets/css/pages/login.css" htmlEscape="true" />">
    <link rel="shortcut icon" href="global/images/favicon.ico">
</head>
<body class="page-login">
<div class="outer">
    <div class="middle bg-overlay">
        <div class="inner"><!--BEGIN LOGO-->
            <div class="header"><a href="#" class="logo">AppManager</a></div>
            <!--END LOGO--><!--BEGIN CONTENT-->
            <c:if test="${error == true}">
            <div class="alert alert-danger alert-dismissable fade in">
                 <button type="button" data-dismiss="alert" aria-hidden="true"
                         class="close">&times;</button>
                 <strong>Sorry, </strong>your E-mail or Password was incorrect.
             </div>
             </c:if>
            <form name="formLogin" id="formLogin" action="../j_spring_security_check" method="post" class="login-form">
            
            	<h4 class="mbl">Login to your account</h4>

                <div class="form-group">
                    <div class="input-icon"><i class="icon-user"></i>
                    <input type="text" placeholder="E-Mail" name="j_username" class="form-control"></div>
                </div>
                <div class="form-group">
                    <div class="input-icon"><i class="icon-lock"></i>
                    <input type="password" placeholder="Password" name="j_password" class="form-control"></div>
                </div>
             
                <div class="form-group">
                    <button type="submit" class="btn btn-success btn-block">Login</button>
                </div>
                   <div class="form-group">
<!--                     <div class="checkbox pull-left"><label><input type="checkbox" value="">Remember me</label></div> -->
                    <a href="#" class="text-info pull-right mtm">Forgot password?</a>

                    <div class="clearfix"></div>
                </div>
                <hr>
                <small>Don't have an account yet? <a href='page_register.html' class='text-info'>New User</a>
                </small>
            </form>
            <!--END CONTENT--></div>
    </div>
</div>

<script src="<spring:url value="/static/global/js/jquery.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/js/jquery-migrate-1.2.1.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/plugins/bootstrap/js/bootstrap.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/js/html5shiv.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/js/respond.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/plugins/iCheck/icheck.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/plugins/iCheck/custom.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/assets/plugins/bootstrap-validator/js/bootstrapValidator.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/global/js/app.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/static/assets/js/pages/login.js" htmlEscape="true" />"></script>
</body>
</html>