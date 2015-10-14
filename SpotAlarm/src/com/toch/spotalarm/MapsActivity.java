package com.toch.spotalarm;

        import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.toch.utils.DatabaseOperations2;
import com.toch.utils.LocationAddress;




public class MapsActivity extends FragmentActivity  {

	double lat, lng;
    GoogleMap googleMap;
    Context ct = this;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    public LocationRequest mLocationRequest;
    
    public LatLng latLngTouch = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SharedPreferences pref = getSharedPreferences("lin", 0);
        final String name = pref.getString("KEY", null);          // getting String


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);


        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        
        
        
                
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		try {
		lat = location.getLatitude();
		lng = location.getLongitude();
		} catch(NullPointerException e) {
			lat = 9.96;
	        lng = 76.5;
		}
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, lng)).zoom(15).build();
		
		pref.edit().putString("c_lat", lat+"").commit();
		pref.edit().putString("c_lng", lng+"").commit();
		
 
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		googleMap.setMyLocationEnabled(true);

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                

                latLngTouch = latLng;
                LocationAddress.getAddressFromLocation(latLng.latitude, latLng.longitude, MapsActivity.this, new GeocoderHandler());
                

                Log.d("=====>", "test50");
                double la = latLng.latitude;
                double ln = latLng.longitude;

                SharedPreferences preff = getSharedPreferences("ll", Context.MODE_PRIVATE);
                SharedPreferences.Editor mmeditor = preff.edit();
                mmeditor.putString("la", String.valueOf(la)); // Storing string
                mmeditor.putString("ln", String.valueOf(ln));
                mmeditor.commit();
                latLng = new LatLng(latLng.latitude, latLng.longitude);


                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                Intent intent = new Intent(getApplicationContext(), ProxAlertActivity.class);
                startActivity(intent);

            }


        });


    }
    
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            SharedPreferences pref = getSharedPreferences("lin", 0);
            final String name = pref.getString("KEY", null);
            
            DatabaseOperations2 d = new DatabaseOperations2(ct);
            d.saveRecord(latLngTouch.latitude, latLngTouch.longitude, name, locationAddress);
            
        }
    }

}