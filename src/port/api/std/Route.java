package port.api.std;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

public interface Route {
	int resolve(JSONObject j, HttpExchange t);
}
