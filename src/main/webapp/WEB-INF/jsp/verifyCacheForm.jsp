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

    <h2>Page To Verify Data In JDBM3 Cache</h2>
    <form:form action="verifyCache" modelAttribute="cacheForm">
        <table class="colored">
            <tr>
                <td>idToGetDetails</td>
                <td><form:input path="id" /></td>
            </tr>
            <tr>
                <td>cacheType</td>
                <td>
                   <form:select path="cacheType">
                     <form:option value="customerCache"/>
                     <form:option value="subscriptionTypeCache"/>
                     <form:option value="resourceCache"/>
                     <form:option value="userResourceCache"/>
                     <form:option value="masterCustomerCache"/>
                     <form:option value="kurtIdCache"/>
                   </form:select>
                </td>
           </tr>
        </table>

        <input type="submit" value="Send" />
    </form:form>
</body>
</html>
