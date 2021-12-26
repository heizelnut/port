package port;

import port.cli.PortMenu;

class Main {
	public static void main(String[] args) {
		PortMenu pm = new PortMenu();

		do {
			pm.execute();
			System.out.println("------------");
		} while (!pm.hasExited());
	}
}
