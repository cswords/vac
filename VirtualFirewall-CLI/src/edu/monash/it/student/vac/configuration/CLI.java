package edu.monash.it.student.vac.configuration;

public class CLI {

	private static CLI instance = null;

	public static CLI getCurrent() {
		if (instance == null)
			instance = new CLI();
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO load conf, config it.

	}

}
