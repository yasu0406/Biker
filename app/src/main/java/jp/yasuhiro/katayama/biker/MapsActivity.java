package jp.yasuhiro.katayama.biker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import static jp.yasuhiro.katayama.biker.R.id.map;

public class MapsActivity extends Activity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private GoogleMap mMap = null;
    static final int RC_LOCATION_PERMISSIONS = 0x01;

    private ImageView mNavProfile;
    private ImageView mNavChat;
    private ImageView mNavMap;
    private ImageView mNavMyPage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapFragment mapFragment  = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("目的地");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("読込み中...");
        mNavProfile = (ImageView) findViewById(R.id.footerNavProfile);
        mNavChat = (ImageView) findViewById(R.id.footerNavChat);
        mNavMap = (ImageView) findViewById(R.id.footerNavMap);
        mNavMyPage = (ImageView) findViewById(R.id.footerNavMyPage);

        mNavProfile.setOnClickListener(this);
        mNavChat.setOnClickListener(this);
        mNavMap.setOnClickListener(this);
        mNavMyPage.setOnClickListener(this);

        // ログイン済みのユーザーを収録する
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng TokyoStation = new LatLng(35.681661, 139.766063);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                TokyoStation, 15
        );
        mMap.moveCamera(cu);
        mMap.addMarker(new MarkerOptions().position(TokyoStation).title("東京駅"));

        setMyLocationEnabled();
        mMap.setTrafficEnabled(true);
        // Search for restaurants nearby
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    void setMyLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, RC_LOCATION_PERMISSIONS);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION_PERMISSIONS) {
            onRequestLocationPermissionsResult(permissions, grantResults);
        }
    }


    void onRequestLocationPermissionsResult(String[] permissions, int[] grantResults) {
        int[] granted2 = {PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED};
        if (Arrays.equals(permissions, PERMISSIONS) && Arrays.equals(grantResults, granted2)) {
            // 権限を取得したのでもう一度setMyLocationEnabled()を呼び出す
            setMyLocationEnabled();
        } else {
            // 権限を取得できなかったので諦める
            Toast.makeText(this, "No location permissions granted", Toast.LENGTH_LONG).show();
        }
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
        }
    }
}