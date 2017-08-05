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
 
 <%//rendering data to client 
 List<Entity> list_object=(List<Entity>)request.getAttribute("list_entity_obj");
 for(Entity entity_object:list_object){
	 System.out.print(entity_object.getProperty("image"));
	 out.print("<td colspan='3'>next table</td></tr><tr>");
	 Map<String,Object> map_object=entity_object.getProperties();
	//properties
	 for(Map.Entry<String,Object> map:map_object.entrySet())
 {
	 out.print("<td>"+map.getKey()+"</td>");
	 
  }
 out.print("</tr><tr>");
 //values 
 for(Map.Entry<String,Object> map:map_object.entrySet())
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