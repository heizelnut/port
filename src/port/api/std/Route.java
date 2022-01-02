package port.api.std;

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

	public int resolve() {
		j.put("ok", false);
		j.put("description", "unimplemented");
		return 501;
	}
}
