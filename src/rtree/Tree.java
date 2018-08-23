package rtree;


public class Tree {
	
	public static int max_node_capacity = 4;
	public static int min_node_capacity = 2;
	
	double max_x_axis = 0;
	double max_y_axis= 0;
	double min_x_axis =0;
	double min_y_axis= 0;
	boolean is_root = true;
	boolean is_leaf = true;
	Node parent_node = null;
	
	Node root = new Node(parent_node,max_x_axis ,max_y_axis, min_x_axis, min_y_axis,  is_root, is_leaf);
	
	public void Insert(Node current_node,Node ele_node) {
		
		if(current_node.is_leaf == true) {
			//System.out.println(current_node.children.size());
			if(current_node.children.size() < max_node_capacity) {
				
				current_node.children.add(ele_node);		
				Node current_node_updated = updateNode(current_node,ele_node.rectangle);
				current_node = current_node_updated;
				iterate(root,"root->");
				return;
				
			}else if(current_node.children.size() >= max_node_capacity) {
				
				current_node.children.add(ele_node);
				Node current_node_updated = updateNode(current_node,ele_node.rectangle);
				current_node = current_node_updated;
				
				Node[] splitted_nodes = splitNode(current_node);
				AdjustParent(current_node,splitted_nodes);
			}
		}else if (current_node.is_leaf == false) {
			
			Node next_node = ChooseSubTree(current_node, ele_node);
			//System.out.println(next_node.is_leaf+"    "+next_node.children.size());
			Insert(next_node,ele_node);
		}
	}



public void AdjustParent(Node current_node,Node[] child_nodes) {
	if(current_node.is_root == true) {
		
		Node temp_root = current_node;
		temp_root.children.clear();
		
		//System.out.println(" parent s a root");
		for(Node c_node: child_nodes) {
			
			temp_root.children.add(c_node);
			temp_root = updateNode(temp_root,c_node.rectangle);
			
			}
		temp_root.is_leaf = false;
		//System.out.println("created new root. returning"); 
		root = temp_root;
		return;
	}else {
		Node parent_node = current_node.parent_node;
		
		if(parent_node.children.size() < max_node_capacity) {
			
		System.out.println("parent has space adding to current");
		parent_node.children.remove(current_node);
		for(Node c_node: child_nodes) {
		parent_node.children.add(c_node);
		}
		return;
	  } else if(parent_node.children.size()+1 >= max_node_capacity) {
		
		System.out.println("parent has no space"); 

			System.out.println("parent is non root");
		Node[] splitted_nodes = splitNode(parent_node);
		AdjustParent(parent_node.parent_node,splitted_nodes);
		}
	}
}

