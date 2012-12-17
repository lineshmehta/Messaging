<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test mobile office delete event</h2>
<form:form action="mobileOfficeDeleteEvent" modelAttribute="mobileOfficeForm">
    <table class="colored">
        <tr>
            <td>directoryNumber</td>
            <td><form:input path="directoryNumber"/></td>
        </tr>
        <tr>
            <td>extensionNumberOld</td>
            <td><form:input path="extensionNumberOld"/></td>
        </tr>
        <tr>
            <td>infoIsDeleted</td>
            <td><form:checkbox path="infoIsDeleted"/></td>
        </tr>
        <tr>
            <td>infoIsDeletedOld</td>
            <td><form:checkbox path="infoIsDeletedOld"/></td>
        </tr>

    </table>

    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
