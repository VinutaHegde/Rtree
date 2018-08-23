package rtree;
import rtree.Rectangle;

import java.util.ArrayList;

/*
 * AUthor: Vinuta Hegde
 * Created Date: 08/18/2018
 * Modified Date: 08/18/2018
 * 
 * Defines the coordinate point with name, x and y values.
 * 
 */

public class Node {
	
	int min_node_capacity;
	int max_node_capacity;
	
	Rectangle rectangle = new Rectangle();
	
	boolean is_root;
	boolean is_leaf;
	
	Node parent_node;
	
	//ArrayList<Rectangle> rectangles = new ArrayList<>();
	ArrayList<Node> children = new ArrayList<>();
	
	
	Node(Node parent_node,double max_x_axis ,double max_y_axis, double min_x_axis, 
			double min_y_axis,boolean is_root, boolean is_leaf){
		
	
		this.rectangle.max_x_axis = max_x_axis;
		//System.out.println(this.rectangle.max_x_axis);
		this.rectangle.max_y_axis = max_y_axis;
		this.rectangle.min_x_axis = min_x_axis;
		this.rectangle.min_y_axis = min_y_axis;	
		this.is_root = is_root;
		this.is_leaf = is_leaf;
		this.max_node_capacity = Tree.max_node_capacity;
		
		if(is_root == true) {
			this.min_node_capacity = 0;
			//this.parent_node  = null;
		}else {
			this.min_node_capacity = Tree.min_node_capacity;
			this.parent_node  = parent_node;
		}

		//this.rectangles = null;
		//this.children = null;
	}
	
	
	
	

}
