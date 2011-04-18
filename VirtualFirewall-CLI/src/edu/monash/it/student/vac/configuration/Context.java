package edu.monash.it.student.vac.configuration;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import edu.monash.it.student.vac.RulePool;

public class Context {

	protected static Context currentContext;

	public static Context getCurrent() {
		if (currentContext == null)
			currentContext = new Context();
		return currentContext;
	}

	private RulePool pool;

	private Context() {
		this.pool = new RulePool();
	}

	public RulePool getRulePool() {
		return this.pool;
	}

	public void saveConfiguration() throws IOException {
		FileUtils.writeStringToFile(new File("vac.conf"), this.getRulePool()
				.toString());
	}

	public void loadConfiguration() {
		// TODO: load rules from a text file
	}
}
