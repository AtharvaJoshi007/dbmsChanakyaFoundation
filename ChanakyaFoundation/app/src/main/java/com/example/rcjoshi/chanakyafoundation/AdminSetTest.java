package com.example.rcjoshi.chanakyafoundation;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.rcjoshi.chanakyafoundation.R.id.addCoursebtnid;


public class AdminSetTest extends Fragment implements View.OnClickListener{

    StudentDB mStDB;
    ArrayList<CharSequence> mStList = new ArrayList<CharSequence>();
    Button mAddTest,mDelTest;
    EditText mInsertDate, mInsertSubj, mInsertType, mInsertTotal, mInsertChap;
    Spinner mClassspN, mBatchspN, mBoardspN;
    ArrayAdapter<String> mClassAdN,mBoardAdN,mBatchAdN;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat dateFormatter;

    int flagIncomplete=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v = inflater.inflate(R.layout.activity_admin_set_test, container, false);

        mStDB = new StudentDB(getContext(),"Student",null,2);

        mAddTest = (Button) v.findViewById(R.id.addTestbtnid);
        mDelTest = (Button) v.findViewById(R.id.deltestbtnid);
        mInsertSubj = (EditText) v.findViewById(R.id.testSubjectid);
        mInsertType = (EditText) v.findViewById(R.id.testTypeid);
        mInsertTotal = (EditText) v.findViewById(R.id.testTotMksid);
        mInsertChap = (EditText) v.findViewById(R.id.testchaptersid);
        mInsertDate = (EditText) v.findViewById(R.id.testDateid);
        mInsertDate.setInputType(InputType.TYPE_NULL);
        mInsertDate.requestFocus();

        dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.US);
        setDateTimeField();

        //setting spinners
        mClassspN = (Spinner) v.findViewById(R.id.stdidTest);
        mBoardspN = (Spinner) v.findViewById(R.id.boardidTest);
        mBatchspN = (Spinner) v.findViewById(R.id.batchidTest);
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
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getContext(), std, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                flagIncomplete=1;
            }
        });


        mAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInsertDate.getText().toString().equals("")|| mInsertSubj.getText().toString().equals("")||
                        mInsertType.getText().toString().equals("")||
                        mInsertTotal.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please Enter all Required Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mClsSel,mBoardSel,mBatchSel;
                    int mCourseid=0;
                    SQLiteDatabase tdb = mStDB.getWritableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();
                    mClsSel = mClassspN.getSelectedItem().toString();
                    mBoardSel = mBoardspN.getSelectedItem().toString();
                    mBatchSel = mBatchspN.getSelectedItem().toString();
                    //s = mClasssp.getSelectedItem().toString();

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

                    if (mCourseid!=0)
                    {
                        String mstype="",msdt="",mssub="",mschap="",mstot="";
                        mstype = mInsertType.getText().toString();
                        msdt = mInsertDate.getText().toString();
                        mssub = mInsertSubj.getText().toString();
                        mschap = mInsertChap.getText().toString();
                        mstot = mInsertTotal.getText().toString();

                            ContentValues cvTestDetails = new ContentValues();
                            cvTestDetails.put("Type", mstype);
                            cvTestDetails.put("TestDate", msdt);
                            cvTestDetails.put("Subject", mssub);
                            cvTestDetails.put("Chapter", mschap);
                            cvTestDetails.put("TotMks", mstot);
                            cvTestDetails.put("CourseId", mCourseid);

                            tdb.insert(StudentDB.mTestDetails, "Type", cvTestDetails);

                        Cursor cur=tdb.query(StudentDB.mTestDetails,null,null,null,null,null,null);
                        cur.moveToLast();
                        //mTestno=cur.getString(0);
                        Toast.makeText(getContext(), "Record inserted as ID "+cur.getString(0), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "TestType: "+cur.getString(1) + "\nTestDate: "+cur.getString(2) +
                                        "\nTestSubject: "+cur.getString(3) +"\nTestTotal: "+cur.getString(5)+"\nCourse Id: "+cur.getString(6) ,
                                Toast.LENGTH_SHORT).show();
                        tdb.close();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                    }


                    mInsertDate.setText("");
                    mInsertSubj.setText("");
                    mInsertType.setText("");
                    mInsertTotal.setText("");
                    mInsertChap.setText("");
                }

            }
        });

        mDelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mClsSel = mClassspN.getSelectedItem().toString();
                String mBoardSel = mBoardspN.getSelectedItem().toString();
                String mBatchSel = mBatchspN.getSelectedItem().toString();
                if (mInsertDate.getText().toString().equals("")||
                        mInsertSubj.getText().toString().equals("")||
                        mInsertType.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Enter Student Name to Search by Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    String mTypeSel, mTDateSel, mTSubjSel;
                    String mCourseid = "";
                    String mTId = "";
                    SQLiteDatabase sdb = mStDB.getReadableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();
                    SQLiteDatabase tdb = mStDB.getReadableDatabase();
                    SQLiteDatabase mdb = mStDB.getWritableDatabase();

                    mTypeSel = mInsertType.getText().toString();
                    mTDateSel = mInsertDate.getText().toString();
                    mTSubjSel = mInsertSubj.getText().toString();

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
                    }

                    if (mTId.equals(""))
                    {
                        Toast.makeText(getContext(), "Incorrect Test Details", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mdb.delete(StudentDB.mTestDetails,("TestId = " + mTId),null);
                        mdb.delete(StudentDB.mStudTest,("TestId = " + mTId),null);
                        Toast.makeText(getContext(), "Test Deleted", Toast.LENGTH_SHORT).show();

                        mInsertDate.setText("");
                        mInsertSubj.setText("");
                        mInsertType.setText("");
                        mInsertTotal.setText("");
                        mInsertChap.setText("");
                    }
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

    @Override
    public void onClick(View v) {
        if(v == mInsertDate) {
            fromDatePickerDialog.show();
        }
    }
    private void setDateTimeField() {
        mInsertDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mInsertDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
