package rtree;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Entry entry = new Entry(2,1);
		//System.out.println(entry);
		//System.out.println(name1(2));
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(0,new Entry(0, 1));
		entries.add(1,new Entry(1, 100));
		entries.add(2,new Entry(2, 3));
		entries.add(3,new Entry(4, 5));
		
		Rtree rtree = new Rtree(4);
		double[] a = entry.calMBR(entries);
		System.out.println(a[0]);
		System.out.println(a[1]);
		System.out.println(a[2]);
		System.out.println(a[3]);
		Node aNode = new Node(a[0], a[1], a[2], a[3], true);
		aNode.setEntry(entries);
		aNode.setParent(null);
		Node u1 = aNode.split();
		System.out.println(aNode.getEntry());
		System.out.println(u1.getEntry());
		
		System.out.println(rtree.rangequery(aNode, 0, 2, 1, 3, 0));
		System.out.println(entries.get(0).isIntersects(0, 2, 1, 3));

 }
}
