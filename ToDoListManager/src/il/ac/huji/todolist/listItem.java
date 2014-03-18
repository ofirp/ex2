package il.ac.huji.todolist;
import java.util.Calendar;
import java.util.Date;


public class listItem {
	Date date;
	String title;
	String dateStr;

	public listItem(String title,Date date){
		this.date = date;
		this.title=title;
		if(this.date!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			dateStr=cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);
		}else{
			dateStr="No due date";
		}
	}
	public Date getDate(){
		return date;
	}

	public String getTitle(){
		return title;
	}

	public String getStrDate(){
		return dateStr;
	}

}
