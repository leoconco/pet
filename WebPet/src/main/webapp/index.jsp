<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Green Apple Cloud Pet</title>
        <link rel="shortcut icon" href="public/img/greenapple.ico"/>
        <link type="text/css"  rel="stylesheet"  href="public/css/bootstrap.css"/>
        <link type="text/css"  rel="stylesheet"  href="public/css/bootstrap-responsive.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="keywords" content="greenapple,pet,leoconco,cloud"/>
        <meta name="description" content="Pet project to test technologies in use for Cloud applications"/>
        <meta name="author" content="Leonardo Contreras Alfonso"/>
        <script type="text/javascript" src="public/js/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="public/js/bootstrap.js"></script>
    </head>
    <body>
        <div class="navbar navbar-inverse">
            <div class="navbar-inner">
                <a class="brand" href="#">Green Apple <i class="icon-leaf icon-white"></i></a>
                <ul class="nav pull-right">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" ><c:out value="${sessionScope['auth-context'].EMail}"></c:out> <c:if test="${not empty sessionScope['auth-context'].tenantId}">@</c:if><c:out value="${sessionScope['auth-context'].tenantName}"></c:out> <i class="icon-user icon-white"></i> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <c:if test="${not empty sessionScope['auth-context'].tenantId}"><li><a href="index.jsp?_set_tenant=_none_"><i class="icon-eject"></i> change account</a></li></c:if>
                        <li><a href="_logout"><i class="icon-remove icon-white"></i> logout</a></li>
                    </ul>
                </ul>
            </div>
        </div>
    </body>
</html>
