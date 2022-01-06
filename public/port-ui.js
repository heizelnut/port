const port = new Port();
const dashBoard = document.getElementsByTagName("main")[0];
const briefBoatCard = document.getElementById("brief-boat");
const nothing = document.getElementById("nothing");

port.getAll().then(boats => {
	for (let boat of boats) {
		nothing.style.display = "none";

		let card = briefBoatCard.content.cloneNode(true);
		let templateHTML = card.firstElementChild.innerHTML;
		templateHTML = templateHTML.replace("%name%", boat.name);
		templateHTML = templateHTML.replace("%place%", boat.place);
		templateHTML = templateHTML.replace("#", (boat.type == "sail") ? "⛵" : "🛥️")
		card.firstElementChild.innerHTML = templateHTML;
		dashBoard.appendChild(card);
	}
});
