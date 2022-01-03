package port.api.std;

import java.util.*;
import java.io.*;
import com.sun.net.httpserver.*;
import java.util.regex.*;
import org.json.JSONObject;

public class Router implements HttpHandler {
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private int prefixLength;

	public Router(String prefix) {
		this.prefixLength = prefix.length();
	}

	public void handle(HttpExchange t) throws IOException {
		String localPath = t.getRequestURI().getRawPath();
		localPath = localPath.substring(prefixLength);
		Boolean found = false;

		for (Resource r : this.resources) {
			Matcher m = r.pattern.matcher(localPath);
			Boolean isRightResource = m.find();
			if (isRightResource) {
				found = true;
				this.call(r.route, t);
				break;
			}
		}

		if (!found) {
			JSONObject error = new JSONObject();
			error.put("description", "resource not found");
			String response = error.toString();
			t.sendResponseHeaders(404, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	public void on(String rule, Route route) {
		Pattern p = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
		this.resources.add(new Resource(p, route));
	}

	private void call(Route route, HttpExchange t) {
		JSONObject reply = new JSONObject();
		String path = t.getRequestURI().getRawPath();
		path = path.substring(this.prefixLength);
		route.setRequest(path, reply, t);
		int code = route.resolve();
		String response = reply.toString();
		try {
			t.sendResponseHeaders(code, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		} catch (IOException e) {
			System.out.println("No content sent");
		}
	}
}
