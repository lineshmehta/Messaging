<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test change subscription status event</h2>
<form:form action="subscriptionChangedStatusEvent" modelAttribute="subscriptionForm">
    <table class="colored">
      <tr>
        <td>subscriptionId</td>
        <td><form:input path="subscrId" />
        </td>
      </tr>
      <tr>
            <td>new_subscrStatusId</td>
            <td><form:input path="subscrStatusId"/></td>
        </tr>
        <tr>
            <td>old_subscrStatusId</td>
            <td><form:input path="oldSubscriptionStatusId"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>
