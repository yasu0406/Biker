package jp.yasuhiro.katayama.biker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyPageDetailActivity extends  Activity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int CHOOSER_REQUEST_CODE = 100;

    private EditText nickEdit;
    private RadioGroup sexRadio;
    private RadioButton mwRadio;
    private EditText ageEdit;
    private Spinner bloodspinner;
    private EditText regionEdit;
    private EditText mybikeEdit;
    private EditText bikehistoryEdit;
    private EditText emailEdit;
    private EditText introductionEdit;
    private ImageView mProfileImage;
    private Uri mPictureUri;
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
        setContentView(R.layout.activity_my_page_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("マイページ編集");

        nickEdit = (EditText) findViewById(R.id.nicknameEdit);
        sexRadio = (RadioGroup)findViewById(R.id.sex);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        bloodspinner = (Spinner) findViewById(R.id.bloodspinner);
        regionEdit = (EditText) findViewById(R.id.regionEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        bikehistoryEdit = (EditText) findViewById(R.id.bikehistoryEdit);
        introductionEdit = (EditText) findViewById(R.id.introductionEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        mProfileImage = (ImageView) findViewById(R.id.mProfileImage);

        mNavProfile = (ImageView) findViewById(R.id.footerNavProfile);
        mNavChat = (ImageView) findViewById(R.id.footerNavChat);
        mNavMap = (ImageView) findViewById(R.id.footerNavMap);
        mNavMyPage = (ImageView) findViewById(R.id.footerNavMyPage);
        mRegistrationButton = (Button) findViewById(R.id.registrationButton);

        mNavProfile.setOnClickListener(this);
        mNavChat.setOnClickListener(this);
        mNavMap.setOnClickListener(this);
        mNavMyPage.setOnClickListener(this);
        mProfileImage.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("登録中...");

        mAuth = FirebaseAuth.getInstance();

        // ログイン済みのユーザーを収録する
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // ログインしていなければログイン画面に遷移させる
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());
        userRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ログイン済みのユーザーを収録する
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                        nickEdit.setText(nickname);
                        if(sex.equals("男性")) {
                            sexRadio.check(R.id.manRdio);
                            Log.d("男性", sex);
                        }else if(sex.equals("女性")){
                            Log.d("女性", sex);
                            sexRadio.check(R.id.womanRadio);
                        }
                        ageEdit.setText(age);
                        //bloodspinner.setText();
                        mybikeEdit.setText(myBike);
                        bikehistoryEdit.setText(bikeHistory);
                        regionEdit.setText(region);
                        emailEdit.setText(user.getEmail());
                        if(imageString != null){
                            image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
                            mProfileImage = (ImageView) findViewById(R.id.mProfileImage);
                            mProfileImage.setImageBitmap(image);
                        }
                        introductionEdit.setText(introduction);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());

                Map<String, String> data = new HashMap<String, String>();
                String nickName = nickEdit.getText().toString();
                int checkedId = sexRadio.getCheckedRadioButtonId();
                mwRadio = (RadioButton) findViewById(checkedId);
                String sex = mwRadio.getText().toString();
                String age = ageEdit.getText().toString();
                String blood = (String)bloodspinner.getSelectedItem();
                String region = regionEdit.getText().toString();
                String myBike = mybikeEdit.getText().toString();
                String bikeHistory = bikehistoryEdit.getText().toString();
                String introduction = introductionEdit.getText().toString();

                // 添付画像を取得する
                BitmapDrawable drawable = (BitmapDrawable) mProfileImage.getDrawable();
                // 添付画像が設定されていれば画像を取り出してBASE64エンコードする
                if (drawable != null) {
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    data.put("profileImage", bitmapString);
                }

                data.put("nickName", nickName);
                data.put("sex", sex);
                data.put("age", age);
                data.put("blood", blood);
                data.put("region", region);
                data.put("myBike", myBike);
                data.put("bikeHistory", bikeHistory);
                data.put("introduction", introduction);
                mProgress.show();
                userRef.setValue(data);

                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
                mProgress.dismiss();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ユーザーが許可したとき
                    showChooser();
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSER_REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                if (mPictureUri != null) {
                    getContentResolver().delete(mPictureUri, null, null);
                    mPictureUri = null;
                }
                return;
            }

            // 画像を取得
            Uri uri = (data == null || data.getData() == null) ? mPictureUri : data.getData();

            // URIからBitmapを取得する
            Bitmap image;
            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(uri);
                image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                return;
            }

            // 取得したBimapの長辺を500ピクセルにリサイズする
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            float scale = Math.min((float) 700 / imageWidth, (float) 700 / imageHeight); // (1)

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true);

            // BitmapをImageViewに設定する
            mProfileImage.setImageBitmap(resizedImage);

            mPictureUri = null;
        }
    }
    private void showChooser() {
        // ギャラリーから選択するIntent
        Intent garallyIntent = new Intent(Intent.ACTION_GET_CONTENT);
        garallyIntent.setType("image/*");
        garallyIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // カメラで撮影するIntent
        String filename = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mPictureUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);

        // ギャラリー選択のIntentを与えてcreateChooserメソッドを呼ぶ
        Intent chooserIntent = Intent.createChooser(garallyIntent, "画像を取得");

        // EXTRA_INITIAL_INTENTS にカメラ撮影のIntentを追加
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, CHOOSER_REQUEST_CODE);
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
        if(v == mProfileImage){

            // パーミッションの許可状態を確認する
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 許可されている
                    showChooser();
                } else {
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

                    return;
                }
            } else {
                showChooser();
            }

        }
    }
}
