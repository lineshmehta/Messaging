<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test delete UserMapping event</h2>
<form:form action="tnuIdUserMappingDeleteEvent" modelAttribute="tnuUserIdMappingForm" >
    <table class="colored">
        <tr>
            <td>tnuId</td>
            <td><form:input path="tnuId"/></td>
        </tr>
        <tr>
            <td>applicationId</td>
            <td><form:input path="applicationId" /></td>
        </tr>
                    <tr>
                        <td>csUserId</td>
                        <td><form:input path="csUserId"/></td>
                    </tr>        
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>