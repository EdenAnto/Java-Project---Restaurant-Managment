import java.time.LocalDateTime;


public class myDate implements Comparable<myDate> { /// assuming all month has 31 days
	private int day;
	private int month;
	private int year;
	
	// constructor 
	public myDate(boolean dateForNewProduct) {
		LocalDateTime now = LocalDateTime.now();  
		day=now.getDayOfMonth();
		month=now.getMonthValue();
		year=now.getYear();

		if (dateForNewProduct) {
			if (month==12)
			{
				year+=1;
				month=1;
			}
			else 
				month+=1;
		}
		
	}

	// constructor 
	public myDate(myDate date) {
		day=date.getDay();
		month=date.getMonth();
		year=date.getYear();		
	}
	
	// constructor 
	public myDate(int day,int month,int year) {
		this.day=day;
		this.month=month;
		this.year=year;

	}

	// Override to use treeMap
	@Override
	public int compareTo(myDate d) { // return 1 if this date is before d
		
		if (d.getYear()==year) {
			if (d.getMonth()==month) {
				if (d.getDay()==day)
					return 0;
				else {
					if(d.getDay()>day)
						return 1;
					else 
						return -1;
					}
				}
				if (d.getMonth()>month)
					return 1;
				else 
					return -1;
			}
		else
		{
			if (d.getYear()>year)
				return 1;
			else
				return -1;		
		}
	}
	
	// simple get set methods
	public void setDay(int day) {
		this.day = day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;

	
	}
	//
	
	
	//Override toString
	@Override
	public String toString() {
		String output="";
		if (day<10)
			output="0";
		output+=getDay()+"/";
		
		if (month<10)
			output+="0";
		output+=getMonth()+"/";
		 
		return output+getYear();
	}
	
}
