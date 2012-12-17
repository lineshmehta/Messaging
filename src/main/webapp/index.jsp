<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/screen.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="css/reset-min.css"/>
    <link rel="stylesheet" type="text/css" href="css/style.css" media="all" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="css/MenuMatic.css" media="screen" charset="utf-8" />
    <link rel="shortcut icon" href="img/favicon.png"/>
    <title>Home of the brave</title>
</head>
<body>
<img src="img/apache-camel-7.png" alt="en kamel" style="float: left"/>
<p style="text-align:justify; font-size:xx-large;font-style:normal;">Welcome to COS Messaging</p>
<div id="container" >
  <ul id="nav">
         <li><a href="#">Subscription Events</a>
        
                <ul>
                    <li><a href="test/subscriptionNewEvent">Subscription New Event</a></li>
                    <li><a href="#">Subscription Update Events</a>
                        <ul>
                            <li><a href="test/subscriptionChangedAccountEvent">Change Account</a></li>
                            <li><a href="test/subscriptionChangeTypeEvent">Change Subscription Type</a></li>
                            <li><a href="test/subscriptionChangeUserEvent">Change User</a></li>
                            <li><a href="test/subscriptionChangedStatusEvent">Update Subscription Status</a></li>
                            <li><a href="test/subscriptionSecretNumberEvent">Change Secret Number</a></li>
                            <li><a href="test/subscriptionUserReferenceNewEvent">New UserReference</a></li>
                            <li><a href="test/subscriptionInvoiceReferenceUpdateEvent">Update Subscription InvoiceRef</a></li>
                            <li><a href="test/subscriptionUserReferenceDescrUpdateEvent">Update Subscription UserRefDescr</a></li>
                            <li><a href="test/subscriptionUserRefLogicalDeleteEvent">Subscription UserReference LogicalDelete</a></li>
                        </ul>
                    </li>
                    <li><a href="test/subscriptionLogicalDeleteEvent">Subscription Logical Delete Event</a></li>
                    <li><a href="test/subscriptionExpiredEvent">Subscription Expired Events</a></li>
                    <li><a href="test/subscriptionDKExpiredEvent">SubscriptionDK Expired Events</a></li>
                </ul>
            </li>
            
            <li><a href="#">Account Events</a>
                <ul>
                    <li><a href="test/accountNewEvent">new Account</a></li>
                    <li><a href="#">Account Update Events</a>
                        <ul>
                            <li><a href="test/accountInvoiceFormatUpdateEvent">Change Account InvoiceFormat</a></li>
                            <li><a href="test/accountNameChangeEvent">Change Account Name</a></li>
                            <li><a href="test/accountPayerUpdateEvent">Change Account Payer</a></li>
                            <li><a href="test/accountPaymentStatusUpdateEvent">Change Account PaymentStatus</a></li>
                            <li><a href="test/accountStatusUpdateEvent">Change Account Status</a></li>
                            <li><a href="test/accountTypeUpdateEvent">Change Account Type</a></li>
                        </ul>
                    </li>
                    <li><a href="test/accountLogicalDeleteEvent">Account Logical Delete Event</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#">Agreement Events</a>
                <ul>
                    <li><a href="test/agreementNewEvent">new Agreement</a></li>
                    <li><a href="test/agreementLogicalDeleteEvent">Logical Delete Agreement</a></li>
                </ul>
            </li>        
            <li>
                <a href="#">AgreementMember Events</a>
                <ul>
                    <li><a href="test/agreementMemberNewEvent">New AgreementMember</a></li>
                    <li><a href="test/agreementMemberLogicalDeleteEvent">Logical Delete AgreementMember</a></li>
                </ul>
            </li>        
            <li>
                <a href="#">AgreementOwner Events</a>
                <ul>
                    <li><a href="test/agreementOwnerNewEvent">New AgreementOwner</a></li>
                </ul>
            </li>     
            <li><a href="#">Customer Events</a>
                <ul>
                    <li><a href="test/customerNewEvent">new Customer</a></li>
                    <li><a href="test/customerLogicalDeleteEvent">LogicalDeleteCustomer</a></li>
                    <li><a href="test/customerAddressChangeEvent">AdressChangeCustomer</a></li>
                    <li><a href="test/customerNameChangeEvent">NameChangeCustomer</a></li>
                </ul>
            </li>
            <li><a href="#">MasterCustomer Events</a>
                <ul>
                    <li><a href="test/masterCustomerNewEvent">new masterCustomer</a></li>
                    <li><a href="test/masterCustomerLogicalDeleteEvent">logical delete masterCustomer</a></li>
                    <li><a href="test/masterCustomerNameChangeEvent">name change in masterCustomer</a></li>
                </ul>
            </li>
            <li><a href="#">MasterStructure Events</a>
                <ul>
                    <li><a href="test/masterStructureNewEvent">Master Structure New Event</a></li>
                    <li><a href="test/masterStructureUpdateEvent">Master Structure Update Event</a></li>
                    <li><a href="test/masterStructureDeleteEvent">Master Structure Delete Event</a></li>
                </ul>
            </li>
            <li><a href="#">Resource Events</a>
                <ul>
                    <li><a href="test/resourceNewEvent">New Resource</a></li>
                    <li><a href="#">Resource Update Events</a>
                        <ul>
                            <li><a href="test/resourceContentInheritUpdateEvent">Update Resource ContentInherit</a></li>
                            <li><a href="test/resourceStructureInheritUpdateEvent">Update Resource StructureInherit</a></li>
                            <li><a href="test/resourceTypeIdKeyUpdateEvent">Update Resource TypeId Key</a></li>
                            <li><a href="test/resourceTypeIdUpdateEvent">Update Resource TypeId</a></li>
                        </ul>
                    </li>
                    <li><a href="test/resourceLogicalDeleteEvent">Resource Logical Delete</a></li>
                </ul>
            </li>
            <li><a href="#">UserResource Events</a>
                <ul>
                    <li><a href="test/userResourceNewEvent">New UserResource</a></li>
                    <li><a href="test/userResourceIdUpdateEvent">Update csUser ResourceId</a></li>
                    <li><a href="test/userResourceCsUserIdUpdateEvent">Update UserResource csUserId</a></li>
                    <li><a href="test/userResourceDeleteEvent">Delete UserResource</a></li>
                </ul>
            </li>
            <li><a href="#">UserMapping Events</a>
                <ul>
                    <li><a href="test/tnuIdUserMappingNewEvent">new UserMapping</a></li>
                    <li><a href="test/tnuIdUserMappingDeleteEvent">delete UserMapping</a></li>
                </ul>
            </li>
            <li><a href="#">MobileOffice Events</a>
                <ul>
                    <li><a href="test/mobileOfficeNewEvent">new mobileOffice</a></li>
                    <li><a href="test/mobileOfficeUpdateEvent">update mobileOffice</a></li>
                    <li><a href="test/mobileOfficeDeleteEvent">delete mobileOffice</a></li>
                </ul>
            </li>
            <li><a href="#">SubscriptionEquipment Events</a>
                <ul>
                    <li><a href="test/subscriptionEquipmentNewEvent">new subscriptionEquipment</a></li>
                    <li><a href="test/subscriptionEquipmentUpdateEvent">update subscriptionEquipment</a></li>
                    <li><a href="test/subscriptionEquipmentDeleteEvent">delete subscriptionEquipment</a></li>
                </ul>
            </li>
            <li><a href="#">Other Links</a>
                <ul>
                    <li><a href="/amqconsole">ActiveMq Console</a></li>
                    <li><a href="test/verifyCache">verifyCache</a></li>
                    <li><a href="test/test">do <strong>not</strong> follow this link</a></li>
                </ul>
            </li>
        </ul>
     </div>
    <!-- Load the Mootools Framework -->
    <script src="http://www.google.com/jsapi"></script><script>google.load("mootools", "1.2.1");</script>   
    
    <!-- Load the MenuMatic Class -->
    <script src="js/MenuMatic_0.68.3.js" type="text/javascript" charset="utf-8"></script>
    
    <!-- Create a MenuMatic Instance -->
    <script type="text/javascript" >
        window.addEvent('domready', function() {            
            var myMenu = new MenuMatic({ orientation:'vertical' });         
        });     
    </script>
</body>
<p>
    <jsp:include page="infopage.jsp"/>
</p>
</html>