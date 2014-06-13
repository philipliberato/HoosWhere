package edu.virginia.cs.hooswhere;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LocationsOverlayItem extends OverlayItem {
int good_rating; int busy_rating;
double latitude; double longitude; 
	public LocationsOverlayItem(GeoPoint location, String location_name, String group_name, int mygood_rating, int mybusy_rating, double myLatitude, double myLongitude) {
		super(location, location_name, group_name);
		this.good_rating=mygood_rating;
		this.busy_rating=mybusy_rating;
		this.latitude=myLatitude;
		this.longitude=myLongitude;
		// TODO Auto-generated constructor stub
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public int getGood_rating() {
		return good_rating;
	}
	public void setGood_rating(int good_rating) {
		this.good_rating = good_rating;
	}
	public int getBusy_rating() {
		return busy_rating;
	}
	public void setBusy_rating(int busy_rating) {
		this.busy_rating = busy_rating;
	}

}
