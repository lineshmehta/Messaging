<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test LogicalDelete Customer event</h2>
<form:form action="customerLogicalDeleteEvent" modelAttribute="customerForm">
    <table class="colored">
        <tr>
            <td>customerId</td>
            <td><form:input path="custId"/></td>
        </tr>
        <tr>
            <td>info_is_deleted</td>
            <td><form:checkbox path="infoIsDeleted"/></td>
        </tr>
    </table>
    
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
