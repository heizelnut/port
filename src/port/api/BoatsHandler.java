package port.api;

import java.util.*;
import java.io.*;
import com.sun.net.httpserver.*;
import org.json.JSONObject;

public class BoatsHandler implements HttpHandler {
	private int prefix;
	private String localPath;

	public BoatsHandler(String radix) {
		this.prefix = radix.length();
	}
	
	public void handle(HttpExchange t) throws IOException {
		// InputStream body = t.getRequestBody();
		
		Headers reqh = t.getRequestHeaders();
		Headers resh = t.getResponseHeaders();
		
		this.localPath = t.getRequestURI().getRawPath().substring(this.prefix);
		
		System.out.println(t.getRemoteAddress() + " - BOATS: " + this.localPath);
		
		resh.set("Content-Type", "application/json");
		JSONObject obj = new JSONObject();
		obj.put("msg", "boats service");
		obj.put("user-agent", reqh.getFirst("User-Agent"));
		String response = obj.toString();
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
   }
}
