<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body>
<table border="1"style=" width="400px";>
<tr>
 <%
 List<Entity> list_obj=(List<Entity>)request.getAttribute("e");
 for(Entity entity_obj:list_obj){
	 out.print("<td colspan='3'>next table</td></tr><tr>");
	 Map<String,Object> map_obj=entity_obj.getProperties();
 for(Map.Entry<String,Object> map:map_obj.entrySet())
 {
	 out.print("<td>"+map.getKey()+"</td>");
	 
  }
 out.print("</tr><tr>");
 for(Map.Entry<String,Object> map:map_obj.entrySet())
 {
	 out.print("<td>"+map.getValue()+"</td>");
	 
  }
out.print("</tr><tr>");
 }
 
 %>
 </tr>
 </table>
 </body>
</html>