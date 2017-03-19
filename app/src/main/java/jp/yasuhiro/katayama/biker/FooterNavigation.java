package jp.yasuhiro.katayama.biker;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import static jp.yasuhiro.katayama.biker.R.id.footerNavMap;

/**
 * Created by katayama on 2017/03/17.
 */

public class FooterNavigation {


    public static void createFooterNav(View.OnClickListener footerNav, Activity setActivity) {
        ImageView mNavProfile;
        ImageView mNavChat;
        ImageView mNavMap;
        ImageView mNavMyPage;

        mNavProfile = (ImageView) setActivity.findViewById(R.id.footerNavProfile);
        mNavChat = (ImageView) setActivity.findViewById(R.id.footerNavChat);
        mNavMap = (ImageView) setActivity.findViewById(footerNavMap);
        mNavMyPage = (ImageView) setActivity.findViewById(R.id.footerNavMyPage);

        mNavProfile.setOnClickListener(footerNav);
        mNavChat.setOnClickListener(footerNav);
        mNavMap.setOnClickListener(footerNav);
        mNavMyPage.setOnClickListener(footerNav);

    }

    public static ImageView getFooterNav(int id, Activity setActivity){
        return (ImageView)setActivity.findViewById(id);
    }

    public static void moveFooterNav(View v, Activity activity){
        if(v == getFooterNav(R.id.footerNavProfile,activity)){
            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
            activity.startActivity(intent);
        }else if(v == getFooterNav(R.id.footerNavChat,activity)){
            Intent intent = new Intent(activity.getApplicationContext(), ChatListActivity.class);
            activity.startActivity(intent);
        }else if(v == getFooterNav(footerNavMap,activity)){
            Intent intent = new Intent(activity.getApplicationContext(), MapsActivity.class);
            activity.startActivity(intent);
        }else if(v == getFooterNav(R.id.footerNavMyPage,activity)){
            Intent intent = new Intent(activity.getApplicationContext(), MyPageActivity.class);
            activity.startActivity(intent);
        }
    }
}
