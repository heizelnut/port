package port.api;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class PortServer {
	HttpServer server;
	int port;

	public PortServer(int port) throws IOException {
		this.port = port;
		this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
		this.server.createContext("/", new RootHandler());
		this.server.createContext("/boats", new BoatsHandler());
		this.server.setExecutor(null);
	}

	public void start() {
		this.server.start();
	}
}