	public Node ChooseSubTree(Node current_node, Node ele_node) {
	    
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
		
		Node nearest_node = null;
		double min_area_difference = Double.MAX_VALUE;
		
		for(Node current_child: current_node.children) {
			
			Node temp = updateNode(current_child,ele_node.rectangle);
			
			double current_area_difference = temp.rectangle.GetArea() - current_child.rectangle.GetArea();
			
			//System.out.println("current_area_difference is :"+current_area_difference);
			if(min_area_difference >= current_area_difference) {
				min_area_difference=current_area_difference;
				nearest_node = current_child;
			}
		}
		return nearest_node;
	}

	
	

	
	public Node[] splitNode(Node current_node) {
		System.out.println(" splitting the node");
		
		Node[] seeds = PickSeed(current_node);
		
		boolean is_root = false;
		
		System.out.println("current_node.is_leaf  :"+current_node.is_leaf);
		boolean is_leaf = current_node.is_leaf;
		
		Node G1 = new Node(current_node,seeds[0].rectangle.max_x_axis ,seeds[0].rectangle.max_y_axis,
				seeds[0].rectangle.min_x_axis, seeds[0].rectangle.min_y_axis,  is_root, is_leaf);
	    G1.children.add(seeds[0]);
	    Node temp_G1 = updateNode(G1,seeds[0].rectangle);
		G1 = temp_G1;
		Node G2 = new Node(current_node,seeds[1].rectangle.max_x_axis ,seeds[1].rectangle.max_y_axis,
				seeds[1].rectangle.min_x_axis, seeds[1].rectangle.min_y_axis,  is_root, is_leaf);
		G2.children.add(seeds[1]);
		Node temp_G2 = updateNode(G2,seeds[1].rectangle);
		G2 = temp_G2;
		
		//System.out.println(" current node children : "+current_node.children.size());
		
		for(int i=0 ; i< current_node.children.size(); i++) {
			
			double max_d = 0;
			double d1_opt = 0;
			double d2_opt = 0;
			Node next_node_to_add = null;
			
    	for(Node in_loop_child: current_node.children) {
			
			if(!(G1.children.contains(in_loop_child) || G2.children.contains(in_loop_child))) {
				
				//System.out.println(" current node not in group");
				
				Node temp1 = updateNode(G1,in_loop_child.rectangle);
				Node temp2 = updateNode(G2,in_loop_child.rectangle);
		
			
				double d1 = temp1.rectangle.GetArea() - G1.rectangle.GetArea();
				double d2 = temp2.rectangle.GetArea() - G2.rectangle.GetArea();
								
				double d = Math.abs(d1-d2);
				
		    if(max_d<=d) {
					//System.out.println("max_d<d  swaping next node to add");
					max_d = d;
					d1_opt = d1;
					d2_opt = d2;
					next_node_to_add = in_loop_child;
				}
				
			}
			else {
				
				//System.out.println(" current node already in group");
			}
		}
			
		if(next_node_to_add != null) {
			
		if(i>current_node.children.size()-min_node_capacity-2) {
			
			System.out.println(" need to check min capacity condition");
			if(G1.children.size()<min_node_capacity) {
				//System.out.println(" c ount low in G1");
				G1.children.add(next_node_to_add);
				Node G1_temp =updateNode( G1,next_node_to_add.rectangle);
				G1= G1_temp;
				
			}else if(G2.children.size()<min_node_capacity) {
				//System.out.println(" c ount low in G2");
				G2.children.add(next_node_to_add);
				Node G2_temp = updateNode(G2,next_node_to_add.rectangle);
				G2 = G2_temp;
			}else {
				
				//System.out.println(" all good going the usual way");
				if(d1_opt<=d2_opt) {
					
					G1.children.add(next_node_to_add);
					Node G1_temp =updateNode( G1,next_node_to_add.rectangle);
					G1= G1_temp;
					//System.out.println(" adding to G1!!");
					
				}else if(d2_opt<d1_opt){
					G2.children.add(next_node_to_add);
					Node G2_temp = updateNode(G2,next_node_to_add.rectangle);
					G2 = G2_temp;
				}
			}
			
		}else {
		if(d1_opt<=d2_opt) {
			
			G1.children.add(next_node_to_add);
			Node G1_temp =updateNode( G1,next_node_to_add.rectangle);
			G1= G1_temp;
			//System.out.println(" adding to G1!!");
			
		}else if(d2_opt<d1_opt){
			G2.children.add(next_node_to_add);
			Node G2_temp = updateNode(G2,next_node_to_add.rectangle);
			G2 = G2_temp;
		}
		}
		}else {
			//System.out.println(" current was already inserted");
		}
		}
	
		//System.out.println("G1 children"+G1.children.size());
		//System.out.println("G2 children"+G2.children.size());
		Node[] splitted_nodes = {G1,G2};
		return splitted_nodes;
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
	
	
public Node updateNode(Node current_Node, Rectangle new_entry) {
	
	
		Node updated_node = current_Node;
		
      if(updated_node.rectangle.max_x_axis<new_entry.max_x_axis) {
    	  updated_node.rectangle.max_x_axis = new_entry.max_x_axis;
      }
      
      if(updated_node.rectangle.max_y_axis<new_entry.max_y_axis) {
    	  updated_node.rectangle.max_y_axis = new_entry.max_y_axis;
      }
	
      if(updated_node.rectangle.min_x_axis>new_entry.min_x_axis) {
    	  updated_node.rectangle.min_x_axis = new_entry.min_x_axis;
      }
      if(updated_node.rectangle.min_y_axis>new_entry.min_y_axis) {
    	  updated_node.rectangle.min_y_axis = new_entry.min_y_axis;
      }
      
  return updated_node;
      
	}


	public void iterate(Node current_node, String path) {
		if (current_node == null) {
				return;
			} else if (current_node.is_leaf == true) {
				System.out.println(path + current_node.children.size());
				return;
			}else {
			
				for(int i =0; i <current_node.children.size();i++) {
					iterate(current_node.children.get(i),path+Integer.toString(i)+"->");
				}
		}
	}
	

}
