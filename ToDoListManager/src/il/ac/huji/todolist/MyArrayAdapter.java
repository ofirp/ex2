package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MyArrayAdapter extends ArrayAdapter<listItem> {
	
	
	Context context;
	ArrayList<listItem> itemsArrayList;
	
	public MyArrayAdapter(Context context,ArrayList<listItem> itemsArrayList) {
		 
        super(context, R.layout.row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
        
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row, parent, false);
		
		TextView titleView = (TextView) rowView.findViewById(R.id.txtTodoTitle);
        TextView dateView = (TextView) rowView.findViewById(R.id.txtTodoDueDate);
        
        listItem item = itemsArrayList.get(position);
        
        if (isBeforeToday(item.getDate())){
        	titleView.setTextColor(Color.RED);
        	dateView.setTextColor(Color.RED);
        	titleView.setText(item.getTitle());
        	dateView.setText(item.getStrDate());
        }else{
        	titleView.setText(item.getTitle());
        	dateView.setText(item.getStrDate());
        }

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