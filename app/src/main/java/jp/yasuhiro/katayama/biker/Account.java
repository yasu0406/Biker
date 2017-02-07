package jp.yasuhiro.katayama.biker;

import java.io.Serializable;

/**
 * Created by katayama on 2017/02/06.
 */

public class Account implements Serializable {
    private String mNickName;
    private String mMan;
    private String mWoman;
    private int mAge;
    private String mBlood;
    private String mRegion;
    private String mMyBike;
    private String mBikeHistory;
    private String mEmail;
    private String mPassWord;

    public Account(String mNickName, String mMan, String mWoman, int mAge, String mBlood, String mRegion, String mMyBike, String mBikeHistory, String mEmail, String mPassWord) {
        this.mNickName = mNickName;
        this.mMan = mMan;
        this.mWoman = mWoman;
        this.mAge = mAge;
        this.mBlood = mBlood;
        this.mRegion = mRegion;
        this.mMyBike = mMyBike;
        this.mBikeHistory = mBikeHistory;
        this.mEmail = mEmail;
        this.mPassWord = mPassWord;
    }

    public String getmNickName() {
        return mNickName;
    }

    public void setmNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getmMan() {
        return mMan;
    }

    public void setmMan(String mMan) {
        this.mMan = mMan;
    }

    public String getmWoman() {
        return mWoman;
    }

    public void setmWoman(String mWoman) {
        this.mWoman = mWoman;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
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
}
