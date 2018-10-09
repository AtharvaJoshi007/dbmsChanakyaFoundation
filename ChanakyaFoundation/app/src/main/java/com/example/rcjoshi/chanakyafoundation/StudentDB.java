package com.example.rcjoshi.chanakyafoundation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by RC Joshi on 07-09-2017.
 */

public class StudentDB extends SQLiteOpenHelper {

    public static final String mDBname = "Stud_DB";
    public static final String mStudPer = "Personal_Details";
    public static final String mTestDetails = "Test_details";
    public static final String mStudTest = "Student_Test";
    public static final String mCourses = "Courses";

    public StudentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL("CREATE TABLE "+ mStudPer +" (" +
                "StId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "StudName TEXT, " +
                "ParName TEXT, " +
                "School TEXT, " +
                "StudMob NUMERIC, " +
                "ParMob NUMERIC, " +
                "MailID TEXT, " +
                "CourseId INTEGER, " +
                "FOREIGN KEY (CourseId) REFERENCES "+ mCourses +"(CourseId)" +
                ");");

        db.execSQL("CREATE TABLE "+ mTestDetails +" (" +
                "TestId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "Type TEXT, " +
                "TestDate DATE, " +
                "Subject TEXT, " +
                "Chapter TEXT, " +
                "TotMks INTEGER, " +
                "CourseId INTEGER, " +
                "FOREIGN KEY (CourseId) REFERENCES "+ mCourses +"(CourseId)" +
                ");");

        db.execSQL("CREATE TABLE "+ mStudTest +" (" +
                "TestId INTEGER NOT NULL, " +
                "StId INTEGER NOT NULL, " +
                "StMarks INTEGER, " +
                "FOREIGN KEY (TestId) REFERENCES "+ mTestDetails +"(TestId), " +
                "FOREIGN KEY (StId) REFERENCES "+ mStudPer +"(StId), " +
                "PRIMARY KEY (TestId,StId)" +
                ");");

        db.execSQL("CREATE TABLE "+ mCourses+" (" +
                "CourseId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "Class TEXT, " +
                "Board TEXT, " +
                "Batch TEXT, " +
                "Classroom INTEGER, " +
                "Hours INTEGER" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ mStudPer);
        db.execSQL("DROP TABLE IF EXISTS "+ mTestDetails);
        //db.execSQL("DROP TABLE IF EXISTS "+ mStudPer);
    }

}
