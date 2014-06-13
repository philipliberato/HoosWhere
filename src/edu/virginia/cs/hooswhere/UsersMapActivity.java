package edu.virginia.cs.hooswhere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.*;

import com.google.android.maps.*;

import edu.virginia.cs.hooswhere.R;

public class UsersMapActivity extends MapActivity implements SensorEventListener {
	String username="";
	String userID="";
	Location currentLocation = new Location("current location");
	float myOrientation;
	MapView mapView; // Displays the map
	MapController mapController; // Allows for the manipulation of the map
	MyLocationOverlay myLocationOverlay;
	private SensorManager orientationManager;
	Sensor compass;
	ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	
	/**
	 * Hides the map display
	 */
	public void setMapInvisible() {
		mapView.setVisibility(4);
	}
	
	public GeoPoint LoctoGeoPoint(Location location) {
		return (new GeoPoint((int) (location.getLatitude() * 1000000),
				(int) (location.getLongitude() * 1000000)));
	}
	
	LocationListener locationListener = new LocationListener() {
		/**
		 * Updates the user's current location in terms of its latitude and
		 * longitude and adds their location to the List of GeoPoint objects
		 * 
		 * @param Location
		 *            the user's current location
		 */
		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
			geoPoints.add(LoctoGeoPoint(location));
			//mapController.animateTo(LoctoGeoPoint(location));
			myOrientation = myLocationOverlay.getOrientation();
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
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		username=getIntent().getStringExtra("username");
		userID=getIntent().getStringExtra("userID");
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.games);
	    UsersMapsItemizedOverlay itemizedoverlay = new UsersMapsItemizedOverlay(drawable, this);
	    orientationManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		compass = orientationManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		orientationManager.registerListener(this, compass,
				SensorManager.SENSOR_DELAY_UI);
		
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Sets the provider String
		Criteria criterion = new Criteria();
		criterion.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = locMan.getBestProvider(criterion, true);
		
		// Enables the LocationManager to receive Location Updates
		locMan.requestLocationUpdates(provider, 0, 0, locationListener);


		// Enables the built in zoom controls for this mapView
		mapView.setBuiltInZoomControls(true);

		mapController = mapView.getController();
		
		mapController.setZoom(16); // sets the zoom

		// Enables mylocation and a compass reflecting myLocationinformation
		myLocationOverlay = new MyLocationOverlay(getBaseContext(), mapView);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add(myLocationOverlay);
		
		ArrayList<GeoPoint> locationInputsArray = new ArrayList<GeoPoint>();
		double latitude=0.0;
		double longitude=0.0;
		
		HandleJSON getUserData = new HandleJSON();
		Intent intent = getIntent();
		String groupName = intent.getStringExtra("usersTag");
		String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/group/users/"+groupName;
		getUserData.execute(webServiceHttpAddress);
		
		
		JSONArray jsonArray = null;
		try {
			jsonArray = getUserData.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
 		// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        // Pulling items from the array
        try {
			if (jsonArray != null) {
				for(int i=0;i<jsonArray.length();i++)
				{
					JSONObject user = jsonArray.getJSONObject(i);
					latitude = user.getDouble("latitude");
					longitude = user.getDouble("longitude");
					locationInputsArray.add(new GeoPoint((int)(1000000*latitude), (int)(1000000*longitude)));
					OverlayItem overlayitem = new OverlayItem(locationInputsArray.get(i), "Person", "User");
					itemizedoverlay.addOverlay(overlayitem);
				}	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	    //add all overlay points
	    mapOverlays.add(itemizedoverlay);
	   
	    //mapController.animateTo(myLocationOverlay.getMyLocation());
	   mapController.animateTo(new GeoPoint(38033044,-78507847));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    Intent menuActivityIntent = new Intent(getApplication(), MenuActivity.class);
	    menuActivityIntent.putExtra("username", username);
	    menuActivityIntent.putExtra("userID", userID);
	    Intent locationsMenuActivity = new Intent(getApplication(), LocationsMenuActivity.class);
	    locationsMenuActivity.putExtra("username", username);
	    locationsMenuActivity.putExtra("userID", userID);
	    //menuActivityIntent.addCategory(Intent.CATEGORY_HOME);
	    
		int itemId = item.getItemId();
		if (itemId == R.id.People) {
			return true;
		} else if (itemId == R.id.Places) {
			startActivity(locationsMenuActivity);
			return true;
		} else if (itemId == R.id.Main_Menu) {
			startActivity(menuActivityIntent);
			return true;
		} else {
			return onOptionsItemSelected(item);
		}
	}
}




