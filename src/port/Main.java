package port;

import java.io.*;
import port.cli.PortMenu;
import port.api.PortServer;

class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			startCli();
		} else {
			switch (args[0]) {
			case "serve":
				String port = System.getenv("PORT");
				port = (port == null) ? "8000" : port;
				startServer(Integer.parseInt(port));
			break;
			}
		}
	}

	public static void startCli() {
		PortMenu pm = new PortMenu();

		do {
			pm.execute();
			System.out.println("------------");
		} while (!pm.hasExited());
	}

	public static void startServer(int port) {
		try {
			PortServer ps = new PortServer(port);
			ps.start();
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}
}
