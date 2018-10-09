package com.example.rcjoshi.chanakyafoundation;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notice extends Fragment {
    StudentDB mStDB;
    ArrayList<CharSequence> mStList = new ArrayList<CharSequence>();;
    View mSendNoticebtn;
    EditText mCircular;
    Spinner mClassspN, mBatchspN, mBoardspN;
    ArrayAdapter<String> mClassAdN,mBoardAdN,mBatchAdN;
    int flagIncomplete = 0;

    public Notice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notice, container, false);
        mStDB=new StudentDB(getContext(),"Student",null,2);

        //setting spinners
        mClassspN = (Spinner) v.findViewById(R.id.stdidN);
        mBoardspN = (Spinner) v.findViewById(R.id.boardidN);
        mBatchspN = (Spinner) v.findViewById(R.id.batchidN);
        ArrayList<String> splist1 = this.getClassDetails("Class", mStDB);
        ArrayList<String> splist2 = this.getClassDetails("Board", mStDB);
        ArrayList<String> splist3 = this.getClassDetails("Batch", mStDB);
        mClassAdN = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo1,splist1);
        mBoardAdN = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo2,splist2);
        mBatchAdN = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo3,splist3);
        mClassspN.setAdapter(mClassAdN);
        mBoardspN.setAdapter(mBoardAdN);
        mBatchspN.setAdapter(mBatchAdN);

        mClassspN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mClassspN.getSelectedItem().toString();
                Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mBoardspN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mBoardspN.getSelectedItem().toString();
                Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mBatchspN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mBatchspN.getSelectedItem().toString();
                Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });


        //Initialising button and editText
        mSendNoticebtn = v.findViewById(R.id.sendnoticebtnid);
        mCircular = (EditText) v.findViewById(R.id.circularid);

        //mCircular.setText("\n\nChanakya Foundation");
        mSendNoticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mClsSel,mBoardSel,mBatchSel;
                int mCourseid=0;
                SQLiteDatabase sdb = mStDB.getReadableDatabase();
                SQLiteDatabase cdb = mStDB.getReadableDatabase();
                mClsSel = mClassspN.getSelectedItem().toString();
                mBoardSel = mBoardspN.getSelectedItem().toString();
                mBatchSel = mBatchspN.getSelectedItem().toString();

                Cursor curCId;
                String selectCId = "SELECT * FROM Courses";
                curCId = cdb.rawQuery(selectCId,null);
                if (curCId.getCount() > 0)
                {
                    while (curCId.moveToNext())
                    {
                        if (mClsSel.equals(curCId.getString(curCId.getColumnIndex("Class")))&&
                                mBoardSel.equals(curCId.getString(curCId.getColumnIndex("Board"))) &&
                                mBatchSel.equals(curCId.getString(curCId.getColumnIndex("Batch")))
                                )
                        {
                            mCourseid = Integer.parseInt(curCId.getString(0));
                        }
                    }
                }
                //cdb.setTransactionSuccessful();
                //cdb.close();

                if (mCourseid!=0)
                {
                    mStList = GetStList(mStDB,mCourseid);
                }
                else
                {
                    Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                    sdb.close();
                }
                //sdb = mStDB.getReadableDatabase();
                //ArrayList<String> mParNos = new ArrayList<String>();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},7);
                    return;
                }
                for (CharSequence mParMobNo:mStList)
                {
                    SmsManager sms=SmsManager.getDefault();
                    String detail= (String) mParMobNo;
                    sms.sendTextMessage(detail,null,mCircular.getText().toString(),null,null);
                    Toast.makeText(getContext(), detail, Toast.LENGTH_SHORT).show();
                }
                sdb.close();
                Toast.makeText(getContext(), "Messgae Sent", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public static ArrayList<CharSequence> GetStList(StudentDB mStDB, int mCourseid)
    {
        ArrayList<CharSequence> list = new ArrayList<CharSequence>();
        SQLiteDatabase db = mStDB.getReadableDatabase();
        db.beginTransaction();
        try
        {
            String selectquery = "SELECT DISTINCT ParMob FROM Personal_Details WHERE CourseId="+mCourseid;
            Cursor cursor = db.rawQuery(selectquery,null);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String detail = cursor.getString(cursor.getColumnIndex("ParMob"));
                    list.add(detail);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }


    public static ArrayList<String> getClassDetails(String mClassDetail, StudentDB mspCourse)
    {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = mspCourse.getReadableDatabase();
        db.beginTransaction();
        try
        {
            String selectquery = "SELECT DISTINCT "+ mClassDetail + " FROM Courses ORDER BY "+mClassDetail;
            Cursor cursor = db.rawQuery(selectquery,null);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String detail = cursor.getString(cursor.getColumnIndex(mClassDetail));
                    list.add(detail);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }

}
