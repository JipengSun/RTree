package rtree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {

		try {
			String pathname = "./dataset.txt";
			File filename = new File(pathname);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);
			// Get the total number
			String line = "";
			line = br.readLine();
			int num = Integer.parseInt(line);
			double[][] dataset = new double[num][2];
			// Get the first data row alone to initialize the R-Tree.
			line = br.readLine();
			String[] StrArray = line.split(" ");
			dataset[0][0] = Double.parseDouble(StrArray[1]);
			dataset[0][1] = Double.parseDouble(StrArray[2]);
			Entry e0 = new Entry(dataset[0][0], dataset[0][1]);
			Rtree rtree = new Rtree(450, e0);
			// Read and insert the R-Tree.
			Entry[] entries = new Entry[num];
			entries[0] = e0;
			for(int i = 1; i<num; i++) {
				line = br.readLine();
				String[] StrArray1 = line.split(" ");
				dataset[i][0] = Double.parseDouble(StrArray1[1]);
				dataset[i][1] = Double.parseDouble(StrArray1[2]);
				Entry ei = new Entry(dataset[i][0], dataset[i][1]);
				entries[i] = ei;
				rtree.insert(rtree.getRoot(), ei);
			}
			br.close();
			String Querypathname = "./test_query.txt";
			File Queryfilename = new File(Querypathname);
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(Queryfilename));
			BufferedReader br1 = new BufferedReader(reader1);
			
			
			int testnum = 10;
			double[][] testquery = new double[testnum][4];
			for(int i = 0;i<testnum;i++) {
				String line1 = "";
				line1 = br1.readLine();
				String[] strarray1 = line1.split(" ");
				testquery[i][0] = Double.parseDouble(strarray1[0]);
				testquery[i][1] = Double.parseDouble(strarray1[1]);
				testquery[i][2] = Double.parseDouble(strarray1[2]);
				testquery[i][3] = Double.parseDouble(strarray1[3]);
			}
			
			// Start testing the sequential-scan benchmark
			//long startTime1 = System.currentTimeMillis();
			int []testresult = new int[testnum];
			long startTime1 = System.nanoTime();
			for (int i = 0; i<testnum;i++) {
				for (int j = 0; j<num;j++) {
					if (entries[j].isIntersects(testquery[i][0], testquery[i][1], testquery[i][2], testquery[i][3])) {
						testresult[i]++;
					}
				}
				//System.out.println(testresult[i]);
			}
			/* Test the new Bench Mark
			String Datapathname = "./dataset.txt";
			File filename2 = new File(Datapathname);
			InputStreamReader reader2 = new InputStreamReader(new FileInputStream(filename2));
			BufferedReader br2 = new BufferedReader(reader2);
			String line2 = "";
			line2 = br2.readLine();
			while(line2!=null) {
				line2 = br2.readLine();
			}
			br2.close();
			//long endTime1 = System.currentTimeMillis();
			 */
			long endTime1 = System.nanoTime();
			long sscantime = (endTime1-startTime1)/testnum;
			System.out.println("The sequential-scan benchmark is "+sscantime+"ns");
			int [] queryresults = new int[10];
			// Start testing the average query time
			//long startTime = System.currentTimeMillis();
			long startTime = System.nanoTime();
			for(int i = 0; i<testnum;i++) {
				queryresults[i] = rtree.rangequery(rtree.getRoot(), testquery[i][0],testquery[i][1], testquery[i][2], testquery[i][3]);
			}
			//long endTime = System.currentTimeMillis();
			long endTime = System.nanoTime();
			long averageQuerytime = (endTime-startTime)/testnum;
			System.out.println("The Average Query Time is: "+averageQuerytime+"ns.");
			System.out.println("So my R-Tree average query time is "+(sscantime/averageQuerytime)+" times faster than the sequential scan!");
			br1.close();
			File writename = new File("./Query_Result.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			for(int i = 0;i<testnum;i++) {
				out.write(queryresults[i]+"\r\n");
			}
			out.flush();
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
 }
}
