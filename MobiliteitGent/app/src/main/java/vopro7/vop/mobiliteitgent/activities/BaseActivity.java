package vopro7.vop.mobiliteitgent.activities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.communication.Communication;

/**
 * @author App Team
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BASE";

    protected LinearLayout layout;

    protected SharedPreferences prefs;
    protected Communication comm;

    protected Intent pushMessageService;

    protected boolean loading;

    private String accessToken;
    private Date expTime;

    public static final String REFRESH_TOKEN = "token";
    public static final String ACCESS_TOKEN = "token";
    public static final String EXP_TIME = "exp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        comm = Communication.getInstance(this);

        loading = true;

        layout = (LinearLayout) findViewById(R.id.layout);

        Intent intent = getIntent();

        if (prefs.getString(REFRESH_TOKEN, null) != null) {
            //Already logged in, so we can get an access token from the stored refresh token
            String token = prefs.getString(REFRESH_TOKEN, null);
            try {
                comm.getAccessToken(this, token);
            } catch (JSONException e) {
                showSnackBar(getString(R.string.wrong_communication));
                Log.e(TAG, "Failed to create credentials", e);
            }
        } else {
            //Not logged in so we have to create a new refresh and access token
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra(LoginActivity.EXTRA_RETURN_INTENT, intent);
            startActivity(loginIntent);
            finish();
        }
    }

    /**
     * Check if the refresh token is present and request the access token from the communication module
     *
     * @return The access token string
     */
    protected String getAccessToken() {
        Date current = new Date(System.currentTimeMillis());
        if (accessToken == null || current.after(expTime)) {
            if (prefs.getString(REFRESH_TOKEN, null) != null) {
                try {
                    comm.getAccessToken(this, prefs.getString(REFRESH_TOKEN, null));
                } catch (JSONException e) {
                    showSnackBar(getString(R.string.wrong_communication));
                    Log.e(TAG, "Failed to create credentials", e);
                }
                accessToken = null;
                expTime = null;
                loading = true;
            }
            return null;
        } else {
            return accessToken;
        }
    }

    /**
     * Method that shows a Snackbar with a message at the bottom of the screen
     *
     * @param message The message to be shown
     */
    protected void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Function to let a user logout and stop the push service
     */
    public void logout() {
        if (prefs.getString(REFRESH_TOKEN, null) != null) {
            String token = prefs.getString(REFRESH_TOKEN, null);

            try {
                comm.logout(this, token, accessToken);
            } catch (JSONException e) {
                showSnackBar(getString(R.string.wrong_communication));
                Log.e(TAG, "Failed to delete credentials", e);
            }
        }
    }

    /**
     * Function that clears all activities and tokens, if logout was successful on the server
     *
     * @param forced true if the logout was forced, false if the user chose it
     */
    public void setLogout(boolean forced) {
        if (forced) {
            //Something went wrong with the communication
            showSnackBar(getString(R.string.wrong_communication));
        }
        prefs.edit().remove(BaseActivity.REFRESH_TOKEN).apply();
        prefs.edit().remove(LoginActivity.USER_ID).apply();
        prefs.edit().remove(LoginActivity.USER_URL).apply();

        accessToken = null;
        expTime = null;
        loading = true;

        // stop push messages service
        if(pushMessageService != null) {
            stopService(pushMessageService);
        }

        //Logout and clear everything, then go to main
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Display an error message
     *
     * @param error The error to show on the snackbar
     */
    public void setError(String error) {
        showSnackBar(error);
    }

    /**
     * Function that parses a token from a JSONObject
     *
     * @param response The JSONObject response that was received
     */
    public void setDoneLoading(JSONObject response) {
        if (response.has(ACCESS_TOKEN) && response.has(EXP_TIME)) {
            try {
                accessToken = response.getString(ACCESS_TOKEN);
                String isoFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
                SimpleDateFormat dateFormat = new SimpleDateFormat(isoFormat, Locale.getDefault());
                expTime = dateFormat.parse(response.getString(EXP_TIME));
                loading = false;
            } catch (ParseException | JSONException e) {
                showSnackBar(getString(R.string.wrong_communication));
                Log.e(TAG, "Failed to parse Refresh token", e);
            }
        } else {
            try {
                String token = prefs.getString(REFRESH_TOKEN, null);
                comm.getAccessToken(this, token);
            } catch (JSONException e) {
                showSnackBar(getString(R.string.wrong_communication));
                Log.e(TAG, "Failed to create credentials", e);
            }
        }
    }

    /**
     * Function that is called when we want to invalidate the refresh token and redirect to login
     */
    public void setInvalidRefreshToken() {
        setLogout(true);
    }
}
