package com.example.rcjoshi.chanakyafoundation;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestResults extends Fragment {
    Button mSendResultbtn;
    ArrayList<CharSequence> mCourseStudList = new ArrayList<>();
    ListView mListView;
    //ArrayAdapter listadapter;
    CustomAdapter listadapter;
    Button mDispList;
    EditText mInputMks;
    StudentDB mStDB;
    Spinner mClasssp,mBatchsp,mBoardsp;
    Spinner mTestDatesp, mTestSubjsp, mTestTypesp;
    ArrayAdapter<String> mClassAd,mBoardAd,mBatchAd;
    ArrayAdapter<String> mTestDateAd, mTestSubjAd, mTestTypeAd;
    String mCourseid="",mTId="",mSid="";
    int flagIncomplete=0;
    public TestResults() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test_results, container, false);
        mStDB=new StudentDB(getContext(),"Student",null,2);
        //mStDB = new StudentDB(getContext(),"Courses",null,2);
        //mStDB = new StudentDB(getContext(),"Test",null,2);
       // mStDB = new StudentDB(getContext(),"Marks",null,2);

        //initialise widgets
        mDispList = (Button) v.findViewById(R.id.dispListbtn);
        mSendResultbtn = (Button) v.findViewById(R.id.sendattendbtnid);
        mListView = (ListView) v.findViewById(R.id.result_list);
        mInputMks = (EditText) v.findViewById(R.id.studmksid);
        //mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //set spinners
        mClasssp = (Spinner) v.findViewById(R.id.stdidRe);
        mBoardsp = (Spinner) v.findViewById(R.id.boardidRe);
        mBatchsp = (Spinner) v.findViewById(R.id.batchidRe);
        ArrayList<String> splist1 = this.getClassDetails("Class", mStDB);
        ArrayList<String> splist2 = this.getClassDetails("Board", mStDB);
        ArrayList<String> splist3 = this.getClassDetails("Batch", mStDB);
        mClassAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo1,splist1);
        mBoardAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo2,splist2);
        mBatchAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_courses, R.id.demo3,splist3);
        mClasssp.setAdapter(mClassAd);
        mBoardsp.setAdapter(mBoardAd);
        mBatchsp.setAdapter(mBatchAd);

        mTestDatesp = (Spinner) v.findViewById(R.id.dateidRe);
        mTestSubjsp = (Spinner) v.findViewById(R.id.subjidRe);
        mTestTypesp = (Spinner) v.findViewById(R.id.typeidRe);
        //ArrayList<String> splist4 = this.getTestDetails("Class",mStDB);
        //ArrayList<String> splist5 = this.getTestDetails("Board",mStDB);
        //ArrayList<String> splist6 = this.getTestDetails("Batch",mStDB);
        ArrayList<String> splist4 = this.getTestDetails("TestDate", mStDB);
        ArrayList<String> splist5 = this.getTestDetails("Subject", mStDB);
        ArrayList<String> splist6 = this.getTestDetails("Type", mStDB);
        mTestDateAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_test, R.id.demo4,splist4);
        mTestSubjAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_test, R.id.demo5,splist5);
        mTestTypeAd = new ArrayAdapter<>(getActivity().getBaseContext(), R.layout.contents_spinner_test, R.id.demo6,splist6);
        mTestDatesp.setAdapter(mTestDateAd);
        mTestSubjsp.setAdapter(mTestSubjAd);
        mTestTypesp.setAdapter(mTestTypeAd);

        if (splist1.isEmpty()||splist2.isEmpty()||splist3.isEmpty()
                ||splist4.isEmpty()||splist5.isEmpty()||splist6.isEmpty())
        {
            flagIncomplete=1;
        }
        else
        {
            flagIncomplete=0;
        }

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


        mTestDatesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mTestDatesp.getSelectedItem().toString();
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mTestTypesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mTestTypesp.getSelectedItem().toString();
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });

        mTestSubjsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String std = mTestSubjsp.getSelectedItem().toString();
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
                String mTypeSel,mTDateSel,mTSubjSel;

                SQLiteDatabase sdb = mStDB.getReadableDatabase();
                SQLiteDatabase cdb = mStDB.getReadableDatabase();
                SQLiteDatabase tdb = mStDB.getReadableDatabase();
                SQLiteDatabase mdb = mStDB.getWritableDatabase();
                if(flagIncomplete!=1) {
                    mClsSel = mClasssp.getSelectedItem().toString();
                    mBoardSel = mBoardsp.getSelectedItem().toString();
                    mBatchSel = mBatchsp.getSelectedItem().toString();
                    mTypeSel = mTestTypesp.getSelectedItem().toString();
                    mTDateSel = mTestDatesp.getSelectedItem().toString();
                    mTSubjSel = mTestSubjsp.getSelectedItem().toString();

                    Cursor curCId;
                    String selectCId = "SELECT * FROM Courses";
                    curCId = cdb.rawQuery(selectCId, null);
                    if (curCId.getCount() > 0) {
                        while (curCId.moveToNext()) {
                            if (mClsSel.equals(curCId.getString(curCId.getColumnIndex("Class"))) &&
                                    mBoardSel.equals(curCId.getString(curCId.getColumnIndex("Board"))) &&
                                    mBatchSel.equals(curCId.getString(curCId.getColumnIndex("Batch")))
                                    ) {
                                mCourseid = curCId.getString(0);
                            }
                        }
                    }
                    //cdb.setTransactionSuccessful();
                    //cdb.close();

                    if (mCourseid != "") {
                        Cursor curTId;
                        String selectTId = "SELECT * FROM " + StudentDB.mTestDetails;
                        curTId = tdb.rawQuery(selectTId, null);
                        if (curCId.getCount() > 0) {
                            while (curTId.moveToNext()) {
                                if (mTypeSel.equals(curTId.getString(curTId.getColumnIndex("Type"))) &&
                                        mTDateSel.equals(curTId.getString(curTId.getColumnIndex("TestDate"))) &&
                                        mTSubjSel.equals(curTId.getString(curTId.getColumnIndex("Subject"))) &&
                                        curTId.getString(6).equals(mCourseid)
                                        ) {
                                    mTId = curTId.getString(0);
                                }
                            }
                        }
                        //tdb.close();

                        if (mTId != "") {
                            int flagUnique = 1;
                            String selectquery = "SELECT DISTINCT StId FROM Personal_Details WHERE CourseId=" + mCourseid;
                            Cursor cursor = sdb.rawQuery(selectquery, null);
                            if (cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {
                                    flagUnique=1;
                                    mSid=cursor.getString(cursor.getColumnIndex("StId"));
                                    ContentValues cvMarks = new ContentValues();
                                    cvMarks.put("TestId", mTId);
                                    cvMarks.put("StId", mSid);
                                    cvMarks.put("StMarks", 0);
                                    //Toast.makeText(getContext(), "Tid:"+mTId+"\nSid:"+mSid, Toast.LENGTH_SHORT).show();
                                    String checkquery = "SELECT TestId,StId FROM " + StudentDB.mStudTest;
                                    Cursor cUnique = mdb.rawQuery(checkquery, null);
                                    if (cUnique.getCount() > 0) {
                                        while (cUnique.moveToNext()) {
                                            if ((cUnique.getString(cUnique.getColumnIndex("StId")).equals
                                                    (mSid))
                                                    && (cUnique.getString(cUnique.getColumnIndex("TestId")).equals
                                                    (mTId))) {
                                                flagUnique = 0;
                                                //Toast.makeText(getContext(), "Tid:"+cUnique.getString(cUnique.getColumnIndex("TestId"))+"\nSid:"+cUnique.getString(cUnique.getColumnIndex("StId")), Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        }
                                    }
                                    if (flagUnique == 1) {
                                        mdb.insert(StudentDB.mStudTest, "TestId", cvMarks);
                                        Cursor curStMks = mdb.query(StudentDB.mStudTest, null, null, null, null, null, null);
                                        curStMks.moveToLast();
                                        //Toast.makeText(getContext(), "Student Marks inserted as "+ curStMks.getString(2), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                            mdb.close();
                            ArrayList<Marks> mStList = new ArrayList<Marks>();
                            mStList = DispStList(getContext(), mStDB, mCourseid, mTId);
                            listadapter = new CustomAdapter(getContext(),
                                    R.layout.test_list_contents, mStList);
                            mListView.setAdapter(listadapter);
                            String checkquery = "SELECT * FROM "+StudentDB.mStudTest;
                            /*SQLiteDatabase mdbR = mStDB.getReadableDatabase();
                            Cursor chkcur = mdbR.rawQuery(checkquery,null);
                            chkcur.moveToFirst();
                            while (chkcur.moveToNext())
                            {
                                String mTid,mStMks,mSId;
                                mTid = cursor.getString(cursor.getColumnIndex("TestId"));
                                mSId = cursor.getString(cursor.getColumnIndex("StId"));
                                mStMks = cursor.getString(cursor.getColumnIndex("StMarks"));
                                //Toast.makeText(mContext, "Nm: "+mStNm+"\nMks: "+mStMks, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Marks updated as\nStId:" + mSId + "\nTestId:" + mTid + "\nMarks:" + mStMks, Toast.LENGTH_SHORT).show();

                            }
                            */
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Invalid Test for selected Course", Toast.LENGTH_SHORT).show();
                        }
                        sdb.close();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                        sdb.close();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Invalid Course or test", Toast.LENGTH_SHORT).show();
                    sdb.close();
                }
            }

        });

        mSendResultbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sdb = mStDB.getReadableDatabase();
                ArrayList<String> mParNos = new ArrayList<String>();
                if (mCourseid!="" && mTId!="")
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 7);
                        return;
                    }
                    Cursor curPer,curTest;
                    //String selectCId = "SELECT * FROM Personal_Details";
                    String selectCId = "SELECT A.StudName,A.ParMob,B.StId,B.TestId,B.StMarks FROM " + StudentDB.mStudPer +
                            " AS A INNER JOIN " + StudentDB.mStudTest + " AS B ON B.StId=A.StId AND B.TestId=" +
                            mTId + " AND A.CourseId=" + mCourseid + " ORDER BY B.StMarks DESC";
                    curPer = sdb.rawQuery(selectCId, null);

                    String testSelect = "SELECT * FROM "+StudentDB.mTestDetails;
                    curTest = sdb.rawQuery(testSelect,null);

                    if (curPer.getCount() > 0)
                    {
                        String detail = "",mks="",nm="",maxmks="",tot="",dt="",mtid="",maxnm="";
                        curPer.moveToFirst();
                        maxmks = curPer.getString(curPer.getColumnIndex("StMarks"));
                        maxnm = curPer.getString(curPer.getColumnIndex("StudName"));
                        curPer.moveToFirst();
                        while (curPer.moveToNext())
                        {
                            detail = curPer.getString(curPer.getColumnIndex("ParMob"));
                            nm = curPer.getString(curPer.getColumnIndex("StudName"));
                            mtid = curPer.getString(curPer.getColumnIndex("TestId"));
                            mks = curPer.getString(curPer.getColumnIndex("StMarks"));
                            while (curTest.moveToNext())
                            {
                                if (mtid.equals(curTest.getString(curTest.getColumnIndex("TestId"))))
                                {
                                    tot = curTest.getString(curTest.getColumnIndex("TotMks"));
                                    dt = curTest.getString(curTest.getColumnIndex("TestDate"));
                                }
                            }
                            //mParNos.add(detail);
                            SmsManager sms = SmsManager.getDefault();
                            String text = "TEST RESULT:\nYour ward "+nm+" scored "+mks+" out of "+tot+
                                    " in test conducted on "+dt+"\nHighest marks: "+maxmks+
                                    " scored by "+maxnm+"\nChanakya Foundation";
                            sms.sendTextMessage(detail, null, text, null, null);
                        }
                        //Toast.makeText(getContext(), detail, Toast.LENGTH_SHORT).show();
                        curPer.moveToFirst();
                    }
                    //cdb.setTransactionSuccessful();
                    sdb.close();
                    Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Failed to get details", Toast.LENGTH_SHORT).show();
                }

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

    public static ArrayList<String> getTestDetails(String mTestDetail, StudentDB mspTest)
    {
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase db = mspTest.getReadableDatabase();
        db.beginTransaction();
        try
        {
            String selectquery = "SELECT DISTINCT "+ mTestDetail + " FROM Test_Details ORDER BY "+mTestDetail;
            Cursor cursor = db.rawQuery(selectquery,null);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    String detail = cursor.getString(cursor.getColumnIndex(mTestDetail));
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


    public static ArrayList<Marks> DispStList(Context c,StudentDB mStDB, String mCourseid, String mTId)
    {
        ArrayList<Marks> list = new ArrayList<Marks>();
        SQLiteDatabase db = mStDB.getReadableDatabase();

        String mSid,mStMks,mStNm;
        db.beginTransaction();
        try
        {
            int mLocalRno=1;
            //String selectquery = "SELECT DISTINCT StudName FROM Personal_Details WHERE CourseId="+mCourseid+" ORDER BY StudName";
            String joinquery = "SELECT A.StudName,B.StId,B.TestId,B.StMarks FROM "+StudentDB.mStudPer+
                    " AS A INNER JOIN "+StudentDB.mStudTest+" AS B ON B.StId=A.StId AND B.TestId="+
                    mTId+" AND A.CourseId="+mCourseid+" ORDER BY A.StudName";
            String mTot="";
            Cursor cursor = db.rawQuery(joinquery,null);
            Cursor curTest;
            String testSelect = "SELECT * FROM "+StudentDB.mTestDetails;
            curTest = db.rawQuery(testSelect,null);
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    mStNm=cursor.getString(cursor.getColumnIndex("StudName"));
                    mStMks=cursor.getString(cursor.getColumnIndex("StMarks"));
                    mSid=cursor.getString(cursor.getColumnIndex("StId"));
                    while (curTest.moveToNext())
                    {
                        if (mTId.equals(curTest.getString(curTest.getColumnIndex("TestId"))))
                        {
                            mTot = curTest.getString(curTest.getColumnIndex("TotMks"));
                        }
                    }
                    //String detail = mLocalRno+".    "+cursor.getString(cursor.getColumnIndex("StudName"));
                    //String detail = cursor.getString(cursor.getColumnIndex("StudName"));
                    //Marks detail = new Marks("",cursor.getString(cursor.getColumnIndex("StudName")),"",mTId);
                    Marks detail = new Marks(mSid,mStNm,mStMks,mTId,mTot);
                    //Toast.makeText(c, "Nm: "+mStNm+"\nMks: "+mStMks, Toast.LENGTH_SHORT).show();
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
