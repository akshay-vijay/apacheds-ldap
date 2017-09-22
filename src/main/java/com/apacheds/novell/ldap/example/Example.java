package com.apacheds.novell.ldap.example;

import java.io.IOException;
import java.util.HashMap;

import com.apacheds.novell.ldap.LdapConnectionPool;
import com.apacheds.novell.ldap.LdapOperations;
import com.apacheds.novell.ldap.dao.LdapConnectionDao;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Example {
	
	    public static void main(String argv[]) {
	    	
	    	// Example to add an Entry into LDAP

	    	String connectionDetails = "{\"ldapHost\":\"127.0.0.1\",\"ldapPort\":\"10389\",\"loginDN\":\"uid=admin,ou=system\",\"password\":\"secret\",\"ldapVersion\":\"3\",\"poolSize\":\"1\"}"; 
	        String dn = "cn=First Entry,dc=example,dc=com";
	        String attributesMapping = "{\"description\": \"First Entry\",     	\"objectclass\": \"inetOrgPerson\",     	\"givenname\": \"XYZ\",     	\"mail\": \"xyz@example.com\",     	\"sn\": \"snEntry\",     	\"uid\": \"firstentry\",     	\"userpassword\": \"unkown\"}";
	        
	        System.out.println("*** Trying to add an entry into LDAP ***");
	        System.out.println("Entry Dn: "+dn);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        LdapConnectionDao dao = null;
	        HashMap<String, String> map=null;
	        try {
				dao = mapper.readValue(connectionDetails, LdapConnectionDao.class);
				map = mapper.readValue(attributesMapping, new HashMap<String, String>().getClass());
			} catch (IOException e) {
				System.out.println(e);
			}
	        
	        LdapConnectionPool.setDao(dao);
	        
	        System.out.println("Entry added: "+LdapOperations.addEntry(dn, map));
	        
	        String searchBase = "dc=hpe,dc=com";
	        String searchFilter = "(objectclass=top)";
	        
	        System.out.println("*** Trying to search an entry into LDAP ***");
	        
	        String data = LdapOperations.search(searchBase, searchFilter);
	        System.out.println("Search Data: "+data);
	        
	        System.out.println("*** Trying to update an entry into LDAP ***");
	        
	        String updatedAttributesMapping = "{\"description\": \"Updated First Entry\",     	\"objectclass\": \"inetOrgPerson\",     	\"givenname\": \"XYZ\",     	\"mail\": \"xyz@example.com\",     	\"sn\": \"snEntry\",     	\"uid\": \"firstentry\",     	\"userpassword\": \"unkown\"}";
	        System.out.println("Entry updated: "+LdapOperations.modifyEntry(dn, map));
	        
	        System.out.println("*** Trying to delete an attribute from an entry into LDAP ***");
	        
	        String description = "description";
	        System.out.println("Attribute deleted: "+LdapOperations.deleteAttribute(dn, description));
	        
	        System.out.println("*** Trying to delete an entry into LDAP ***");
	        System.out.println("Entry deleted: "+LdapOperations.deleteEntry(dn));
	    }
}
