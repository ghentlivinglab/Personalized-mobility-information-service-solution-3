package vopro7.vop.mobiliteitgent.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.models.Event;
import vopro7.vop.mobiliteitgent.models.Jam;
import vopro7.vop.mobiliteitgent.models.Transport;

/**
 * @author App Team
 */
public class EventActivity extends BaseActivity {

    private static final String TAG = "EVENT";
    public static final String EXTRA_EVENT = "Event";

    private TextView locationField;
    private Event event;

    private LinearLayout jams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView descriptionField = (TextView) findViewById(R.id.description_field);
        TextView publicationTimeField = (TextView) findViewById(R.id.pub_time_field);
        locationField = (TextView) findViewById(R.id.location_field);
        TextView lastEditTimeField = (TextView) findViewById(R.id.edit_time_field);
        TextView typeField = (TextView) findViewById(R.id.type_field);
        CheckBox activeField = (CheckBox) findViewById(R.id.active_field);

        TextView description = (TextView) findViewById(R.id.description);
        TextView publicationTime = (TextView) findViewById(R.id.pub_time);
        TextView location = (TextView) findViewById(R.id.location);
        TextView lastEditTime = (TextView) findViewById(R.id.edit_time);
        TextView type = (TextView) findViewById(R.id.type);
        TextView jam = (TextView) findViewById(R.id.jam);
        TextView transport = (TextView) findViewById(R.id.transport);

        jams = (LinearLayout) findViewById(R.id.jams);
        LinearLayout transports = (LinearLayout) findViewById(R.id.transports);

