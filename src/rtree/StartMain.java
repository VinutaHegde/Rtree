package rtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * AUthor: Vinuta Hegde
 * Created Date: 08/18/2018
 * Modified Date: 08/18/2018
 * 
 * Defines the coordinate point with name, x and y values.
 * 
 */

public class StartMain {

	public static int max_node_capacity = 4;
	public static int min_node_capacity = 4;
	public static void main(String[] args) {
		// 
			try {
				File file = new File(".\\input\\GSV_spatialdata.txt"); // input file
				
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
			
				double max_x_axis = 99999.00; // infinity
				double max_y_axis= 99999.00;
				double min_x_axis = -99999.00; // -infinity
				double min_y_axis= -99999.00;
				boolean is_root = true;
				boolean is_leaf = true;
				Node parent_node = null;
				
				Node root = new Node(parent_node,max_x_axis ,max_y_axis, min_x_axis, min_y_axis, max_node_capacity, min_node_capacity, is_root, is_leaf);		
				Tree r_tree = new Tree(); 
				
				String line;
				while ((line = bufferedReader.readLine()) != null) { //reading the file line by line.
					String[] row = line.split(" ", 0); 
					
					//parse and create a point object
					CoordintePoint cp = new CoordintePoint();
					cp.point_name = row[0];
					cp.x_axis = Double.parseDouble(row[1]);
					cp.y_axis = Double.parseDouble(row[2]); 
					
					//insert the point to tree
					r_tree.Insert(root,root, cp);			
					}
				fileReader.close();
				
				//Traverse the tree 
				r_tree.iterate(root , "root->");	
			} catch (IOException e) {
				e.printStackTrace();
			}

		
	}

}
