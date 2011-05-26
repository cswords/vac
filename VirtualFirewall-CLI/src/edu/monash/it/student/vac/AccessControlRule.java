package edu.monash.it.student.vac;

import edu.monash.it.student.util.EnumHelper;
import edu.monash.it.student.vac.Identity.IdentityType;

/**
 * @author xyqin1
 */
public class AccessControlRule extends AbstractRule {

	/**
	 * @uml.property name="source"
	 * @uml.associationEnd
	 */
	protected Identity source;

	/**
	 * @uml.property name="target"
	 * @uml.associationEnd
	 */
	protected NetworkService target;

	/**
	 * @uml.property name="operation"
	 * @uml.associationEnd
	 */
	protected Operation operation;

	@Override
	public String toIPTablesRule() {
		// iptables -A OUTPUT -p TCP -d google.com --dport 80 -m owner
		// --uid-owner xyqin1 -j DROP
		String result = "iptables -A OUTPUT -p "
				+ this.target.getBaseProtocol();
		result += " -d " + target.getSocketAddress().getHostName();
		result += " --dport " + target.getSocketAddress().getPort();
		result += " -m owner";
		switch (source.getIdentityType()) {
		case User:
			result += " --uid-owner " + source.getName();
			break;
		case Group:
			result += " --gid-owner " + source.getName();
			break;
		default:
			throw new UnsupportedOperationException(
					"Identity type not supported.");
		}
		result += " -j " + this.getOperation();
		return result;
	}

	/**
	 * @return
	 * @uml.property name="source"
	 */
	public Identity getSource() {
		return source;
	}

	/**
	 * @param source
	 * @uml.property name="source"
	 */
	public void setSource(Identity source) {
		this.source = source;
	}

	/**
	 * @return
	 * @uml.property name="target"
	 */
	public NetworkService getTarget() {
		return target;
	}

	/**
	 * @param target
	 * @uml.property name="target"
	 */
	public void setTarget(NetworkService target) {
		if (this.getTarget() != null)
			this.target.getAclPool().remove(this);
		target.getAclPool().add(this);
		this.target = target;
	}

	/**
	 * @return
	 * @uml.property name="operation"
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 * @uml.property name="operation"
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @author xyqin1
	 */
	public static enum Operation {
		/**
		 * @uml.property name="dROP"
		 * @uml.associationEnd
		 */
		DROP, /**
		 * @uml.property name="rEJECT"
		 * @uml.associationEnd
		 */
		REJECT, /**
		 * @uml.property name="aCCEPT"
		 * @uml.associationEnd
		 */
		ACCEPT
	}

	public String toString() {
		String result = this.operation + " " + this.getSource();
		return result;
	}

	public static AccessControlRule Parse(String input, NetworkService service) {
		String[] words = input.split(" ");
		AccessControlRule rule = null;
		if (words.length >= 3) {
			Operation o = EnumHelper.valueOf(Operation.class, words[0]);
			if (o == null)
				return null;
			IdentityType type = EnumHelper
					.valueOf(IdentityType.class, words[1]);
			if (type == null)
				return null;
			String name = words[2];
			service.deleteAccessControlRule(o, type, name);
			rule = new AccessControlRule();
			rule.setOperation(o);
			Identity i = Identity.Instance(type);
			i.setName(name);
			rule.setSource(i);
			rule.setTarget(service);
		}
		return rule;
	}
}
