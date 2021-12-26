package port.core;

import java.util.Date;

public class Boat {
	public String name;
	public String nationality;
	public int place;
	private long dockingDate;
	public float length;
	public float volume;
	public boolean isSailing;

	public Boat(String nm, String nt, Date d, float l, float v, boolean s) {
		this.name = nm;
		this.nationality = nt;
		this.place = 0;
		this.dockingDate = d.getTime();
		this.length = l;
		this.volume = v;
		this.isSailing = s;
	}
	
	public float getDaysOfResidence() {
		return (System.currentTimeMillis() - this.dockingDate) / (1000 * 60 * 60 * 24);
	}

	public Date getDockingDate() {
		return new Date(this.dockingDate);
	}

	public float getPriceUntilNow() {
		return (this.isSailing)
			// sailing boat: pay 10 euros a day per length [m]
			? 10 * this.getDaysOfResidence() * this.length
			// motor boat: pay 20 euros a day per volume [q]
			: 20 * this.getDaysOfResidence() * this.volume;
	}
}
