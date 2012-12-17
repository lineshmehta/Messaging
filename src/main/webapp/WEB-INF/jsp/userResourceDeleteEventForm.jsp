<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test UserResource Delete event</h2>
<form:form action="userResourceDeleteEvent" modelAttribute="userResourceForm" >
    <table class="colored">
        <tr>
            <td>csUserId</td>
            <td><form:input path="csUserId"/></td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td><form:input path="resourceId" /></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>