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

  <h2>Test new Resource event</h2>
  <form:form action="resourceNewEvent" modelAttribute="resourceForm">
    <table class="colored">
      <tr>
        <td>ResourceId</td>
        <td><form:input path="resourceId" />
        </td>
      </tr>
    </table>

    <table class="colored">
      <tr>
        <td>resourceTypeId</td>
        <td><form:input path="resourceTypeId" />
        </td>
      </tr>
      <tr>
        <td>resourceTypeIdKey</td>
        <td><form:input path="resourceTypeIdKey" />
        </td>
      </tr>
      <tr>
        <td>resourceHasContentInherit</td>
        <td><form:checkbox path="resourceHasContentInherit" />
        </td>
      </tr>
      <tr>
        <td>resourceHasStructureInherit</td>
        <td><form:checkbox path="resourceHasStructureInherit" />
        </td>
      </tr>
    </table>
    <input type="submit" value="Send" />
  </form:form>
</body>
</html>