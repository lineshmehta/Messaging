<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Account Name Change event</h2>
<form:form action="accountNameChangeEvent" modelAttribute="accountForm">
    <table class="colored">
      <tr>
        <td>accountId</td>
        <td><form:input path="accId" /></td>
      </tr>
      <tr>
           <td>new_acc_name</td>
           <td><form:input path="accountName"/></td>
        </tr>
        <tr>
           <td>old_acc_name</td>
           <td><form:input path="oldAccountName"/></td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>