package smartcity.begrouped.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import smartcity.begrouped.R;
import smartcity.begrouped.controllers.GroupManager;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MessageAdapter;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.Msg;
import smartcity.begrouped.utils.MyApplication;

public class ChatActivity extends Activity {
    private List<String> recipientsIds=new ArrayList<String>();
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private MessageAdapter messageAdapter;
    private ListView messagesList;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private boolean sent=false;
    private List<Msg> messages=new ArrayList<Msg>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);
            bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);
            currentUserId = ParseUser.getCurrentUser().getObjectId();
            Log.v("currentuserid", currentUserId);

            messagesList = (ListView) findViewById(R.id.listMessages);
            messageAdapter = new MessageAdapter(this);
            messagesList.setAdapter(messageAdapter);
            getUsersOfChoosenGroup();
            Log.v("history","history");
            messageBodyField = (EditText) findViewById(R.id.messageBodyField);

            findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sent=false;
                    sendMessage();
                }
            });
        }
        catch(Exception e)
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        populateMessageHistory();
    }

    private final void getUsersOfChoosenGroup()
    {
        // Get members of choosen group
        final LinkedList<User> members= MyApplication.currentGroup.getMembers();

        // Get RecipientID's of members
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        for(int j=0; j < members.size();j++)
                        {

                            if ( userList.get(i).getUsername().equals(members.get(j).getUsername()))
                            {
                                recipientsIds.add(userList.get(i).getObjectId());
                                Log.v("recipientId",userList.get(i).getObjectId());
                                break;
                            }

                        }

                    }
                }
                populateMessageHistory();
            }
        });
       //populateMessageHistory();
    }
    private final void createNotification(String sender, String content){
       /* NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,ChatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("From " + sender)
                .setContentText(content)
                .setSmallIcon(R.drawable.abc_ic_menu_selectall_mtrl_alpha)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        n.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(0, n);*/
    }
    //get previous messages from parse & display
    private void populateMessageHistory() {
        String[] userIds=new String[recipientsIds.size()+1];
        userIds[0]=currentUserId;
        for(int i=0;i<recipientsIds.size();i++)
        {
            userIds[i+1]=recipientsIds.get(i);
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
        query.whereContainedIn("senderId", Arrays.asList(userIds));
        query.whereContainedIn("recipientId", Arrays.asList(userIds));
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {

                    WritableMessage message;
                    messages = new ArrayList<Msg>();
                   // messages.add(new Msg(messageList.get(0).get("recipientId").toString(), messageList.get(0).get("senderId").toString(),messageList.get(0).getCreatedAt(), messageList.get(0).get("messageText").toString()));


                    for(int i=0;i<messageList.size();i++)
                    {
                        boolean found=false;
                        Log.v("list",messages.toString());
                        for(int j=0;j<messages.size();j++)
                        {

                            if (messages.get(j).equals(new Msg(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("senderId").toString(), messageList.get(i).getCreatedAt(), messageList.get(i).get("messageText").toString())))

                            {
                                Log.v("found", "true");
                                found=true;
                            }
                        }
                        if( ! found)
                        {
                            messages.add(new Msg(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("senderId").toString(),messageList.get(i).getCreatedAt(), messageList.get(i).get("messageText").toString()));

                            message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
                            if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
                                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                            } else {
                                messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                            }
                        }

                    }
            }}
        });
    }

    private void sendMessage() {
        messageBody = messageBodyField.getText().toString();
        if (messageBody.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        Log.v("message",recipientsIds.toString());
       // for(int i=0;i<recipientsIds.size();i++)
       // {
        messageService.sendMessage(recipientsIds, MyApplication.myIdentity.getUsername() + " says: " + messageBody);
        //}
        messageBodyField.setText("");
    }

    @Override
    public void onDestroy() {
        messageService.removeMessageClientListener(messageClientListener);
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void showSpinner() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean success = intent.getBooleanExtra("success", false);
                progressDialog.dismiss();
                if (!success) {
                    Toast.makeText(getApplicationContext(), "Messaging service failed to start", Toast.LENGTH_LONG).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("ChatActivity"));
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.v("ServiceConnection", "ServiceConnection");
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.addMessageClientListener(messageClientListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo)
        {
            Toast.makeText(ChatActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
            Log.v("failure:",failureInfo.toString());
        }


        @Override
        public void onIncomingMessage(MessageClient client, Message message) {

            Log.v("OnIncoming","OnIncoming");
            //if (message.getSenderId().equals(message.getRecipientIds().get(0)))
           // {
                Log.v("senderid",message.getSenderId().toString());
                Log.v("recipieeentid",message.getRecipientIds().get(0));
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds(), message.getTextBody());
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
           // }
            ParseUser currentUser = ParseUser.getCurrentUser();
            String username= currentUser.getUsername();
            //createNotification(username,message.getTextBody());
        }


        @Override
        public void onMessageSent(MessageClient client, Message message, final String recipientId) {

            Log.v("OnSent","OnSent");

            if(! sent) {
                final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

                //only add message to parse database if it doesn't already exist there
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
                query.whereEqualTo("sinchId", message.getMessageId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                        if (e == null) {
                            if (messageList.size() == 0) {
                                for (int i = 0; i < recipientsIds.size(); i++) {
                                    ParseObject parseMessage = new ParseObject("ParseMessage");
                                    parseMessage.put("senderId", currentUserId);
                                    parseMessage.put("recipientId", recipientsIds.get(i));
                                    parseMessage.put("messageText", writableMessage.getTextBody());
                                    parseMessage.put("sinchId", writableMessage.getMessageId());
                                    parseMessage.saveInBackground();
                                }

                                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
                            }
                        }
                    }
                });
                sent=true;
            }


        }

        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }


}
