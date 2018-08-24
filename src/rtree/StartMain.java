package rtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * AUthor: Vinuta Hegde
 * Created Date: 08/18/2018
 * Modified Date: 08/23/2018
 * 
 * Defines the rectangles with name, (x-min,y-min) and (x-max,y-max) values.
 * Adds the new entries to Rtree.
 * 
 */

public class StartMain {

	public static void main(String[] args) {

		try {
			// File file = new File(".\\input\\GSV_spatialdata.txt"); // input file
			File file = new File(".\\input\\GSV_spatialdataSample100.txt"); // input file
			// File file = new File(".\\input\\sampledata"); // input file

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			Tree r_tree = new Tree();

			String line;
			while ((line = bufferedReader.readLine()) != null) { // reading the file line by line.
				String[] row = line.split(" ", 0);

				// parse and create a point object

				double x_axis = Double.parseDouble(row[1]);
				double y_axis = Double.parseDouble(row[2]);

				//System.out.println(x_axis + "," + y_axis);
				double max_x = x_axis;
				double max_y = y_axis;
				double min_x = x_axis;
				double min_y = y_axis;

				// Points are considered as nodes with rectangles with 0 area. i.e max and min
				// coordinates are same.
				Node ele_node = new Node(null, max_x, max_y, min_x, min_y, false, false);

				// insert the point to tree
				r_tree.Insert(r_tree.root, ele_node);
			}
			fileReader.close();

			// Traverse the tree
			r_tree.iterate(r_tree.root, "root->");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
