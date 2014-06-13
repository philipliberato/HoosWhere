package edu.virginia.cs.hooswhere;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

public class HandleJSON extends AsyncTask<String, Void, JSONArray>{
		@Override
		protected JSONArray doInBackground(String... params) {
			JSONArray jsonArray = null;
			try{
			    // Create a new HTTP Client
			    DefaultHttpClient defaultClient = new DefaultHttpClient();

			    // Execute a request in the client
			    String param0 = params[0].replaceAll("\\s", "%20");
			    HttpResponse httpResponse = defaultClient.execute(new HttpGet(param0));
			    // Grab the response
			    
			    BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
			    String json = reader.readLine();

			    // Instantiate a JSON object from the request response
			    jsonArray = new JSONArray(json);

			} catch(Exception e){
			    // In your production code handle any errors and catch the individual exceptions
			    e.printStackTrace();
			}
			return jsonArray;
		}
		
		@Override
		protected void onPostExecute(JSONArray object) {
			Log.d("GetLocationData", "Completed fetching location data");
		}
		
	}

