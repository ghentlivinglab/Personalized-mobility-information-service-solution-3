package vopro7.vop.mobiliteitgent.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.models.Event;

/**
 * @author App Team
 */
public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap map;

    public static final String EXTRA_EVENT = "Events";
    private static final String TAG = "MAP";

    private ArrayList<Event> events;

    private final Map<Event, String> addressMap = new HashMap<>();
    private final Map<Marker, Event> markerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = this.getIntent();

        events = (ArrayList<Event>) intent.getSerializableExtra(EXTRA_EVENT);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        addressMap.clear();
        new GetAddresses().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent userIntent = new Intent(MapsActivity.this, UserActivity.class);
            startActivity(userIntent);
            return true;
        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.logging_out))
                    .setMessage(getString(R.string.are_you_sure))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }

                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.clear();
        markerMap.clear();

        if (events.size() == 1) {
            Event e = events.get(0);
            LatLng place = new LatLng(e.getCoordinates().getLatitude(), e.getCoordinates().getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(place)
                    .title(e.getType().getType()));
            if(e.getJams() == null) {
                //Alert
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            } else {
                //Jam
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            markerMap.put(marker, e);
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
            map.animateCamera(cu);
        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Event e : events) {
                if (e.getCoordinates() != null) {
                    LatLng place = new LatLng(e.getCoordinates().getLatitude(), e.getCoordinates().getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions().position(place)
                                .title(e.getType().getType()));
                    if(e.getJams() == null) {
                        //Alert
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    } else {
                        //Jam
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                    builder.include(marker.getPosition());
                    markerMap.put(marker, e);
                }
            }
            map.setTrafficEnabled(true);
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (height * 0.15);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            map.animateCamera(cu);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event e = markerMap.get(marker);
                if (e != null) {
                    String address = addressMap.get(e);
                    if (address != null) {
                        marker.setTitle(e.getType().getType());
                        marker.setSnippet(address);
                    }
                }
                marker.showInfoWindow();
                return true;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Event e = markerMap.get(marker);
                if (e != null) {
                    Intent eventIntent = new Intent(MapsActivity.this, EventActivity.class);
                    eventIntent.putExtra(EventActivity.EXTRA_EVENT, e);
                    startActivity(eventIntent);
                }
            }
        });
    }

    private class GetAddresses extends AsyncTask<Void, Void, Void> {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        @Override
        protected Void doInBackground(Void... params) {
            for (Event e : events) {
                try {
                    if(e.getCity() != null && !e.getCity().equals("null") && e.getStreet() != null && !e.getStreet().equals("null") && !addressMap.containsKey(e)) {
                        addressMap.put(e, e.getStreet() + ", " + e.getCity());
                    } else if(e.getStreet() != null && !e.getStreet().equals("null") && e.getCoordinates() != null && !addressMap.containsKey(e)) {
                        List<Address> listAddresses = geocoder.getFromLocation(e.getCoordinates().getLatitude(), e.getCoordinates().getLongitude(), 1);
                        if (null != listAddresses && listAddresses.size() > 0) {
                            String city = listAddresses.get(0).getLocality();
                            addressMap.put(e, e.getStreet() + " , " + city);
                        }
                    } else if (e.getCoordinates() != null && !addressMap.containsKey(e)) {
                            List<Address> listAddresses = geocoder.getFromLocation(e.getCoordinates().getLatitude(), e.getCoordinates().getLongitude(), 1);
                            if (null != listAddresses && listAddresses.size() > 0) {
                                String street = listAddresses.get(0).getThoroughfare();
                                String city = listAddresses.get(0).getLocality();
                                addressMap.put(e, street + " , " + city);
                            }

                    }
                } catch (IOException ex) {
                    Log.e(TAG, "Error while getting addresses", ex);
                }
            }
            return null;
        }
    }
}
