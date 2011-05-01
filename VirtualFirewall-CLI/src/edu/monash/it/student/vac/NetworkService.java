package edu.monash.it.student.vac;

import java.net.*;
import java.util.*;

import edu.monash.it.student.util.EnumHelper;

/**
 * @author  xyqin1
 */
public class NetworkService {

	/**
	 * @uml.property  name="socketAddress"
	 */
	InetSocketAddress socketAddress;

	/**
	 * @uml.property  name="description"
	 */
	protected String description;

	/**
	 * @uml.property  name="id"
	 */
	protected int id;

	/**
	 * @uml.property  name="baseProtocol"
	 * @uml.associationEnd  
	 */
	protected Protocol baseProtocol;

	/**
	 * @return
	 * @uml.property  name="socketAddress"
	 */
	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	/*
	 * Notice: IPTables will lookup a domain name before applying the policy on
	 * each IP of the domain.
	 */
	/**
	 * @param socketAddress
	 * @uml.property  name="socketAddress"
	 */
	public void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 * @uml.property  name="baseProtocol"
	 */
	public Protocol getBaseProtocol() {
		return baseProtocol;
	}

	/**
	 * @param baseProtocol
	 * @uml.property  name="baseProtocol"
	 */
	public void setBaseProtocol(Protocol baseProtocol) {
		this.baseProtocol = baseProtocol;
	}

	/**
	 * @author   xyqin1
	 */
	public static enum Protocol {
		/**
		 * @uml.property  name="tCP"
		 * @uml.associationEnd  
		 */
		TCP, /**
		 * @uml.property  name="uDP"
		 * @uml.associationEnd  
		 */
		UDP;
	}

	/**
	 * @uml.property  name="acl"
	 */
	private Collection<AccessControlRule> acl = new Vector<AccessControlRule>();

	Collection<AccessControlRule> getAclPool() {
		return this.acl;
	}

	/**
	 * @return
	 * @uml.property  name="acl"
	 */
	public AccessControlRule[] getAcl() {
		AccessControlRule[] value = new AccessControlRule[this.acl.size()];
		value = this.acl.toArray(value);
		return value;
	}

	public String toString() {
		String result = "service " + this.getId() + " "
				+ this.getSocketAddress() + " " + this.getBaseProtocol()
				+ " //" + this.getDescription();
		return result;
	}

	public static NetworkService parse(String input) {
		String[] words = input.split(" ");
		NetworkService result = null;

		if (words.length >= 4) {
			result = new NetworkService();
			try {
				result.setBaseProtocol(EnumHelper.valueOf(Protocol.class, words[3]));
				String[] address = words[2].split(":");
				int port = Integer.parseInt(address[1]);
				result.setSocketAddress(InetSocketAddress.createUnresolved(
						address[0], port));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		String[] l = input.split("//");
		if (l.length >= 2) {
			result.setDescription(l[1]);
		}
		return result;
	}
}
