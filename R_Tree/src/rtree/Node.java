package rtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Node {
	// Nodes include internal nodes and leaf nodes
	// Leaf nodes have no children but have entries.
	private Node parent;
	private ArrayList<Node> children;
	private ArrayList<Entry> entries;
	protected double[] MBR = new double[4];
	public boolean isLeaf;
	private final int M = 50;
	private final int min = (int) Math.ceil(M*0.4);
	
	public Node(double left,double right,double down,double up, boolean isLeaf) {
		this.isLeaf = isLeaf;
		MBR[0] = left;
		MBR[1] = right;
		MBR[2] = down;
		MBR[3] = up;
		children = new ArrayList<Node>();
		entries = new ArrayList<Entry>();
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
	public void addChildren(Node child) {
		children.add(child);
	}
	public ArrayList<Entry> getEntry() {
		return entries;
	}
	public void setEntry(ArrayList<Entry> e){
		this.entries = e;
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
	public Node split() {
		if(this.isLeaf) {
			ArrayList<Entry> all = this.getEntry();
			int m = all.size();
			double bestmin = Double.MAX_VALUE;
			int [] bestdiv = new int[2];
			// The best division is divided by X dimension for value =  1, Y for value = 2; 
			bestdiv[0] = -1;
			// The best division is divided in NO.i time.
			bestdiv[1] = -1;
			
			// First check each division in X dimension.
			Collections.sort(all,new PointCompX());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Entry> s1 = new ArrayList<Entry>();
				ArrayList<Entry> s2 = new ArrayList<Entry>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calMBR(s1))+calPerimeter(calMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 1;
					bestdiv[1] = i;
				}
			}
			// Then, check each division in Y dimension.
			Collections.sort(all,new PointCompY());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Entry> s1 = new ArrayList<Entry>();
				ArrayList<Entry> s2 = new ArrayList<Entry>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calMBR(s1))+calPerimeter(calMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 2;
					bestdiv[1] = i;
				}
			}
			if(bestdiv[0]==1) {
				Collections.sort(all, new PointCompX());
			}
			// Split Node u to new Node u and u'. Return u'.
			ArrayList<Entry> uEntries = new ArrayList<>();
			ArrayList<Entry> u1Entries = new ArrayList<>();
			for(int i = 0; i< bestdiv[1];i++) {
				uEntries.add(all.get(i));
			}
			for(int i = bestdiv[1];i<m;i++) {
				u1Entries.add(all.get(i));
			}
			double[] uMBR = calMBR(uEntries);
			double[] u1MBR = calMBR(u1Entries);
			this.MBR = uMBR;
			this.entries = uEntries;
			Node u1 = new Node(u1MBR[0], u1MBR[1], u1MBR[2], u1MBR[3], true);
			u1.setParent(this.getParent());
			u1.entries = u1Entries;
			return u1;
		}
		else {
			ArrayList<Node> all = this.getChildren();
			int m = all.size();
			double bestmin = Double.MAX_VALUE;
			int [] bestdiv = new int[2];
			// The best division is divided based on left(1), right(2), down(3), up(4) bound; 
			bestdiv[0] = -1;
			// The best division is divided in NO.i time.
			bestdiv[1] = -1;
			// First check each division by LeftBound.
			Collections.sort(all,new BoundCompLeft());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Node> s1 = new ArrayList<Node>();
				ArrayList<Node> s2 = new ArrayList<Node>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calInternalMBR(s1))+calPerimeter(calInternalMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 1;
					bestdiv[1] = i;
				}
			}
			// Then check each division by RightBound.
			Collections.sort(all,new BoundCompRight());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Node> s1 = new ArrayList<Node>();
				ArrayList<Node> s2 = new ArrayList<Node>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calInternalMBR(s1))+calPerimeter(calInternalMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 2;
					bestdiv[1] = i;
				}
			}
			// Then check each division by DownBound.
			Collections.sort(all,new BoundCompDown());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Node> s1 = new ArrayList<Node>();
				ArrayList<Node> s2 = new ArrayList<Node>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calInternalMBR(s1))+calPerimeter(calInternalMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 3;
					bestdiv[1] = i;
				}
			}
			// Then check each division by UpBound.
			Collections.sort(all,new BoundCompUp());
			for (int i = min; i<= m-min;i++) {
				ArrayList<Node> s1 = new ArrayList<Node>();
				ArrayList<Node> s2 = new ArrayList<Node>();
				for (int j = 0;j< i;j++) {
					s1.add(all.get(j));
				}
				for (int k = i;k< m;k++) {
					s2.add(all.get(k));
				}
				double sum = calPerimeter(calInternalMBR(s1))+calPerimeter(calInternalMBR(s2));
				if (sum< bestmin) {
					bestmin = sum;
					bestdiv[0] = 4;
					bestdiv[1] = i;
				}
			}
			if(bestdiv[0]==1) {
				Collections.sort(all, new BoundCompLeft());
			}
			if(bestdiv[0]==2) {
				Collections.sort(all, new BoundCompRight());
			}
			if(bestdiv[0]==3) {
				Collections.sort(all, new BoundCompDown());
			}
			// Split Node u to new Node u and u'. Return u'.
			ArrayList<Node> uNode = new ArrayList<>();
			ArrayList<Node> u1Node = new ArrayList<>();
			for(int i = 0; i< bestdiv[1];i++) {
				uNode.add(all.get(i));
			}
			for(int i = bestdiv[1];i<m;i++) {
				u1Node.add(all.get(i));
			}
			double[] uMBR = calInternalMBR(uNode);
			double[] u1MBR = calInternalMBR(u1Node);
			this.MBR = uMBR;
			this.children = uNode;
			Node u1 = new Node(u1MBR[0], u1MBR[1], u1MBR[2], u1MBR[3], false);
			u1.setParent(this.getParent());
			u1.children = u1Node;
			return u1;
		}
		
		
	}
	public double[] calMBR(ArrayList<Entry> a) {
		double [] tempMBR = new double[4];
		Collections.sort(a, new PointCompX());
		tempMBR[0] = a.get(0).getMBR()[0];
		tempMBR[1] = a.get(a.size()-1).getMBR()[1];
		Collections.sort(a, new PointCompY());
		tempMBR[2] = a.get(0).getMBR()[2];
		tempMBR[3] = a.get(a.size()-1).getMBR()[3];
		return tempMBR;
		
	}
	public double[] calInternalMBR(ArrayList<Node> a) {
		double [] tempMBR = new double[4];
		Collections.sort(a, new BoundCompLeft());
		tempMBR[0] = a.get(0).getMBR()[0];
		Collections.sort(a, new BoundCompRight());
		tempMBR[1] = a.get(a.size()-1).getMBR()[1];
		Collections.sort(a, new BoundCompDown());
		tempMBR[2] = a.get(0).getMBR()[2];
		Collections.sort(a, new BoundCompUp());
		tempMBR[3] = a.get(a.size()-1).getMBR()[3];
		return tempMBR;
		
	}

}

