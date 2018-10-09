package com.example.rcjoshi.chanakyafoundation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RC Joshi on 24-09-2017.
 */

public class CustomAdapter extends ArrayAdapter<Marks> {

    public static final String TAG = "CustomListAdapter";
    private Context mContext;
    StudentDB mStDB;
    int mResource;
    String name="";
    int mks=0;
    String tid="";
    String sid="";
    int tot=0;

    static class ListViewHolder
    {
        TextView mStNameTextView;
        EditText mStMksEditText;
    };

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Marks> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }
/*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        name = getItem(position).getmStNameTest();
        mks = getItem(position).getmStudMarksTest();

        Marks marks = new Marks(name,mks);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        mStNameTextView = (TextView) convertView.findViewById(R.id.stnametestlistid);
        mStMksEditText = (EditText) convertView.findViewById(R.id.studmksid);

        mStNameTextView.setText(name);
        mStDB = new StudentDB(getContext(),"Marks",null,2);
        SQLiteDatabase mdbW = mStDB.getWritableDatabase();

        mStMksEditText.setText(mks);
        mks=mStMksEditText.getText().toString();
        //mStMksEditText.setText(mks);

        return convertView;
    }
*/

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        name = getItem(position).getmStNameTest();
        mks = Integer.parseInt(getItem(position).getmStudMarksTest());
        tid = getItem(position).getmTid();
        sid = getItem(position).getmStId();
        tot = Integer.parseInt(getItem(position).getmTot());
        mStDB = new StudentDB(getContext(),"Student",null,2);
        final SQLiteDatabase mdbW = mStDB.getWritableDatabase();
        final SQLiteDatabase mdbR = mStDB.getReadableDatabase();

        //Marks marks = new Marks(name,mks,tid);
        final ListViewHolder viewHolder;
        if (view == null)
        {
            viewHolder = new ListViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource,parent,false);
            view = inflater.inflate(R.layout.test_list_contents, null, true);
            viewHolder.mStNameTextView = (TextView) view.findViewById(R.id.stnametestlistid);
            viewHolder.mStMksEditText = (EditText) view.findViewById(R.id.studmksid);
            viewHolder.mStMksEditText.setText(String.valueOf(mks));
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ListViewHolder) view.getTag();
            // loadSavedValues();
        }
        viewHolder.mStNameTextView.setText(getItem(position).getmStNameTest());
        viewHolder.mStMksEditText.setId(position);
        /*if (selItems != null && selItems.get(position) != null)
        {
            viewHolder.mStMksEditText.setText(selItems.get(position));
        }
        else
        {
            viewHolder.mStMksEditText.setText(null);
        }*/
        // Add listener for edit text
        viewHolder.mStMksEditText
        .setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            String checkquery = "SELECT * FROM "+StudentDB.mStudTest;
            Cursor cursor = mdbR.rawQuery(checkquery,null);
            String mTid,mStMks,mSid;
            {
                if (hasFocus) {
                    //int itemIndex = v.getId();
                    int enteredMarks;
                    if (!viewHolder.mStMksEditText.getText().toString().equals(""))
                        enteredMarks= Integer.parseInt(viewHolder.mStMksEditText.getText().toString());
                    else
                        enteredMarks = 0;
                    name = getItem(position).getmStNameTest();
                    mks = Integer.parseInt(getItem(position).getmStudMarksTest());
                    tid = getItem(position).getmTid();
                    sid = getItem(position).getmStId();
                    tot = Integer.parseInt(getItem(position).getmTot());
                    if (enteredMarks<=tot)
                    {
                        ContentValues cv = new ContentValues();
                        cv.put("StMarks", enteredMarks);
                        mdbW.update(StudentDB.mStudTest, cv, "StId = " + sid + " AND TestId = " + tid, null);
                    }
                    else
                    {
                        ContentValues cv = new ContentValues();
                        cv.put("StMarks", 0);
                        mdbW.update(StudentDB.mStudTest, cv, "StId = " + sid + " AND TestId = " + tid, null);
                        viewHolder.mStMksEditText.setText("0");
                        Toast.makeText(mContext, "Please enter value less than total marks "+tot, Toast.LENGTH_SHORT).show();
                    }
                    //mTid = cursor.getString(cursor.getColumnIndex("TestId"));
                    //mSid = cursor.getString(cursor.getColumnIndex("StId"));
                    //mStMks = cursor.getString(cursor.getColumnIndex("StMarks"));
                    //Toast.makeText(mContext, "Nm: "+mStNm+"\nMks: "+mStMks, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(mContext, "Marks updated as\nStNm:" +
                    //        viewHolder.mStNameTextView.getText() + "\nStId:" + mSid + "\nTestId:" + mTid + "\nMarks:" + mStMks, Toast.LENGTH_SHORT).show();

                }
            }
            cursor.moveToFirst();
        }
        });
        return view;
    }
}

/*

class MyTextWatcher implements TextWatcher {

    private View view;
    private Context mContext;
    StudentDB mStDB;
    int mResource;
    String name="",mks="",tid="",sid="";

    static class ListViewHolder
    {
        TextView mStNameTextView;
        EditText mStMksEditText;
    };
    final ListViewHolder viewHolder;
    SQLiteDatabase mdbW = mStDB.getWritableDatabase();
    SQLiteDatabase mdbR = mStDB.getReadableDatabase();
    public MyTextWatcher(View view, Context mContext, ListViewHolder viewHolder) {
        this.view = view;
        this.mContext = mContext;
        this.viewHolder = viewHolder;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing
    }
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //do nothing
    }
    public void afterTextChanged(Editable s) {

        String enteredMarks = viewHolder.mStMksEditText.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put("StMarks",enteredMarks);
        mdbW.update(StudentDB.mStudTest,cv,"StId = "+sid+" AND TestId = "+tid,null);
        String joinquery = "SELECT * FROM "+StudentDB.mStudTest;
        Cursor cursor = mdbR.rawQuery(joinquery,null);
        String mTid,mStMks,mSid;
        mSid=cursor.getString(cursor.getColumnIndex("StId"));
        mStMks=cursor.getString(cursor.getColumnIndex("StMarks"));
        Toast.makeText(mContext, "Marks updated as\nStNm:"+
                viewHolder.mStNameTextView.getText()+"\nMarks:"+mStMks, Toast.LENGTH_SHORT).show();


        return;
    }
}

 */
