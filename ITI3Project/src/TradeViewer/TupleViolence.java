package TradeViewer;

public class TupleViolence  { 
	  public  Integer yesCount; 
	  public  Integer  noCount; 
	 
	  public TupleViolence(Integer yesCount, Integer noCount) { 
	    this.yesCount = yesCount; 
	    this.noCount=noCount;
	  } 
	  
	  public Integer getYesValue(){
		  return yesCount;
	  }

	public Integer getNoValue() {
		return noCount;
	}
	
	public void incrementYes(){
		yesCount++;
	}
	
	public void incrementNo(){
		noCount++;
	}
	@Override
	public String toString(){
		return  "  "+ yesCount+"  "+ noCount;
	}
	} 