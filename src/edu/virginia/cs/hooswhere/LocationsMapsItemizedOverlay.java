package edu.virginia.cs.hooswhere;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationsMapsItemizedOverlay extends ItemizedOverlay {
	private ArrayList<LocationsOverlayItem> mapOverlays = new ArrayList<LocationsOverlayItem>();
	Context mContext;

	
	public LocationsMapsItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public LocationsMapsItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}
	@Override
	protected LocationsOverlayItem createItem(int i) {
		return mapOverlays.get(i);

	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	public void addOverlay(LocationsOverlayItem overlay) {
	    mapOverlays.add(overlay);
	    populate();
	}

	@Override
	protected boolean onTap(int index) {
	  
	  final LocationsOverlayItem item = mapOverlays.get(index);
	  final Dialog dialog = new Dialog(mContext);
	  dialog.setContentView(R.layout.customdialog);
	  
	  ImageView closeX = (ImageView) dialog.findViewById(R.id.closeX);
	  closeX.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            dialog.dismiss();
          }
      });

	  Button ratingButton = (Button) dialog.findViewById(R.id.ratingButton);
	  ratingButton.setOnClickListener(new View.OnClickListener() {
		  int good_rating=0; int busy_rating=0;
          public void onClick(View v) {
        	  Intent locationInformationActivityIntent = new Intent(mContext,LocationsInformationActivity.class);
        	  good_rating=item.getGood_rating();
        	  busy_rating=item.getBusy_rating();
        	  locationInformationActivityIntent.putExtra("locationName", item.getTitle());
        	  locationInformationActivityIntent.putExtra("goodRating", good_rating);
        	  locationInformationActivityIntent.putExtra("busyRating", busy_rating);
        	  mContext.startActivity(locationInformationActivityIntent);
        	  
          }
      });
	  
	  dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}
