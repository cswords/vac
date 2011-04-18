package edu.monash.it.student.vac;

public abstract class Identity {

	public abstract IdentityType getIdentityType();

	public enum IdentityType {
		User, Group;
	}

	private static class UserIdentity extends Identity {

		@Override
		public IdentityType getIdentityType() {
			return IdentityType.User;
		}

	}

	private static class GroupIdentity extends Identity {

		@Override
		public IdentityType getIdentityType() {
			return IdentityType.Group;
		}

	}

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Identity Instance(IdentityType type) {
		Identity result = null;
		switch (type) {
		case User:
			result = new UserIdentity();
			break;
		case Group:
			result = new GroupIdentity();
			break;
		default:
			throw new UnsupportedOperationException(
					"Identity type not supported.");
		}
		return result;
	}

	public static Identity Instance(IdentityType type, String name) {
		Identity result = Instance(type);
		result.setName(name);
		return result;
	}

	public String toString() {
		return this.getIdentityType() + " " + this.getName();
	}
}
