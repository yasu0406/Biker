package jp.yasuhiro.katayama.biker;

import java.io.Serializable;

/**
 * Created by katayama on 2017/03/03.
 */

public class Chat implements Serializable {
    private String title;
    private String message;
    private String chatUid;
    private String nickName;
    private String uid;
    private byte[] mBitmapArray;

    public Chat(String title, String nickName) {
        this.title = title;
        this.nickName = nickName;
    }

    public Chat(String title, String chatUid, String nickName, String uid, byte[] bytes) {
        this.title = title;
        this.chatUid = chatUid;
        this.nickName = nickName;
        this.uid = uid;
        this.mBitmapArray = bytes.clone();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatUid() {
        return chatUid;
    }

    public String getUid() {
        return uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public byte[] getmBitmapArray() {
        return mBitmapArray;
    }
}
