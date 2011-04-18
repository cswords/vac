package edu.monash.it.student.vac;

import java.net.*;
import java.util.*;

public class NetworkService {

	InetSocketAddress socketAddress;

	protected String description;

	protected int id;

	protected Protocol baseProtocol;

	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Protocol getBaseProtocol() {
		return baseProtocol;
	}

	public void setBaseProtocol(Protocol baseProtocol) {
		this.baseProtocol = baseProtocol;
	}

	public enum Protocol {
		TCP, UDP
	}

	private Collection<AccessControlRule> acl = new Vector<AccessControlRule>();

	Collection<AccessControlRule> getAclPool() {
		return this.acl;
	}

	public AccessControlRule[] getAcl() {
		AccessControlRule[] value = new AccessControlRule[this.acl.size()];
		value = this.acl.toArray(value);
		return value;
	}

	public String toString() {
		String result = "service " + this.getId()
				+ " " + this.getSocketAddress() + " " + this.getBaseProtocol() + " //" + this.getDescription();
		return result;
	}
}
