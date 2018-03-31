package rtree;

import java.util.ArrayList;

public class Node {
	// Nodes include internal nodes and leaf nodes
	// Leaf nodes have no children but have entries.
	private Node parent;
	private ArrayList<Node> children;
	private ArrayList<Node> entries;
	protected double[] MBR = new double[4];
	public boolean isLeaf;
	private int M;
	private int min = (int) Math.ceil(M*0.4);
	
	public Node(double left,double right,double down,double up, boolean isLeaf) {
		this.isLeaf = isLeaf;
		MBR[0] = left;
		MBR[1] = right;
		MBR[2] = down;
		MBR[3] = up;
		children = new ArrayList<Node>();
		entries = new ArrayList<Node>();
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public ArrayList<Node> getChildren(){
		return children;
	}
	public ArrayList<Node> getEntry() {
		return entries;
	}
	public Node getRoot() {
		if(parent == null) {
			return this;
		}
		Node root = parent;
		while (root.getParent()!= null) {
			root = root.getParent();
		}
		return root;
	}
	public double[] getMBR() {
		return this.MBR;
	}
	public boolean isIntersects(double x1, double x2, double y1, double y2) {
		// If the center's difference is less than the half of the sum of the length of sides in both x and y axis.
		if((Math.abs((this.MBR[0]+this.MBR[1])/2-(x1+x2)/2)<=(x2-x1+MBR[1]-MBR[0])/2)&&
				(Math.abs((this.MBR[2]+this.MBR[3])/2-(y1+y2)/2)<=(y2-y1+MBR[3]-MBR[2])/2)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	public double increasePerimeter(Entry p) {
		double[] increased = new double[4];
		increased[0] = Math.min(this.getMBR()[0], p.getMBR()[0]);
		increased[1] = Math.max(this.getMBR()[1], p.getMBR()[1]);
		increased[2] = Math.min(this.getMBR()[2], p.getMBR()[2]);
		increased[3] = Math.max(this.getMBR()[3], p.getMBR()[3]);
		return calPerimeter(increased)-calPerimeter(this.MBR);
	}
	public double calPerimeter(double[] p) {
		return (p[1]-p[0]+p[3]-p[2])*2;
	}

}

