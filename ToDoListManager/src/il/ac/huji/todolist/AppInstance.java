package il.ac.huji.todolist;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Application;

public class AppInstance extends Application {
	private static final String APP_ID = "dfCIMhlRWGZ4PG33PUGzyFrXuH8uhb5oBbJqvrGJ";
	private static final String CLIENT_KEY = "jiUuWHVuwzsdg4tBU8FrPaX1oW509cON1j3RvMKf"; 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Parse.initialize(this, APP_ID, CLIENT_KEY);
		ParseUser.enableAutomaticUser();
	}
	
	
	
}
