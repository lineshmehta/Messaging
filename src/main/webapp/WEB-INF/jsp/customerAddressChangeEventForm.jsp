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

	<h2>Test Address change Customer event</h2>
	<form:form action="customerAddressChangeEvent" modelAttribute="customerForm">
		
      <table class="colored">
      <tr>
        <td>
          <p>New Values:</p>
          <table>
            <tr>
					<td>customerId</td>
					<td><form:input path="custId" /></td>
				</tr>
				<tr>
					<td>postcodeIdMain</td>
					<td><form:input path="postcodeIdMain" /></td>
				</tr>
				<tr>
					<td>postcodeNameMain</td>
					<td><form:input path="postcodeNameMain" /></td>
				</tr>
				<tr>
					<td>addressLineMain</td>
					<td><form:input path="addrLineMain" /></td>
				</tr>
				<tr>
					<td>addressCoName</td>
					<td><form:input path="addrCOName" /></td>
				</tr>
				<tr>
					<td>addressStreetName</td>
					<td><form:input path="addrStreetName" /></td>
				</tr>
				<tr>
					<td>addressStreetNumber</td>
					<td><form:input path="addrStreetNumber" /></td>
				</tr>
          </table></td>
        <td>
          <p>OLD Values:</p>
          <table>
            <tr>
					<td>oldPostcodeIdMain</td>
					<td><form:input path="oldPostcodeIdMain" /></td>
				</tr>
				<tr>
					<td>oldPostcodeNameMain</td>
					<td><form:input path="oldPostcodeNameMain" /></td>
				</tr>
				<tr>
					<td>oldAddressLineMain</td>
					<td><form:input path="oldAddrLineMain" /></td>
				</tr>
				<tr>
					<td>oldAddressCoName</td>
					<td><form:input path="oldAddrCOName" /></td>
				</tr>
				<tr>
					<td>oldAddressStreetName</td>
					<td><form:input path="oldAddrStreetName" /></td>
				</tr>
				<tr>
					<td>oldAddressStreetNumber</td>
					<td><form:input path="oldAddrStreetNumber" /></td>
				</tr>
          </table></td>
      </tr>
    </table>

		<input type="submit" value="Send" />
	</form:form>
</body>
</html>
