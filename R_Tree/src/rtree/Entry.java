package rtree;


public class Entry extends Node {
	
	public Entry(float[] point) {
		super(point, true);
		
	}
	public String toString() {
		return "Entry"+ MBR[0];
		
	}

}
