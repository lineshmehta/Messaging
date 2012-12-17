<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Subscription InvoiceReference UpdateEvent</h2>
<form:form action="subscriptionInvoiceReferenceUpdateEvent" modelAttribute="userReferenceForm">
    <table class="colored">
        <tr>
            <td>subscriptionId</td>
            <td><form:input path="subscriptionId"/></td>
        </tr>
        <tr>
            <td>einvoice_ref</td>
            <td><form:input path="eInvoiceRef"/></td>
        </tr>
    </table>
<input type="submit" value="Send"/>
</form:form>
</body>
</html>
