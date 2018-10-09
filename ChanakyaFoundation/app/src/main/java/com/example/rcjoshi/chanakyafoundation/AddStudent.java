package com.example.rcjoshi.chanakyafoundation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddStudent extends Fragment {

    StudentDB mStDB;
    Uri uri;
    Bitmap bitmap;
    String picpath="";
    Button mAdd,mClear,mUpdate,mFind,mDel;
    ImageView mProfPic;
    EditText mStname,mParname,mSchool, mStMob, mParMob,mMail;
    Spinner mClasssp,mBatchsp,mBoardsp;
    ArrayAdapter<String> mClassAd,mBoardAd,mBatchAd;
    int flagIncomplete=0;

    public AddStudent() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_student, container, false);
        mStDB=new StudentDB(getContext(),"Student",null,2);
        //mStDB = new StudentDB(getContext(),"Courses",null,2);
        //setting spinners
        mClasssp = (Spinner) v.findViewById(R.id.stdid);
        mBoardsp = (Spinner) v.findViewById(R.id.boardid);
        mBatchsp = (Spinner) v.findViewById(R.id.batchid);

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
        mClassAd = new ArrayAdapter<String>(getContext(), R.layout.contents_spinner_courses, R.id.demo1,splist1);
        mBoardAd = new ArrayAdapter<String>(getContext(), R.layout.contents_spinner_courses, R.id.demo2,splist2);
        mBatchAd = new ArrayAdapter<String>(getContext(), R.layout.contents_spinner_courses, R.id.demo3,splist3);
        //mClassAd =ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.set_class,android.R.layout.simple_spinner_dropdown_item);
        //mBoardAd = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.set_board,android.R.layout.simple_spinner_dropdown_item);
        //mBatchAd = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.set_batch,android.R.layout.simple_spinner_dropdown_item);

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

        //initialising edittexts
        mStname = (EditText) v.findViewById(R.id.stnameid);
        mSchool = (EditText) v.findViewById(R.id.schoolid);
        mParname = (EditText) v.findViewById(R.id.parnameid);
        mStMob = (EditText) v.findViewById(R.id.stmobid);
        mParMob = (EditText) v.findViewById(R.id.parmobid);
        mMail = (EditText) v.findViewById(R.id.mailid);


        //setting button listeners
        mAdd = (Button) v.findViewById(R.id.addbtnid);
        mClear = (Button) v.findViewById(R.id.clearbtnid);
        mUpdate = (Button) v.findViewById(R.id.updatebtnid);
        mFind = (Button) v.findViewById(R.id.findbtnid);
        mDel = (Button) v.findViewById(R.id.delbtnid);
        mProfPic = (ImageView) v.findViewById(R.id.stpicid);

        mProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent();
                iGallery.setType("image/*");
                iGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(iGallery,"Select Picture"),7);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStname.getText().toString().equals("")||mParname.getText().toString().equals("")||
                        mSchool.getText().toString().equals("")|| mParMob.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please Enter all Required Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //int mClsSel,mBoardSel,mBatchSel;
                    String mClsSel,mBoardSel,mBatchSel;
                    int mCourseid=0;
                    SQLiteDatabase sdb = mStDB.getWritableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();
                    if (flagIncomplete==0)
                    {
                        mClsSel = mClasssp.getSelectedItem().toString();
                        mBoardSel = mBoardsp.getSelectedItem().toString();
                        mBatchSel = mBatchsp.getSelectedItem().toString();
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
                        //cdb.setTransactionSuccessful();
                        //cdb.close();
                    }


                    if (mCourseid!=0)
                    {
                        String manm="",masch="",mapnm="",masno="",mapno="",maem="";
                        manm = mStname.getText().toString();
                        masch = mSchool.getText().toString();
                        mapnm = mParname.getText().toString();
                        masno = mStMob.getText().toString();
                        mapno = mParMob.getText().toString();
                        maem = mMail.getText().toString();

                        if (isEmailValid(maem) || maem.equals(""))
                        {
                            if (mapno.length()==10 && (masno.length()==10||masno.equals("")) &&
                                    !mapno.contains(" ") && !masno.contains(" ")) {
                                ContentValues cvStPer = new ContentValues();

                                cvStPer.put("StudName", manm);
                                cvStPer.put("School", masch);
                                cvStPer.put("ParName", mapnm);
                                cvStPer.put("StudMob", masno);
                                cvStPer.put("ParMob", mapno);
                                cvStPer.put("MailID", maem);
                                cvStPer.put("CourseId", mCourseid);

                                sdb.insert(StudentDB.mStudPer, "StudName", cvStPer);

                                Cursor curStId = sdb.query(StudentDB.mStudPer, null, null, null, null, null, null);
                                curStId.moveToLast();
                                Toast.makeText(getContext(), "Record inserted as ID " + curStId.getString(0), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Course selected is:" + mCourseid, Toast.LENGTH_SHORT).show();
                                reset();
                            }
                            else {
                                Toast.makeText(getContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Invalid Email ID", Toast.LENGTH_SHORT).show();
                            mMail.requestFocus();
                        }
                        sdb.close();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                        sdb.close();
                        reset();
                    }
                }

            }
        });

        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStname.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Enter Student Name to Search by Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mClsSel,mBoardSel,mBatchSel;
                    String mStNameUp = mStname.getText().toString();
                    String mStId="",mCourseidOld="";

                    String mCourseid="";
                    SQLiteDatabase sdb = mStDB.getWritableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();
                    mClsSel = mClasssp.getSelectedItem().toString();
                    mBoardSel = mBoardsp.getSelectedItem().toString();
                    mBatchSel = mBatchsp.getSelectedItem().toString();

                    Cursor cfind;
                    cfind = sdb.rawQuery("SELECT * FROM "+StudentDB.mStudPer,null);
                    //Cursor curCId;
                    while (cfind.moveToNext())
                    {
                        if (cfind.getString(cfind.getColumnIndex("StudName")).equals(mStNameUp))
                        {
                            //mStId=cfind.getString(cfind.getColumnIndex("StId"));
                            //mCourseidOld=cfind.getString(cfind.getColumnIndex("CourseId"));
                            mSchool.setText(cfind.getString(cfind.getColumnIndex("School")));
                            mParname.setText(cfind.getString(cfind.getColumnIndex("ParName")));
                            mStMob.setText(cfind.getString(cfind.getColumnIndex("StudMob")));
                            mParMob.setText(cfind.getString(cfind.getColumnIndex("ParMob")));
                            mMail.setText(cfind.getString(cfind.getColumnIndex("MailID")));
                            break;
                        }
                    }
                }

            }
        });
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStname.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Enter Student Name to Search by Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mClsSel,mBoardSel,mBatchSel;
                    String mStNameUp = mStname.getText().toString();
                    String mStId="",mCourseidOld="";

                    String mCourseid="";
                    SQLiteDatabase sdb = mStDB.getWritableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();

                    mClsSel = mClasssp.getSelectedItem().toString();
                    mBoardSel = mBoardsp.getSelectedItem().toString();
                    mBatchSel = mBatchsp.getSelectedItem().toString();

                    Cursor cfind;
                    cfind = sdb.rawQuery("SELECT * FROM "+StudentDB.mStudPer,null);
                    Cursor curCId;
                    while (cfind.moveToNext())
                    {
                        if (cfind.getString(cfind.getColumnIndex("StudName")).equals(mStNameUp))
                        {
                            mStId=cfind.getString(cfind.getColumnIndex("StId"));
                            mCourseidOld=cfind.getString(cfind.getColumnIndex("CourseId"));
                            break;
                        }
                    }
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
                                mCourseid = curCId.getString(0);
                            }
                        }
                    }
                    //cdb.close();

                    if (mCourseid.equals(mCourseidOld.toString()))
                    {
                        if (mSchool.getText().toString().equals("")
                                || mParname.getText().toString().equals("")
                                || mParMob.getText().toString().equals("")
                                )
                        {
                            Toast.makeText(getContext(), "Fill New Values", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String manm="",masch="",mapnm="",masno="",mapno="",maem="";
                            manm = mStname.getText().toString();
                            masch = mSchool.getText().toString();
                            mapnm = mParname.getText().toString();
                            masno = mStMob.getText().toString();
                            mapno = mParMob.getText().toString();
                            maem = mMail.getText().toString();

                            if (isEmailValid(maem) || maem.equals(""))
                            {
                                if (mapno.length()==10 && (masno.length()==10||masno.equals("")) &&
                                        !mapno.contains(" ") && !masno.contains(" "))
                                {
                                    ContentValues cvStPer = new ContentValues();

                                    cvStPer.put("StudName", manm);
                                    cvStPer.put("School", masch);
                                    cvStPer.put("ParName", mapnm);
                                    cvStPer.put("StudMob", masno);
                                    cvStPer.put("ParMob", mapno);
                                    cvStPer.put("MailID", maem);
                                    cvStPer.put("CourseId", mCourseid);

                                    sdb.update(StudentDB.mStudPer, cvStPer, ("StId = " + mStId), null);
                                    Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                                    reset();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Invalid Email ID", Toast.LENGTH_SHORT).show();
                                mMail.requestFocus();
                            }
                        }

                        sdb.close();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                        sdb.close();
                        reset();
                    }
                }

            }
        });

        mDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStname.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Enter Student Name to Search by Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mClsSel,mBoardSel,mBatchSel;
                    String mStNameUp = mStname.getText().toString();
                    String mStId="",mCourseidOld="";

                    String mCourseid="";
                    SQLiteDatabase sdb = mStDB.getWritableDatabase();
                    SQLiteDatabase cdb = mStDB.getReadableDatabase();

                    mClsSel = mClasssp.getSelectedItem().toString();
                    mBoardSel = mBoardsp.getSelectedItem().toString();
                    mBatchSel = mBatchsp.getSelectedItem().toString();

                    Cursor cfind;
                    cfind = sdb.rawQuery("SELECT * FROM "+StudentDB.mStudPer,null);
                    Cursor curCId;
                    while (cfind.moveToNext())
                    {
                        if (cfind.getString(cfind.getColumnIndex("StudName")).equals(mStNameUp))
                        {
                            mStId=cfind.getString(cfind.getColumnIndex("StId"));
                            mCourseidOld=cfind.getString(cfind.getColumnIndex("CourseId"));
                            break;
                        }
                    }
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
                                mCourseid = curCId.getString(0);
                            }
                        }
                    }
                    //cdb.close();

                    if (mCourseid.equals(mCourseidOld.toString()))
                    {

                            sdb.delete(StudentDB.mStudPer,("StId = " + mStId),null);
                            sdb.delete(StudentDB.mStudTest,("StId = " + mStId),null);
                            Toast.makeText(getContext(), "Record Successfully deleted", Toast.LENGTH_SHORT).show();
                        sdb.close();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Invalid Course", Toast.LENGTH_SHORT).show();
                        sdb.close();
                        reset();
                    }
                }

            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==7)
        {
            try {
                Context applicationContext = MainActivity.getContextOfApplication();
                uri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(),uri);
                mProfPic.setImageBitmap(bitmap);
                mProfPic.setMaxHeight(100);
                mProfPic.setMaxWidth(100);
                picpath = uri.toString();

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void reset()
    {
        mStname.setText("");
        mParname.setText("");
        mSchool.setText("");
        mStMob.setText("");
        mParMob.setText("");
        mMail.setText("");
        mClasssp.setSelection(0);
        mBoardsp.setSelection(0);
        mBatchsp.setSelection(0);
    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
