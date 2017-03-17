package jp.yasuhiro.katayama.biker;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static jp.yasuhiro.katayama.biker.R.id.age;
import static jp.yasuhiro.katayama.biker.R.id.bikeHistory;
import static jp.yasuhiro.katayama.biker.R.id.myBike;
import static jp.yasuhiro.katayama.biker.R.id.nickName;
import static jp.yasuhiro.katayama.biker.R.id.region;
import static jp.yasuhiro.katayama.biker.R.id.sex;

public class MyPageActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView mNickName;
    private TextView mSex;
    private TextView mAge;
    private TextView mRegion;
    private TextView mBlood;
    private TextView mMyBike;
    private TextView mBikeHistory;
    private TextView mIntroduction;
    private ImageView mProfileImage;
    private ProgressDialog mProgress;


    private ImageView mNavProfile;
    private ImageView mNavChat;
    private ImageView mNavMap;
    private ImageView mNavMyPage;
    private Button mRegistrationButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("マイページ");

        mNickName = (TextView) findViewById(nickName);
        mSex = (TextView) findViewById(sex);
        mAge = (TextView) findViewById(age);
        mRegion = (TextView) findViewById(region);
        mBlood = (TextView) findViewById(R.id.bloodType);
        mMyBike = (TextView) findViewById(myBike);
        mBikeHistory = (TextView) findViewById(bikeHistory);
        mIntroduction = (TextView) findViewById(R.id.introduction);

        mNavProfile = (ImageView) findViewById(R.id.footerNavProfile);
        mNavChat = (ImageView) findViewById(R.id.footerNavChat);
        mNavMap = (ImageView) findViewById(R.id.footerNavMap);
        mNavMyPage = (ImageView) findViewById(R.id.footerNavMyPage);
        mRegistrationButton = (Button) findViewById(R.id.registrationButton);

        mNavProfile.setOnClickListener(this);
        mNavChat.setOnClickListener(this);
        mNavMap.setOnClickListener(this);
        mNavMyPage.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("読み込み中...");

        mAuth = FirebaseAuth.getInstance();

        // ログイン済みのユーザーを収録する
        FirebaseUser user = mAuth.getCurrentUser();

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());
        userRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        HashMap map = (HashMap) dataSnapshot.getValue();
                        String nickname = (String)map.get("nickName");
                        String sex = (String)map.get("sex");
                        String age = (String)map.get("age");
                        String blood = (String)map.get("blood");
                        String myBike = (String)map.get("myBike");
                        String bikeHistory = (String)map.get("bikeHistory");
                        String region = (String)map.get("region");
                        String imageString = (String)map.get("profileImage");
                        Bitmap image = null;
                        byte[] bytes;
                        if (imageString != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            bytes = Base64.decode(imageString, Base64.DEFAULT);
                        } else {
                            bytes = new byte[0];
                        }
                        String introduction = (String)map.get("introduction");
                        mNickName.setText(nickname);
                        mSex.setText(sex);
                        mAge.setText(age + "歳");
                        mBlood.setText(blood);
                        mMyBike.setText(myBike);
                        mBikeHistory.setText(bikeHistory + "年");
                        mRegion.setText(region);
                        if(imageString != null){
                            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
                            mProfileImage = (ImageView) findViewById(R.id.profileImage);
                            mProfileImage.setImageBitmap(image);
                        }
                        mIntroduction.setText(introduction);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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
        }else if(v == mRegistrationButton){
            Intent intent = new Intent(getApplicationContext(), MyPageDetailActivity.class);
            startActivity(intent);
        }
    }
}
