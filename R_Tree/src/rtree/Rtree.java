package rtree;

public class Rtree {
	private int M;
	private int min = (int) Math.ceil(M*0.4);
	private Node root;
	
	
	public Rtree(final int M, Entry first) {
		// Create a leaf node for the first entry and let the node be the root.
		this.M = M;
		this.root = createRoot(first);
	
		
	}
	public Node getRoot() {
		return root;
	}
	
	public Node createRoot(Entry e) {
		Node root = new Node(e.getMBR()[0], e.getMBR()[1], e.getMBR()[2], e.getMBR()[3], true);
		root.getEntry().add(e);
		root.setParent(null);
		e.setParent(root);
		return root;
	}
	
	public void setRoot(Node a) {
		this.root = a;
	}
	
	public int rangequery(Node u, double x1, double x2, double y1, double y2) {
		int count = 0;
		if (u.isLeaf == true) {
			int en = u.getEntry().size();
			for (int i = 0;i< en;i++) {
				if(u.getEntry().get(i).isIntersects(x1, x2, y1, y2)) {
					count ++;
				}
			}
			return count;
		}
		
		else {
			int size = u.getChildren().size();
			for (int j = 0; j< size; j++) {
				if (u.getChildren().get(j).isIntersects(x1, x2, y1, y2))
				count = count + rangequery(u.getChildren().get(j), x1, x2, y1, y2);
			}
			return count;
			
		}
		//return count;
	}
	public void insert(Node u, Entry p) {
		if(u.isLeaf) {
			u.getEntry().add(p);
			p.setParent(u);
			updateMBR(u,p);
			if(u.getEntry().size()>M) {
				handleoverflow(u);
			}

		}
		else {
			Node v = choose_subtree(u,p);
			insert(v, p);
		}
	}
	public void updateMBR(Node u,Node p) {
		// Update current MBR by the children or entries. Don't care about the overflow in this function. 
		// For leaf nodes, execute this in entries, for internal nodes, execute this in children.
			// MBR needs to be expanded.
			if(!(p.getMBR()[0]>=u.getMBR()[0]&&p.getMBR()[1]<= u.getMBR()[1]
					&&p.getMBR()[2]>=u.getMBR()[2]&&p.getMBR()[3]<= u.getMBR()[3])) {
				u.getMBR()[0] = Math.min(u.getMBR()[0], p.getMBR()[0]);
				u.getMBR()[1] = Math.max(u.getMBR()[1], p.getMBR()[1]);
				u.getMBR()[2] = Math.min(u.getMBR()[2], p.getMBR()[2]);
				u.getMBR()[3] = Math.max(u.getMBR()[3], p.getMBR()[3]);
				// Check if the expansion causes the cascading expansion 
				if(u.getParent()!=null)
					updateMBR( u.getParent(), u);
			}
		
	}
	public Node choose_subtree(Node u,Entry p) {
		int num = u.getChildren().size();
		for (int i = 0; i< num; i++) {
			if(u.getChildren().get(i).isIntersects(p.getMBR()[0], p.getMBR()[1], p.getMBR()[2], p.getMBR()[3])) {
				return u.getChildren().get(i);
			}
			
		}
		// The MBR has to extend to cover the new point. Choose the minimum increase in perimeter.
		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i< num;i++) {
			if(u.increasePerimeter(p)< min) {
				index = i;
			}
		}
		return u.getChildren().get(index);
		
	}
	public void handleoverflow(Node u) {
		Node u1 = u.split();
		if (u.getParent()==null) {
			// Create new root and add u,u1 as its children. 
			// Note that the current 'root' number is and only is 2, can't be more since the only way to create root Node
			// after the first insertion is splitting. And if current root split to two parts, then the algorithm will
			// generate a new root to contain two nodes. So the root number is back to 1 again.
			double[] newMBR = new double[4];
			newMBR[0] = Math.min(u.getMBR()[0], u1.getMBR()[0]);
			newMBR[1] = Math.max(u.getMBR()[1], u1.getMBR()[1]);
			newMBR[2] = Math.min(u.getMBR()[2], u1.getMBR()[2]);
			newMBR[3] = Math.max(u.getMBR()[3], u1.getMBR()[3]);
			Node root1 = new Node(newMBR[0], newMBR[1], newMBR[2], newMBR[3], false);
			root = root1;
			root1.setParent(null);
			root1.addChildren(u);
			root1.addChildren(u1);
			u.setParent(root1);
			u1.setParent(root1);// u1's parent used to be as same as u, null.
		}
		else {
			Node w = u.getParent();
			//Update w 's MBR
			w.addChildren(u1);
			// Note that adding new point to a node can only leads to the MBR to increase or stay the same before splitting.
			// And even after splitting, the parent of the node is still increasing or constant.
			// We can't know which node the inserting point belongs to, but we definitely know it must belong to u or u1.
			// So we just update u and u1 to w, the effect is same as the update the inserted point to w.
			updateMBR(w, u);
			updateMBR(w, u1);
			if(w.getChildren().size()>M) {
				handleoverflow(w);
			}
		}
		}
	

}
