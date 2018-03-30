package rtree;


public class Entry<T> extends Node {
	public final T entry;
	public Entry(double x, double y, T entry) {
		super(x,x,y,y, true);
		this.entry = entry;
		
	}
	public String toString() {
		return "Entry"+ MBR[0];
		
	}

}
