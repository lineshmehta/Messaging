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

	<h2>Test Name change Customer event</h2>
	<form:form action="customerNameChangeEvent" modelAttribute="customerForm">
		<table class="colored">
			<tr>
				<td>customerId</td>
				<td><form:input path="custId" /></td>
			</tr>
			<tr>
				<td>masterCustomerId</td>
				<td><form:input path="masterId" /></td>
			</tr>
			<tr>
				<td>firstName</td>
				<td><form:input path="custFirstName" /></td>
			</tr>
			<tr>
				<td>middleName</td>
				<td><form:input path="custMiddleName" /></td>
			</tr>
			<tr>
				<td>lastName</td>
				<td><form:input path="custLastName" /></td>
			</tr>
			<tr>
				<td>custUnitNumber</td>
				<td><form:input path="custUnitNumber" /></td>
			</tr>
		</table>

		<input type="submit" value="Send" />
	</form:form>
</body>
</html>
