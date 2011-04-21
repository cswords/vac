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

	/*
	 * Notice: IPTables will lookup a domain name before applying the policy on each IP of the domain.
	 */
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
			result.setBaseProtocol(Protocol.valueOf(words[3]));
			String[] address = words[2].split(":");
			try {
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
