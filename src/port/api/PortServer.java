package port.api;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import port.core.Port;

public class PortServer {
	HttpServer server;
	Port port;
	int portNumber;

	public PortServer(int portNumber) throws IOException {
		this.port = new Port();
		this.portNumber = portNumber;
		this.server = HttpServer.create(new InetSocketAddress(portNumber), 0);
		this.server.createContext("/", new RootRouter("/"));
		this.server.createContext("/boats", new BoatsRouter("/boats", port));
		this.server.setExecutor(null);
	}

	public void start() {
		this.server.start();
	}
}
