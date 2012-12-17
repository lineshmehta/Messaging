<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/screen.css"
  media="screen" />
</head>
<body>

  <a href="../">To start page</a>

  <h2>Test Resource Logical Delete event</h2>
  <form:form action="resourceLogicalDeleteEvent"
    modelAttribute="resourceForm">
    <table class="colored">
      <tr>
        <td>ResourceId</td>
        <td><form:input path="resourceId" />
        </td>
      </tr>
      <tr>
        <td>infoIsDeleted</td>
        <td><form:checkbox path="infoIsDeleted" />
        </td>
      </tr>
    </table>
    <input type="submit" value="Send" />
  </form:form>
</body>
</html>