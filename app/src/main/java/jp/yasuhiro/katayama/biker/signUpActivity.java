package jp.yasuhiro.katayama.biker;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.graphics.Matrix;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, DatabaseReference.CompletionListener {
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
    private EditText passEdit;
    private EditText introductionEdit;
    private ImageView mProfileImage;
    private Button registrationButton;
    private ProgressDialog mProgress;

    private Uri mPictureUri;

    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    OnCompleteListener<AuthResult> mCreateAccountListener;
    OnCompleteListener<AuthResult> mLoginListener;
    FirebaseUser users;

    // アカウント作成時にフラグを立て、ログイン処理後に名前をFirebaseに保存する
    boolean mIsCreateAccount = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // FirebaseAuthのオブジェクトを取得する
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("登録中...");
        nickEdit = (EditText) findViewById(R.id.nicknameEdit);
        sexRadio = (RadioGroup)findViewById(R.id.sex);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        bloodspinner = (Spinner) findViewById(R.id.bloodspinner);
        regionEdit = (EditText) findViewById(R.id.regionEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        bikehistoryEdit = (EditText) findViewById(R.id.bikehistoryEdit);
        introductionEdit = (EditText) findViewById(R.id.introductionEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passEdit = (EditText) findViewById(R.id.passEdit);
        users = mAuth.getCurrentUser();

        //登録処理
        registrationButton = (Button) findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(this);

        //画像登録処理
        mProfileImage = (ImageView) findViewById(R.id.mProfileImage);
        mProfileImage.setOnClickListener(this);

        // アカウント作成処理のリスナー
        mCreateAccountListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // 成功した場合
                    // ログインを行う
                    String email = emailEdit.getText().toString();
                    String pass = passEdit.getText().toString();
                    login(email, pass);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {

                    // 失敗した場合
                    // エラーを表示する
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "アカウント作成に失敗しました", Snackbar.LENGTH_LONG).show();

                    // プログレスダイアログを非表示にする
                    mProgress.dismiss();
                }
            }
        };

        // ログイン処理のリスナー
        mLoginListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // 成功した場合
                    FirebaseUser users = mAuth.getCurrentUser();
                    DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(users.getUid());

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
                    data.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());


                    mProgress.show();
                    userRef.setValue(data);

                    // プログレスダイアログを非表示にする
                    mProgress.dismiss();

                    // Main画面に遷移
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    // 失敗した場合
                    // エラーを表示する
                    View view = findViewById(android.R.id.content);
                    Snackbar.make(view, "ログインに失敗しました", Snackbar.LENGTH_LONG).show();

                    // プログレスダイアログを非表示にする
                    mProgress.dismiss();
                }
            }
        };
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

    @Override
    public void onClick(View v) {
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

        if(v == registrationButton){
            // キーボードが出てたら閉じる
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            String nickName = nickEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String pass = passEdit.getText().toString();

            if(nickName.length() == 0){
                Snackbar.make(v,"ニックネームを入力してください",Snackbar.LENGTH_LONG).show();
            }else if(pass.length() == 0){
                Snackbar.make(v,"パスワードを入力してください",Snackbar.LENGTH_LONG).show();
            }else if(pass.length() < 6){
                Snackbar.make(v,"パスワードを6文字以上入力してください",Snackbar.LENGTH_LONG).show();
            }else {
                mIsCreateAccount = true;
                createAccount(email, pass);
            }
        }

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
    public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
        mProgress.dismiss();

        if (databaseError == null) {
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "投稿に失敗しました", Snackbar.LENGTH_LONG).show();
        }
    }
    private void login(String email, String pass) {
        // プログレスダイアログを表示する
        mProgress.show();

        // ログインする
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(mLoginListener);
    }

    private void createAccount(String email, String pass) {
        // プログレスダイアログを表示する
        mProgress.show();

        // アカウントを作成する
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(mCreateAccountListener);
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
}
