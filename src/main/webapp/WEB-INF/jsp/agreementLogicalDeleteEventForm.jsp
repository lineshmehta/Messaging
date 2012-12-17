<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Logical Delete Agreement event</h2>
<form:form action="agreementLogicalDeleteEvent" modelAttribute="agreementForm">
    <table class="colored">
        <tr>
            <td>agreementId</td>
            <td><form:input path="agreementId"/></td>
        </tr>
    </table>
    
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>