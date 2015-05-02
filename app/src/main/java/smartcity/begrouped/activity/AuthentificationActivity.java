package smartcity.begrouped.activity;

import android.app.Activity;
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
import com.parse.ParseUser;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.UserManager;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MyApplication;


public class AuthentificationActivity extends Activity {
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentification);


            login = (Button) findViewById(R.id.buttonLogin);
            register = (Button) findViewById(R.id.buttonRegister);
            username = (EditText) findViewById(R.id.editTextId);
            password = (EditText) findViewById(R.id.editTextPassword);
            login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (MyApplication.locationManager != null)
                        MyApplication.locationManager.removeUpdates(MyApplication.locationListener);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    Object user = UserManager.authenticate(username.getText().toString(), password.getText().toString());
                    if (user instanceof User && user != null) {


                        MyApplication.myIdentity = (User) user;

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
                    } else {
                        Toast.makeText(getApplicationContext(), "NO ACCOUNT AT THIS NAME", Toast.LENGTH_LONG).show();
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
            Log.v("throwable:","wik wik");
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
