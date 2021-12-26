package port.core;

import java.util.*;

public class Port {
	private Boat boats[] = new Boat[100];

	public Port () {
		for (int i = 0; i < this.boats.length; i++)
			this.boats[i] = null;
	}
	
	public int size() {
		return this.boats.length;
	}

	private boolean isLargeBoat(Boat boat) {
		return (boat.length > 10);
	}

	public int addBoat(Boat boat) {
		int free = -1;
		int i;
		
		if (boat.isSailing) {
			// put sailing boats from place 51
			// i = 50 => place = 51
			i = 50;
		} else {
			// put motor boats from place 1 (i = 0)
			// put larger boats from place 21 (i = 20)
			i = this.isLargeBoat(boat) ? 20 : 0;
		}

		for (; i < this.boats.length && free == -1; i++)
			free = (this.boats[i] == null) ? i : -1;
		
		if (free != -1) {
			boat.place = free + 1;
			this.boats[free] = boat;
		}

		return boat.place;
	}

	public float removeBoat(int place) {
		Boat removed = this.getBoat(place);
		if (removed == null)
			return 0.0f;

		this.boats[place-1] = null;

		return removed.getPriceUntilNow();
	}

	public Boat getBoat(int place) {
		if (place < 0 || place > this.size())
			return null;

		return this.boats[place-1];
	}
	
	private boolean checkBoatLength(Boat boat, float min, float max) {
		if (boat == null) return false;
		return (boat.length >= min && boat.length < max);
	}

	public Boat[] getBoatsByLength(float min, float max) {
		int count = 0;
		for (Boat boat : this.boats)
			count += this.checkBoatLength(boat, min, max) ? 1 : 0;

		Boat found[] = new Boat[count];
		int j = 0;

		for (Boat boat : this.boats) {
			if (this.checkBoatLength(boat, min, max)) {
				found[j++] = boat;
			}
		}

		return found;
	}
}
