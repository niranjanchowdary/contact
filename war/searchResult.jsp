<%@page import="java.awt.Window"%>
<%@page import="com.google.appengine.api.blobstore.BlobKey"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page import= "com.google.appengine.api.images.ImagesServiceFactory" %>
<%@ page import= "com.google.appengine.api.images.ImagesService" %>
<%@ page import="com.google.appengine.api.images.ServingUrlOptions" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
</head>
<body  >

	<table border="1" style="margin-right: 300px">
		<tr>

			<%
				//rendering data to client 
				ArrayList<BlobKey> blob;

				BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
				List<Entity> list_object = (List<Entity>) request.getAttribute("list_entity_obj");
				for (Entity entity_object : list_object) {
					
					blob = (ArrayList<BlobKey>) entity_object.getProperty("image");
					 ImagesService imagesService = ImagesServiceFactory.getImagesService();
			         ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blob.get(0));
			         String servingUrl = imagesService.getServingUrl(servingOptions);
					
					
					out.print("<td colspan='3'><img src="+servingUrl+"></td></tr><tr>");
					Map<String, Object> map_object = entity_object.getProperties();
					//properties
					for (Map.Entry<String, Object> map : map_object.entrySet()) {
						if(map.getKey().equals("image"))
							continue;
						out.print("<td>" + map.getKey() + "</td>");

					}
					out.print("</tr><tr>");
					//values 
					for (Map.Entry<String, Object> map : map_object.entrySet()) {
						if(map.getKey().equals("image"))
							continue;
						out.print("<td>" + map.getValue() + "</td>");

					}
					out.print("</tr><tr>");

				}
			%>
		</tr>
	</table>
</body>
</html>