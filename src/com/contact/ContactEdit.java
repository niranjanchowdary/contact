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
		String name = request.getParameter("name");
		String option = request.getParameter("option");
		String newvalue = request.getParameter("newvalue");
		if (name != null && name != "" && newvalue != null) {
			if (ContactsStore.editContact(name,option,newvalue)) {
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
