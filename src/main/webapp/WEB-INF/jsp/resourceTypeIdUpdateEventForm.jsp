<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test Resource TypeId Update event</h2>
<form:form action="resourceTypeIdUpdateEvent" modelAttribute="resourceForm" >
    <table class="colored">
      <tr>
        <td>ResourceId</td>
        <td><form:input path="resourceId" /></td>
      </tr>
     <tr>
       <td>newResourceTypeId</td>
       <td><form:input path="resourceTypeId"/></td>
     </tr>
     <tr>
       <td>oldResourceTypeId</td>
       <td><form:input path="oldResourceTypeId"/></td>
     </tr>
     <tr>
        <td>oldResourceTypeIdKey</td>
        <td><form:input path="oldResourceTypeIdKey"/></td>
      </tr>
      <tr>
        <td>oldResourceHasStructureInherit</td>
        <td><form:checkbox path="oldResourceHasStructureInherit" /></td>
      </tr>
      <tr>
        <td>oldResourceHasContentInherit</td>
        <td><form:checkbox path="oldResourceHasContentInherit" />
        </td>
      </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>