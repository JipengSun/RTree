package rtree;


public class Entry extends Node {
	//public final T entry;
	public Entry(double x, double y) {
		super(x,x,y,y, false);
	//	this.entry = entry;
		
	}
	public String toString() {
		return "Entry"+ MBR[0];
		
	}

}
