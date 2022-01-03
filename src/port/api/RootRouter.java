package port.api;

import com.sun.net.httpserver.*;
import org.json.JSONObject;

import port.api.std.*;

class RootRoute extends Route {
	@Override
	public int resolve() {
		j.put("ok", true);
		return 200;
	}
}

public class RootRouter extends Router {
	public RootRouter(String prefix) {
		super(prefix);
		this.on("^$", new RootRoute());
	}
}
