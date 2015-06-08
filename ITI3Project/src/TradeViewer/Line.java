package TradeViewer;

// useful geometric classes
// 15 jan 98

public class Line {
	protected Point start, end; // endpoints of line
	
	public Line()	{setLine(new Point(), new Point()); }
	
	public Line(Point startPt, Point endPt)	{
		setLine(startPt, endPt);
	}
	
	public void setLine(Point startPt, Point endPt)	{
		start = startPt;
		end = endPt;
	}
	
	public Point start()	{
		return start;
	}
	
	public Point end()	{
		return end;
	}
	
	public String toString()	{
		return "<" + start.toString() + ", " + end.toString() + ">";
	}
} // end Line
