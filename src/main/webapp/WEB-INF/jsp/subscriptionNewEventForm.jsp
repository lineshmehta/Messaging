<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../css/screen.css" media="screen"/>
</head>
<body>

<a href="../">To start page</a>

<h2>Test new subscription event</h2>
<form:form action="subscriptionNewEvent" modelAttribute="subscriptionForm" >
    <table class="colored">
        <tr>
            <td>
                <table>
                   <tr>
                       <td>subscriptionId</td>
                       <td><form:input path="subscrId"/></td>
                   </tr>
                   <tr>
                        <td>contractId</td>
                        <td><form:input path="contractId"/></td>
                    </tr>
                    <tr>
                        <td>s212ProductId</td>
                        <td><form:input path="s212ProductId"/></td>
                    </tr>
                    <tr>
                        <td>msisdn</td>
                        <td><form:input path="directoryNumberId"/></td>
                    </tr>
                    <tr>
                        <td>cust_id_resp</td>
                        <td><form:input path="custIdResp"/></td>
                    </tr>
                    <tr>
                        <td>cust_id_payer</td>
                        <td><form:input path="custIdPayer"/></td>
                    </tr>
                    <tr>
                        <td>cust_id_user</td>
                        <td><form:input path="custIdUser"/></td>
                    </tr>
                </table>
            </td>
            <td>
                <table>
                    <tr>
                        <td>subscr_valid_from_date</td>
                        <td><form:input path="subscrValidFromDate"/></td>
                    </tr>
                    <tr>
                        <td>subscr_status_id</td>
                        <td><form:input path="subscrStatusId"/></td>
                    </tr>
                    <tr>
                        <td>eInvoiceReference</td>
                        <td><form:input path="userReferenceForm.eInvoiceRef"/></td>
                    </tr>
                    <tr>
                        <td>userReferenceDescription</td>
                        <td><form:input path="userReferenceForm.userRefDescr"/></td>
                    </tr>
                    <tr>
                        <td>numberType</td>
                        <td><form:select path="userReferenceForm.numberType">
                           <form:option value="ES"/>
                           <form:option value="TM"/>
                        </form:select></td>
                    </tr>
                    <tr>
                        <td>subscr_has_secret_number</td>
                        <td><form:checkbox path="subscrHasSecretNumber"/></td>
                    </tr>
                    <tr>
                        <td>acc_id</td>
                        <td><form:input path="accId"/></td>
                    </tr>
                </table>
            </td>
        </tr>
     </table>
    <p>
        Form for Twin And DataKort.
    </p>
    <table class="colored">
      <tr>
        <td>
           <table>
             <tr>
                 <td>subscr_id_owner</td>
                 <td><form:input path="relSubscriptionForm.subscrIdOwner"/></td>
              </tr>
              <tr>
                 <td>subscr_id_member</td>
                 <td><form:input path="relSubscriptionForm.subscrIdMember"/></td>
              </tr>
              <tr>
                 <td>rel_subscr_type</td>
                 <td><form:input path="relSubscriptionForm.relSubscrType"/></td>
              </tr>
              <tr>
                 <td>msisdn</td>
                 <td><form:input path="relSubscriptionForm.directoryNumberId"/></td>
              </tr>
           </table>
        </td>
      </tr>
    </table>
<input type="submit" value="Send"/>
</form:form>
</body>
</html>