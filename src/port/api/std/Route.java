package port.api.std;

import java.util.HashMap;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

public class Route {
	protected String p = "";
	protected JSONObject j = null;
	protected HttpExchange t = null;

	public void setRequest(String path, JSONObject reply, HttpExchange exchange) {
		this.p = path;
		this.j = reply;
		this.t = exchange;
	}
	
	private HashMap<String,String> parseQueryString(String query) {
		char queryChar[] = query.toCharArray();
		
		HashMap<String,String> raw = new HashMap<String,String>();
		String key = "", value = "";
		Boolean isKey = true;

		for (char c : queryChar) {
			switch(c) {
			case '=':
				isKey = false;
				continue;
			case '&':
				isKey = true;
				raw.put(key, value);
				key = "";
				value = "";
				continue;
			default:
				if (isKey) key += c;
				if (!isKey) value += c;
			}
		}
		
		// put last entry in dictionary
		raw.put(key, value);
		
		// remove all blank key-value pairs
		raw.remove("");

 		// url-decode everything
		HashMap<String,String> decoded = new HashMap<String,String>();
		for (HashMap.Entry<String,String> entry : raw.entrySet())
			decoded.put(this.urlDecode(entry.getKey()),
					this.urlDecode(entry.getValue()));

		return decoded;
	}
	
	private String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            System.out.println("couldn't decode url string");
			return value;
        }
    }

	protected String q(String key) {
		String query = t.getRequestURI().getRawQuery();

		// don't parse an empty query string
		if (query == null || "".equals(query))
			return null;

		HashMap<String,String> table = parseQueryString(query);
		return table.get(key);
	}

	public int resolve() {
		j.put("ok", false);
		j.put("description", "not implemented");
		return 501;
	}
}
