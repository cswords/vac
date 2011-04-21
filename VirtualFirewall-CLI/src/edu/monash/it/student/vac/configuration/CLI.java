package edu.monash.it.student.vac.configuration;

import java.io.*;

import edu.monash.it.student.vac.*;

public class CLI {

	private CLI() throws IOException {
		this.getContext().loadConfiguration();
	}

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
				instance.parseShowCommand(line);
				instance.parseServiceCommand(line);
				instance.parseUseCommand(line);
				instance.parseAclCommand(line);
				instance.parseExitCommand(line);
				instance.parseApplyCommand(line);
				// //////////////
				if (getCurrent().getCurrentService() == null)
					System.out.print("VAC>");
				else
					System.out.print("VAC(Service "
							+ getCurrent().getCurrentService().getId() + ")>");

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

	public Context getContext() {
		return Context.getCurrent();
	}

	private NetworkService currentService = null;

	public void setCurrentService(NetworkService currentService) {
		this.currentService = currentService;
	}

	public NetworkService getCurrentService() {
		return currentService;
	}

	/*
	 * sample: exit notice: exit! means exit the program.
	 */
	public void parseExitCommand(String line) {
		String[] words = line.split(" ");
		if (words.length > 0)
			if (words[0].equals(ShowCommand)) {
				System.out.println(this.getContext().getRulePool().toString());
			}
	}

	/*
	 * sample: show WTF$%^^ &**_
	 */
	public void parseShowCommand(String line) {
		if (line.equals(ExitCommand)) {
			this.setCurrentService(null);
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
					if (words[1] == "whitelist")
						this.getContext().getRulePool().setWhiteList(true);
					if (words[1] == "blacklist")
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
		// TODO
	}
}
