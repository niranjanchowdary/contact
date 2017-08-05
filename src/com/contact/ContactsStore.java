package com.contact;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class ContactsStore {
	// data store creation
	static DatastoreService contact_datastore = DatastoreServiceFactory.getDatastoreService();
	// entity
	static Entity contact;

	// adding contact to contacts
	// based on mobile number mobile0 number must be unique
	public static boolean addContact(String first_name, String last_name, String phone_no[], String email_id[],
			String door_no, String state, String country, List<BlobKey> blob_contact_image) {
		Entity phoneNumber = null;
		// checking entity whether it is there or not
		boolean check = getId1(phone_no, email_id);
		// if check false then try to add an entity
		if (check != true) {
			try {
				// local entity with mobile0 kind
				Entity contact = new Entity("mobile0", phone_no[0]);
				contact.setProperty("fname", first_name);
				contact.setProperty("lname", last_name);
				//storing phone numbers into mobile store and mobile0 
				for (int i = 0; i < phone_no.length; i++) {
					phoneNumber = new Entity("mobileStore", contact.getKey());
					phoneNumber.setProperty("mobilekey", contact.getKey());
					phoneNumber.setProperty("mobile", phone_no[i]);
					contact.setProperty("mobile" + i, phone_no[i]);
					contact_datastore.put(phoneNumber);

				}
				//storing email ids into mobile store and mobile0 
				for (int i = 0; i < email_id.length; i++) {
					phoneNumber.setProperty("emailkey", contact.getKey());
					phoneNumber.setProperty("email", email_id[i]);
					contact.setProperty("email" + i, email_id[i]);
					contact_datastore.put(phoneNumber);
				}
				contact.setProperty("drno", door_no);
				contact.setProperty("state", state);
				contact.setProperty("country", country);
				contact.setProperty("image", blob_contact_image);

				// persisting data
				contact_datastore.put(contact);
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
	public static boolean getId1(String[] phone_number, String[] email_id) {
		//list of Entities as a generic type 
		List<Entity> phoneresults = null;
		List<Entity> emailresults = null;
		
		int counter_var_1 = 0, counter_var_2 = 0;

		//searching whether the phone no is there or not 
		for (int j = 0; j < phone_number.length; j++) {
			// Key key = KeyFactory.createKey("mobile0", pho[0]);
			Filter phoneFilter = new FilterPredicate("mobile", FilterOperator.EQUAL, phone_number[j]);

			Query phonequery = new Query("mobileStore").setFilter(phoneFilter);

			System.out.println(phonequery);
			phoneresults = contact_datastore.prepare(phonequery).asList(FetchOptions.Builder.withDefaults());

			System.out.println(phoneresults);
			if (!phoneresults.isEmpty()) {
				counter_var_1++;
				System.out.println("we r here");
			}

		}
		
		//searching email whether it is there or not 
		for (int j = 0; j < email_id.length; j++) {
			Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email_id[j]);
			Query emailquery = new Query("mobileStore").setFilter(emailFilter);
			emailresults = contact_datastore.prepare(emailquery).asList(FetchOptions.Builder.withDefaults());
			if (!emailresults.isEmpty()) {
				counter_var_2++;
				System.out.println("we r here");
			}
		}

		if (counter_var_1 != 0 || counter_var_2 != 0) {
			System.out.println("here");
			return true;
		}
		return false;
	}

	// searching entity with mobile number and email and other options
	public static List<Entity> searchContact(String contact_field, String contact_details) {
		List<Entity> search_results = null;
		List<Entity> result_entity = null;
		//checking whether the contact field is a mobile,email or any other 
		if (contact_field.equals("mobile") || contact_field.equals("email")) {
			System.out.println("we r here");
			try {
				//query to retrieve the details of a particular person based on mobile or email
				Query filter_query = new Query("mobileStore").setFilter(new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details));
				search_results = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
				System.out.println(search_results);
				//checking result is empty or not 
				//if not empty then return result to client 
				if (!search_results.isEmpty()) {
					System.out.println("returned");
					Key parent_key;
					for (Entity en : search_results) {
						parent_key = (Key) en.getProperty("mobilekey");
						System.out.println(parent_key);
						result_entity = contact_datastore.prepare(new Query("mobile0").setAncestor(parent_key).setFilter(new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,FilterOperator.EQUAL, parent_key))).asList(FetchOptions.Builder.withDefaults());
						System.out.println(result_entity);
					}
					return result_entity;
				} else {
					return null;
				}
			} catch (Exception e) {

				return null;
			}
		} else {
			//fetching details of a contact user with given fields except mobile and email
			Filter key_Filter = new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details);
			Query query = new Query("mobile0").setFilter(key_Filter);
			search_results = contact_datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
			if (search_results != null && !(search_results.isEmpty())) {
				return search_results;
			} else {
				System.out.println("not done");
				return null;
			}
		}
	}

	// edit contact with the help of mobile number, email and other options
	public static boolean editContact(String contact_details, String contact_field, String new_value) {
		// TODO Auto-generated method stub
		//Beginning transaction on data store
		Transaction transaction = contact_datastore.beginTransaction();
		List<Entity> list_entities_result;
		//checking whether the contact filed is a mobile number ,email or any other 
		if (contact_field.equals("mobile") || contact_field.equals("email")) {
			// query to retrieve the details to edit the contact  
			Query filter_query = new Query("mobileStore").setFilter(new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details));
			list_entities_result = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
			if (!list_entities_result.isEmpty()) {
				try {
					Key parent_key;
					for (Entity entity : list_entities_result) {
						parent_key = (Key) entity.getProperty("mobilekey");
						Entity parent_entity = null;
						try {
							parent_entity = contact_datastore.get(parent_key);
						} catch (EntityNotFoundException e) {
							// TODO Auto-generated catch block
							return  false;
						}
						parent_entity.setProperty(contact_field, new_value);
						entity.setProperty(contact_field, new_value);
						contact_datastore.put(transaction, entity);
						contact_datastore.put(transaction, parent_entity);
						transaction.commit();
						return true;
					}
				} finally {
					if (transaction.isActive()) {
						transaction.rollback();
						return false;
					}
				}
			}
		}
		//if contact field is not a mobile or email 
		else {
			int counter_var = 0;
			try {
				List<Entity> edit_entity_result;
				//filter and query based on the user option
				Filter key_Filter = new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details);
				Query filter_query = new Query("mobile0").setFilter(key_Filter);
				edit_entity_result = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
				if (!edit_entity_result.isEmpty())
					for (Entity entity : edit_entity_result) {
						transaction = contact_datastore.beginTransaction();
						entity.setProperty(contact_field, new_value);
						contact_datastore.put(transaction, entity);
						transaction.commit();
						counter_var++;
					}
			} finally {
				if (transaction.isActive()) {
					transaction.rollback();
					return false;
				}
			}
			if (counter_var != 0)
				return true;
		}
		return false;
	}

	// deleting contact based on given input
	@SuppressWarnings("unused")
	public static String deleteContact(String contact_details, String contact_field) {
		// TODO Auto-generated method stub
		List<Entity> delete_entity_result = null;
		//checking contact filed is a mobile ,email or any other 
		if (contact_field.equals("mobile") || contact_field.equals("email")) {
			System.out.println("first");
			Query filter_query = new Query("mobileStore").setFilter(new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details));
			delete_entity_result = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
			if (!delete_entity_result.isEmpty()) {
				Key parent_key;
				System.out.println(delete_entity_result);
				for (Entity entity : delete_entity_result) {
					parent_key = (Key) entity.getProperty("mobilekey");
					entity.removeProperty(contact_field);
					System.out.println(parent_key);
					try {
						contact = contact_datastore.get(parent_key);
						System.out.println(contact);
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						return "contact not found with that given data";
					}
					//map object to iterate the entity properties 
					Map<String, Object> map_obj = contact.getProperties();
					for (Map.Entry<String, Object> obj : map_obj.entrySet()) {
						if (obj.getValue().toString().equals(contact_details)) {
							contact.removeProperty(obj.getKey());
						}
					}
					contact_datastore.put(contact);
					contact_datastore.put(entity);

					return " contact details are deleted";

				}
			} else {
				return "contact is not there";
			}
		} else if (contact_field.equals("contact")) {
			//if given option is contact then delete based on mobile number and email
			System.out.println("contact me");
			Query filter_query = new Query("mobileStore").setFilter(new FilterPredicate("mobile", FilterOperator.EQUAL, contact_details));
			System.out.println(filter_query);
			delete_entity_result = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
			System.out.println(delete_entity_result);
			if (!delete_entity_result.isEmpty()) {
				Key parent_key = null;
				Entity parent_entity = null;
				for (Entity entity : delete_entity_result) {
					parent_key = (Key) entity.getProperty("mobilekey");
					System.out.println("got key");
					contact_datastore.delete(entity.getKey());
					if (parent_key == null) {
						System.out.println("key is empty");
						parent_key = (Key) entity.getProperty("emailkeys");
					}
					try {
						parent_entity = contact_datastore.get(parent_key);
						contact_datastore.delete(parent_entity.getKey());
						return "contact is deleted successfully";
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						return "contact not found with that given data";
					}
				}
			}
		} else {
			//else then delete the contact details based on given option
			System.out.println("helo");
			Query filter_query = new Query("mobile0").setFilter(new FilterPredicate(contact_field, FilterOperator.EQUAL, contact_details));
			delete_entity_result = contact_datastore.prepare(filter_query).asList(FetchOptions.Builder.withDefaults());
			System.out.println(delete_entity_result);
			if (!delete_entity_result.isEmpty()) {
				System.out.println("deleting");
				for (Entity entity : delete_entity_result) {
					entity.removeProperty(contact_field);
					contact_datastore.put(entity);
					return "details are deleted";
				}
			}

		}
		return "contact is not deleted";
	}
}