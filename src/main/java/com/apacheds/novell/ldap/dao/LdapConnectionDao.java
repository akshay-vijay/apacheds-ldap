package com.apacheds.novell.ldap.dao;

import java.io.Serializable;

public class LdapConnectionDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ldapHost;

	private int ldapPort;
		
    private String loginDN;

    private String password;

    private int ldapVersion;
    
    private int poolSize;
    
	public String getLdapHost() {
		return ldapHost;
	}

	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public int getLdapPort() {
		return ldapPort;
	}

	public void setLdapPort(int ldapPort) {
		this.ldapPort = ldapPort;
	}

	public String getLoginDN() {
		return loginDN;
	}

	public void setLoginDN(String loginDN) {
		this.loginDN = loginDN;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLdapVersion() {
		return ldapVersion;
	}

	public void setLdapVersion(int ldapVersion) {
		this.ldapVersion = ldapVersion;
	}
	
	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	@Override
	public String toString() {
		return "LdapConnectionDao [ldapHost=" + ldapHost + ", ldapPort=" + ldapPort + ", loginDN=" + loginDN
				+ ", password=" + password + ", ldapVersion=" + ldapVersion + ", poolSize=" + poolSize + "]";
	}
}
