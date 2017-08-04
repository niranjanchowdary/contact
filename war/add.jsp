<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
	<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>


<body>
	<center>
		<h1>Add contact</h1>
	</center>
<script type="text/javascript">
   function add() {
     var element = document.createElement("input");
     element.setAttribute("type", "text");
     element.setAttribute("name", "mytext");
    var spanvar = document.getElementById("myspan");
    spanvar .appendChild(element);
   }
  function addEmail() {
     var element = document.createElement("input");
     element.setAttribute("type", "text");
     element.setAttribute("name", "mytext1");
    var spanvar = document.getElementById("myspan1");
    spanvar .appendChild(element);
   }

</SCRIPT>
<form action="<%= blobstoreService.createUploadUrl("/contacts") %>" method="post" enctype="multipart/form-data">
<div id="fname">
FNAME<input type="text" name="fname"/>
</div>
<div id="lname">
LNAME<input type="text" name="lname"/>
</div>
<div id="certificationtog">
PHNO   <input type="button" id="addrows" name="phno" class="addperson" 
        value="Addphno" onclick="add();"/>
      <span id="myspan"></span>
      <br><br>
</div>
<div id="email">
EMAIL <input type="button" id="addrows" name="email" class="addperson" 
        value="AddEmail" onclick="addEmail();"/>
<span id="myspan1"></span>
</div>
<div id="drno">
 DRNO <input type="text" name="dr_no"/>
</div>
<div id="stateName">
 STATE <input type="text" name="state"/>
</div>
<div id="countryName">
  COUNTRY<input type="text" name="country"/>
</div>

<div id="img">
  IMAGE<input type="file" name="myFile"/>
</div>
<input type="submit" value="add">
</form></body>
</html>