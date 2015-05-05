package smartcity.begrouped.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.GlobalMethodes;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class RegisterActivity extends ActionBarActivity implements AsyncResponse {

    private Button accept;
    private Button cancel;

    private TextView textView;
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

    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = (TextView) findViewById(R.id.label);
        accept = (Button) findViewById(R.id.button_validate);
        cancel = (Button) findViewById(R.id.button_cancel);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        confirmpasswordField = (EditText) findViewById(R.id.confirmpassword);
        firstnameField = (EditText) findViewById(R.id.prenom);
        lastnameField = (EditText) findViewById(R.id.nom);
        phonenumberField = (EditText) findViewById(R.id.phonenumber);
        termAcceptation = (CheckBox) findViewById(R.id.terms);

        textView.setText(Html.fromHtml("<font color=#ffffff>Sign</font> <font color=#c62828>In</font>"));

        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (MyApplication.locationManager != null)
                    MyApplication.locationManager.removeUpdates(MyApplication.locationListener);

                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                confirmpassword = confirmpasswordField.getText().toString();
                firstname = firstnameField.getText().toString();
                lastname = lastnameField.getText().toString();
                phonenumber = phonenumberField.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || firstname.isEmpty() || lastname.isEmpty())
                    Toast.makeText(RegisterActivity.this, MessageUser.get("1106"), Toast.LENGTH_SHORT).show();
                else if (!password.equals(confirmpassword))
                    Toast.makeText(RegisterActivity.this, MessageUser.get("1107"), Toast.LENGTH_SHORT).show();
                else if (!termAcceptation.isChecked())
                    Toast.makeText(RegisterActivity.this, MessageUser.get("1108"), Toast.LENGTH_SHORT).show();
                else {
                    try {
                        action = "register";
                        String encodedFirstName = URLEncoder.encode(firstname, "utf-8").replace("+", "%20");
                        String encodedLastName = URLEncoder.encode(lastname, "utf-8").replace("+", "%20");
                        String hashedPassWord = GlobalMethodes.md5(password);

                        Downloader downloader = new Downloader(RegisterActivity.this, RegisterActivity.this);
                        downloader.execute(AllUrls.register_user_url + username + "/" + hashedPassWord + "/" + encodedFirstName + "/" + encodedLastName + "/" + phonenumber);
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(RegisterActivity.this, MessageUser.get("0000"),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
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

    @Override
    public void executeAfterDownload(String output) {
        Log.v("aymen",output);
        if (action.equals("register")) {
            if (isNumeric(output.charAt(0))) {
                Toast.makeText(RegisterActivity.this, MessageUser.get(output),Toast.LENGTH_SHORT).show();
            }
            else {
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        Toast.makeText(RegisterActivity.this, MessageUser.get("2101"),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
            startActivity(i);
        }
        action="";
    }
}
