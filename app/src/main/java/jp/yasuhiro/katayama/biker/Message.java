package jp.yasuhiro.katayama.biker;

/**
 * Created by katayama on 2017/03/03.
 */

public class Message {
    private String message;
    private String nickName;
    private byte[] mBitmapArray;
    public byte[] getmBitmapArray() {
        return mBitmapArray;
    }

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    Message() {
    }

    Message(String message) {
        this.message = message;
    }

    Message(String message, String nickName, byte[] bytes) {
        this.message = message;
        this.nickName = nickName;
        this.mBitmapArray = bytes.clone();
    }

    public String getMessage() {
        return message;
    }

    public String getNickName() {
        return nickName;
    }
}
