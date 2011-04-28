package edu.monash.it.student.vac;

import edu.monash.it.student.util.EnumHelper;
import edu.monash.it.student.vac.Identity.IdentityType;

public class AccessControlRule extends AbstractRule {

	protected Identity source;

	protected NetworkService target;

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

	public Identity getSource() {
		return source;
	}

	public void setSource(Identity source) {
		this.source = source;
	}

	public NetworkService getTarget() {
		return target;
	}

	public void setTarget(NetworkService target) {
		if (this.getTarget() != null)
			this.target.getAclPool().remove(this);
		target.getAclPool().add(this);
		this.target = target;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public static enum Operation {
		DROP, REJECT, ACCEPT
	}

	public String toString() {
		String result = this.operation + " " + this.getSource();
		return result;
	}

	public static AccessControlRule Parse(String input, NetworkService service) {
		String[] words = input.split(" ");
		AccessControlRule rule = new AccessControlRule();
		if (words.length >= 3) {
			Operation o=EnumHelper.valueOf(Operation.class, words[0]);
			if(o==null)
				return null;
			rule.setOperation(o);
			IdentityType type = EnumHelper.valueOf(IdentityType.class, words[1]);
			Identity i = Identity.Instance(type);
			i.setName(words[2]);
			rule.setSource(i);
			rule.setTarget(service);
		}
		return rule;
	}
}
