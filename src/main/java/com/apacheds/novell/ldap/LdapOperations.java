package com.apacheds.novell.ldap;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;

public class LdapOperations {
	
	private static final Logger log = Logger.getLogger(LdapOperations.class);
	
	public static boolean addEntry(String dn, HashMap<String, String> attrs) {
		
		LDAPAttributeSet attributeSet = new LDAPAttributeSet();
		
		Set<String> keys = attrs.keySet();
		
		Iterator<String> itr = keys.iterator();
		
		while(itr.hasNext()) {
			String key = itr.next();
			attributeSet.add(new LDAPAttribute(key,attrs.get(key)));
		}
		
		LDAPEntry newEntry = new LDAPEntry( dn, attributeSet);
		try {
			LdapConnectionPool.getLdapConnection().add( newEntry );
			return true;
		} catch (LDAPException e) {
			System.out.println( "Error:  " + e.toString());
		}
		
		return false;
	}
	
	public static boolean deleteEntry(String dn) { 
		
		try {
			LdapConnectionPool.getLdapConnection().delete(dn);
			return true;
		} catch (LDAPException e1) {
			System.out.println( "Error:  " + e1.toString());
		}
		return false;
	}
	
	public static boolean modifyEntry(String dn, HashMap<String, String> attrs) {
		
		Set<String> keys = attrs.keySet();
		
		Iterator<String> itr = keys.iterator();
		
		while(itr.hasNext()) {
			String key = itr.next();
			modifyAttribute(dn, key,attrs.get(key));
		}
		return true;
	}
	
	public static boolean addAttribute(String dn, String desc, String value) {

		LDAPAttribute attribute = new LDAPAttribute( desc, value);
		
		try {
			LdapConnectionPool.getLdapConnection().modify(dn, new LDAPModification(LDAPModification.ADD, attribute));
			return true;
		} catch (LDAPException e) {
			System.out.println( "Error:  " + e.toString());
		}
		return false;
	}
	
	public static boolean deleteAttribute(String dn, String desc) {

		LDAPAttribute attribute = new LDAPAttribute( desc);
		
		try {
			LdapConnectionPool.getLdapConnection().modify(dn, new LDAPModification(LDAPModification.DELETE, attribute));
			return true;
		} catch (LDAPException e) {
			System.out.println( "Error:  " + e.toString());
		}
		return false;
	}
	
	public static boolean modifyAttribute(String dn, String desc, String value) {

		LDAPAttribute attribute = new LDAPAttribute( desc, value);
		
		try {
			LdapConnectionPool.getLdapConnection().modify(dn, new LDAPModification(LDAPModification.REPLACE, attribute));
			return true;
		} catch (LDAPException e) {
			System.out.println( "Error:  " + e.toString());
		}
		return false;
	}
	
public static String search(String searchBase, String searchFilter) {
		
		int searchScope = LDAPConnection.SCOPE_SUB;
        boolean attributeOnly = false;
        String attrs[] = {LDAPConnection.ALL_USER_ATTRS};
        String result="";
        
        LDAPSearchResults searchResults;
		try {
			searchResults = LdapConnectionPool.getLdapConnection().search( searchBase, searchScope, searchFilter, attrs, attributeOnly);
		} catch (LDAPException e1) {
			System.out.println("Error: " + e1.toString());
			return result;
		}
        
        while ( searchResults.hasMore()) {

            LDAPEntry nextEntry = null;

            try {
                nextEntry = searchResults.next();
            }
            catch(LDAPException e) {
                System.out.println("Error: " + e.toString());
                continue;
            }
            
           LDAPAttributeSet atrrSet = nextEntry.getAttributeSet();
           System.out.println("DN: "+nextEntry.getDN());
           result += "DN: "+nextEntry.getDN()+"\n";
           Iterator<LDAPAttribute> itr = atrrSet.iterator();
           
           while(itr.hasNext()) {
        	   LDAPAttribute ldapAttr = itr.next();
        	   
        	   System.out.println("Attr Name: " + ldapAttr.getName());
        	   result += "Attr Name: " + ldapAttr.getName()+"\n";
        	   System.out.println("Value(s): ");
        	   result += "Value(s): "+"\n";
        	   
        	   Enumeration<String> allValues = ldapAttr.getStringValues();
        	   
               if( allValues != null) {

                   while(allValues.hasMoreElements()) {
                       String Value = allValues.nextElement();
                       System.out.println(Value);
                       result += Value+"\n";
                   }
               }
           }
        }
        	return result;
	}
}
