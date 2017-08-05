package com.contact;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ContactEdit extends HttpServlet {
 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// input from user to edit
		PrintWriter out = response.getWriter();
		String contact_details = request.getParameter("name");
		String contact_field = request.getParameter("option");
		String new_value = request.getParameter("newvalue");
		if (contact_details != null && contact_details != "" && new_value != null) {
			//passing data to retrieve and checking retrieved data  
			if (ContactsStore.editContact(contact_details,contact_field,new_value)) {
				System.out.println("edited");
				out.print("contact edited successfully");
			} else {
				System.out.println("not edited");
				out.println("contact details are not edited ");
			}
		}
		// invalid input
		else {
			response.sendError(000, "not a valid contact please enter valid one to edit");
			out.println("<a href='/about.jsp' ><button type='button'>Home</button></a>");
		}
	}
}
