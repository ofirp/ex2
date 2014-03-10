package il.ac.huji.todolist;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ColoredArrayAdapter extends ArrayAdapter<String> {
	
	public ColoredArrayAdapter(Context context,int resourceId,List<String>objects){
		super(context,resourceId,objects);
	}
	
	
	public View getView(int position,View convertView,ViewGroup parent){
		
		TextView view =(TextView) super.getView(position, convertView, parent);
		
		//changing the color of the text according to the position
		if(position%2==0){
			view.setTextColor(Color.RED);
		}else{
			view.setTextColor(Color.BLUE);
		}
		
		
		return view;
		
		
		
	}

}
