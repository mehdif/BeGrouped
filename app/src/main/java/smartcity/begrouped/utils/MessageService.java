package smartcity.begrouped.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.List;

public class MessageService extends Service implements SinchClientListener {

    private static final String APP_KEY = "8a414cde-baa7-4e85-bde0-96a94e9a45f6";
    private static final String APP_SECRET = "yzMKSX/rR0KEHyQYg/zLng==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    public final MessageServiceInterface serviceInterface = new MessageServiceInterface();
    private SinchClient sinchClient = null;
    private MessageClient messageClient = null;
    private String currentUserId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("service","on start command");
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        if (currentUserId != null && !isSinchClientStarted()) {
            Log.v("service","currentuserid is not null and client is not started");
            Log.v("service",currentUserId);
            ParseInstallation parseInstallation =ParseInstallation.getCurrentInstallation();
            parseInstallation.put("userName",currentUserId);
            parseInstallation.saveInBackground();
            startSinchClient(currentUserId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void startSinchClient(String username) {
        sinchClient = Sinch.getSinchClientBuilder().context(this).userId(username).applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET).environmentHost(ENVIRONMENT).build();

        sinchClient.addSinchClientListener(this);

        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportActiveConnectionInBackground(true);

        sinchClient.setSupportPushNotifications(true);

        String project_id="426691860129";
        sinchClient.checkManifest();
        sinchClient.registerPushNotificationData(project_id.getBytes());
        sinchClient.start();
        Log.v("service","Sinchclient has started");
    }

    private boolean isSinchClientStarted() {
        return sinchClient != null && sinchClient.isStarted();
    }

    @Override
    public void onClientFailed(SinchClient client, SinchError error) {
        sinchClient = null;
        Log.v("service","sinch client failed");

    }

    @Override
    public void onClientStarted(SinchClient client) {

        client.startListeningOnActiveConnection();
        messageClient = client.getMessageClient();
        Log.v("service","sinch client started");
    }

    @Override
    public void onClientStopped(SinchClient client) {
        sinchClient = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }

    @Override
    public void onLogMessage(int level, String area, String message) {


    }

    @Override
    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration)
    {
    }

    public void sendMessage(List<String> recipientsIds, String textBody) {

        Log.v("message","on send message");
        if ( messageClient == null)
        {
            Log.v("message","messageclient is null");
        }
        if (messageClient != null) {
            WritableMessage message = new WritableMessage(recipientsIds,textBody);
            messageClient.send(message);
            Log.v("textBody:",textBody);
            Log.v("recipientsid:",recipientsIds.toString());
        }
    }

    public void addMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.addMessageClientListener(listener);
        }
    }

    public void removeMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.removeMessageClientListener(listener);
        }
    }

    @Override
    public void onDestroy() {
//        sinchClient.stopListeningOnActiveConnection();
//       sinchClient.terminate();
    }

    public class MessageServiceInterface extends Binder {
        public void sendMessage(List<String> recipientsIds, String textBody) {
            Log.v("messageserviceinterface",textBody);
            Log.v("messageserviceinterface",recipientsIds.toString());
            MessageService.this.sendMessage(recipientsIds, textBody);
        }

        public void addMessageClientListener(MessageClientListener listener) {
            MessageService.this.addMessageClientListener(listener);
        }

        public void removeMessageClientListener(MessageClientListener listener) {
            MessageService.this.removeMessageClientListener(listener);
        }

        public boolean isSinchClientStarted()
        {
            return MessageService.this.isSinchClientStarted();
        }
    }
}

