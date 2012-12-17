<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test subscription equipment delete event</h2>
<form:form action="subscriptionEquipmentDeleteEvent" modelAttribute="subscriptionEquipmentForm">
    <table class="colored">
        <tr>
            <td>subscriptionId</td>
            <td><form:input path="subscriptionIdOld"/></td>
        </tr>
        <tr>
            <td>imsi</td>
            <td><form:input path="imsiOld"/></td>
        </tr>
        <tr>
            <td>infoIsDeleted</td>
            <td><form:input path="infoIsDeleted"/></td>
        </tr>
        <tr>
            <td>validToDate</td>
            <td><form:input path="validToDate"/></td>
        </tr>
    </table>

    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
