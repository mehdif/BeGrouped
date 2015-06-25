package smartcity.begrouped.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import smartcity.begrouped.R;
import smartcity.begrouped.utils.GlobalMethodes;

public class CallActivity extends ActionBarActivity {

    private Call call;
    private SinchClient sinchClient;
    private Button button;
    private String callerId;
    private String recipientId;
    private String recipientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);

        Intent intent = getIntent();
        callerId = intent.getStringExtra("callerId");
        Log.v("call",callerId);
        recipientName = intent.getStringExtra("recipientName");
        recipientId = GlobalMethodes.getRecipientId(recipientName);
        Log.v("call",recipientId);

        button = (Button) findViewById(R.id.button);
        button.setText("Call " + recipientName);
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(callerId)
                .applicationKey("afa3e17f-7786-464f-88a8-062f328e62cc")
                .applicationSecret("WOkMEuTBwkukOz07L4SQrw==")
                .environmentHost("sandbox.sinch.com")
                .build();
        Log.v("call", String.valueOf(sinchClient.isStarted()));

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        Log.v("call", String.valueOf(sinchClient.isStarted()));

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call == null) {
                        call = sinchClient.getCallClient().callUser(recipientId);
                        call.addCallListener(new SinchCallListener());
                        button.setText("Hang Up");

                }
                 else {
                    call.hangup();
                }
            }
        });
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            button.setText("Call " + recipientName);
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            button.setText("Hang Up");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            AlertDialog.Builder builder = new AlertDialog.Builder(CallActivity.this);
            builder.setMessage(recipientName + " is calling...")
                    .setCancelable(false)
                    .setPositiveButton("Answer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            call.answer();
                            call.addCallListener(new SinchCallListener());
                            button.setText("Hang Up");
                        }
                    })
                    .setNegativeButton("Hang Up", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            call.hangup();
                            call = null;
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }
}

