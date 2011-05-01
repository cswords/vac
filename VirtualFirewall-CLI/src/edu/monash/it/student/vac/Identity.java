package edu.monash.it.student.vac;

/**
 * @author  xyqin1
 */
public abstract class Identity {

	public abstract IdentityType getIdentityType();

	/**
	 * @author   xyqin1
	 */
	public static enum IdentityType {
		/**
		 * @uml.property  name="user"
		 * @uml.associationEnd  
		 */
		User, /**
		 * @uml.property  name="group"
		 * @uml.associationEnd  
		 */
		Group;
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

	/**
	 * @uml.property  name="name"
	 */
	protected String name;

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
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
