package port.api.std;

import java.util.*;
import java.io.*;
import com.sun.net.httpserver.*;
import java.util.regex.*;
import org.json.JSONObject;

public class Router implements HttpHandler {
	private ArrayList<Pair> routes = new ArrayList<Pair>();
	private int prefixLength;

	public Router(String prefix) {
		this.prefixLength = prefix.length();
		this.setup();
	}

	public void handle(HttpExchange t) throws IOException {
		String localPath = t.getRequestURI().getRawPath();
		localPath = localPath.substring(prefixLength);
		Boolean found = false;

		for (Pair p : routes) {
			Matcher m = p.pattern.matcher(localPath);
			Boolean isRightRoute = m.find();
			if (isRightRoute) {
				found = true;
				this.call(p.route, t);
				break;
			}
		}

		if (!found) {
			JSONObject error = new JSONObject();
			error.put("description", "resource not found.");
			String response = error.toString();
			t.sendResponseHeaders(404, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public void setup() {
		return;
	}

	public void on(String rule, Route route) {
		Pattern p = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
		this.routes.add(new Pair(p, route));
	}

	private void call(Route route, HttpExchange t) {
		JSONObject reply = new JSONObject();
		int code = route.resolve(reply, t);
		String response = reply.toString();
		t.sendResponseHeaders(code, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
