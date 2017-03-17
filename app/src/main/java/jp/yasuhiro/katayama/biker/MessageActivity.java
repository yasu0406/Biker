package jp.yasuhiro.katayama.biker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private ImageView mNavProfile;
    private ImageView mNavChat;
    private ImageView mNavMap;
    private ImageView mNavMyPage;
    private ProgressDialog mProgress;
    private ImageButton mSendMessage;
    private ImageView mProfileImage;
    private ListView nickName;

    private DatabaseReference mDatabaseReference;
    private ListView mListView;
    private ArrayList<Chat> mChatArrayList;
    private MessageListAdapter mAdapter;
    private Chat mChat;
    private Message mMessage;
    private ArrayList<Message> mMessageArrayList;

    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String mMessage = (String) map.get("message");
            String mNickName = (String) map.get("nickName");
            String imageString = (String)map.get("profileImage");
            byte[] bytes;
            if (imageString != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                bytes = Base64.decode(imageString, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
            }

            Message message = new Message(mMessage, mNickName ,bytes) ;
            mMessageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();
        mChat = (Chat) extras.get("chat");
        toolbar.setTitle(mChat.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ログイン済みのユーザーを収録する
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference chatRef = mDatabaseReference.child(Const.ChatsPath).child(mChat.getChatUid()).child(Const.MessagePath);
        DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());
        // ListViewの準備
        mListView = (ListView) findViewById(R.id.messageList);

        mAdapter = new MessageListAdapter(this);
        mMessageArrayList = new ArrayList<Message>();
        mMessageArrayList.clear();
        mAdapter.setMessagesArrayList(mMessageArrayList);
        mListView.setAdapter(mAdapter);
        chatRef.addChildEventListener(mEventListener);

        mNavProfile = (ImageView) findViewById(R.id.footerNavProfile);
        mNavChat = (ImageView) findViewById(R.id.footerNavChat);
        mNavMap = (ImageView) findViewById(R.id.footerNavMap);
        mNavMyPage = (ImageView) findViewById(R.id.footerNavMyPage);
        mSendMessage = (ImageButton) findViewById(R.id.sendButton);

        mNavProfile.setOnClickListener(this);
        mNavChat.setOnClickListener(this);
        mNavMap.setOnClickListener(this);
        mNavMyPage.setOnClickListener(this);
        mSendMessage.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else if(id == R.id.navLogout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v){

        if(v == mNavProfile){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if(v == mNavChat){
            Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
            startActivity(intent);
        }else if(v == mNavMap){
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }else if(v == mNavMyPage){
            Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
            startActivity(intent);
        }else if(v == mSendMessage){
            sendMessage();
        }
    }

    private void sendMessage() {
        // ログイン済みのユーザーを収録する
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());
        final DatabaseReference chatRef = mDatabaseReference.child(Const.ChatsPath).child(mChat.getChatUid()).child(Const.MessagePath);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = (HashMap)dataSnapshot.getValue();
                String nickName = (String)map.get("nickName");
                String imageString = (String)map.get("profileImage");

                Map<String, String> data = new HashMap<String, String>();
                EditText inputText = (EditText) findViewById(R.id.messageInput);
                String message = inputText.getText().toString();
                if (!inputText.equals("") || inputText == null) {
                    // Create our 'model', a Chat object
                    data.put("message", message);
                    data.put("nickName", nickName);
                    data.put("profileImage", imageString);
                    // Create a new, auto-generated child of that chat location, and save our chat data there
                    chatRef.push().setValue(data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
