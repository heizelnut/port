package port.api.std;

import java.util.regex.Pattern;

class Pair {
	public Pattern pattern;
	public Route route;

	public Pair(Pattern p, Route r) {
		this.pattern = p;
		this.route = r;
	}
}
