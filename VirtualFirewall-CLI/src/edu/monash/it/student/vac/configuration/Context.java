package edu.monash.it.student.vac.configuration;

import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import edu.monash.it.student.vac.*;

public class Context {

	public static final String ConfigurationFile = "vac.conf";

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
		FileUtils.writeStringToFile(new File(ConfigurationFile), this.getRulePool()
				.toString());
	}

	public void loadConfiguration() throws IOException {
		// TODO: load rules from a text file
		List<String> conf = FileUtils.readLines(new File(ConfigurationFile));
		for (int i = 0; i < conf.size(); i++) {
			String rule = conf.get(i);
			String[] words = rule.split(" ");
			if (words.length == 0)
				continue;
			else {
				if (words[0].equals(CLI.UseCommand)) {
					if (words.length >= 2) {
						if (words[1] == "whitelist")
							this.getRulePool().setWhiteList(true);
						if (words[1] == "blacklist")
							this.getRulePool().setBlackList(true);
					}
				} else if (words[0].equals("service")) {
					// load service
					NetworkService s = null;
					if (words.length >= 4) {
						s = NetworkService.parse(rule);
					}
					if (s == null)
						continue;
					else {
						this.getRulePool().addService(s);
						// load acls
						int j = 1;
						while (i + j < conf.size()) {
							if (conf.get(i + j).startsWith("\t")) {
								AccessControlRule.Parse(conf.get(i + j)
										.substring(1), s);
							} else if (conf.get(i + j).trim().length() != 0) {
								// there is something, but not an acl rule.
								break;
							}
							j++;
						}
					}
				} else {
					continue;
				}
			}
		}
	}
}
