package TradeViewer;

// useful geometric classes
// 15 jan 98

public class Point	{
	protected int x, y;	// coordinates of the point
	
	public Point () { setPoint(0,0); }
	
	public Point(int a, int b)	{ setPoint(a,b); }
	
	public void setPoint (int a, int b)	{
		x = a;
		y = b;
		
	}
	
	public void setX(int a)	{
		x = a;
	}
	
	public void setY(int a)	{
		y = a;
	}
	
	public int getX()	{ return x; }
	
	public int getY()	{ return y; }
	
	public String toString()	{
		return "[" + x + ", " + y + "]"; 
	}
} // end Point
