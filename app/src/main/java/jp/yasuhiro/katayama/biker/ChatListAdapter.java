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

import java.util.ArrayList;

import static jp.yasuhiro.katayama.biker.R.id.nickName;
import static jp.yasuhiro.katayama.biker.R.id.titleTextView;

/**
 * Created by katayama on 2017/03/04.
 */

public class ChatListAdapter extends BaseAdapter {
    private LayoutInflater mlayoutInflater = null;
    private ArrayList<Chat> mChatArrayList;
    private ArrayList<Account> mAccountArrayList;

    public ChatListAdapter(Context context) {
        mlayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mChatArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return mChatArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mlayoutInflater.inflate(R.layout.list_chat, parent, false);
        }

        TextView mNickName = (TextView) convertView.findViewById(nickName);
        mNickName.setText(mChatArrayList.get(position).getNickName());

        TextView mTitle = (TextView) convertView.findViewById(titleTextView);
        mTitle.setText(mChatArrayList.get(position).getTitle());

        byte[] bytes = mChatArrayList.get(position).getmBitmapArray();
        if (bytes.length != 0) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
            ImageView mProfileImage = (ImageView) convertView.findViewById(R.id.mProfileImage);
            mProfileImage.setImageBitmap(image);
        }

        return convertView;
    }
    public void setmChatArrayList(ArrayList<Chat> chatArrayList) {
        mChatArrayList = chatArrayList;
    }
}
