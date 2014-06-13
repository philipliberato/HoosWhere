package edu.virginia.cs.hooswhere;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class UsersMapsItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	
	public UsersMapsItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public UsersMapsItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  Context mContext = context;
		}
	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);

	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
	    mapOverlays.add(overlay);
	    populate();
	}

}
