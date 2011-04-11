package edu.monash.it.student.vac;

import java.util.*;

public class Context {

	private Context() {
	}

	protected static Context currentContext;

	public static Context getCurrent() {
		if (currentContext == null)
			currentContext = new Context();
		return currentContext;
	}

	Collection<NetworkService> networkServices = new LinkedList<NetworkService>();

	public void addService(NetworkService service) {
		service.setId(this.networkServices.size());
		this.networkServices.add(service);
	}

	public NetworkService getServiceAt(int id) {
		return ((LinkedList<NetworkService>) this.networkServices).get(id);
	}

	Collection<AccessControlRule> rules = new LinkedList<AccessControlRule>();

	public void addRule(AccessControlRule rule) {
		this.rules.add(rule);
	}

	public AccessControlRule getRuleForService(NetworkService service) {
		for (AccessControlRule rule : this.rules) {
			if (rule.getTarget().id == service.id) {
				return rule;
			}
		}
		return null;
	}
}
