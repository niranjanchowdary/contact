<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<center>
		<h1>Delete contact</h1>
	</center>
	<form action="/deletecon" method="post">
		NAME: <input type="text" name="name" style="margin-left: 10px;"><br>
		<br>ATTRIBUTE:<select name="option">
		<option>fname</option>
		<option>lname</option>
		<option>mobile</option>
		<option>email</option>
		<option>state</option>
		<option>country</option>
		<option>drno</option>
		</select> 
		<br> <br> <input type="submit" value="delete"
			style="margin-left: 70px;">
	</form>
</body>
</html>