package rtree;

public class Rectangle {
	
	double max_x_axis;
	double max_y_axis;
	
	double min_x_axis;
	double min_y_axis;
	
	public double calculate_area(double max_x_axis, double max_y_axis,double min_x_axis,double min_y_axis) {
		double area = (max_x_axis-min_x_axis)*(max_y_axis-min_y_axis);
		return area;
	}
}
