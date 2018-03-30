package rtree;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
	private Node parent;
	protected ArrayList<Node> children;
	protected ArrayList<Node> entry;
	protected double[] MBR = new double[4];
	private boolean isleaf;
	private int M;
	private int min = (int) Math.ceil(M*0.4);
	
	public Node(double x1,double x2,double y1,double y2, boolean isleaf) {
		this.isleaf = isleaf;
		MBR[0] = x1;
		MBR[1] = x2;
		MBR[2] = y1;
		MBR[3] = y2;
		children = new ArrayList<Node>();
		entry = new ArrayList<Node>();
	}

}

