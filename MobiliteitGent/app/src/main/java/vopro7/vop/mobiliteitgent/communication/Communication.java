package vopro7.vop.mobiliteitgent.communication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vopro7.vop.mobiliteitgent.activities.BaseActivity;
import vopro7.vop.mobiliteitgent.activities.LoginActivity;
import vopro7.vop.mobiliteitgent.activities.MainActivity;
import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.activities.UserActivity;
import vopro7.vop.mobiliteitgent.models.Event;
import vopro7.vop.mobiliteitgent.models.User;

/**
 * @author App Team
 */
public class Communication {

    private static final String TAG = "COMMUNICATION";
    private static Communication instance;

    private RequestQueue queue;
    private Context context;

    private Communication(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        queue.start();
    }

    /**
     * Get the instance of the communication module
     *
     * @param context The context of the app
     * @return The only instance of the communication module
     */
    public static Communication getInstance(Context context) {
        if (instance == null) {
            instance = new Communication(context);
        }

        return instance;
    }

    /**
     * Function that gets a refresh token if the credentials are correct
     *
     * @param activity The activity we come from
     * @param email    Email of the user
     * @param password Password of the user
     * @throws JSONException
     */
    public void getRefreshToken(final LoginActivity activity, String email, String password) throws JSONException {
        if(hasActiveInternetConnection()) {
            String url = context.getString(R.string.base_url) + context.getString(R.string.refresh_token_url);

            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            activity.setRefreshToken(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        activity.setError(handleError(error.networkResponse.statusCode));
                    } else {
                        activity.setRefreshToken(null);
                        Log.e(TAG, "Failed to login", error);
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            activity.setError(context.getString(R.string.wrong_communication));
        }
    }

    private String handleError(int code) {
        switch (code) {
            case 418:
                return context.getString(R.string.error_418);
            case 401:
                return context.getString(R.string.error_401);
            case 403:
                return context.getString(R.string.error_403);
            default:
                return context.getString(R.string.wrong_communication);
        }
    }

    /**
     * Request access token from the refresh token
     *
     * @param activity The activity we come from
     * @param token    The refresh token
     * @throws JSONException
     */
    public void getAccessToken(final BaseActivity activity, String token) throws JSONException {
        if(hasActiveInternetConnection()) {
            String url = context.getString(R.string.base_url) + context.getString(R.string.access_token_url);

            JSONObject body = new JSONObject();
            body.put("token", token);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            activity.setDoneLoading(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activity.setInvalidRefreshToken();
                    if (error.networkResponse != null) {
                        activity.setError(handleError(error.networkResponse.statusCode));
                    } else {
                        Log.e(TAG, "Failed to get access token", error);
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            activity.setError(context.getString(R.string.wrong_communication));
        }
    }

    /**
     * Logout function that communicates to the logout endpoint on the server
     *
     * @param activity     The activity we come from
     * @param refreshToken The refresh token of the user
     * @param accessToken  the access token of the user
     * @throws JSONException
     */
    public void logout(final BaseActivity activity, String refreshToken, String accessToken) throws JSONException {
        if(hasActiveInternetConnection()) {
            String url = context.getString(R.string.base_url) + context.getString(R.string.logout_url);

            final JSONObject body = new JSONObject();
            body.put("token", refreshToken);

            final Map<String, String> headers = new HashMap<>();
            headers.put("X-Token", accessToken);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            activity.setLogout(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activity.setLogout(true);
                    Log.e(TAG, "Failed to logout", error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };


            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            activity.setError(context.getString(R.string.wrong_communication));
            activity.setLogout(true);
        }
    }

    /**
     * Get the relevant events for the user
     *
     * @param activity    The activity we come from
     * @param userId      The Id of the user
     * @param accessToken The access token of the user
     */
    public void getEventsForUser(final MainActivity activity, String userId, String accessToken) {
        if (!userId.equals("") && accessToken != null && !accessToken.equals("") && hasActiveInternetConnection()) {
            String url = context.getString(R.string.base_url) + context.getString(R.string.user_events_url) + userId;

            final Map<String, String> headers = new HashMap<>();
            headers.put("X-Token", accessToken);

            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            List<Event> events = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                Event e = new Event();
                                try {
                                    e.loadFromJson(response.getJSONObject(i));
                                    events.add(e);
                                } catch (JSONException e1) {
                                    // error
                                    Log.e(TAG, "Failed to load event", e1);
                                }
                            }
                            activity.fillList(events);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        activity.setError(handleError(error.networkResponse.statusCode));
                    } else {
                        activity.fillList(null);
                        Log.e(TAG, "Failed to load events", error);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            activity.setError(context.getString(R.string.wrong_communication));
        }
    }

    /**
     * Get the user
     *
     * @param activity    The activity we come from
     * @param url         The url of the user
     * @param accessToken The acces token of the user
     */
    public void getUser(final UserActivity activity, String url, String accessToken) {
        if (!url.equals("") && accessToken != null && !accessToken.equals("") && hasActiveInternetConnection()) {
            final Map<String, String> headers = new HashMap<>();
            headers.put("X-Token", accessToken);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            User u = new User();
                            try {
                                u.loadFromJson(response);
                                activity.setUser(u);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.setUser(null);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        activity.setError(handleError(error.networkResponse.statusCode));
                    } else {
                        activity.setUser(null);
                        Log.e(TAG, "Failed to load user", error);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            activity.setError(context.getString(R.string.wrong_communication));
        }
    }

    private boolean hasActiveInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
