package smartcity.begrouped.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.AllUrls;
import smartcity.begrouped.utils.AsyncResponse;
import smartcity.begrouped.utils.Downloader;
import smartcity.begrouped.utils.GlobalMethodes;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MessageUser;
import smartcity.begrouped.utils.MyApplication;

import static smartcity.begrouped.utils.GlobalMethodes.isNumeric;


public class AuthentificationActivity extends ActionBarActivity implements AsyncResponse {

    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private String action="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MessageUser.init();
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentification);
            Parse.initialize(this, "o0vvZbqThRgTotm9VKxeSfl7yaDebOfOa51sLXNc", "PMz0wBtgfmQVSJtINeBP85L1GwwbooeEMGu4tkMc");
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
               Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
               startService(serviceIntent);
            }
            login = (Button) findViewById(R.id.buttonLogin);
            register = (Button) findViewById(R.id.buttonRegister);
            username = (EditText) findViewById(R.id.editTextId);
            password = (EditText) findViewById(R.id.editTextPassword);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MyApplication.locationManager != null) MyApplication.locationManager.removeUpdates(MyApplication.locationListener);
                    String login = username.getText().toString();
                    String pass = password.getText().toString();
                    if (login.isEmpty() || pass.isEmpty()) Toast.makeText(AuthentificationActivity.this, MessageUser.get("1105"),Toast.LENGTH_SHORT).show();
                    else {
                        Downloader downloader = new Downloader(AuthentificationActivity.this,AuthentificationActivity.this);
                        String hashedPass= GlobalMethodes.md5(pass);
                        action="login";
                        downloader.execute(AllUrls.authenticate_user_url+login+"/"+hashedPass);
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                }
            });
        }
        catch ( Throwable t)
        {
            Log.v("Problem :",t.getMessage());
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


    @Override
    public void executeAfterDownload(String output) {
        Log.v("aymen",output);
        if (action.equals("login")) {
            if (isNumeric(output.charAt(0))) {
                Toast.makeText(AuthentificationActivity.this, MessageUser.get(output),Toast.LENGTH_SHORT).show();
            }
            else {
                UserManager.authenticateUser(output);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
                            startService(serviceIntent);
                            Log.v("service", "start service");
                        }
                    }
                });

                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        }
        action="";
    }
}
