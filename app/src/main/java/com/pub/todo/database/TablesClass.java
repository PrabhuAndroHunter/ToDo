package com.pub.todo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.pub.todo.utils.Constants;

import java.io.File;

import static com.pub.todo.utils.Constants.DATABASE_NAME;


/**
 * Created by prabhu on 24/1/18.
 */

public class TablesClass extends SQLiteOpenHelper {
    /**
     * Write all create table statements here in this class on oncreate method
     * If any changes in table structure go for onUpgrade method
     */

    Context context;

    public TablesClass(Context context, String DatabaseName, String nullColumnHack, int databaseVersion) {
//        super(context, DATABASE_NAME, null, Constants.DATABASE_VERSION);
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + "PRABHU"
                + File.separator + DATABASE_NAME, null, 1);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()
                + File.separator + "PRABHU"
                + File.separator + DATABASE_NAME, null);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = ("CREATE TABLE " + Constants.TASK_TABLE + " "
                + "(" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.TASK_TITLE + " text,"
                + Constants.TASK_DESCRIPTION + " text,"
                + Constants.TASK_DATE + " text,"
                + Constants.TASK_STATUS + " INTEGER );");
        db.execSQL(table1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(DATABASE_NAME);
        onCreate(db);
    }
}