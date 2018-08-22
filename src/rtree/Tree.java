package rtree;


public class Tree {
	
	public void Insert(Node root,Node current_node, Node ele_node) {
		
		if(current_node.is_leaf == true) {
			System.out.println(current_node.children.size());
			if(current_node.children.size() < StartMain.max_node_capacity) {
				
				current_node.children.add(ele_node);
				current_node = updateNode(current_node,ele_node.rectangle);
				return;
				
			}else if(current_node.children.size() >= StartMain.min_node_capacity) {
				
				current_node.children.add(ele_node);
				current_node = updateNode(current_node,ele_node.rectangle);
				Node[] splitted_nodes = splitNode(root,current_node);
				if(!current_node.is_root) {
				current_node.parent_node.children.remove(current_node);
				AdjustParent(root,current_node.parent_node,splitted_nodes);
				} else {
					//current_node.parent_node.children.remove(current_node);
					AdjustParent(root,current_node,splitted_nodes);	
				}
				
			}
			
		}else if (current_node.is_leaf == false) {
			
			Node next_node = ChooseSubTree(root, current_node, ele_node);
			Insert(root,next_node,ele_node);
		}

	}
	
	public Node updateNode(Node current_node,Rectangle new_entry) {
	
      if(current_node.rectangle.max_x_axis<new_entry.max_x_axis) {
    	  current_node.rectangle.max_x_axis = new_entry.max_x_axis;
      }
      
      if(current_node.rectangle.max_y_axis<new_entry.max_y_axis) {
    	  current_node.rectangle.max_y_axis = new_entry.max_y_axis;
      }
	
      if(current_node.rectangle.min_x_axis>new_entry.min_x_axis) {
    	  current_node.rectangle.min_x_axis = new_entry.min_x_axis;
      }
      if(current_node.rectangle.min_y_axis>new_entry.min_y_axis) {
    	  current_node.rectangle.min_y_axis = new_entry.min_y_axis;
      }
      
      return current_node;
      
	}


public void AdjustParent(Node root,Node parent_node,Node[] child_nodes) {
	if(parent_node.children.size() < StartMain.max_node_capacity) {
		for(Node c_node: child_nodes) {
		parent_node.children.add(c_node);
		return;
		}
		
	} else if(parent_node.children.size()+1 >= StartMain.max_node_capacity) {
		
		Node[] splitted_nodes = splitNode(root,parent_node);
		parent_node.parent_node.children.remove(parent_node);
		AdjustParent(root,parent_node.parent_node,splitted_nodes);
	}
}

	public Node ChooseSubTree(Node root,Node current_node, Node ele_node) {
	    
		Node next_node = current_node;
		boolean overlap = false;
		
		for(Node current_child: current_node.children) {
			
		if(current_child.rectangle.CheckIsInside(ele_node.rectangle)) {
			next_node = current_child;
			overlap = true;
			break;
		}
		}
		
		if(overlap == false) {
			next_node = getNearest(current_node,ele_node);
		}
		return next_node;
	}

	
	public Node getNearest(Node current_node,Node ele_node) {
		
		Node nearest_node = current_node;
		double min_area_difference = 999999999;
		
		for(Node current_child: current_node.children) {
			
			Node temp = updateNode(current_node,ele_node.rectangle);
			
			double current_area_difference = temp.rectangle.GetArea() - current_child.rectangle.GetArea();
			
			if(min_area_difference > current_area_difference) {
				min_area_difference=current_area_difference;
				nearest_node = current_child;
			}
		}
		return nearest_node;
	}

	
	

	
	public Node[] splitNode(Node root, Node current_node) {

		Node[] seeds = PickSeed(current_node);
		
		boolean is_root = false;
		boolean is_leaf = current_node.is_leaf;
		
		Node G1 = new Node(current_node,seeds[0].rectangle.max_x_axis ,seeds[0].rectangle.max_y_axis,
				seeds[0].rectangle.min_x_axis, seeds[0].rectangle.min_y_axis,  is_root, is_leaf);
	    G1.children.add(seeds[0]);
		
		Node G2 = new Node(current_node,seeds[1].rectangle.max_x_axis ,seeds[1].rectangle.max_y_axis,
				seeds[1].rectangle.min_x_axis, seeds[1].rectangle.min_y_axis,  is_root, is_leaf);
		G2.children.add(seeds[1]);
		
		for(int i =0 ; i< current_node.children.size();i++) {
			
			double max_d = 0;
			double d1_opt = 0;
			double d2_opt = 0;
			Node next_node_to_add = current_node;
			
		for(int j =0 ; j< current_node.children.size();j++) {
			
			if(!(current_node.children.get(j).equals(seeds[0]) || current_node.children.get(j).equals(seeds[1]))) {
				
				Node temp1 = updateNode(G1,current_node.children.get(j).rectangle);
				Node temp2 = updateNode(G2,current_node.children.get(j).rectangle);
				
				double d1 = temp1.rectangle.GetArea() - G1.rectangle.GetArea();
				double d2 = temp2.rectangle.GetArea() - G2.rectangle.GetArea();
				
				double d = Math.abs(d1-d2);
				
				if(max_d<d) {
					max_d = d;
					d1_opt = d1;
					d2_opt = d2;
					next_node_to_add = current_node.children.get(j);
				}
				
			}
		}
		
		if(i>current_node.children.size()-StartMain.min_node_capacity-2) {
			
			if(G1.children.size()<StartMain.min_node_capacity) {
				
			}else if(G1.children.size()<StartMain.min_node_capacity) {
				
			}else {
				
				if(d1_opt<d2_opt) {
					
					G1.children.add(next_node_to_add);
					G1 = updateNode(G1,next_node_to_add.rectangle);
					
				}else if(d2_opt<d1_opt){
					G2.children.add(next_node_to_add);
					G2 = updateNode(G2,next_node_to_add.rectangle);
				}
			}
		
		}
		else {
		if(d1_opt<d2_opt) {
			
			G1.children.add(next_node_to_add);
			G1 = updateNode(G1,next_node_to_add.rectangle);
			
		}else if(d2_opt<d1_opt){
			G2.children.add(next_node_to_add);
			G2 = updateNode(G2,next_node_to_add.rectangle);
		}
		
		}
		}
	
		return seeds;
	}
	
	
	public Node[] PickSeed(Node current_node) {
		
		Node[] seeds = new Node[2];
		double max_area = 0;
		
		for(int i = 0 ; i<current_node.children.size();i++) {
			for(int j = i+1 ; j<current_node.children.size();j++) {
			
				Node temp = updateNode(current_node.children.get(i),current_node.children.get(j).rectangle);
				if(max_area < temp.rectangle.GetArea()) {
					max_area = temp.rectangle.GetArea();
					seeds[0] = current_node.children.get(i);
					seeds[1] = current_node.children.get(j);
				}
				
			}
		}
		return seeds;
	}
	
	public void iterate(Node current_node, String path) {
		if (current_node == null) {
				return;
			} else if (current_node.is_leaf == true) {
				System.out.println(path + current_node.children.size());
				return;
			}else {
			
				for(int i =0; i <current_node.children.size();i++) {
					iterate(current_node,path+i+"->");
				}
		}
	}
	

}
