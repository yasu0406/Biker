package jp.yasuhiro.katayama.biker;

import java.io.Serializable;

/**
 * Created by katayama on 2017/02/06.
 */

public class Account implements Serializable {
    private String mNickName;
    private String mSex;
    private String mAge;
    private String mBlood;
    private String mRegion;
    private String mMyBike;
    private String mBikeHistory;
    private String mEmail;
    private String mPassWord;
    private String uid;
    private byte[] mBitmapArray;
    private String mIntroduction;

    public Account(String mNickName) {
        this.mNickName = mNickName;
    }
    public Account(byte[] bytes) {
        this.mBitmapArray = bytes.clone();
    }

    public Account(String nickname, String sex, String age, String blood, String region, String myBike, String bikeHistory, byte[] bytes, String introduction, String uid) {
        this.mNickName = nickname;
        this.mSex = sex;
        this.mAge = age;
        this.mBlood = blood;
        this.mRegion = region;
        this.mMyBike = myBike;
        this.mBikeHistory = bikeHistory;
        this.mBitmapArray = bytes.clone();
        this.mIntroduction = introduction;
        this.uid = uid;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmBlood() {
        return mBlood;
    }

    public void setmBlood(String mBlood) {
        this.mBlood = mBlood;
    }

    public String getmRegion() {
        return mRegion;
    }

    public void setmRegion(String mRegion) {
        this.mRegion = mRegion;
    }

    public String getmMyBike() {
        return mMyBike;
    }

    public void setmMyBike(String mMyBike) {
        this.mMyBike = mMyBike;
    }

    public String getmBikeHistory() {
        return mBikeHistory;
    }

    public void setmBikeHistory(String mBikeHistory) {
        this.mBikeHistory = mBikeHistory;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassWord() {
        return mPassWord;
    }

    public void setmPassWord(String mPassWord) {
        this.mPassWord = mPassWord;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public byte[] getmBitmapArray() {
        return mBitmapArray;
    }

    public void setmBitmapArray(byte[] mBitmapArray) {
        this.mBitmapArray = mBitmapArray;
    }

    public String getmIntroduction() {
        return mIntroduction;
    }

    public void setmIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
    }
}
