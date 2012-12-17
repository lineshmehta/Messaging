<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test changed type event</h2>
<form:form action="subscriptionChangeTypeEvent" modelAttribute="subscriptionForm">
    <table class="colored">
    	<tr>
             <td>subscriptionId</td>
             <td><form:input path="subscrId"/></td>
        </tr>
        <tr>
            <td>new_s212ProductId</td>
            <td><form:input path="s212ProductId"/></td>
        </tr>
        <tr>
            <td>old_s212ProductId</td>
            <td><form:input path="oldS212ProductId"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
