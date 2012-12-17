<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test User ResourceId update event</h2>
<form:form action="userResourceIdUpdateEvent" modelAttribute="userResourceForm" >
    <table class="colored">
        <tr>
            <td>oldResourceId</td>
            <td><form:input path="resourceIdOld" /></td>
        </tr>
        <tr>
            <td>newResourceId</td>
            <td><form:input path="resourceId" /></td>
        </tr>
        <tr>
            <td>csUserId</td>
            <td><form:input path="csUserIdOld"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>