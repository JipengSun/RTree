package rtree;

public class Rtree {
	private int M;
	private int min = (int) Math.ceil(M*0.4);
	private Node root;
	
	
	public Rtree(final int M) {
		// TODO Auto-generated constructor stub
		this.M = M;
	
		
	}
	public void setRoot(Node a) {
		this.root = a;
	}
	public int rangequery(Node u, double x1, double x2, double y1, double y2,int count) {
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
				rangequery(u.getChildren().get(j), x1, x2, y1, y2,count);
			}
		}
		return count;
	}
	public void insert(Node u, Entry p) {
		if(u.isLeaf) {
			u.getEntry().add(p);
			p.setParent(u);
			updateMBR(u,p);
			if(u.getEntry().size()>M) {
				handleoverflow(u);
			}
			else {
				Node v = choose_subtree(u,p);
				insert(v, p);
			}
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
			
		}
		else {
			Node w = u.getParent();
			
		}
		}
	

}
