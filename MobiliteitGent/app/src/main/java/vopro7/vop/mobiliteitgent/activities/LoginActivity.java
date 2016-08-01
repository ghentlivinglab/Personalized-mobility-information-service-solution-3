package vopro7.vop.mobiliteitgent.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.communication.Communication;

/**
 * @author App Team
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN";
    public static final String EXTRA_RETURN_INTENT = "intent";

    public static final String USER_ID = "user_id";
    public static final String USER_URL = "user_url";

    private SharedPreferences prefs;
    private Communication comm;

    private LinearLayout layout;
    private EditText email;
    private EditText password;
    private Button login;
    private TextView registration;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        comm = Communication.getInstance(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        layout = (LinearLayout) findViewById(R.id.login_layout);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        registration = (TextView) findViewById(R.id.registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.base_url) + getString(R.string.registration_url)));
                startActivity(intent);
            }
        });

        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.base_url) + getString(R.string.forgot_password_url)));
                startActivity(intent);
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && password.getText().length() != 0) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && email.getText().length() != 0) {
                    login.setEnabled(true);
                } else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        login.setEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
                try {
                    comm.getRefreshToken(LoginActivity.this, email.getText().toString(), password.getText().toString());
                } catch (JSONException e) {
                    showSnackBar(getString(R.string.wrong_communication));
                    Log.e(TAG, "Failed to create credentials", e);
                }
            }
        });
    }

    /**
     * Hide the keyboard when there is interaction with another view
     * @param view
     */
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Set the refresh token
     * @param response JSON response received from the server
     */
    public void setRefreshToken(JSONObject response) {
        if (response != null) {
            try {
                if (response.has(BaseActivity.REFRESH_TOKEN) && response.has(USER_ID) && response.has(USER_URL)) {
                    prefs.edit().putString(BaseActivity.REFRESH_TOKEN, response.getString(BaseActivity.REFRESH_TOKEN)).apply();
                    prefs.edit().putString(USER_ID, response.getString(USER_ID)).apply();
                    prefs.edit().putString(USER_URL, response.getString(USER_URL)).apply();
                    restartPreviousActivity();
                }
            } catch (JSONException e) {
                showSnackBar(getString(R.string.wrong_communication));
                Log.e(TAG, "Failed to parse Refresh token", e);
            }
        } else {
            //Something went wrong with the communication
            showSnackBar(getString(R.string.wrong_communication));
        }
    }

    private void restartPreviousActivity() {
        Intent returnIntent = getIntent().getParcelableExtra(EXTRA_RETURN_INTENT);
        if (returnIntent != null) {
            Intent intent = new Intent();
            intent.setClassName(LoginActivity.this, returnIntent.getComponent().getClassName());
            intent.putExtras(returnIntent);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
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
}
