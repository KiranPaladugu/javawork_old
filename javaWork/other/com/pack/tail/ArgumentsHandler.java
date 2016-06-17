package com.pack.tail;

public class ArgumentsHandler {
	String args[];
	String file;

	public ArgumentsHandler(String[] args) {
		this.args = args;
		if ((args == null) || (args.length == 0)) {
			Notifier.notify("Possible Usage  ....");
			Notifier.notify("tail [arguments] <filename>");
			Notifier.notify("try tail -help for help");
		}
		int length = args.length;
		if (length == 1) {
			if (args[0].trim().equalsIgnoreCase("-help")) {
				String msg = "Possible Usage  ....\n";
				msg += "tail [-f] [-d] <filename>\n";
				msg += "-d for simply filename. This is optional .\n";
				msg += "-f <no of line> to be displayed. Last lines will be displayed.by default 72 lines.";
				Notifier.notify(msg);
			}
		}
	}

}
