package com.contact;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class ContactsAdd extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		// input from user to add contact
		String fname = req.getParameter("fname");
		String lname = req.getParameter("lname");
		String[] phone_no = req.getParameterValues("mytext");
		String[] email = req.getParameterValues("mytext1");
		String dr_no = req.getParameter("dr_no");
		String state = req.getParameter("state");
		String country = req.getParameter("country");
		if (fname != "" && fname != null && lname != "" && lname != null&& phone_no[0]!=null && phone_no[0]!="") {
			if (dr_no == null)
				dr_no = "";
			if (state == null)
				state = "";
			if (country == null)
				country = "";
			try {
				for (int i = 1; i < phone_no.length-1; i++){
					if (phone_no[i] == null)
						phone_no[i] = "";}
			} catch (Exception e) {
			
			}
			try {
				for (int i = 0; i < email.length; i++) {
					if (email[i] == null)
						email[i] = "";
				}
			} catch (Exception e) {
				email[0] = "";
			}
			if (ContactsStore.addContact(fname, lname, phone_no, email, dr_no, state, country)) {
				System.out.println("successfully done");
				out.print("contact added successfully");
			} else {
				out.println("contact already there...");
				System.out.println("not done");
			}
		} else {
			resp.sendError(420, "not a contact");
			out.println("<a href='/about.jsp' ><button type='button'>Home</button></a>");
		}
	}
}