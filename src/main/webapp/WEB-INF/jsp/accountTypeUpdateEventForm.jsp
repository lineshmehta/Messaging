<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Account Type Update event</h2>
<form:form action="accountTypeUpdateEvent" modelAttribute="accountForm">
    <table class="colored">
      <tr>
        <td>accountId</td>
        <td><form:input path="accId" /></td>
      </tr>
      <tr>
        <td>new_acc_type_id</td>
        <td><form:input path="accTypeId" /></td>
      </tr>
      <tr>
        <td>old_acc_type_id</td>
        <td><form:input path="oldAccountTypeId" /></td>
      </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>