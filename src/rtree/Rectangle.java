package rtree;

public class Rectangle {
	
	double max_x_axis;
	double max_y_axis;
	
	double min_x_axis;
	double min_y_axis;
	
	/*Rectangle(double max_x_axis,double max_y_axis,double min_x_axis,double min_y_axis){
		
		this.max_x_axis=max_x_axis;
		this.max_y_axis=max_y_axis;
		
		this.min_x_axis=min_x_axis;
		this.min_y_axis=min_y_axis;
		
	}*/
	
	
	public double GetArea() {
		double area = (this.max_x_axis-this.min_x_axis)*(this.max_y_axis-this.min_y_axis);
		return area;
	}
	
    public boolean CheckIsInside(Rectangle new_rect) {
    
    	boolean overlap_flag = false;
    	
    	if(this.min_x_axis <= new_rect.min_x_axis && this.min_y_axis <= new_rect.min_y_axis && this.max_x_axis >= new_rect.max_x_axis && this.max_y_axis >= new_rect.max_y_axis) {
    		overlap_flag = true;
    	}else{
    		overlap_flag = false;
    	}
    	return overlap_flag;
    }
    
    
}