        // set event
        event = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);

        new GetAddresses().execute();

        if (event.getDescription() != null && descriptionField != null) {
            descriptionField.setText(event.getDescription());
        } else {
            if (description != null) {
                ViewGroup parent = (ViewGroup) description.getParent();
                parent.removeView(description);
                parent.removeView(descriptionField);
            }
        }

        if ((event.getCoordinates() != null || (event.getStreet() != null && !event.getStreet().equals("null") && event.getCoordinates() != null) || (event.getCity() != null && !event.getCity().equals("null") && event.getStreet() != null && !event.getStreet().equals("null"))) && locationField != null) {
            if(event.getCity() != null && !event.getCity().equals("null") && event.getStreet() != null && !event.getStreet().equals("null")) {
                locationField.setText(String.format("%s, %s", event.getStreet(), event.getCity()));
            } else if(event.getStreet() != null && !event.getStreet().equals("null")) {
                locationField.setText(event.getStreet());
            } else {
                locationField.setText(event.getCoordinates().toString());
            }
        } else {
            if (location != null) {
                ViewGroup parent = (ViewGroup) location.getParent();
                parent.removeView(location);
                parent.removeView(locationField);
            }
        }

        if (event.getPublicationTime() != null && publicationTimeField != null) {
            publicationTimeField.setText(event.getPublicationTime());
        } else {
            if (publicationTime != null) {
                ViewGroup parent = (ViewGroup) publicationTime.getParent();
                parent.removeView(publicationTime);
                parent.removeView(publicationTimeField);
            }
        }

        if (event.getLastEditTime() != null && lastEditTimeField != null) {
            lastEditTimeField.setText(event.getLastEditTime());
        } else {
            if (lastEditTime != null) {
                ViewGroup parent = (ViewGroup) lastEditTime.getParent();
                parent.removeView(lastEditTime);
                parent.removeView(lastEditTimeField);
            }
        }

        if (event.getType() != null && typeField != null) {
            typeField.setText(event.getType().getType());
        } else {
            if (type != null) {
                ViewGroup parent = (ViewGroup) type.getParent();
                parent.removeView(type);
                parent.removeView(typeField);
            }
        }

        if (activeField != null) {
            activeField.setChecked(event.isActive());
        }

        if (event.getJams() != null) {
            for(Jam j: event.getJams()) {
                TextView t = new TextView(this);
                t.setText(j.toString());
                t.setTextColor(Color.BLACK);
                t.setTextSize(15);
                jams.addView(t);
            }
        } else {
            if (jam != null) {
                ViewGroup parent = (ViewGroup) jam.getParent();
                parent.removeView(jam);
                parent.removeView(jams);
            }
        }

        if (event.getReleventTransportationTypes() != null && transports != null) {
            for(Transport tr: event.getReleventTransportationTypes()) {
                TextView t = new TextView(this);
                t.setText(tr.toString());
                t.setTextColor(Color.BLACK);
                t.setTextSize(15);
                transports.addView(t);
            }
        } else {
            if (transport != null) {
                ViewGroup parent = (ViewGroup) transport.getParent();
                parent.removeView(transport);
                parent.removeView(transports);
            }
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent userIntent = new Intent(EventActivity.this, UserActivity.class);
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
            //Clear stack to go back, then go to main
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function that fills in the address field
     * @param address The address you want to fill in
     */
    public void setLocationField(final String address) {
        locationField.post(new Runnable() {
            public void run() {
                locationField.setText(address);
            }
        });
    }

    private void setJamsField(final List<String> result) {
        jams.post(new Runnable() {
            public void run() {
                jams.removeAllViews();
                for(String s: result) {
                    TextView t = new TextView(EventActivity.this);
                    t.setText(s);
                    t.setTextColor(Color.BLACK);
                    t.setTextSize(15);
                    jams.addView(t);
                }
            }
        });
    }

    /**
     * Class to calculate address from coordinates
     */
    private class GetAddresses extends AsyncTask<Void, Void, Void> {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(event.getStreet() != null && !event.getStreet().equals("null") && (event.getCity() == null || event.getCity().equals("null"))) {
                    List<Address> listAddresses = geocoder.getFromLocation(event.getCoordinates().getLatitude(), event.getCoordinates().getLongitude(), 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String city = listAddresses.get(0).getLocality();
                        setLocationField(event.getStreet() + " , " + city);
                    }
                } else if((event.getStreet() == null || event.getStreet().equals("null")) && (event.getCity() == null || event.getCity().equals("null")) && event.getCoordinates() != null) {
                    List<Address> listAddresses = geocoder.getFromLocation(event.getCoordinates().getLatitude(), event.getCoordinates().getLongitude(), 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String street = listAddresses.get(0).getThoroughfare();
                        String city = listAddresses.get(0).getLocality();
                        setLocationField(street + " , " + city);
                    }
                }

                if(event.getJams() != null) {
                    for(Jam j: event.getJams()) {
                        List<String> result = new ArrayList<>();
                        if(j.getStart() != null && j.getEnd() != null) {
                            List<Address> startListAddresses = geocoder.getFromLocation(j.getStart().getLatitude(), j.getStart().getLongitude(), 1);
                            List<Address> endListAddresses = geocoder.getFromLocation(j.getEnd().getLatitude(), j.getEnd().getLongitude(), 1);
                            if (null != startListAddresses && startListAddresses.size() > 0 && null != endListAddresses && endListAddresses.size() > 0) {
                                String startStreet = startListAddresses.get(0).getThoroughfare();
                                String startCity = startListAddresses.get(0).getLocality();
                                String endStreet = endListAddresses.get(0).getThoroughfare();
                                String endCity = endListAddresses.get(0).getLocality();
                                String jam = "From " + startStreet + ", " + startCity + " to " + endStreet + ", " + endCity + "\n";
                                if(j.getDelay() != -1) {
                                    int minutes = j.getDelay()/60;
                                    if(minutes == 0) {
                                        minutes = 1;
                                    }
                                    jam += "Delay: " + minutes + " minutes, ";
                                }
                                jam += "Speed: " + j.getSpeed() + " km/u";
                                result.add(jam);
                            }
                        }
                        setJamsField(result);
                    }
                }
            } catch (IOException ex) {
                Log.e(TAG, "Error while getting addresses", ex);
            }
            return null;
        }
    }
}
