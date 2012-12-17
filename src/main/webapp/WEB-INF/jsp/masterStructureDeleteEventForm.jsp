<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test logical delete of master structure event</h2>
<form:form action="masterStructureDeleteEvent" modelAttribute="masterStructureForm">
    <table class="colored">
        <tr>
            <td>MAST_ID_MEMBER (Master Customer Id)</td>
            <td><form:input path="mastIdMember"/></td>
        </tr>
        <tr>
            <td>info_is_deleted</td>
            <td><form:checkbox path="infoIsDeleted" checked="checked"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
