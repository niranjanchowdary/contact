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
		String name = request.getParameter("name");
		String option = request.getParameter("option");
		if (name != null && name != "") {
			String message="";
			message = ContactsStore.deleteContact(name, option);
				out.println("message from server "+ message);
			
		}
		// invalid input
		else {
			response.sendError(120, "not a valid contact please enter valid one to delete");
			out.println("<a href='/about.jsp' ><button type='button'>Home</button></a>");
		}
	}
}
