package edu.monash.it.student.vac;

import java.util.*;

import edu.monash.it.student.vac.Identity.IdentityType;

/**
 * @author  xyqin1
 */
public class RulePool {

	/**
	 * @uml.property  name="whiteList"
	 */
	private boolean whiteList = true;

	/**
	 * @return
	 * @uml.property  name="whiteList"
	 */
	public boolean isWhiteList() {
		return whiteList;
	}

	/**
	 * @param whiteList
	 * @uml.property  name="whiteList"
	 */
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

	public NetworkService getServiceById(int id) {
		return this.services.get(id);
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

	public String getCliendCmd(String vac, String user, String group) {
		// ssh -N -f -L 80:10.1.1.2:80 xyqin1@10.2.2.1
		String result = "# You probably need to change your port before executing this file!\n";
		for (NetworkService service : this.services) {
			for (AccessControlRule rule : service.getAcl()) {
				if ((rule.getSource().getIdentityType() == IdentityType.User & rule
						.getSource().getName().equals(user))
						| (rule.getSource().getIdentityType() == IdentityType.Group & rule
								.getSource().getName().equals(group))) {
					String host = service.getSocketAddress().getAddress() == null ? service
							.getSocketAddress().getHostName().toString()
							: service.getSocketAddress().getAddress()
									.toString();
					result += "ssh -N -f -L "
							+ service.getSocketAddress().getPort() + ":" + host
							+ ":" + service.getSocketAddress().getPort() + " "
							+ user + "@" + vac + "\n";
				}
			}
		}
		return result;
	}
}
