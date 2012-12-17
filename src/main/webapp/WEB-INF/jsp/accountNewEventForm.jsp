<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test new Account event</h2>
<form:form action="accountNewEvent" modelAttribute="accountForm">
    <table class="colored">
        <tr>
            <td>accountId</td>
            <td><form:input path="accId"/></td>
        </tr>
    </table>
    
    <table class="colored">
        <tr>
            <td>
                <table>
                    <tr>
                        <td>cust_id_resp</td>
                        <td><form:input path="custIdResp"/></td>
                    </tr>
                    <tr>
                        <td>cust_id_payer</td>
                        <td><form:input path="custIdPayer"/></td>
                    </tr>
                    <tr>
                        <td>acc_name</td>
                        <td><form:input path="accountName"/></td>
                    </tr>
                    <tr>
                        <td>acc_status_id</td>
                        <td><form:input path="accStatusId"/></td>
                    </tr>
                </table>
            </td>
            <td>
                <table>
                    <tr>
                        <td>acc_type_id</td>
                        <td><form:input path="accTypeId"/></td>
                    </tr>
                    <tr>
                        <td>acc_status_id2</td>
                        <td><form:input path="accStatusId2"/></td>
                    </tr>
                    <tr>
                        <td>acc_inv_medium</td>
                        <td><form:input path="accInvMedium"/></td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <input type="submit" value="Send"/>
</form:form>
</body>
</html>