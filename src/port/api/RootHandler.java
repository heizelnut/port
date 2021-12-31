package port.api;

import java.util.*;
import java.io.*;
import com.sun.net.httpserver.*;
import org.json.JSONObject;

public class RootHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException {
		InputStream is = t.getRequestBody();
		
		System.out.println(t.getRequestURI());

		Headers reqh = t.getRequestHeaders();
		System.out.println(reqh.getFirst("User-Agent"));

		Headers resh = t.getResponseHeaders();
		resh.set("Content-Type", "application/json");
		JSONObject obj = new JSONObject();
		obj.put("msg", "Hello world!");

		String response = obj.toString();
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
   }
}
