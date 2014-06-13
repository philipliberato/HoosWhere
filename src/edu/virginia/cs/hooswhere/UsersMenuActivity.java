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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class UsersMenuActivity extends ListActivity {
	String username="";
	String userID="";
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		username=getIntent().getStringExtra("username");
		userID=getIntent().getStringExtra("userID");
		
		HandleJSON getUsersTags = new HandleJSON();
		String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/tagslist/users";
		String[] values = new String[1];
		getUsersTags.execute(webServiceHttpAddress);
		try {
			JSONArray inputTags = getUsersTags.get(3000, TimeUnit.MILLISECONDS);
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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_users_menu,R.id.label, values);
		setListAdapter(adapter);
		//ImageButton joinButton = (ImageButton) findViewById(R.layout.activity_users_menu);
		
		/*joinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
				i.putExtra("username", ((EditText)findViewById(R.id.Username)).getText().toString());
				startActivity(i);
			}
			});*/
		}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		Intent usersMapsActivityIntent = new Intent(getApplication(), UsersMapActivity.class);
		usersMapsActivityIntent.putExtra("username", username);
		usersMapsActivityIntent.putExtra("usersTag", item);
		usersMapsActivityIntent.putExtra("userID", userID);
		startActivity(usersMapsActivityIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent locationsMenuActivity = new Intent(getApplication(), LocationsMenuActivity.class);
		locationsMenuActivity.putExtra("username", username);
		locationsMenuActivity.putExtra("userID", userID);
		Intent usersMapsActivityIntent = new Intent(getApplication(), UsersMapActivity.class);
		usersMapsActivityIntent.putExtra("username", username);
		usersMapsActivityIntent.putExtra("userID", userID);
		Intent menuActivityIntent = new Intent(getApplication(), MenuActivity.class);
		menuActivityIntent.putExtra("username", username);
		menuActivityIntent.putExtra("userID", userID);

		int itemId = item.getItemId();
		if (itemId == R.id.People) {
			startActivity(usersMapsActivityIntent);
			return true;
		} else if (itemId == R.id.Places) {
			startActivity(locationsMenuActivity);
			return true;
		} else if (itemId == R.string.Back) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

}

