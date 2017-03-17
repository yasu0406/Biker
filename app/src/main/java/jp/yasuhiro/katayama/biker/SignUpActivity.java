package jp.yasuhiro.katayama.biker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements DatabaseReference.CompletionListener {

    private EditText nickEdit;
    private RadioButton manRdio;
    private RadioButton womanRadio;
    private EditText ageEdit;
    private Spinner bloodspinner;
    private EditText regionEdit;
    private EditText mybikeEdit;
    private EditText bikehistoryEdit;
    private EditText emailEdit;
    private EditText passEdit;
    private Button registrationButton;
    private ProgressDialog mProgress;

    FirebaseAuth mAuth;
    OnCompleteListener<AuthResult> mCreateAccountListener;
    OnCompleteListener<AuthResult> mLoginListener;
    DatabaseReference mDatabaseReference;

    // アカウント作成時にフラグを立て、ログイン処理後に名前をFirebaseに保存する
    boolean mIsCreateAccount = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("登録中...");

        nickEdit = (EditText) findViewById(R.id.nicknameEdit);
        manRdio = (RadioButton) findViewById(R.id.manRdio);
        womanRadio = (RadioButton) findViewById(R.id.womanRadio);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        bloodspinner = (Spinner) findViewById(R.id.bloodspinner);
        regionEdit = (EditText) findViewById(R.id.regionEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        bikehistoryEdit = (EditText) findViewById(R.id.bikehistoryEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passEdit = (EditText) findViewById(R.id.passEdit);
        // FirebaseAuthのオブジェクトを取得する
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // アカウント作成処理のリスナー
        mCreateAccountListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
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


        registrationButton = (Button) findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // キーボードが出てたら閉じる
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid());

                String nickName = nickEdit.getText().toString();
                String age = ageEdit.getText().toString();
                String region = regionEdit.getText().toString();
                String myBike = mybikeEdit.getText().toString();
                String bikeHistory = bikehistoryEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String pass = passEdit.getText().toString();

                mIsCreateAccount = true;
                createAccount(email, pass);

                Map<String, String> data = new HashMap<String, String>();

                data.put("nickName", nickName);
                data.put("age", age);
                data.put("region", region);
                data.put("myBike", myBike);
                data.put("bikeHistory", bikeHistory);
                data.put("email", email);
                data.put("pass", pass);

                mProgress.show();
                userRef.push().setValue(data);
            }
        });
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
}
