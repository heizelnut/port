package port.web;

import java.io.*;
import java.util.*;
import java.text.DateFormat;
import com.sun.net.httpserver.*;
import org.json.*;

import port.web.http.*;
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
		String minParam = q("min");
		String maxParam = q("max");
		float min = (minParam != null) ? Float.parseFloat(minParam) : 0.0f;
		float max = (maxParam != null) ? Float.parseFloat(maxParam) : 999999.0f;

		for (Boat boat : port.getBoatsByLength(min, max)) {
			if (boat == null)
				continue;

			entry = new JSONObject();
			entry.put("name", boat.name);
			entry.put("place", boat.place);
			entry.put("length", boat.length);
			entry.put("type", boat.isSailing ? "sail" : "motor");
			result.put(entry);
		}

		j.put("result", result);
		return 200;
	}
	
	public int createBoat() {
		InputStream is = t.getRequestBody();
		String body;
		try {
			body = new String(is.readAllBytes());
			is.close();
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
		
		Boolean isSailing = false;
		Boolean valid = true;

		long dockingDate = 0;
		String name = "";
		String nationality = "";
		float length = 0;
		float volume = 0;
		String type = "";

		try {
			name = request.getString("name");
			nationality = request.getString("nationality");
			length = request.getFloat("length");
			volume = request.getFloat("volume");
			type = request.getString("type");
		} catch (JSONException e) {
			valid = false;
		}

		if ("sail".equals(type))
			isSailing = true;
		else if ("motor".equals(type))
			isSailing = false;
		else
			valid = false;


		if (!valid) {
			j.put("ok", false);
			j.put("description", "invalid fields");
			return 400;
		}

		try {
			dockingDate = request.getInt("date") * 1000;
		} catch (JSONException e) {
			dockingDate = System.currentTimeMillis();
		}

		Date docking = new Date(dockingDate);
		Boat boat = new Boat(name, nationality, docking,
				length, volume, isSailing);

		int place = port.addBoat(boat);

		if (place == 0) {
			j.put("ok", false);
			j.put("description", "no place available");
			return 409;
		}

		j.put("ok", true);
		j.put("result", (new JSONObject()).put("place", place));
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
			code = 405;
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
			code = 405;
			break;
		}
		return code;
	}
}

class APIRouter extends Router {
	public APIRouter(String prefix, Port port) {
		super(prefix);
		this.on("^(|/)$", new BoatsShowAll(port));
		this.on("^/\\d+$", new BoatsShowOne(port));
	}
}
