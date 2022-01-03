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
	DateFormat fmt = DateFormat.getInstance();

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
			entry.put("place", b.place);
			entry.put("type", b.isSailing ? "sail" : "motor");
			result.put(entry);
		}

		j.put("result", result);
		j.put("query", q("ciao"));
		return 200;
	}
	
	public int createBoat() {
		InputStream is = t.getRequestBody();
		String body;
		try {
			body = new String(is.readAllBytes());
		} catch (IOException e) {
			j.put("ok", false);
			j.put("description", "no body given");
			return 400;
		}

		JSONObject request;
		try {
			request = new JSONObject(body);
		} catch (JSONException e) {
			j.put("ok", false);
			j.put("description", "body is not json");
			return 400;
		}
		
		j.put("ok", true);
		j.put("result", request);
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
	DateFormat fmt = DateFormat.getInstance();

	public BoatsShowOne(Port port) {
		this.port = port;
	}
	
	private int getBoat() {
		int place = Integer.parseInt(p.substring(1));
		Boat boat = port.getBoat(place);
		
		if (boat == null) {
			j.put("ok", false);
			j.put("description", "boat not found");
			return 404;
		}

		JSONObject result = new JSONObject();
		result.put("name", boat.name);
		result.put("nationality", boat.nationality);
		result.put("place", boat.place);
		result.put("since", fmt.format(boat.getDockingDate()));
		result.put("price", boat.getPriceUntilNow());
		result.put("length", boat.length);
		result.put("volume", boat.volume);
		result.put("type", boat.isSailing ? "sail" : "motor");

		j.put("ok", true);
		j.put("result", result);
		return 200;
	}
	
	private int deleteBoat() {
		int place = Integer.parseInt(p.substring(1));
		float price = port.removeBoat(place);

		if (price == -1.0f) {
			j.put("ok", false);
			j.put("description", "boat not found");
			return 404;
		}

		j.put("ok", true);
		j.put("result", (new JSONObject()).put("price", price));
		return 200;
	}

	@Override
	public int resolve() {
		int code;
		switch (t.getRequestMethod()) {
		case "GET":
			code = getBoat();
			break;
		case "DELETE":
			code = deleteBoat();
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

class BoatsRouter extends Router {
	public BoatsRouter(String prefix, Port port) {
		super(prefix);
		this.on("^(|/)$", new BoatsShowAll(port));
		this.on("^/\\d+$", new BoatsShowOne(port));
	}
}
