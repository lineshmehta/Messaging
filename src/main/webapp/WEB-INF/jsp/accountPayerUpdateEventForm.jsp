<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Account Payer Change event</h2>
<form:form action="accountPayerUpdateEvent" modelAttribute="accountForm">
    <table class="colored">
      <tr>
        <td>accountId</td>
        <td><form:input path="accId" /></td>
      </tr>
      <tr>
           <td>new_cust_id_payer</td>
           <td><form:input path="custIdPayer"/></td>
        </tr>
        <tr>
           <td>old_cust_id_payer</td>
           <td><form:input path="oldCustIdPayer"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>