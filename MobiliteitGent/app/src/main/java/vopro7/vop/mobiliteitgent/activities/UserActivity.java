package vopro7.vop.mobiliteitgent.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import vopro7.vop.mobiliteitgent.R;
import vopro7.vop.mobiliteitgent.models.User;

/**
 * @author App Team
 */
public class UserActivity extends BaseActivity {

    private TextView email;
    private TextView emailField;
    private TextView firstName;
    private TextView firstNameField;
    private TextView lastName;
    private TextView lastNameField;
    private TextView cellNumber;
    private TextView cellNumberField;
    private CheckBox mute;
    private CheckBox emailValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user);
        super.onCreate(savedInstanceState);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        email = (TextView) findViewById(R.id.email);
        emailField = (TextView) findViewById(R.id.email_field);
        firstName = (TextView) findViewById(R.id.first_name);
        firstNameField = (TextView) findViewById(R.id.first_name_field);
        lastName = (TextView) findViewById(R.id.last_name);
        lastNameField = (TextView) findViewById(R.id.last_name_field);
        cellNumber = (TextView) findViewById(R.id.cell_number);
        cellNumberField = (TextView) findViewById(R.id.cell_number_field);
        mute = (CheckBox) findViewById(R.id.mute_field);
        emailValidated = (CheckBox) findViewById(R.id.email_validated_field);

        if (!loading) {
            String token = getAccessToken();
            if(token != null) {
                comm.getUser(this, prefs.getString(LoginActivity.USER_URL, ""), token);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
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

    @Override
    public void setDoneLoading(JSONObject response) {
        super.setDoneLoading(response);

        String token = getAccessToken();
        if(token != null) {
            comm.getUser(this, prefs.getString(LoginActivity.USER_URL, ""), token);
        }
    }

    /**
     * Fill in the fields of the user view
     * @param user The user object we want to be shown
     */
    public void setUser(User user) {
        if (user != null) {
            if (user.getEmail() != null && !user.getEmail().equals("null")) {
                emailField.setText(user.getEmail());
            } else {
                ViewGroup parent = (ViewGroup) email.getParent();
                parent.removeView(email);
                parent.removeView(emailField);
            }

            if (user.getFirstName() != null && !user.getFirstName().equals("null")) {
                firstNameField.setText(user.getFirstName());
            } else {
                ViewGroup parent = (ViewGroup) firstName.getParent();
                parent.removeView(firstName);
                parent.removeView(firstNameField);
            }

            if (user.getLastName() != null && !user.getLastName().equals("null")) {
                lastNameField.setText(user.getLastName());
            } else {
                ViewGroup parent = (ViewGroup) lastName.getParent();
                parent.removeView(lastName);
                parent.removeView(lastNameField);
            }

            if (user.getCellNumber() != null && !user.getCellNumber().equals("null")) {
                cellNumberField.setText(user.getCellNumber());
            } else {
                ViewGroup parent = (ViewGroup) cellNumber.getParent();
                parent.removeView(cellNumber);
                parent.removeView(cellNumberField);
            }

            mute.setChecked(!user.isMute());
            emailValidated.setChecked(user.isEmailValidated());
        } else {
            //Something went wrong with the communication
            showSnackBar(getString(R.string.wrong_communication));
        }
    }
}
