package edu.monash.it.student.vac;

public class AccessControlRule extends AbstractRule {

	protected Identity source;

	protected NetworkService target;

	protected Operation operation;

	@Override
	public String toIPTablesRule() {
		// iptables -A OUTPUT -p tcp --dport 80 -m owner --uid-owner xyqin1 -j
		// REJECT
		String result = "iptables -A OUTPUT -p "
				+ this.target.getBaseProtocol();
		result += " --d " + target.getSocketAddress().getHostName();
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
		this.target = target;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public enum Operation {
		DROP, REJECT, ACCEPT
	}
}
