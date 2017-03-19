package jp.yasuhiro.katayama.biker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountDetailActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Account mAccount;
    private ImageView mProfileImage;
    private TextView mNickName;
    private TextView mSex;
    private TextView mAge;
    private TextView mRegion;
    private TextView mBlood;
    private TextView mMyBike;
    private TextView mBikeHistory;
    private TextView mIntroduction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("プロフィール");

        Bundle extras = getIntent().getExtras();
        mAccount = (Account) extras.get("account");

        checkLoginUser();

        setupUser();

        FooterNavigation.createFooterNav(this,this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.account_detail, menu);
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
        FooterNavigation.moveFooterNav(v, this);
    }

    // ログインチェックメソッド
    public void checkLoginUser() {
        // ログイン済みのユーザーを収録する
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            moveLoginPage();
        }
    }

    // ユーザー情報メソッド
    public void setupUser() {
        mProfileImage = (ImageView) findViewById(R.id.mProfileImage);
        byte[] bytes = mAccount.getmBitmapArray();
        if (bytes.length != 0) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
            mProfileImage = (ImageView)findViewById(R.id.mProfileImage);
            mProfileImage.setImageBitmap(image);
        }
        mNickName = (TextView) findViewById(R.id.nickNameTitle);
        mNickName.setText(mAccount.getmNickName());
        mRegion = (TextView) findViewById(R.id.regionTitle);
        mRegion.setText(mAccount.getmRegion());


        mNickName = (TextView) findViewById(R.id.nickName);
        mNickName.setText(mAccount.getmNickName());
        mSex = (TextView) findViewById(R.id.sex);
        mSex.setText(mAccount.getmSex());
        mAge = (TextView)findViewById(R.id.age);
        mAge.setText(mAccount.getmAge() + "歳");
        mRegion = (TextView) findViewById(R.id.region);
        mRegion.setText(mAccount.getmRegion());
        mBlood = (TextView) findViewById(R.id.bloodType);
        mBlood.setText(mAccount.getmBlood());
        mMyBike = (TextView) findViewById(R.id.myBike);
        mMyBike.setText(mAccount.getmMyBike());
        mBikeHistory = (TextView) findViewById(R.id.bikeHistory);
        mBikeHistory.setText(mAccount.getmBikeHistory() + "年");
        mIntroduction = (TextView)findViewById(R.id.introduction);
        mIntroduction.setText(mAccount.getmIntroduction());
    }

    // ログインページ遷移メソッド
    public void moveLoginPage(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
