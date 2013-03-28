<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html  xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>oAuth redirect</title>
    </head>
    <body>
        <h1>Login</h1>
        <form name="openid-form-redirection" id="openid-form-redirection" action="${message.OPEndpoint}" method="post" accept-charset="utf-8">
            <c:forEach var="parameter" items="${message.parameterMap}">
                <input type="hidden" name="${parameter.key}" value="${parameter.value}"/>
            </c:forEach>
            <button class="btn btn-primary" type="submit">sign up</button>
        </form>
    </body>
</html>
