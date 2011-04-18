package edu.monash.it.student.vac;

import java.util.*;

public class RulePool {

	private boolean whiteList = true;

	public boolean isWhiteList() {
		return whiteList;
	}

	public void setWhiteList(boolean whiteList) {
		this.whiteList = whiteList;
	}

	public boolean isBlackList() {
		return !whiteList;
	}

	public void setBlackList(boolean blackList) {
		this.whiteList = !blackList;
	}

	private List<NetworkService> services = new Vector<NetworkService>();

	public boolean addService(NetworkService service) {
		for (NetworkService s : this.services) {
			if (s.getSocketAddress().equals(service.getSocketAddress()))
				return false;
		}
		service.setId(services.size());
		this.services.add(service);
		return true;
	}

	public void removeServiceAt(int id) {
		services.remove(id);
		for (int i = id; i < services.size(); i++) {
			services.get(i).setId(i);
		}
	}

	public String toIPTablesRule() {
		String result = "iptables -F OUTPUT\n";
		for (NetworkService service : this.services) {
			AccessControlRule[] acl = service.getAcl();
			for (AccessControlRule rule : acl) {
				result += rule.toIPTablesRule() + "\n";
			}
		}
		result += this.whiteList ? "iptables -A OUTPUT -j DROP\n"
				: "iptables -A OUTPUT -j ACCEPT\n";
		return result;
	}

	public String toString() {
		String result = "";
		result += this.whiteList ? "use whitelist" : "use blacklist";
		for (NetworkService service : this.services) {
			result += "\n" + service;
			AccessControlRule[] acl = service.getAcl();
			for (AccessControlRule rule : acl) {
				result += "\n\t" + rule;
			}
		}
		return result;
	}
}
