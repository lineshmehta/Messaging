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

  <h2>Test Name change Master Customer event</h2>
  <form:form action="masterCustomerNameChangeEvent"
    modelAttribute="masterCustomerForm">
    <table class="colored">
      <tr>
        <td>MasterId</td>
        <td><form:input path="masterId" /></td>
      </tr>
    </table>

    <table class="colored">
      <tr>
        <td>
          <p>New Values:</p>
          <table>
            <tr>
              <td>CustLastName</td>
              <td><form:input path="custLastName" />
              </td>
            </tr>
            <tr>
              <td>CustFirstName</td>
              <td><form:input path="custFirstName" />
              </td>
            </tr>
            <tr>
              <td>CustMiddleName</td>
              <td><form:input path="custMiddleName" />
              </td>
            </tr>
          </table></td>
        <td>
          <p>OLD Values:</p>
          <table>
            <tr>
              <td>CustLastName</td>
              <td><form:input path="oldLastName" />
              </td>
            </tr>
            <tr>
              <td>CustFirstName</td>
              <td><form:input path="oldFirstName" />
              </td>
            </tr>
            <tr>
              <td>CustMiddleName</td>
              <td><form:input path="oldMiddleName" />
              </td>
            </tr>
          </table></td>
      </tr>
    </table>

    <input type="submit" value="Send" />

  </form:form>
</body>
</html>
