import java.awt.*;

public class Line {
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public Line(Point a, Point b) {
		x1 = a.x;
		y1 = -1*a.y;
		x2 = b.x;
		y2 = -1*b.y;
	}
	
	public boolean onLine(Point p) {
		// determines if a point is on the line
		
		double slope = getSlope();
		double intercept = getInterceptY();
		double ls;
		double rs;
		if (getSlope() == Double.NEGATIVE_INFINITY || getSlope() == Double.POSITIVE_INFINITY) {
			ls = -1*p.y;
			rs = p.x;
		}
		else {
			ls = -1*(double)p.y;
			rs = (double)p.x * slope + intercept;
		}
		
		if (ls == rs) { return true; }
		else { return false; }
	}
	
	public String toString() {
		/* returns equation of line
		 */
		String out = "y = ";
		if (getSlope() == Double.NEGATIVE_INFINITY || getSlope() == Double.POSITIVE_INFINITY) {
			out = "x = ";
			out += "" + x1;
		}
		else if (getSlope() ==  0) {
			out = "y = ";
			out += "" + y1;
		}
		else {
			out += ""+getSlope();
			out += "x + ";
			out += ""+getInterceptY();
		}
		return out;
	}
	
	public double getSlope() {
		// returns slope of the line
		return (double)(y1 - y2)/(double)(x1 - x2);
	}
	
	public double getAngle() {
		// returns angle of the line in radians
		double out = -1*Math.atan(getSlope());
		return out;
	}
	
	public double getInterceptX() {
		/* returns X intercept of line
		 */
		if (getSlope() == 0) {
			return Double.NaN;
		}
		return x1;
	}
	
	public double getInterceptY() {
		/* returns y intercept of line
		 */
		if (getSlope() == 0) {
			return (double)y1;
		}
		return (double)y1 - (double)x1*getSlope();
	}
	
	public double[] getVar() {
		/* returns variables of equation of line
		 * in standard form Ax + By + C = 0
		 */
		 
		double[] out = new double[3];
		
		if (getSlope() == Double.NEGATIVE_INFINITY || getSlope() == Double.POSITIVE_INFINITY) { // if a vertical line
			out[0] = 1;
			out[1] = 0;
			out[2] = -1 * x1;
		}
		else {
			out[0] = -1 * getSlope();
			out[1] = 1;
			out[2] = -1 * getInterceptY();
		}
		
		return out;
	}
	
	public double dist(Point p) {
		/* returns the distance from a point to the line
		 * given the formula |Ax + By + C| / sqrt(A^2 + B^2)
		 */
		
		double[] var = getVar();
		double a = var[0];
		double b = var[1];
		double c = var[2];
		
		double top = Math.abs( a*p.getX() - b*p.getY() + c );
		double bottom = Math.sqrt( a*a + b*b );
		
		return top/bottom;
	}
}
