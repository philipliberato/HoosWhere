package edu.virginia.cs.hooswhere;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;
import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class LocationsInformationActivity extends Activity {
	String locationName="";
	String username="";
	String userId="";
	int goodRatingPull=0; 
	int busyRatingPull=0;
	float rating=0;
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_locations_information);
		
		TextView ratingText = (TextView) findViewById(R.id.ratingsText);
		
		username=VariableContainer.getUserName();
		userId=VariableContainer.getUserId();
		locationName=getIntent().getStringExtra("locationName");
		goodRatingPull=getIntent().getIntExtra("goodRating", 0);
		busyRatingPull=getIntent().getIntExtra("busyRating", 0);
		
		TextView goodRatingPullView = (TextView) findViewById(R.id.goodRatingPullView);
		TextView busyRatingPullView = (TextView) findViewById(R.id.busyRatingPullView);
		Button submitRating = (Button) findViewById(R.id.submitRating);
		
		HandleJSON pullLocation = new HandleJSON();
		String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/getlocation/"+locationName;
		//Toast.makeText(getApplicationContext(), webServiceHttpAddress, Toast.LENGTH_LONG).show();
		pullLocation.execute(webServiceHttpAddress);
		
		try {
			if (pullLocation != null) {
					JSONArray locationArray = pullLocation.get();
					if(locationArray!=null && locationArray.length()>0) {
						JSONObject myLocation = locationArray.getJSONObject(0);
						
						if(myLocation!=null) busyRatingPull = (int)myLocation.getDouble("num_nearby_users"); //Later needs to be changed!!!
						if(myLocation!=null) goodRatingPull = (int)myLocation.getDouble("good_rating");
						}
					}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(goodRatingPull!=0)	goodRatingPullView.setText("How good is "+locationName+" today? "+goodRatingPull+" out of 10"+"\n");
						else	goodRatingPullView.setText("How good is "+locationName+" today? "+"Be the first to rate this value!"+"\n");
		if(busyRatingPull!=0)	busyRatingPullView.setText("How busy is "+locationName+" today? "+busyRatingPull+" out of 10"+"\n");
						else	busyRatingPullView.setText("How busy is "+locationName+" today? "+"There are no Hoos there right now..."+"\n");
		
		RatingBar goodRating = (RatingBar) findViewById(R.id.goodRatingBar);
		rating=goodRating.getRating();
		
		goodRating.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){
			@Override
			public void onRatingChanged(RatingBar ratingBar, float myrating, boolean userTouch) {
					//Toast.makeText(getApplication(), "location: "+locationName+", rating: "+myrating+", userId: "+userId, Toast.LENGTH_LONG).show();
					rating=myrating;
					//HandleJSON updateRating = new HandleJSON();
					//String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/rategood/"+locationName+"/"+myrating;
					//updateRating.execute(webServiceHttpAddress);
			}
			
		});
		
		submitRating.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(rating!=0)
				{
				//Toast.makeText(getApplication(), "location: "+locationName+", rating: "+rating+", userId: "+userId, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplication(), "Submitted! Thank you for your input.", Toast.LENGTH_LONG).show();
				HandleJSON updateRating = new HandleJSON();
				String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/rategood/"+locationName+"/"+rating+"/"+userId;
				updateRating.execute(webServiceHttpAddress);	
				try {
					updateRating.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
					}
//				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//				i.putExtra("username", userID);
//				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent usersMenuActivityIntent = new Intent(getApplication(), UsersMenuActivity.class);
		usersMenuActivityIntent.putExtra("username", username);
		usersMenuActivityIntent.putExtra("userID", userId);
		Intent menuActivityIntent = new Intent(getApplication(), MenuActivity.class);
		menuActivityIntent.putExtra("username", username);
		menuActivityIntent.putExtra("userID", userId);
		
		int itemId = item.getItemId();
		if (itemId == R.id.People) {
			startActivity(usersMenuActivityIntent);
			return true;
		} else if (itemId == R.id.Places) {
			//startActivity(locationsMenuActivityIntent);
			return true;
		} else if (itemId == R.string.Back) {
			finish();
			return true;
		} else if (itemId == R.id.Main_Menu) {
			startActivity(menuActivityIntent);
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	}

}
