package com.example.rcjoshi.chanakyafoundation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.rcjoshi.chanakyafoundation.R.id.addClassid;
import static com.example.rcjoshi.chanakyafoundation.R.id.addCoursebtnid;

public class AdminSetCourse extends Fragment {

    StudentDB mStDB;
    Button mAddCourse;
    EditText mAddClass,mAddBoard,mAddBatch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v = inflater.inflate(R.layout.activity_admin_set_course, container, false);

        mStDB=new StudentDB(getContext(),"Student",null,2);
        mAddCourse = (Button) v.findViewById(addCoursebtnid);
        mAddClass = (EditText) v.findViewById(addClassid);
        mAddBoard = (EditText) v.findViewById(R.id.addBoardid);
        mAddBatch = (EditText) v.findViewById(R.id.addBatchid);

        mAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddClass.getText().toString().equals("")||mAddBoard.getText().toString().equals("")||
                        mAddBatch.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please Enter all Required Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SQLiteDatabase cdb = mStDB.getWritableDatabase();
                    ContentValues cvCourses = new ContentValues();
                    cvCourses.put("Class",mAddClass.getText().toString());
                    cvCourses.put("Board",mAddBoard.getText().toString());
                    cvCourses.put("Batch",mAddBatch.getText().toString());

                    cdb.insert(StudentDB.mCourses,"Class",cvCourses);

                    Cursor cur=cdb.query(StudentDB.mCourses,null,null,null,null,null,null);
                    cur.moveToLast();
                    Toast.makeText(getContext(), "Record inserted as ID "+cur.getString(0), Toast.LENGTH_SHORT).show();

                    cdb.close();

                    mAddClass.setText("");
                    mAddBoard.setText("");
                    mAddBatch.setText("");
                }

            }
        });
        return v;

    }
}
