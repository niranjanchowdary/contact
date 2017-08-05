package com.contact;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;



@SuppressWarnings("serial")
public class ContactSearch extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		// input from user to search contact
		String contact_field = req.getParameter("option");
		String contact_details = req.getParameter("value");
		if (contact_details != null && contact_details != "") {
		List<Entity> list_entity_obj =   ContactsStore.searchContact(contact_field, contact_details);
			if (list_entity_obj == null) {
				resp.sendError(420, "sorry contact is there");
			} else {
				//redirecting to result page 
				req.setAttribute("list_entity_obj", list_entity_obj);
				req.getRequestDispatcher("/searchResult.jsp").include(req, resp);
				
				
			}
		} 
		//invalid details from user side 
		else {
			resp.sendError(120, "not a valid contact please enter valid one");
			out.println("<a href='/about.jsp' ><button type='button'>Home</button></a>");
		}
	}

}
