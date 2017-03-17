package jp.yasuhiro.katayama.biker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by katayama on 2017/02/18.
 */

public class AccountDetailAdapter extends BaseAdapter{
    private final static int TYPE_QUESTION = 0;
    private final static int TYPE_ANSWER = 1;
    private LayoutInflater mlayoutInflater = null;
    private Account mAccount;

    public AccountDetailAdapter(Context context, Account account) {
        mlayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAccount = account;
    }

    @Override
    public int getCount(){
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position){
        return mAccount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mlayoutInflater.inflate(R.layout.list_account, parent, false);
        }

        String nickName = mAccount.getmNickName();


        TextView mNickName = (TextView) convertView.findViewById(R.id.nickName);
        mNickName.setText(nickName);


        byte[] bytes = mAccount.getmBitmapArray();
        if (bytes.length != 0) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
            ImageView mProfileImage = (ImageView) convertView.findViewById(R.id.mProfileImage);
            mProfileImage.setImageBitmap(image);
        }

        return convertView;
    }
}
