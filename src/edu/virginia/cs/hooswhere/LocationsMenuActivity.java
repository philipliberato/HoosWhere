package edu.virginia.cs.hooswhere;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LocationsMenuActivity extends ListActivity {
	String username="";
	String userID="";
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		username=getIntent().getStringExtra("username");
		userID=getIntent().getStringExtra("userID");
		
		HandleJSON getlocationsTags = new HandleJSON();
		String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/tagslist/locations";
		String[] values = new String[1];
		getlocationsTags.execute(webServiceHttpAddress);
		try {
			JSONArray inputTags = getlocationsTags.get(3000, TimeUnit.MILLISECONDS);
			if(inputTags!=null) values=new String[inputTags.length()];
			for(int i=0;i<inputTags.length();i++)
			{
				String tempString = inputTags.getString(i);
				values[i] = tempString.substring(0,1).toUpperCase() + tempString.substring(1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.activity_locations_menu,R.id.label, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		Intent locationsMapsActivityIntent = new Intent(getApplication(), LocationsMapActivity.class);
		locationsMapsActivityIntent.putExtra("locationsTag", item);
		locationsMapsActivityIntent.putExtra("username", username);
		locationsMapsActivityIntent.putExtra("userID", userID);
		startActivity(locationsMapsActivityIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent locationsMapsActivityIntent = new Intent(getApplication(), LocationsMapActivity.class);
		locationsMapsActivityIntent.putExtra("username", username);
		locationsMapsActivityIntent.putExtra("userID", userID);
		Intent usersMenuActivity = new Intent(getApplication(), UsersMenuActivity.class);
		usersMenuActivity.putExtra("username", username);
		locationsMapsActivityIntent.putExtra("userID", userID);
		Intent menuActivityIntent = new Intent(getApplication(), MenuActivity.class);
		menuActivityIntent.putExtra("username", username);
		locationsMapsActivityIntent.putExtra("userID", userID);

		int itemId = item.getItemId();
		if (itemId == R.id.People) {
			startActivity(usersMenuActivity);
			return true;
		} else if (itemId == R.id.Places) {
			startActivity(locationsMapsActivityIntent);
			return true;
		} else if (itemId == R.string.Back) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	

}

