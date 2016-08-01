package vopro7.vop.mobiliteitgent.activities;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vopro7.vop.mobiliteitgent.PushMessageService;
import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.models.Event;

/**
 * @author App Team
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MAIN";
    private ArrayAdapter<Event> eventsAdapter;
    private List<Event> events;

    private SwipeRefreshLayout swipeLayout;
    private TextView noEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        noEvents = (TextView) findViewById(R.id.no_events);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEvents();
            }
        });

        // set onItemClickListener for list view users
        final ListView eventsListView = (ListView) findViewById(R.id.events);
        if (eventsListView != null) {
            eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Event selected = (Event) eventsListView.getItemAtPosition(position);
                    Intent eventIntent = new Intent(MainActivity.this, EventActivity.class);
                    eventIntent.putExtra(EventActivity.EXTRA_EVENT, selected);
                    startActivity(eventIntent);
                }
            });

            eventsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition =
                            (eventsListView.getChildCount() == 0) ?
                                    0 : eventsListView.getChildAt(0).getTop();
                    swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }
            });
        }

        // init lists
        events = new ArrayList<>();

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        if (eventsListView != null) {
            eventsListView.setAdapter(eventsAdapter);
        }

        if (!loading) {
            String token = getAccessToken();
            if(token != null) {
                comm.getEventsForUser(this, prefs.getString(LoginActivity.USER_ID, ""), token);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
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
        } else if (id == R.id.map) {
            if (events.size() == 0) {
                showSnackBar(getString(R.string.no_events));
            } else {
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                ArrayList<Event> eventList = new ArrayList<>(events);
                mapIntent.putExtra(MapsActivity.EXTRA_EVENT, eventList);
                startActivity(mapIntent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to check if the user has new events
     */
    public void refreshEvents() {
        if (!loading) {
            String token = getAccessToken();
            if(token != null) {
                comm.getEventsForUser(this, prefs.getString(LoginActivity.USER_ID, ""), token);
            }
        }
    }

    /**
     * Fill in the list of events
     * @param newEvents A list of events you want to be filled in
     */
    public void fillList(List<Event> newEvents) {
        if (newEvents != null) {
            eventsAdapter.clear();
            events.clear();
            Collections.reverse(newEvents);
            if (newEvents.isEmpty()) {
                noEvents.setText(getString(R.string.no_events));
            } else {
                events.addAll(newEvents);
                noEvents.setText("");
            }
            eventsAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        } else {
            //Something went wrong with the communication
            showSnackBar(getString(R.string.wrong_communication));
        }
    }

    @Override
    public void setDoneLoading(JSONObject response) {
        super.setDoneLoading(response);

        String token = getAccessToken();
        if(token != null) {
            comm.getEventsForUser(this, prefs.getString(LoginActivity.USER_ID, ""), token);
        }

        pushMessageService = new Intent(this, PushMessageService.class);
        pushMessageService.putExtra(LoginActivity.USER_ID, prefs.getString(LoginActivity.USER_ID, "-1"));
        startService(pushMessageService);
    }
}
