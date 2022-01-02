package port.api;

import java.io.*;
import java.util.*;
import java.text.DateFormat;
import com.sun.net.httpserver.*;
import org.json.*;

import port.api.std.*;
import port.core.*;

class BoatsShowAll extends Route {
	Port port;

	public BoatsShowAll(Port port) {
		this.port = port;
	}
	
	public int getAllBoats() {
		JSONArray result = new JSONArray();
		j.put("ok", true);
		JSONObject entry;

		for (Boat b : port.getBoats()) {
			if (b == null)
				continue;

			entry = new JSONObject();
			entry.put("name", b.name);
			entry.put("nationality", b.nationality);
			entry.put("place", b.place);
			entry.put("since", DateFormat.getInstance().format(b.getDockingDate()));
			entry.put("type", b.isSailing ? "sail" : "motor");
			result.put(entry);
		}

		j.put("result", result);
		return 200;
	}
	
	public int createBoat() {
		j.put("ok", true);
		port.addBoat(new Boat("kek", "italia", new Date(System.currentTimeMillis()), 5, 10, true));
		port.addBoat(new Boat("lol", "spagna", new Date(System.currentTimeMillis()), 5, 10, false));
		return 201;
	}
	
	@Override
	public int resolve() {
		int code;
		switch (t.getRequestMethod()) {
		case "GET":
			code = getAllBoats();
			break;
		case "POST":
			code = createBoat();
			break;
		default:
			j.put("ok", false);
			j.put("description", "invalid method on resource");
			code = 400;
			break;
		}
		return code;
	}
}

class BoatsShowOne extends Route {
	Port port;
	
	public BoatsShowOne(Port port) {
		this.port = port;
	}
	
	@Override
	public int resolve() {
		if (t.getRequestMethod() == "GET") {
			j.put("ok", false);
			j.put("description", "invalid method on resource");
			return 400;
		}

		j.put("ok", true);
		j.put("path", p);
		return 200;
	}
}

class BoatsRouter extends Router {
	public BoatsRouter(String prefix, Port port) {
		super(prefix);
		this.on("^(|/)$", new BoatsShowAll(port));
		this.on("^/\\d+$", new BoatsShowOne(port));
	}
}
