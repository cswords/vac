package edu.monash.it.student.vac.configuration;

import java.io.IOException;

public class ServiceHelper {

	public static void main(String[] args) {
		Context r = Context.getCurrent();
		try {
			r.loadConfiguration();
			System.out.print(r.getRulePool().toString());
		} catch (IOException e) {
			System.out
					.println("Error occured on loading the configuration file.");
			e.printStackTrace();
		}
	}
}
