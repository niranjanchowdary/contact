package com.contact;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

@SuppressWarnings("serial")
public class ContactDelete extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// input from user to delete contact
		PrintWriter out = response.getWriter();
		//input from user side
		String contact_value = request.getParameter("name");
		String contact_field = request.getParameter("option");
		//checking the contact value
		if (contact_value != null && contact_value != "") {
			String message_2_client="";
			message_2_client = ContactsStore.deleteContact(contact_value, contact_field);
				out.println("message from server "+ message_2_client);
			
		}
		// invalid input
		else {
			response.sendError(120, "not a valid contact please enter valid one to delete");
			out.println("<a href='/about.jsp' ><button type='button'>Home</button></a>");
		}
	}
}
