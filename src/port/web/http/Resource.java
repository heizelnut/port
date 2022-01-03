package port.web.http;

import java.util.regex.Pattern;

class Resource {
	public Pattern pattern;
	public Route route;

	public Resource(Pattern p, Route r) {
		this.pattern = p;
		this.route = r;
	}
}
