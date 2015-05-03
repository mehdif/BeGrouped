package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.utils.MyApplication;


public class RegisterActivity extends ActionBarActivity {

    private Button accept;
    private Button cancel;

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmpasswordField;
    private EditText firstnameField;
    private EditText lastnameField;
    private EditText phonenumberField;
    private CheckBox termAcceptation;
    private ProgressDialog progressDialog;


    private String username;
    private String password;
    private String confirmpassword;
    private String firstname;
    private String lastname;
    private String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accept = (Button) findViewById(R.id.button_validate);
        cancel = (Button) findViewById(R.id.button_cancel);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        confirmpasswordField = (EditText) findViewById(R.id.confirmpassword);
        firstnameField= (EditText) findViewById(R.id.prenom);
        lastnameField=  (EditText) findViewById(R.id.nom);
        phonenumberField=(EditText) findViewById(R.id.phonenumber);
        termAcceptation=(CheckBox) findViewById(R.id.terms);

        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (MyApplication.locationManager!=null) MyApplication.locationManager.removeUpdates(MyApplication.locationListener);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                confirmpassword=confirmpasswordField.getText().toString();


                firstname= firstnameField.getText().toString();
                lastname=  lastnameField.getText().toString();
                phonenumber=phonenumberField.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || firstname.isEmpty() || lastname.isEmpty())
                    Toast.makeText(RegisterActivity.this, "All fields should be completed",Toast.LENGTH_SHORT).show();
                else if (! password.equals(confirmpassword))
                    Toast.makeText(RegisterActivity.this, "Please confirm your password correctly",Toast.LENGTH_SHORT).show();
                else if (! termAcceptation.isChecked())
                    Toast.makeText(RegisterActivity.this, "Please accept the terms and conditions of use",Toast.LENGTH_SHORT).show();
                else {
                    showProgress();
                    // Register in our server
                    UserManager.register(username, password, firstname, lastname, phonenumber, RegisterActivity.this);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
    }
    public void registerUser(){



        // Register in Cloud
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {

                Toast.makeText(getApplicationContext(),
                        "Signing up successfully!"
                        , Toast.LENGTH_LONG).show();

            }
        });

        hideProgress();

        Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
        startActivity(i);
    }

    public void showProgress(){
        progressDialog = ProgressDialog.show(this, null,
                "Loading...", true);
    }

    public void hideProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
