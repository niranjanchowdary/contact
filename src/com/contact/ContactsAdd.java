package com.contact;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class ContactsAdd extends HttpServlet {
	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		// input from user to add contact
		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");
		String first_name = req.getParameter("fname");
		String last_name = req.getParameter("lname");
		String[] phone_number = req.getParameterValues("mytext");
		String[] email_id = req.getParameterValues("mytext1");
		String door_number = req.getParameter("dr_no");
		String state = req.getParameter("state");
		String country = req.getParameter("country");
		//Checking user data
		if (first_name != "" && first_name != null && last_name != "" && last_name != null&& phone_number[0]!=null && phone_number[0]!="") {
			if (door_number == null)
				door_number = "";
			if (state == null)
				state = "";
			if (country == null)
				country = "";
			
			try {
				for (int i = 0; i < email_id.length; i++) {
					if (email_id[i] == null)
						email_id[i] = "";
				}
			} catch (Exception e) {
				email_id[0] = "";
			}
			if (ContactsStore.addContact(first_name, last_name, phone_number, email_id, door_number, state, country,blobKeys)) {
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
