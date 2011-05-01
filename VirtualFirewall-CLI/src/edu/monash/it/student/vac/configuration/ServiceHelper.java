package edu.monash.it.student.vac.configuration;

import java.io.IOException;

public class ServiceHelper {

	public static void main(String[] args) {
		try {
			Context r = Context.getCurrent();
			r.loadConfiguration();
			try {
				String[] commands = r.getRulePool().toIPTablesRule()
						.split("\n");
				// commands = new String[] { "ping google.com" }; //test the
				// output
				for (String command : commands) {
					String[] commandWords = command.split(" ");
					Process child = Runtime.getRuntime().exec(commandWords);
					CLI.pipeOutput(child);
					child.waitFor();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out
					.println("Error occured on loading the configuration file.");
			e.printStackTrace();
		}
	}
}
