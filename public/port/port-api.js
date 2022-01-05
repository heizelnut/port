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

	getOne(id) {
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

	setErrorHandler(handler) {
		this.errorHandler = handler;
	}
}
