package com.example.myfirstapp;
import android.util.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.*;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.android.maps.*;

public class MainActivity extends MapActivity implements SensorEventListener {
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
			mapController.animateTo(LoctoGeoPoint(location));
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
		JSONObject jsonObject = new JSONObject();
		try{
		    // Create a new HTTP Client
		    DefaultHttpClient defaultClient = new DefaultHttpClient();
		    // Setup the get request
		    HttpGet httpGetRequest = new HttpGet("http://plato.cs.virginia.edu/~cs4720s14peas/phase1/users/view/rotunda");

		    // Execute the request in the client
		    HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
		    // Grab the response
		    BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
		    String json = reader.readLine();

		    // Instantiate a JSON object from the request response
		    jsonObject = new JSONObject(json);

		} catch(Exception e){
		    // In your production code handle any errors and catch the individual exceptions
		    e.printStackTrace();
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.games);
	    MapsItemizedOverlay itemizedoverlay = new MapsItemizedOverlay(drawable, this);
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

		mapView = (MapView) findViewById(R.id.mapview);

		// Enables the built in zoom controls for this mapView
		mapView.setBuiltInZoomControls(true);

		mapController = mapView.getController();
		
		mapController.setZoom(16); // sets the zoom

		// Enables mylocation and a compass reflecting myLocationinformation
		myLocationOverlay = new MyLocationOverlay(getBaseContext(), mapView);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		GeoPoint myLocation = myLocationOverlay.getMyLocation();
		mapView.getOverlays().add(myLocationOverlay);
		
		ArrayList<GeoPoint> mockJSONArray = new ArrayList<GeoPoint>();
		double latitude=38.030044;
		double longitude=-78.505847;
		
        // Pulling items from the array
        try {
			latitude = jsonObject.getDouble("latitude");
			longitude = jsonObject.getDouble("longitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		double[][] points = { {38.033044,-78.507847} , {38.035320,-78.500728}, {38.036471,-78.499998}, {latitude,longitude} };
		
		for(int i=0;i<points.length;i++)
		{
			mockJSONArray.add(new GeoPoint((int)(1000000*points[i][0]), (int)(1000000*points[i][1])));
		}
		
		//create a point to be added
	    for(GeoPoint point: mockJSONArray)
	    	{
	    		OverlayItem overlayitem = new OverlayItem(point, "UVa Student", "UVa Student");
	    		itemizedoverlay.addOverlay(overlayitem);
	    	}
	  
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.games);
				
		
	    
	    //add all overlay points
	    mapOverlays.add(itemizedoverlay);
	    mapController.animateTo(new GeoPoint(38033044,-78507847));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.main, menu);
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
	
}


