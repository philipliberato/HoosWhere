package edu.virginia.cs.hooswhere;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.facebook.Session;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import edu.virginia.cs.hooswhere.LocationsMapActivity;
import edu.virginia.cs.hooswhere.MenuActivity;
import edu.virginia.cs.hooswhere.UsersMapActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	String username="";
	String userID="";
	private Button logoutButton;
	private SimpleFacebook mSimpleFacebook;
	
private OnLogoutListener mOnLogoutListener = new OnLogoutListener() {
		
		@Override
		public void onFail(String reason) {
			Toast.makeText(getBaseContext(), "logout fail: " + reason, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onException(Throwable throwable) {
			Toast.makeText(getBaseContext(), "logout exception: " + throwable, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onThinking() {
			Toast.makeText(getBaseContext(), "logout thinking...", Toast.LENGTH_LONG).show();			
		}
		
		@Override
		public void onLogout() {
			Toast.makeText(getBaseContext(), "Logged out!", Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		logoutButton = (Button) findViewById(R.id.btnLogout);
		
		ImageView logo = (ImageView) findViewById(R.id.logo);
		TextView myUsername = (TextView) findViewById(R.id.username);
		TextView welcome = (TextView) findViewById(R.id.welcome);
		
		username=getIntent().getStringExtra("username");
		userID=getIntent().getStringExtra("userID");
		
		myUsername.setText(username);
		
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		
		if(mSimpleFacebook.isLogin()) logoutButton.setVisibility(Button.VISIBLE);
		
		final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			
		final MyLocationListener locationListener = new MyLocationListener();
		
		locationManager.requestLocationUpdates(
		LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
		
		Button updateButton = (Button) findViewById(R.id.updateLocation);
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				double latitude = locationListener.getLatitude();
				double longitude = locationListener.getLongitude();
				// Switching to Register screen
				//Toast.makeText(getApplicationContext(),"user: "+userID+" lat: " + latitude +", long: " + longitude, Toast.LENGTH_LONG).show();
				
				//HandleJSON getLocationData = new HandleJSON();
				//Intent intent = getIntent();
				//String groupName = intent.getStringExtra("locationsTag");
				if((int)(latitude*1000000)!=0 && (int)(longitude*1000000)!=0){
					
				HandleJSON updateUserLoc = new HandleJSON();
				String webservice = "http://plato.cs.virginia.edu/~cs4720s14peas/phase1/users/update/";
				webservice += (userID + "/" + latitude + "/" + longitude);
				
				//Toast.makeText(getApplicationContext(),webservice, Toast.LENGTH_LONG).show();
				updateUserLoc.execute(webservice);
				
				try {
					updateUserLoc.get(4000, TimeUnit.MILLISECONDS);
					//Toast.makeText(getApplicationContext(),"Location Updated!: "+webservice, Toast.LENGTH_LONG).show();
					Toast.makeText(getApplicationContext(),"Location Updated!", Toast.LENGTH_LONG).show();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
				else{
					Toast.makeText(getApplicationContext(),"Getting GPS Lock", Toast.LENGTH_LONG).show();
				}
				
			}});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			
				if (mSimpleFacebook.isLogin()) {
					mSimpleFacebook.logout(mOnLogoutListener);

					Session session = Session.getActiveSession();
					session.closeAndClearTokenInformation();
					
					Intent loginIntent = new Intent(getApplication(), LoginPage.class);
					finish();
					startActivity(loginIntent);
;				}
//				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//				i.putExtra("username", userID);
//				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		  Intent locationsMenuActivityIntent = new Intent(getApplication(), LocationsMenuActivity.class);
		  locationsMenuActivityIntent.putExtra("username", username);
		  locationsMenuActivityIntent.putExtra("userID", userID);
		  Intent usersMenuActivityIntent = new Intent(getApplication(), UsersMenuActivity.class);
		  usersMenuActivityIntent.putExtra("username", username);
		  usersMenuActivityIntent.putExtra("userID", userID);
		  //Intent usersMapsActivityIntent = new Intent(getApplication(), UsersMapActivity.class);
	    
		int itemId = item.getItemId();
		if (itemId == R.id.People) {
			startActivity(usersMenuActivityIntent);
			return true;
		} else if (itemId == R.id.Places) {
			startActivity(locationsMenuActivityIntent);
			return true;
		} else if (itemId == R.id.Main_Menu) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onBackPressed(){
		Intent loginIntent = new Intent(getApplication(), LoginPage.class);
			finish();
		startActivity(loginIntent);
	}
	
}

final class MyLocationListener implements LocationListener {
	private double latitude=0.0;
	private double longitude=0.0;
	
	public double getLatitude() {return latitude;}
	public double getLongitude() {return longitude;}
	
	/**
	 * Updates the user's current location in terms of its latitude and
	 * longitude and adds their location to the List of GeoPoint objects
	 * 
	 * @param Location
	 *            the user's current location
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location == null) return;

		GeoPoint currentPoint = new GeoPoint((int) location.getLatitude() * 1000000,
				(int) location.getLongitude() * 1000000);

		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	/**
	 * No functionality (satisfies the superclass)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// N/A
	}

	/**
	 * No functionality (satisfies the superclass)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// N/A
	}

	/**
	 * No functionality (satisfies the superclass)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// N/A
	}
}
