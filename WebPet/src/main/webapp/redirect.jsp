<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html  xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link rel="shortcut icon" href="public/img/greenapple.ico"/>
            <link type="text/css"  rel="stylesheet"  href="public/css/bootstrap.css"/>
            <link type="text/css"  rel="stylesheet"  href="public/css/bootstrap-responsive.css"/>
            <script type="text/javascript" src="public/js/jquery-1.9.1.js"></script>
            <script type="text/javascript" src="public/js/bootstrap.js"></script>
            <title>Green Apple OpenId redirection</title>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row-fluid">&nbsp;</div>
            <div class="row-fluid" align="center">

                <div class="navbar navbar-inverse navbar-inner">
                    <form name="openid-form-redirection" id="openid-form-redirection" action="${message.OPEndpoint}" method="post" accept-charset="utf-8">
                        <c:forEach var="parameter" items="${message.parameterMap}">
                            <input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
                        </c:forEach>
                        <p>Please press sign up button if automatic redirection fails</p>
                        <button class="btn btn-primary" type="submit">sign up</button>
                    </form></div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $('#openid-form-redirection').submit();
    </script>
</html>