class PointCompX implements Comparator<Entry> {
	public int compare(Entry n1, Entry n2) {
		if(n1.getMBR()[0]!= n2.getMBR()[0]) {
			return n1.getMBR()[0]>n2.getMBR()[0]? 1:-1;
		}
		else {
			return n1.getMBR()[2]>n2.getMBR()[2]? 1:-1;
		}
	}
}
class PointCompY implements Comparator<Entry> {
	public int compare(Entry n1, Entry n2) {
		if(n1.getMBR()[2]!= n2.getMBR()[2]) {
			return n1.getMBR()[2]>n2.getMBR()[2]? 1:-1;
		}
		else {
			return n1.getMBR()[0]>n2.getMBR()[0]? 1:-1;
		}
	}
}
class BoundCompLeft implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getMBR()[0]!= n2.getMBR()[0]) {
			return n1.getMBR()[0]>n2.getMBR()[0]? 1:-1;
		}
		else {
			return n1.getMBR()[1]>n2.getMBR()[1]? 1:-1;
		}
	}
}
class BoundCompRight implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getMBR()[1]!= n2.getMBR()[1]) {
			return n1.getMBR()[1]>n2.getMBR()[1]? 1:-1;
		}
		else {
			return n1.getMBR()[0]>n2.getMBR()[0]? 1:-1;
		}
	}
}
class BoundCompDown implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getMBR()[2]!= n2.getMBR()[2]) {
			return n1.getMBR()[2]>n2.getMBR()[2]? 1:-1;
		}
		else {
			return n1.getMBR()[3]>n2.getMBR()[3]? 1:-1;
		}
	}
}
class BoundCompUp implements Comparator<Node> {
	public int compare(Node n1, Node n2) {
		if(n1.getMBR()[3]!= n2.getMBR()[3]) {
			return n1.getMBR()[3]>n2.getMBR()[3]? 1:-1;
		}
		else {
			return n1.getMBR()[2]>n2.getMBR()[2]? 1:-1;
		}
	}
}
