<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title><c:out value="${sessionScope['auth-context'].EMail}"></c:out> - Green Apple Cloud Pet</title>
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
                <a class="brand" href="#"><c:out value="${sessionScope['auth-context'].tenantName}"></c:out> <small>Green Apple</small> <i class="icon-leaf icon-white"></i></a>
                <ul class="nav pull-right">
                    <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" ><c:out value="${sessionScope['auth-context'].EMail}"></c:out> <i class="icon-user icon-white"></i> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <c:if test="${not empty sessionScope['auth-context'].tenantId}"><li><a href="_set_tenant?id=_none_"><i class="icon-eject"></i> change account</a></li></c:if>
                        <li><a href="_logout"><i class="icon-remove"></i> logout</a></li>
                    </ul>
                    </li>
                </ul>
            </div>
        </div>