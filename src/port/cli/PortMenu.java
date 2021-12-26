package port.cli;

import java.util.Date;
import port.core.Port;
import port.core.Boat;

public class PortMenu extends Menu {
	Port port = new Port();

	public PortMenu() {
		setTitle("🚤 Azioni da svolgere:");
		addOption("Aggiungi una barca al porto", "addBoat");
		addOption("Cerca una barca per posto", "getBoat");
		addOption("Calcola importo e rimuovi barca", "removeBoat");
		addOption("Cerca una barca per lunghezza", "searchBoatByLength");
	}

	void addBoat() {
		System.out.print("Nome: ");
		String name = S.next();

		System.out.print("Nazionalità: ");
		String nationality = S.next();

		Date date = new Date(System.currentTimeMillis());

		System.out.print("Lunghezza [m]: ");
		float length = S.nextFloat();

		System.out.print("Stazza [q]: ");
		float volume = S.nextFloat();
		
		String sailOrMoon;
		do {
			System.out.print("Barca a vela o a motore? [v/m] ");
			sailOrMoon = S.next();
			System.out.print(sailOrMoon);
		} while (!("m".equals(sailOrMoon) || "v".equals(sailOrMoon)));
		boolean isSailing = "v".equals(sailOrMoon);
		
		Boat boat = new Boat(name, nationality, date, length, 
				volume, isSailing);

		int place = this.port.addBoat(boat);

		if (place == 0) {
			System.out.println("Nessun posto disponibile per questa barca");
		} else {
			System.out.println("Posto assegnato: " + place);
		}
	}

	void getBoat() {
		System.out.print("Posto da visualizzare: ");
		int place = S.nextInt();

		Boat boat = this.port.getBoat(place);

		if (boat == null) {
			System.out.println("Nessuna barca in posizione " + place);
		} else {
			this.printBoat(boat);
		}
	}

	void removeBoat() {
		System.out.print("Posto da liberare: ");
		int place = S.nextInt();

		Boat boat = this.port.getBoat(place);

		if (boat == null) {
			System.out.println("Nessuna barca in posizione " + place);
			return;
		}

		this.printBoat(boat);
		
		System.out.print("Sei sicuro di voler liberare questo posto? ");
		System.out.print("[y/N] ");
		String delete = S.next();
		
		if (delete == "y") {
			this.port.removeBoat(place);
			float price = boat.getPriceUntilNow();
			System.out.println("Posto liberato. Importo finale: " + price);
		} else {
			System.out.println("Azione cancellata.");
		}
	}
	
	void printBoat(Boat boat) {
		System.out.println("Nome: " + boat.name);
		System.out.println("Nazionalità: " + boat.nationality);
		System.out.println("Posizione: " + boat.place);
		System.out.println("Lunghezza: " + boat.length + "m");
		System.out.println("Stazza: " + boat.volume + "q");
		String type = (boat.isSailing) ? "Barca a vela" : "Barca a motore";
		System.out.println("Tipologia: " + type);
		System.out.println("Importo: €" + boat.getPriceUntilNow());
	}
	
	void searchBoatByLength() {
		System.out.print("Lunghezza minima: ");
		float min = S.nextFloat();
		System.out.print("Lunghezza massima: ");
		float max = S.nextFloat();

		Boat found[] = this.port.getBoatsByLength(min, max);

		for (Boat boat : found) {
			System.out.println("-----------");
			this.printBoat(boat);
		}
	}
}
