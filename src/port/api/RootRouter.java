package port.api;

import com.sun.net.httpserver.*;
import org.json.JSONObject;

import port.api.std.*;

class RootRoute implements Route {
	public int resolve(JSONObject j, HttpExchange t) {
		Headers h = t.getRequestHeaders();
		j.put("msg", "root service");
		j.put("client", h.getFirst("User-Agent"));
		return 200;
	}
}

public class RootRouter extends Router {
	public RootRouter(String prefix) {
		super(prefix);
	}

	@Override
	public void setup() {
		this.on("^$", new RootRoute());
	}
}
