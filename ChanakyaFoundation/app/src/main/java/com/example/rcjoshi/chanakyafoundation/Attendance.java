package com.example.rcjoshi.chanakyafoundation;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance extends Fragment {
    Button mSendAttendbtn;
    ArrayList<CharSequence> mCourseStudList;
    ListView mListView;
    ArrayAdapter listadapter;
    Button mDispList;
    StudentDB mStDB;
    Spinner mClasssp,mBatchsp,mBoardsp;
    ArrayAdapter<String> mClassAd,mBoardAd,mBatchAd;
    int flagIncomplete=0;
    public Attendance() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance, container, false);
        mStDB=new StudentDB(getContext(),"Student",null,2);

        //initialise widgets
        mDispList = (Button) v.findViewById(R.id.dispListbtn);
        mSendAttendbtn = (Button) v.findViewById(R.id.sendattendbtnid);
        mListView = (ListView) v.findViewById(R.id.stud_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //set spinners
        mClasssp = (Spinner) v.findViewById(R.id.stdidAt);
        mBoardsp = (Spinner) v.findViewById(R.id.boardidAt);
        mBatchsp = (Spinner) v.findViewById(R.id.batchidAt);
        ArrayList<String> splist1 = this.getClassDetails("Class", mStDB);
        ArrayList<String> splist2 = this.getClassDetails("Board", mStDB);
        ArrayList<String> splist3 = this.getClassDetails("Batch", mStDB);
        if (splist1.isEmpty()||splist2.isEmpty()||splist3.isEmpty())
        {
            flagIncomplete=1;
        }
        else
        {
            flagIncomplete=0;
        }
        mClassAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo1,splist1);
        mBoardAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo2,splist2);
        mBatchAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo3,splist3);
        mClasssp.setAdapter(mClassAd);
        mBoardsp.setAdapter(mBoardAd);
        mBatchsp.setAdapter(mBatchAd);


        mClasssp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mClasssp.getSelectedItem().toString();
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mBoardsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mBoardsp.getSelectedItem().toString();
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mBatchsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mBatchsp.getSelectedItem().toString();
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        //check correct spinner input
        mDispList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mClsSel,mBoardSel,mBatchSel;
                int mCourseid=0;
                SQLiteDatabase sdb = mStDB.getReadableDatabase();
                SQLiteDatabase cdb = mStDB.getReadableDatabase();
                if (flagIncomplete==0)
                {
                    mClsSel = mClasssp.getSelectedItem().toString();
                    mBoardSel = mBoardsp.getSelectedItem().toString();
                    mBatchSel = mBatchsp.getSelectedItem().toString();
                    Snackbar.make(v, "Mark Absent Students", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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
                }

                if (mCourseid!=0)
                {
                    ArrayList<CharSequence> mStList = new ArrayList<CharSequence>();
                    mCourseStudList = new ArrayList<>();
                    mStList = DispStList(mStDB,mCourseid);
                    listadapter = new ArrayAdapter<CharSequence>(getContext(),
                            R.layout.attendance_checked_rowlayout,R.id.chk_absent,mStList);
                    mListView.setAdapter(listadapter);
                    //listadapter.notifyDataSetChanged();
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CharSequence mSelItems = ((TextView)view).getText().toString();
                            if (mCourseStudList.contains(mSelItems))
                            {
                                mCourseStudList.remove(mSelItems);
                            }
                            else
                            {
                                mCourseStudList.add(mSelItems);
                                //Toast.makeText(getContext(), mSelItems+" added to absent", Toast.LENGTH_SHORT).show();
                            }
                            //listadapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                    sdb.close();
                }
            }

        });

        mSendAttendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdb = mStDB.getReadableDatabase();
                ArrayList<String> mParNos = new ArrayList<String>();
                Cursor curAbStName;
                String selectCId = "SELECT * FROM Personal_Details";
                curAbStName = sdb.rawQuery(selectCId,null);
                if (curAbStName.getCount() > 0)
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},7);
                        return;
                    }
                    for (CharSequence mAbName:mCourseStudList)
                    {
                        String detail="";
                        SmsManager sms=SmsManager.getDefault();
                        String text = "ATTENDANCE:\nYour ward did not attend today's lectures\nChanakya Foundation";
                        while (curAbStName.moveToNext())
                        {
                            if (mAbName.toString().equals(curAbStName.
                                    getString(curAbStName.getColumnIndex("StudName")))
                                    ) {
                                detail = curAbStName.getString(curAbStName.getColumnIndex("ParMob"));
                                mParNos.add(detail);
                                Toast.makeText(getContext(), detail, Toast.LENGTH_SHORT).show();
                                sms.sendTextMessage(detail,null,text,null,null);
                                break;
                            }
                        }

                        curAbStName.moveToFirst();
                    }
                }
                //cdb.setTransactionSuccessful();
                sdb.close();
                Toast.makeText(getContext(), "Messgae Sent", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
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

    public static ArrayList<CharSequence> DispStList(StudentDB mStDB, int mCourseid)
    {
        ArrayList<CharSequence> list = new ArrayList<CharSequence>();
        SQLiteDatabase db = mStDB.getReadableDatabase();
        db.beginTransaction();
        try
        {
            int mLocalRno=1;
            String selectquery = "SELECT DISTINCT StudName FROM Personal_Details WHERE CourseId="+mCourseid+" ORDER BY StudName";
            Cursor cursor = db.rawQuery(selectquery,null);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    //String detail = mLocalRno+".    "+cursor.getString(cursor.getColumnIndex("StudName"));
                    String detail = cursor.getString(cursor.getColumnIndex("StudName"));
                    list.add(detail);
                    mLocalRno++;
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
