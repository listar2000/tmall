package tmall.util;

public class Page 
{
	private int start;
	private int end;
	private int total;
	private String param;
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public Page(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public String getParam() {
        return param;
    }
	
    public void setParam(String param) {
        this.param = param;
    }
	
	public boolean isHasPrevious() {
		return !(start == 0);
	}
	
	public boolean isHasNext() {
		if (start == getLast()) return false;
		return true;
	}
	
	public int getTotalPage() 
	{
		int totalPage;

		if (total % end == 0)
			totalPage = total/end;
		else
			totalPage = total/end + 1;

		if (totalPage == 0)
			totalPage = 1;
		
		return totalPage;
	}
	
	public int getLast() 
	{
		int last;
		if (total % end == 0) last = total - end;
		else last = total - total/end;
		
		return last; 
	}
}
