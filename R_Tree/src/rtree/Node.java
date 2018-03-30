package rtree;

import java.util.LinkedList;

public class Node {
	final LinkedList<Node> children;
	private Node parent;
	protected float[] MBR = new float[4];
	private boolean isleaf;
	
	public Node(float[] MBR, boolean isleaf) {
		this.isleaf = isleaf;
		System.arraycopy(MBR, 0, this.MBR, 0, MBR.length);
		children = new LinkedList<Node>();
	}

}

