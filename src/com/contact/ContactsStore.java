package com.contact;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class ContactsStore {
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	static Entity contact;

	// adding contact to contacts
	// based on mobile number mobile0 number must be unique
	public static boolean addContact(String fname, String lname, String pho[], String email[], String dr_no,
			String state, String country) {
		// checking entity whether it is there or not
		boolean check = getId1(pho);
		// if check false then try to add an entity
		if (check != true) {
			try {

				Entity contact = new Entity("mobile0", pho[0]);
				contact.setProperty("fname", fname);
				contact.setProperty("lname", lname);
				for (int i = 0; i < pho.length; i++)
					contact.setProperty("mobile" + i, pho[i]);
				for (int i = 0; i < email.length; i++)
					contact.setProperty("email" + i, email[i]);
				contact.setProperty("drno", dr_no);
				contact.setProperty("state", state);
				contact.setProperty("country", country);

				// persisting data
				datastore.put(contact);
				return true;
			} catch (NullPointerException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	// checking mobile number is there or not if it is there means then return
	// true else false
	public static boolean getId1(String[] pho) {
		for (int i = 0; i < pho.length; i++) {
			Query query = new Query("mobile0").setFilter(new FilterPredicate("mobile" + i, FilterOperator.EQUAL, pho[i]));
			Entity results = datastore.prepare(query).asSingleEntity();
			if (results != null) {
				return true;
			}
		}
		return false;

	}

	//searching entity with mobile number and email and other options 
	public static List<Entity> searchContact(String attribute, String name) {
		List<Entity> results;
		if (attribute.equals("mobile")) {
			System.out.println("we r here");
			for (int i = 0;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + attribute + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null)
					return results;
			}
		} else if (attribute.equals("email")) {
			for (int i = 0;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + attribute + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null)
					return results;
			}
		} else {
			Filter keyFilter = new FilterPredicate("" + attribute + "", FilterOperator.EQUAL, name);
			Query query = new Query("mobile0").setFilter(keyFilter);
			results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if (results != null && !(results.isEmpty())) {
				return results;
			} else {
				System.out.println("not done");
				return null;
			}
		}
	}

	//edit contact with the help of mobile number, email and other options  
	public static boolean editContact(String name, String option, String value) {
		// TODO Auto-generated method stub
		Transaction txn = datastore.beginTransaction();
		List<Entity> results;
		if (option.equals("mobile")) {
			for (int i = 1;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + option + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null && !results.isEmpty()) {
					try {
						for (Entity entity : results) {
							entity.setProperty(option + i, value);
							datastore.put(txn, entity);
							txn.commit();
							return true;
						}
					} finally {
						if (txn.isActive()) {
							txn.rollback();
							return false;
						}
					}
				}
			}

		}
		if (option.equals("email")) {
			for (int i = 0;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + option + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null && !results.isEmpty()) {
					try {
						for (Entity entity : results) {
							entity.setProperty(option + i, value);
							datastore.put(txn, entity);
							txn.commit();
							return true;
						}
					} finally {
						if (txn.isActive()) {
							txn.rollback();
							return false;
						}
					}
				}
			}
		} else {
			try {
				List<Entity> result;
				Filter keyFilter = new FilterPredicate("" + option + "", FilterOperator.EQUAL, name);
				Query query = new Query("mobile0").setFilter(keyFilter);
				result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (!result.isEmpty() && result != null)
					for (Entity entity : result) {
						entity.setProperty("" + option + "", value);
						datastore.put(txn, entity);
						txn.commit();
						return true;
					}
			} finally {
				if (txn.isActive()) {
					txn.rollback();
					return false;
				}
			}

		}
		return false;
	}
	// deleting contact based on given input 
	public static String deleteContact(String name, String attribute) {
		// TODO Auto-generated method stub
		String nn = null;
		Transaction txt = datastore.beginTransaction();
		List<Entity> results;
		if (attribute.equals("mobile")) {
			for (int i = 0;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + attribute + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null && !results.isEmpty()) {
					try {
						for (Entity entity : results)
							nn = (String) entity.getProperty("mobile0");
						Key key = KeyFactory.createKey("mobile0", nn);
						datastore.delete(txt, key);
						txt.commit();
						return "details are deletred";
					} finally {
						if (txt.isActive()) {
							txt.rollback();
							return "not deleted";
						}
					}
				}
			}
		} else if (attribute.equals("email")) {
			for (int i = 0;; i++) {
				Query query = new Query("mobile0")
						.setFilter(new FilterPredicate("" + attribute + i + "", FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (results != null && !results.isEmpty()) {
					try {
						for (Entity entity : results) {
							entity.removeProperty(attribute);
							datastore.put(txt, entity);
							txt.commit();
						}
					} finally {
						if (txt.isActive()) {
							txt.rollback();
							return "contact not deleted";
						}
					}
					return "deleted contact details ";
				}
			}
		} else {
			Filter keyFilter = new FilterPredicate("" + attribute + "", FilterOperator.EQUAL, name);
			Query query = new Query("mobile0").setFilter(keyFilter);
			results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if (results != null && !(results.isEmpty())) {
				try {
					for (Entity entity : results) {
						entity.removeProperty(attribute);
						datastore.put(txt, entity);
						txt.commit();
					}
				} finally {
					if (txt.isActive()) {
						txt.rollback();
						return "not deleted";
					}
				}
				return "contact is deleted";
			} else {
				return "Contact is not deleted ";
			}
		}

	}

}