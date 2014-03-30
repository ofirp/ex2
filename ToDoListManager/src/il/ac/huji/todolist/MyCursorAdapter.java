package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {

	public MyCursorAdapter(Context context,Cursor cursor){
		super(context,cursor,true);
	}
	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		String dateStr;
		TextView titleView = (TextView) rowView.findViewById(R.id.txtTodoTitle);
		TextView dateView = (TextView) rowView.findViewById(R.id.txtTodoDueDate);

		long longDate = cursor.getLong(2);
		Date date = new Date(longDate);


		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		dateStr=cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);




		if (isBeforeToday(date)){
			titleView.setTextColor(Color.RED);
			dateView.setTextColor(Color.RED);
			titleView.setText(cursor.getString(1));
			dateView.setText(dateStr);
		}else{
			titleView.setTextColor(Color.BLACK);
			dateView.setTextColor(Color.BLACK);
			titleView.setText(cursor.getString(1));
			dateView.setText(dateStr);
		}



	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row, parent, false);
		return rowView;
	}


	public boolean isBeforeToday(Date due){

		Calendar calDue = Calendar.getInstance();
		calDue.setTime(due);

		Calendar calNow = Calendar.getInstance();

		//to ignore time difference
		if(calDue.get(Calendar.DAY_OF_MONTH)<calNow.get(Calendar.DAY_OF_MONTH)){
			if(calDue.get(Calendar.MONTH)<=calNow.get(Calendar.MONTH)+1){
				if(calDue.get(Calendar.YEAR)<=calNow.get(Calendar.YEAR)){
					return true;
				}
			}
		}

		return (calDue.before(calNow));


	}

}
