package jp.yasuhiro.katayama.biker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signUpActivity extends AppCompatActivity implements DatabaseReference.CompletionListener {

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



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nickEdit = (EditText) findViewById(R.id.nicknameEdit);
        manRdio = (RadioButton) findViewById(R.id.manRdio);
        womanRadio = (RadioButton) findViewById(R.id.womanRadio);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        bloodspinner = (Spinner) findViewById(R.id.bloodspinner);
        regionEdit = (EditText) findViewById(R.id.regionEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        mybikeEdit = (EditText) findViewById(R.id.mybikeEdit);
        bikehistoryEdit = (EditText) findViewById(R.id.bikehistoryEdit);
        emailEdit = (EditText) findViewById(R.id.emalEdit);
        passEdit = (EditText) findViewById(R.id.passEdit);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("登録中...");


        registrationButton = (Button) findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // キーボードが出てたら閉じる
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = mDatabaseReference.child(Const.UsersPATH);


                String nickName = nickEdit.getText().toString();
//                String age = ageEdit.getText().toString();
//                String region = regionEdit.getText().toString();
//                String myBike = mybikeEdit.getText().toString();
//                String bikeHistory = bikehistoryEdit.getText().toString();
//                String email = emailEdit.getText().toString();
//                String pass = passEdit.getText().toString();

                Map<String, String> data = new HashMap<String, String>();
                data.put("nickName", nickName);
//                data.put("age", age);
//                data.put("region", region);
//                data.put("myBike", myBike);
//                data.put("bikeHistory", bikeHistory);
//                data.put("email", email);
//                data.put("pass", pass);

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
}
