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

import static jp.yasuhiro.katayama.biker.R.id.message;
import static jp.yasuhiro.katayama.biker.R.id.nickName;

/**
 * Created by katayama on 2017/03/04.
 */

public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater mlayoutInflater = null;
    private ArrayList<Message> mMessageArrayList;
    private Message mMsseage;

    public MessageListAdapter(Context context) {
        mlayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mMessageArrayList.size();
    }

    @Override
    public Object getItem(int position){
        return mMessageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mlayoutInflater.inflate(R.layout.list_chat_detail, parent, false);
        }

        TextView mNickName = (TextView) convertView.findViewById(nickName);
        mNickName.setText(mMessageArrayList.get(position).getNickName());


        TextView mMessage = (TextView) convertView.findViewById(message);
        mMessage.setText(mMessageArrayList.get(position).getMessage());

        byte[] bytes = mMessageArrayList.get(position).getmBitmapArray();
        if (bytes.length != 0) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
            ImageView mProfileImage = (ImageView) convertView.findViewById(R.id.mProfileImage);
            mProfileImage.setImageBitmap(image);
        }

        return convertView;
    }
    public void setMessagesArrayList(ArrayList<Message> messagesArrayList) {
        mMessageArrayList = messagesArrayList;
    }
}
