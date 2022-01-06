class Boat {
	constructor(name, nationality, volume, length, type, when) {
		when = when || new Date();

		if (type != "sail" && type != "motor")
			throw "Type of boat not recognized."

		this.name = name;
		this.nationality = nationality;
		this.volume = volume;
		this.length = length;
		this.type = type;
		this.dockingDate = when.getTime() / 1000;
	}
}

class Port {
	constructor() {
		this.setErrorHandler(console.error);
	}

	getAll() {
		return fetch("/api")
		.then(response => response.json())
		.then(data => data.result)
		.catch(err => this.errorHandler(err));
	}

	getBoat(id) {
		return fetch(`/api/${id}`)
		.then(response => response.json())
		.then(data => {
			if (!data.ok)
				throw "This slot is empty.";
			else
				return data.result;
		})
		.catch(err => this.errorHandler(err));
	}

	createBoat(boat) {
		return fetch("/api", {
			method: 'POST',
			headers: { "Content-type": "application/json" },
			body: JSON.stringify(boat)
		})
		.then(response => response.json())
		.then(data => {
			if (!data.ok)
				throw data.description;
			else
				return data.result.place;
		})
		.catch(err => this.errorHandler(err));
	}

	setErrorHandler(handler) {
		this.errorHandler = handler;
	}
}
