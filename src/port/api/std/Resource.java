package port.api.std;

import java.util.regex.Pattern;

class Resource {
	public Pattern pattern;
	public Route route;

	public Resource(Pattern p, Route r) {
		this.pattern = p;
		this.route = r;
	}
}
