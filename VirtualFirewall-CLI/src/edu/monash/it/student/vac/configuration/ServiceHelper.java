package edu.monash.it.student.vac.configuration;

public class ServiceHelper {

	public static void main(String[] args) {
		Context r = Context.getCurrent();
		r.loadConfiguration();
		System.out.print(r.getRulePool().toString());
	}
}
