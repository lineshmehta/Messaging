<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test UserResource csUserId Update event</h2>
<form:form action="userResourceCsUserIdUpdateEvent" modelAttribute="userResourceForm" >
    <table class="colored">
        <tr>
            <td>oldCsUserId</td>
            <td><form:input path="csUserIdOld"/></td>
        </tr>
        <tr>
            <td>csUserId</td>
            <td><form:input path="csUserId"/></td>
        </tr>
        <tr>
            <td>resourceId</td>
            <td><form:input path="resourceIdOld" /></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>