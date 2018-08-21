package rtree;


public class Tree {
	
	public void Insert(Node root,Node current_node, CoordintePoint cp) {
		
		if(current_node.is_leaf == true) {
			if(current_node.children.size() < StartMain.max_node_capacity) {
				current_node.points.add(cp);
				return;
				
			}else if(current_node.children.size() >= StartMain.min_node_capacity) {
				Node[] splitted_nodes = current_node.splitNode(root,current_node, cp);
				current_node.parent_node.children.remove(current_node);
				AdjustParent(root,current_node.parent_node,splitted_nodes);
			}
			
		}else if (current_node.is_leaf == false) {
			
			Node next_node = ChooseSubTree(root, current_node, cp);
			Insert(root,next_node,cp);
		}

	}
	


public void AdjustParent(Node root,Node parent_node,Node[] child_nodes) {
	if(parent_node.children.size()+1 < StartMain.min_node_capacity) {
		for(Node c_node: child_nodes) {
		parent_node.children.add(c_node);
		return;
		}
		
	} else if(parent_node.children.size()+1 >= StartMain.min_node_capacity) {
		
		Node[] splitted_nodes = parent_node.splitNode(root,parent_node, null);
		parent_node.parent_node.children.remove(parent_node);
		AdjustParent(root,parent_node.parent_node,splitted_nodes);
	}
	
	
}

	public Node ChooseSubTree(Node root,Node current_node, CoordintePoint cp) {
	    
		double max_x_axis = 99999.00; // infinity
		double max_y_axis= 99999.00;
		double min_x_axis = -99999.00; // -infinity
		double min_y_axis= -99999.00;
		boolean is_root = false;
		boolean is_leaf = true;
		Node next_node = new Node(current_node.parent_node,max_x_axis ,max_y_axis, min_x_axis, min_y_axis, StartMain.max_node_capacity, StartMain.min_node_capacity, is_root, is_leaf);
		
		for(Node current_child: current_node.children) {
		if(current_child.rectangle.min_x_axis <= cp.x_axis && cp.x_axis <= current_child.rectangle.max_x_axis) {
			next_node = current_child;
			break;
		}
		}
		return next_node;
	}


	public void iterate(Node current_node, String path) {

	}

	
	// search any coordinate point
	public void search(Node current_node, CoordintePoint search_point, String path) {

	}

	// Delete any coordinate point
	public void delete(Node current_node, CoordintePoint delete_point, String path) {

	}
}
