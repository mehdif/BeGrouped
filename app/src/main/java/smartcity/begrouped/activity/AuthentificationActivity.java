package smartcity.begrouped.activity;

import android.app.Activity;
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
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.MyApplication;


public class AuthentificationActivity extends ActionBarActivity {
    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private ProgressDialog progressDialog;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            context = this;
            login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (MyApplication.locationManager != null)
                        MyApplication.locationManager.removeUpdates(MyApplication.locationListener);

                    String login = username.getText().toString();
                    String pass = password.getText().toString();

                    if (login.isEmpty() || pass.isEmpty()) Toast.makeText(AuthentificationActivity.this, "Please enter your username and your password",Toast.LENGTH_SHORT).show();
                    else {
                        showProgress();
                        UserManager.authenticate(login, pass, AuthentificationActivity.this);
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

    public void getAuthentificatedUser(Object user){

        if (user instanceof User && user != null) {


            MyApplication.myIdentity = (User) user;

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);

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

        hideProgress();
    }

    public void showProgress(){
        progressDialog = ProgressDialog.show(AuthentificationActivity.this, null,
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
