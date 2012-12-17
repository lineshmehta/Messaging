<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="./css/screen.css" media="screen"/>
</head>
<body>
<p>
    Oh no. Ten small, beautiful kittens just died.
</p>
<p>
    <img src="http://placekitten.com/<%= 100 + (int)(Math.random()*200) %>/300"/>
</p>

<p>
    <!--JavaScript code for single Quote of the Day, http://www.tqpage.com/-->
    <script language="javascript" src="http://www.quotationspage.com/data/1qotd.js">
    </script>
    <!--End JavaScript Quotations code-->
</p>
<p>
    <jsp:include page="infopage.jsp"/>
    <c:import url="infopage.jsp" />
</p>
</body>
</html>