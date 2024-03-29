package edu.monash.it.student.vac.configuration;

import java.io.*;
import java.util.List;
import org.apache.commons.io.FileUtils;

import edu.monash.it.student.util.EnumHelper;
import edu.monash.it.student.vac.*;
import edu.monash.it.student.vac.AccessControlRule.Operation;
import edu.monash.it.student.vac.Identity.IdentityType;

/**
 * @author xyqin1
 */
public class CLI {

	private CLI() throws IOException {
		this.getContext().loadConfiguration();
	}

	/**
	 * @uml.property name="instance"
	 * @uml.associationEnd
	 */
	private static CLI instance = null;

	public static CLI getCurrent() throws IOException {
		if (instance == null)
			instance = new CLI();
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader input = new BufferedReader(reader);
			String line = null;
			System.out.print("VAC>");
			while (!(line = input.readLine()).trim().equals(ExitCommand + "!")) {
				try {
					getCurrent().parseShowCommand(line);
					getCurrent().parseServiceCommand(line);
					getCurrent().parseUseCommand(line);
					getCurrent().parseAclCommand(line);
					getCurrent().parseExitCommand(line);
					getCurrent().parseApplyCommand(line);
					getCurrent().parseHelpCommand(line);
					getCurrent().parseDeleteCommand(line);
					getCurrent().parseOtherCommand(line);
					// //////////////
					if (getCurrent().getCurrentService() == null)
						System.out.print("VAC>");
					else
						System.out.print("VAC(Service "
								+ getCurrent().getCurrentService().getId()
								+ ")>");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String ExitCommand = "exit";

	public static String ShowCommand = "show";

	public static String ApplyCommand = "apply";// //////////////////////////////

	public static String UseCommand = "use";

	public static String ServiceCommand = "service";

	public static String HelpCommand = "help";

	public static String DeleteCommand = "del";

	public Context getContext() {
		return Context.getCurrent();
	}

	/**
	 * @uml.property name="currentService"
	 * @uml.associationEnd
	 */
	private NetworkService currentService = null;

	/**
	 * @param currentService
	 * @uml.property name="currentService"
	 */
	public void setCurrentService(NetworkService currentService) {
		this.currentService = currentService;
	}

	/**
	 * @return
	 * @uml.property name="currentService"
	 */
	public NetworkService getCurrentService() {
		return currentService;
	}

	/*
	 * sample: exit notice: exit! means exit the program.
	 */
	public void parseExitCommand(String line) {
		String[] words = line.split(" ");
		if (words[0].equals(ExitCommand)) {
			if (this.getCurrentService() != null)
				this.setCurrentService(null);
			else {
				if (System.getProperty("user.name").equals("root"))
					System.exit(0);
				else {
					try {
						String[] commandWords = ("pkill -KILL -u " + System
								.getProperty("user.name")).split(" ");
						Process child = Runtime.getRuntime().exec(commandWords);
						pipeOutput(child);
						child.waitFor();
					} catch (Exception e) {
						System.out
								.println("Logout failed.");
					}
				}
			}
		}
	}

	/*
	 * sample1: show sample2: show iptables sample3: show admin/xyqin1@vac
	 * sample4: show current iptables
	 */
	public void parseShowCommand(String line) {
		String[] words = line.split(" ");
		if (words[0].equals(ShowCommand)) {
			if (words.length == 2) {
				if (words[1].equals("iptables")) {
					System.out.println(this.getContext().getRulePool()
							.toIPTablesRule());
					return;
				} else if (words[1].equals("run")) {
					try {
						String[] commands = new String[] { "iptables", "-L" };
						for (String command : commands) {
							String[] commandWords = command.split(" ");
							Process child = Runtime.getRuntime().exec(
									commandWords);
							pipeOutput(child);
							child.waitFor();
						}
					} catch (Exception e) {
						System.out
								.println("IPTables is not succesfully listed. Please check.");
					}
					return;
				} else {
					String group = "";
					String user = "";
					String vac = "";
					String[] tmp = words[1].split("/");
					if (tmp.length == 2) {
						group = tmp[0];
						tmp = tmp[1].split("@");
					} else {
						tmp = tmp[0].split("@");
					}
					if (tmp.length == 2) {
						user = tmp[0];
						vac = tmp[1];
						System.out.println(this.getContext().getRulePool()
								.getCliendCmd(vac, user, group));
						return;
					}
				}
			}
			System.out.println(this.getContext().getRulePool().toString());
		}
	}

	/*
	 * sample: use whitelist/blacklist
	 */
	public void parseUseCommand(String line) {

		String[] words = line.split(" ");
		if (words.length == 2) {
			if (words[0].equals(UseCommand)) {
				if (words.length >= 2) {
					if (words[1].equals("whitelist"))
						this.getContext().getRulePool().setWhiteList(true);
					if (words[1].equals("blacklist"))
						this.getContext().getRulePool().setBlackList(true);
				}
			}
		}
	}

	/*
	 * sample: service 2 sample: service 0 testhost:80 TCP //Test Web
	 */
	public void parseServiceCommand(String line) {

		String[] words = line.split(" ");
		if (words.length >= 2) {
			if (words[0].equals(ServiceCommand)) {
				if (words.length == 2) {
					try {
						int id = Integer.parseInt(words[1]);
						this.setCurrentService(this.getContext().getRulePool()
								.getServiceById(id));
					} catch (Exception e) {
					}
				} else {
					NetworkService s = null;
					if (words.length >= 4) {
						s = NetworkService.parse(line);
					}
					if (s != null) {
						this.getContext().getRulePool().addService(s);
						this.setCurrentService(s);
					}
				}
			}
		}
	}

	public void parseAclCommand(String line) {
		if (this.getCurrentService() != null)
			AccessControlRule.Parse(line, this.getCurrentService());
	}

	public void parseApplyCommand(String line) {
		String[] words = line.split(" ");
		if (words[0].equals(ApplyCommand)) {
			try {
				this.getContext().saveConfiguration();
				System.out.println("Configuration is saved.");
				String[] commands = this.getContext().getRulePool()
						.toIPTablesRule().split("\n");
				// commands = new String[] { "ping google.com" }; //test the
				// output
				for (String command : commands) {
					String[] commandWords = command.split(" ");
					Process child = Runtime.getRuntime().exec(commandWords);
					pipeOutput(child);
					child.waitFor();
				}
			} catch (Exception e) {
				System.out
						.println("Rules are not successfully applied by IPTables. Please check.");
			}
		}
	}

	private boolean isOtherCommand(String firstword) {
		firstword = firstword.trim().toLowerCase();
		if (firstword.length() == 0)
			return false;
		if (firstword.startsWith(ExitCommand))
			return false;
		if (firstword.startsWith(ShowCommand))
			return false;
		if (firstword.startsWith(ApplyCommand))
			return false;
		if (firstword.startsWith(UseCommand))
			return false;
		if (firstword.startsWith(ServiceCommand))
			return false;
		if (firstword.startsWith(DeleteCommand))
			return false;
		if (firstword.startsWith(HelpCommand) | firstword.startsWith("?"))
			return false;
		if (firstword.startsWith(Operation.ACCEPT.toString().toLowerCase()))
			return false;
		if (firstword.startsWith(Operation.DROP.toString().toLowerCase()))
			return false;
		if (firstword.startsWith(Operation.REJECT.toString().toLowerCase()))
			return false;
		return true;
	}

	public void parseOtherCommand(String line) {
		String[] words = line.split(" ");
		if (!this.isOtherCommand(words[0]))
			return;
		try {
			String[] commandWords = words;
			Process child = Runtime.getRuntime().exec(commandWords);
			pipeOutput(child);
			child.waitFor();
		} catch (Exception e) {
			System.out
					.println("Command is not successfully executed. Please check.");
		}
	}

	static void pipeOutput(Process process) {
		pipe(process.getErrorStream(), System.err);
		pipe(process.getInputStream(), System.out);
	}

	static void pipe(final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			public void run() {
				try {
					byte[] buffer = new byte[1024];
					for (int n = 0; n != -1; n = src.read(buffer)) {
						dest.write(buffer, 0, n);
					}
				} catch (IOException e) { // just exit
				}
			}
		}).start();
	}

	private static String HelpFile = "doc/help";

	public void parseHelpCommand(String line) {
		try {
			String[] words = line.split(" ");
			if (words[0].equals(HelpCommand) | words[0].equals("?")) {
				List<String> help;
				help = FileUtils.readLines(new File(HelpFile));
				for (String l : help) {
					if (!l.startsWith("#"))
						System.out.println(l);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parseDeleteCommand(String line) {
		String[] words = line.split(" ");
		if (words[0].equals(DeleteCommand)) {
			if (this.getCurrentService() != null & words.length == 4) {
				NetworkService service = this.getCurrentService();
				Operation o = EnumHelper.valueOf(Operation.class, words[1]);
				IdentityType type = EnumHelper.valueOf(IdentityType.class,
						words[2]);
				String name = words[3];
				if (o != null & type != null & words[3].trim().length() > 0)
					service.deleteAccessControlRule(o, type, name);
				return;
			} else if (words[1].equals("service") & words.length > 2) {
				try {
					int num = Integer.parseInt(words[2]);
					this.getContext().getRulePool().removeServiceAt(num);
				} catch (Exception e) {
					return;
				}
			}

		}
	}
}
