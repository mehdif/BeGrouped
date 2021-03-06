package smartcity.begrouped.activity;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;

import android.content.Intent;

import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
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

import java.util.ArrayList;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;

import smartcity.begrouped.R;
import smartcity.begrouped.model.User;
import smartcity.begrouped.utils.MessageAdapter;
import smartcity.begrouped.utils.MessageService;
import smartcity.begrouped.utils.Msg;
import smartcity.begrouped.utils.MyApplication;
import smartcity.begrouped.utils.PushNotificationService;

public class ChatActivity extends ActionBarActivity implements FragmentDrawerGroup.FragmentDrawerListener{
    private List<String> recipientsIds=new ArrayList<String>();
    private EditText messageBodyField;
    private String messageBody;
    private MessageService.MessageServiceInterface messageService;
    private MessageAdapter messageAdapter;
    private ListView messagesList;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private List<Msg> messages=new ArrayList<Msg>();

    private Toolbar mToolbar;
    private FragmentDrawerGroup drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat);

            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawerFragment = (FragmentDrawerGroup) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_home_group);
            drawerFragment.setUp(R.id.fragment_navigation_drawer_home_group, (DrawerLayout) findViewById(R.id.drawer_layout_group), mToolbar);
            drawerFragment.setDrawerListener(this);
            getSupportActionBar().setTitle("Chat");

            bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);
            currentUserId = MyApplication.currentUserId;

            messagesList = (ListView) findViewById(R.id.listMessages);
            messageAdapter = new MessageAdapter(this);
            messagesList.setAdapter(messageAdapter);
            getUsersOfChoosenGroup();
            messageBodyField = (EditText) findViewById(R.id.messageBodyField);

            findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessage();
                }
            });
        }
        catch(Exception e)
        {

        }
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
    }

    @Override
    public void onDestroy() {
        messageService.removeMessageClientListener(messageClientListener);
        unbindService(serviceConnection);
        super.onDestroy();
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
        query.whereContains("groupName",MyApplication.currentGroup.getName());
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
        if ( recipientsIds.size() <=0)
        {
            Toast.makeText(this, "This group is still empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (messageBody.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        messageService.sendMessage(recipientsIds, MyApplication.myIdentity.getUsername() + " says: " + messageBody);
        messageBodyField.setText("");
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
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
                                    MessageFailureInfo failureInfo){
            Toast.makeText(ChatActivity.this, "Message failed to send."+failureInfo.getSinchError().getMessage(), Toast.LENGTH_LONG).show();
            Log.v("failure:",failureInfo.getSinchError().getMessage());
        }


        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            Log.v("message", "onincoming");
            WritableMessage writableMessage = new WritableMessage(message.getRecipientIds(), message.getTextBody());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
        }


        @Override
        public void onMessageSent(MessageClient client, Message message, final String recipientId) {

            Log.v("message", "onmessagesent");
            final WritableMessage writableMessage = new WritableMessage(currentUserId, message.getTextBody());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

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
                                    parseMessage.put("groupName", MyApplication.currentGroup.getName());
                                    parseMessage.saveInBackground();
                                }
                            }
                        }
                    }
                });
        }

        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
            for (int i = 0; i < message.getRecipientIds().size(); i++) {
                PushNotificationService.sendNotification("New message from " + MyApplication.currentGroup.getName(), message.getTextBody(), message.getRecipientIds().get(i));
            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
            return true;
        }

        //if(id == R.id.action_search){
        //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
        //return true;
        //}

        return super.onOptionsItemSelected(item);
    }



    private void displayView(int position) {
        Intent i = null;
        String title = "Home";
        switch (position) {
            case 0:
                title = "Home";
                i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 1:
                title = "Current Group";
                i = new Intent(getApplicationContext(), GroupHomeFragment.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);

                break;
            case 2:
                title = "Map";
                i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 3:
                title = "Members";
                i = new Intent(getApplicationContext(), MembersFragmentActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 4:
                title = "Chat";
                i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);

                break;
            case 5:
                title = "Schedule";
                i = new Intent(getApplicationContext(), ScheduleFragmentActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getSupportActionBar().setTitle(title);
                break;
            case 6:
                Intent in = new Intent(getApplicationContext(), AuthentificationActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                startActivity(in);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();

                break;
            default:
                break;
        }
    }

}
