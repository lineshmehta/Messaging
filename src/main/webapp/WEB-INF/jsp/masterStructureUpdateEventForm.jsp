<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test master structure update event</h2>
<form:form action="masterStructureUpdateEvent" modelAttribute="masterStructureForm">
  <table class="colored">
      <tr>
        <td>
            <p>New Values:</p>
            <table>
              <tr>
                <td>MAST_ID_OWNER</td>
                <td><form:input path="mastIdOwner" /></td>
              </tr>
            </table>
          </td>
        <td>
          <p>OLD Values:</p>
            <table>
              <tr>
                <td>MAST_ID_MEMBER</td>
                <td><form:input path="mastIdMember" /></td>
              </tr>
              <tr>
                <td>MAST_ID_OWNER</td>
                <td><form:input path="oldMastIdOwner" />
                </td>
              </tr>
            </table>
          </td>
      </tr>
  </table>
  <input type="submit" value="Send"/>
</form:form>
</body>
</html>