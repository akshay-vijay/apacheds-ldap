package com.apacheds.novell.ldap;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.apacheds.novell.ldap.dao.LdapConnectionDao;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;

public class LdapConnectionPool {
	
	private static LdapConnectionDao dao;
	private static final Logger log = Logger.getLogger(LdapConnectionPool.class);
	private static LinkedList<LDAPConnection> unLocked;
	private static LinkedList<LDAPConnection> locked;
	
	static {
		unLocked = new LinkedList<LDAPConnection>();
		locked = new LinkedList<LDAPConnection>();
		System.out.println("Connection pool has been initialized");
	}
	
	private LdapConnectionPool () {
		
	}
	
	public static synchronized LDAPConnection getLdapConnection() {
		
		if(unLocked.size() == 0 && locked.size() == dao.getPoolSize()) {
			LinkedList<LDAPConnection> temp = unLocked;
			unLocked = locked;
			locked = temp;
		}
		
		if(unLocked.size()>0) {
			LDAPConnection lc = unLocked.getFirst();unLocked.removeFirst();locked.addFirst(lc);
			System.out.println("getting LDAP connection from connection pool");
			return lc;
		}
		else if(unLocked.size() + locked.size() < dao.getPoolSize()) {
			LDAPConnection ldapConnection = new LDAPConnection();
			try {
				ldapConnection.connect( dao.getLdapHost(), dao.getLdapPort() );
				ldapConnection.bind( dao.getLdapVersion(), dao.getLoginDN(), dao.getPassword().getBytes("UTF8") );
				locked.add(ldapConnection);
				System.out.println("LDAP connection (" +unLocked.size() + locked.size()+ ") created successfully");
	            return ldapConnection;
	        }
	        catch( LDAPException e ) {
	        	System.out.println( "Error: " + e.toString() );
	        }
	        catch( UnsupportedEncodingException e ) {
	        	System.out.println( "Error: " + e.toString() );
	        }
			return new LDAPConnection();
		}
		else {
			System.out.println("Connection pool is full");
			return new LDAPConnection();
		}
	}
	
	public static void setDao(LdapConnectionDao dao) {
		if(LdapConnectionPool.dao == null) {
			LdapConnectionPool.dao = dao;
		}
		else {
			System.out.println("LDAP connection is already exist, Ignoring this DAO, Using LDAP connection from pool");
		}
	}
}
