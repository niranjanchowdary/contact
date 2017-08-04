package com.contact;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.ParseConversionEvent;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
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
			String state, String country,List<BlobKey> blob) {
		Entity phoneNumber = null;
		// checking entity whether it is there or not
		boolean check = getId1(pho, email);
		// if check false then try to add an entity
		if (check != true) {
			try {

				Entity contact = new Entity("mobile0", pho[0]);
				// phoneNumber= new Entity("mobile", contact.getKey());
				// emails= new Entity("emailId", contact.getKey());
				contact.setProperty("fname", fname);
				contact.setProperty("lname", lname);
				for (int i = 0; i < pho.length; i++) {
					phoneNumber = new Entity("mobileStore", contact.getKey());
					phoneNumber.setProperty("mobilekey", contact.getKey());
					phoneNumber.setProperty("mobile", pho[i]);
					contact.setProperty("mobile" + i, pho[i]);
					datastore.put(phoneNumber);

				}
				for (int i = 0; i < email.length; i++) {
					// emails = new Entity("emailId", contact.getKey());
					phoneNumber.setProperty("emailkey", contact.getKey());
					phoneNumber.setProperty("email", email[i]);
					contact.setProperty("email" + i, email[i]);
					datastore.put(phoneNumber);
				}
				contact.setProperty("drno", dr_no);
				contact.setProperty("state", state);
				contact.setProperty("country", country);
				contact.setProperty("image", blob);

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
	public static boolean getId1(String[] pho, String[] email) {
		List<Entity> phoneresults = null;
		List<Entity> emailresults = null;
		int count = 0, count1 = 0;

		for (int j = 0; j < pho.length; j++) {
			// Key key = KeyFactory.createKey("mobile0", pho[0]);
			Filter phoneFilter = new FilterPredicate("mobile", FilterOperator.EQUAL, pho[j]);

			Query phonequery = new Query("mobileStore").setFilter(phoneFilter);

			System.out.println(phonequery);
			phoneresults = datastore.prepare(phonequery).asList(FetchOptions.Builder.withDefaults());

			System.out.println(phoneresults);
			if (!phoneresults.isEmpty()) {
				count++;
				System.out.println("we r here");
			}

		}
		for (int j = 0; j < email.length; j++) {
			// Key key = KeyFactory.createKey("mobile0", pho[0]);
			Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email[j]);
			Query emailquery = new Query("mobileStore").setFilter(emailFilter);
			emailresults = datastore.prepare(emailquery).asList(FetchOptions.Builder.withDefaults());
			if (!emailresults.isEmpty()) {
				count1++;
				System.out.println("we r here");
			}
		}

		if (count != 0 || count1 != 0) {
			System.out.println("here");
			return true;
		}
		return false;

	}

	// searching entity with mobile number and email and other options
	public static List<Entity> searchContact(String attribute, String name) {
		List<Entity> results = null;
		List<Entity> en1 = null;
		if (attribute.equals("mobile") || attribute.equals("email")) {
			System.out.println("we r here");
			try {

				Query query = new Query("mobileStore")
						.setFilter(new FilterPredicate(attribute, FilterOperator.EQUAL, name));
				results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				System.out.println(results);
				if (!results.isEmpty()) {
					System.out.println("returned");
					Key key;
					for (Entity en : results) {
						key = (Key) en.getProperty("mobilekey");
						System.out.println(key);
						en1 = datastore
								.prepare(new Query("mobile0").setAncestor(key)
										.setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,
												FilterOperator.EQUAL, key)))
								.asList(FetchOptions.Builder.withDefaults());
						System.out.println(en1);
					}
					return en1;
				} else {
					return null;
				}
			} catch (Exception e) {

				return null;
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

	// edit contact with the help of mobile number, email and other options
	public static boolean editContact(String name, String option, String value) {
		// TODO Auto-generated method stub
		Transaction txn = datastore.beginTransaction();
		List<Entity> results;
		if (option.equals("mobile") || option.equals("email")) {
			// for (int i = 1;; i++) {
			Query query = new Query("mobileStore").setFilter(new FilterPredicate(option, FilterOperator.EQUAL, name));
			results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if (!results.isEmpty()) {
				try {
					Key key;
					for (Entity entity : results) {
						key = (Key) entity.getProperty("mobilekey");
						Entity parent = null;
						try {
							parent = datastore.get(key);
						} catch (EntityNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						parent.setProperty(option, value);
						entity.setProperty(option, value);
						datastore.put(txn, entity);
						datastore.put(txn, parent);
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

		else { int count = 0;
			try {
				List<Entity> result;
				Filter keyFilter = new FilterPredicate(option, FilterOperator.EQUAL, name);
				Query query = new Query("mobile0").setFilter(keyFilter);
				result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				if (!result.isEmpty())
					for (Entity entity : result) {
						 txn = datastore.beginTransaction();
						entity.setProperty(option, value);
						datastore.put(txn, entity);
						txn.commit();
					count++;
					}
			} finally {
				if (txn.isActive()) {
					txn.rollback();
					return false;
				}
			}
			if(count!=0)
			return true;
		}
		return false;
	}

	// deleting contact based on given input
	@SuppressWarnings("unused")
	public static String deleteContact(String name, String attribute) {
		// TODO Auto-generated method stub
		List<Entity> result = null;
		if (attribute.equals("mobile") || attribute.equals("email")) {
			System.out.println("first");
			Query query = new Query("mobileStore")
					.setFilter(new FilterPredicate(attribute, FilterOperator.EQUAL, name));
			result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if (!result.isEmpty()) {
				Key key;
				//Entity contact = null;
				System.out.println(result);
				for (Entity entity : result) {
					key = (Key) entity.getProperty("mobilekey");
					entity.removeProperty(attribute);
					System.out.println(key);
					try {
						contact = datastore.get(key);
						System.out.println(contact);
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						return "contact not found with that given data";
					}
			 Collection<Object> map_obj = contact.getProperties().values();
			 for(Object vl:map_obj){
				 int i=0;
				 if(vl.toString().equals(name))
				 {
					 contact.removeProperty(attribute+i);
					 
				 }
				 i++;
			 }
					

					datastore.put(contact);
					datastore.put(entity);
					
					return " contact details are deleted";
				}
			} else {
				return "contact is not there";
			}
		}
		else if(attribute.equals("contact")){
			System.out.println("contact me");
			Query query = new Query("mobileStore")
					.setFilter(new FilterPredicate("mobile", FilterOperator.EQUAL, name));
			System.out.println(query);
			result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			System.out.println(result);
			if(!result.isEmpty()){
				Key key=null;
				Entity parent=null;
				for(Entity entity :result){
					key=(Key) entity.getProperty("mobilekey");
					System.out.println("got key");
					datastore.delete(entity.getKey());
					if(key==null){
						System.out.println("key is empty");
						key=(Key)entity.getProperty("emailkeys");
					}
					try {
						parent = datastore.get(key);
						datastore.delete(parent.getKey());
						return "contact is deleted successfully";
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						return "contact not found with that given data";
					}
				}
			}
		}
		else{
			System.out.println("helo");
			Query query = new Query("mobile0")
					.setFilter(new FilterPredicate(attribute, FilterOperator.EQUAL, name));
			result = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			System.out.println(result);
			if (!result.isEmpty()) {
				System.out.println("deleting");
				for(Entity ent: result){
					ent.removeProperty(attribute);
					datastore.put(ent);
					return "details are deleted";
				}
			}
			
		}
		return "contact is not deleted";
	}
}