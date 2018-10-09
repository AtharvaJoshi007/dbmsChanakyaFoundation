package com.example.rcjoshi.chanakyafoundation;

import android.widget.EditText;

/**
 * Created by RC Joshi on 24-09-2017.
 */

public class Marks {
    private String mStId;
    private String mStNameTest;
    private String mStudMarksTest;
    private String mTid;
    private String mTot;

    public Marks(String mStId, String mStNameTest, String mStudMarksTest, String mTid, String mTot) {
        this.mStId = mStId;
        this.mStNameTest = mStNameTest;
        this.mStudMarksTest = mStudMarksTest;
        this.mTid = mTid;
        this.mTot = mTot;
    }

    public String getmStId() {
        return mStId;
    }

    public void setmStId(String mStId) {
        this.mStId = mStId;
    }

    public String getmStNameTest() {
        return mStNameTest;
    }

    public void setmStNameTest(String mStNameTest) {
        this.mStNameTest = mStNameTest;
    }

    public String getmStudMarksTest() {
        return mStudMarksTest;
    }

    public void setmStudMarksTest(String mStudMarksTest) {
        this.mStudMarksTest = mStudMarksTest;
    }

    public String getmTid() {
        return mTid;
    }

    public void setmTid(String mTid) {
        this.mTid = mTid;
    }

    public String getmTot() {
        return mTot;
    }

    public void setmTot(String mTot) {
        this.mTot = mTot;
    }
}
