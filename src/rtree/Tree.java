package rtree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Tree {

	public static int max_node_capacity = 4;
	public static int min_node_capacity = 2;

	double max_x_axis = 0;
	double max_y_axis = 0;
	double min_x_axis = 0;
	double min_y_axis = 0;
	boolean is_root = true;
	boolean is_leaf = true;
	Node parent_node = null;

	// Node of the tree
	Node root = new Node(parent_node, max_x_axis, max_y_axis, min_x_axis, min_y_axis, is_root, is_leaf);

	public void Insert(Node current_node, Node ele_node) {

		if (current_node.is_leaf == true) { // Check whether the current node is a leaf node

			if (current_node.children.size() < max_node_capacity) { // and if the leaf node has capacity
				ele_node.parent_node = current_node; // update the parent of current node
				current_node.children.add(ele_node); // add current node to parent
				updateCurrentNode(current_node, ele_node.rectangle); // update parent area
				return;
			} else if (current_node.children.size() >= max_node_capacity) { // if current node does not have room,
				ele_node.parent_node = current_node; // add the node before and then split
				current_node.children.add(ele_node);
				updateCurrentNode(current_node, ele_node.rectangle);
				Node[] split_nodes = splitNode(current_node);
				AdjustParent(current_node, split_nodes); // adjust the split nodes by adding them to parent
			}
		} else if (current_node.is_leaf == false) { // if current node is not leaf node, choose the next node and insert

			int next_node_index = ChooseSubTree(current_node, ele_node);
			Insert(current_node.children.get(next_node_index), ele_node);

		}
	}

	public void AdjustParent(Node current_node, Node[] child_nodes) {

		if (current_node.is_root == true) {

			/*
			 * if current node is root create new node add split node to newly created node
			 * and swap it with the existing root
			 */

			Node temp_root = new Node(parent_node, max_x_axis, max_y_axis, min_x_axis, min_y_axis, is_root, false);

			for (Node c_node : child_nodes) {

				c_node.parent_node = temp_root;
				temp_root.children.add(c_node);
				updateCurrentNode(temp_root, c_node.rectangle);

			}
			root = temp_root;
			return;
		} else {
			/*
			 * if the node is node not root, check for the parent capacity
			 */
			Node parent_node = current_node.parent_node;

			if (parent_node.children.size() < max_node_capacity) {
				/*
				 * if parent has capacity, just remove the current node from children list and
				 * add split nodes and return
				 */
				parent_node.children.remove(current_node);
				for (Node c_node : child_nodes) {
					c_node.parent_node = parent_node;
					parent_node.children.add(c_node);
				}
				return;
			} else if (parent_node.children.size() >= max_node_capacity) {
				/*
				 * if parent does not have capacity, remove the current node and add split nodes
				 * then split the parent. then call the adjust on parent node and split node
				 * array.
				 */
				parent_node.children.remove(current_node);
				for (Node c_node : child_nodes) {
					c_node.parent_node = parent_node;
					parent_node.children.add(c_node);
				}
				Node[] split_nodes = splitNode(parent_node);
				AdjustParent(parent_node, split_nodes);
			}
		}
	}

	public int ChooseSubTree(Node current_node, Node ele_node) {
		/*
		 * If node to be added, overlaps with any of the children rectangles, return its
		 * index, else return nearest node index.
		 */

		int next_node_index = 0;
		boolean overlap = false;
		for (int i = 0; i < current_node.children.size(); i++) {
			if (current_node.children.get(i).rectangle.CheckIsInside(ele_node.rectangle)) {
				overlap = true;
				next_node_index = i;
			}
		}

		if (overlap == false) {
			next_node_index = getNearest(current_node, ele_node);
		}

		return next_node_index;
	}

	public int getNearest(Node current_node, Node ele_node) {
		/*
		 * Find nearest node for node to be added
		 */
		int nearest_node_index = 0;
		double min_area_difference = Double.MAX_VALUE;

		for (int i = 0; i < current_node.children.size(); i++) {
			Node temp = updateNode(current_node.children.get(i), ele_node.rectangle);
			double current_area_difference = temp.rectangle.GetArea()
					- current_node.children.get(i).rectangle.GetArea();
			if (min_area_difference >= current_area_difference) {
				min_area_difference = current_area_difference;
				nearest_node_index = i;
			}
		}
		return nearest_node_index;
	}

	public Node[] splitNode(Node current_node) {
		/*
		 * Take node, split the nodes into 2 grous.
		 */
		Node[] seeds = PickSeed(current_node); // pick seeds fro the group
		boolean is_root = false;
		boolean is_leaf = current_node.is_leaf;

		/*
		 * Create node G1 add seed 1 to G1
		 */
		Node G1 = new Node(current_node.parent_node, seeds[0].rectangle.max_x_axis, seeds[0].rectangle.max_y_axis,
				seeds[0].rectangle.min_x_axis, seeds[0].rectangle.min_y_axis, is_root, is_leaf);
		seeds[0].parent_node = G1;
		G1.children.add(seeds[0]);
		updateCurrentNode(G1, seeds[0].rectangle);

		/*
		 * Create node G2 add seed 2 to G2
		 */

		Node G2 = new Node(current_node.parent_node, seeds[1].rectangle.max_x_axis, seeds[1].rectangle.max_y_axis,
				seeds[1].rectangle.min_x_axis, seeds[1].rectangle.min_y_axis, is_root, is_leaf);
		seeds[1].parent_node = G2;
		G2.children.add(seeds[1]);
		updateCurrentNode(G2, seeds[1].rectangle);

		/*
		 * Add remaining nodes to G1 or G2
		 */
		for (int i = 0; i < current_node.children.size(); i++) {

			double max_d = 0;
			double d1_opt = 0;
			double d2_opt = 0;
			Node next_node_to_add = null;

			for (Node in_loop_child : current_node.children) {

				if (!(G1.children.contains(in_loop_child) || G2.children.contains(in_loop_child))) {

					Node temp1 = updateNode(G1, in_loop_child.rectangle);
					Node temp2 = updateNode(G2, in_loop_child.rectangle);

					double d1 = temp1.rectangle.GetArea() - G1.rectangle.GetArea();
					double d2 = temp2.rectangle.GetArea() - G2.rectangle.GetArea();

					double d = Math.abs(d1 - d2);

					if (max_d <= d) {
						max_d = d;
						d1_opt = d1;
						d2_opt = d2;
						next_node_to_add = in_loop_child;
					}

				}

			}

			if (next_node_to_add != null) {

				if (i > current_node.children.size() - min_node_capacity - 2) {

					if (G1.children.size() < min_node_capacity) {
						next_node_to_add.parent_node = G1;
						G1.children.add(next_node_to_add);
						updateCurrentNode(G1, next_node_to_add.rectangle);

					} else if (G2.children.size() < min_node_capacity) {
						next_node_to_add.parent_node = G2;
						G2.children.add(next_node_to_add);
						updateCurrentNode(G2, next_node_to_add.rectangle);
					} else {
						if (d1_opt <= d2_opt) {
							next_node_to_add.parent_node = G1;
							G1.children.add(next_node_to_add);
							updateCurrentNode(G1, next_node_to_add.rectangle);
						} else if (d2_opt < d1_opt) {
							next_node_to_add.parent_node = G2;
							G2.children.add(next_node_to_add);
							updateCurrentNode(G2, next_node_to_add.rectangle);
						}
					}

				} else {
					if (d1_opt <= d2_opt) {
						next_node_to_add.parent_node = G1;
						G1.children.add(next_node_to_add);
						updateCurrentNode(G1, next_node_to_add.rectangle);

					} else if (d2_opt < d1_opt) {
						next_node_to_add.parent_node = G2;
						G2.children.add(next_node_to_add);
						updateCurrentNode(G2, next_node_to_add.rectangle);
					}
				}
			}
		}

		Node[] split_nodes = { G1, G2 };
		return split_nodes;
	}

	public Node[] PickSeed(Node current_node) {
		/*
		 * Pick seeds to form the group
		 */
		Node[] seeds = new Node[2];
		double max_area = 0;

		for (int i = 0; i < current_node.children.size(); i++) {
			for (int j = i + 1; j < current_node.children.size(); j++) {

				Node temp = updateNode(current_node.children.get(i), current_node.children.get(j).rectangle);
				if (max_area <= temp.rectangle.GetArea()) {
					max_area = temp.rectangle.GetArea();
					seeds[0] = current_node.children.get(i);
					seeds[1] = current_node.children.get(j);
				}

			}
		}
		return seeds;
	}

	public Node updateNode(Node current_Node, Rectangle new_entry) {

		/*
		 * create new node, update its rectangles based on node to be added return new
		 * node
		 */
		Node updated_node = current_Node;

		if (updated_node.rectangle.max_x_axis < new_entry.max_x_axis) {
			updated_node.rectangle.max_x_axis = new_entry.max_x_axis;
		}

		if (updated_node.rectangle.max_y_axis < new_entry.max_y_axis) {
			updated_node.rectangle.max_y_axis = new_entry.max_y_axis;
		}

		if (updated_node.rectangle.min_x_axis > new_entry.min_x_axis) {
			updated_node.rectangle.min_x_axis = new_entry.min_x_axis;
		}
		if (updated_node.rectangle.min_y_axis > new_entry.min_y_axis) {
			updated_node.rectangle.min_y_axis = new_entry.min_y_axis;
		}

		return updated_node;

	}

	public void updateCurrentNode(Node current_Node, Rectangle new_entry) {

		/*
		 * Update the current node rectangle parameters based on node to be added
		 */
		if (current_Node.rectangle.max_x_axis < new_entry.max_x_axis) {
			current_Node.rectangle.max_x_axis = new_entry.max_x_axis;
		}

		if (current_Node.rectangle.max_y_axis < new_entry.max_y_axis) {
			current_Node.rectangle.max_y_axis = new_entry.max_y_axis;
		}

		if (current_Node.rectangle.min_x_axis > new_entry.min_x_axis) {
			current_Node.rectangle.min_x_axis = new_entry.min_x_axis;
		}
		if (current_Node.rectangle.min_y_axis > new_entry.min_y_axis) {
			current_Node.rectangle.min_y_axis = new_entry.min_y_axis;
		}

	}

	public void iterate(Node current_node, String path) {
		
		
		File file = new File(".\\SampleOutPut\\GSV_spatialdataSampleOutput.txt");


		if (current_node == null) {
			return;
		} else if (current_node.is_leaf == true) {
			try {
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println(path + current_node.children.size());
			bw.write(path + current_node.children.size()+"\n");
			bw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else {

			for (int i = 0; i < current_node.children.size(); i++) {
				iterate(current_node.children.get(i), path + Integer.toString(i) + "thNode->");
			}
		}
		
		
	}

}
