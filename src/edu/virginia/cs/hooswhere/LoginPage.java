package edu.virginia.cs.hooswhere;

import com.facebook.Session;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.entities.Location;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity {

	private OnLoginListener mOnLoginListener = new OnLoginListener() {
		
		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "login fail: " + reason, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "login exception: " + throwable, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onThinking() {
			// TODO Auto-generated method stub
			//Toast.makeText(getBaseContext(), "login thinking...", Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onNotAcceptingPermissions(Type type) {
			// TODO Auto-generated method stub
			//Toast.makeText(getBaseContext(), "login Permissions error: " + type.name(), Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onLogin() {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "Logged in!", Toast.LENGTH_SHORT).show();
			mSimpleFacebook.getProfile(mOnProfileListener);
		}
	};
	
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
	
	private String userID;
	private String username;
	//private Location myLocation;
	
	private OnProfileListener mOnProfileListener = new OnProfileListener() {
		
		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "profile fail: " + reason, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), "profile exception: " + throwable, Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onThinking() {
			// TODO Auto-generated method stub
			//Toast.makeText(getBaseContext(), "profile thinking...", Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onComplete(Profile profile) {
			Toast.makeText(getBaseContext(), "Loading profile info", Toast.LENGTH_SHORT);
			VariableContainer.setUserId(profile.getId());
			VariableContainer.setUserName(profile.getName());
			userID = profile.getId();
			username = profile.getName();
			//myLocation = profile.getLocation();
			logoutButton.setVisibility(Button.VISIBLE);
			Intent i = new Intent(getApplicationContext(), MenuActivity.class);
			i.putExtra("username", username);
			i.putExtra("userID", userID);
			finish();
			startActivity(i);
			HandleJSON addUserToGroup = new HandleJSON();
			String webservice = "http://peaswebservice.herokuapp.com/addusertogroup/";
			webservice += (userID + "/all");
			addUserToGroup.execute(webservice);
		}
		
	};
	
	
	private SimpleFacebook mSimpleFacebook;
	private Button logoutButton;
	
	@Override
	protected void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
		HandleJSON refreshService = new HandleJSON();
		String webServiceHttpAddress = "http://peaswebservice.herokuapp.com/refresh";
		refreshService.execute(webServiceHttpAddress);
		
		Permission[] permissions = new Permission[] {
			Permission.BASIC_INFO,
			Permission.EMAIL	
		};
		
		SimpleFacebookConfiguration SFBconfig = new SimpleFacebookConfiguration.Builder()
			.setAppId("1426319064287336")
			.setNamespace("hooswhereapp")
			.setPermissions(permissions)
			.build();
		SimpleFacebook.setConfiguration(SFBconfig);
		
		mSimpleFacebook = SimpleFacebook.getInstance(this);

		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		TextView titleText = (TextView) findViewById(R.id.title_text);
		Button loginButton = (Button) findViewById(R.id.btnLogin);
		logoutButton = (Button) findViewById(R.id.btnLogout);
		//EditText userName = (EditText) findViewById(R.id.Username);
		// Listening to register new account link
		
		String text = "<font color=#F8793D>Hoos</font> <font color=#0066FF>Where?</font>";
		titleText.setText(Html.fromHtml(text));
		
		registerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
//				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//				i.putExtra("username", userID);
//				startActivity(i);
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mSimpleFacebook.login(mOnLoginListener);			
				
//				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//				i.putExtra("username", userID);
//				startActivity(i);
			}
		});
		
		logoutButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				if (mSimpleFacebook.isLogin()) {
					mSimpleFacebook.logout(mOnLogoutListener);

					Session session = Session.getActiveSession();
					session.closeAndClearTokenInformation();
				}
//				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//				i.putExtra("username", userID);
//				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}


}