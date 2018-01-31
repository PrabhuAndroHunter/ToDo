package com.pub.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pub.todo.model.Task;
import com.pub.todo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 24/1/18.
 * <p>
 * This class manages database Operations
 */

public class DBHelper {
    private final String TAG = DBHelper.class.toString();
    private SQLiteDatabase db;
    private final Context context;
    private final TablesClass dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    /*
    *
    * This method will return instance of DBHelper class
    * @param context to use to open or create the database
    *
    * */
    public static DBHelper getInstance(Context context) {
        try {
            if (db_helper == null) {
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        } catch (IllegalStateException e) {
            //db_helper already open
        }
        return db_helper;
    }

    /*
     * set context of the class and initialize TableClass Object
     * @param context to use to open or create the database
     *
	 */

    public DBHelper(Context c) {
        context = c;
        dbHelper = new TablesClass(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /*
     * close databse.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean dbOpenCheck() {
        try {
            return db.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    /*
    *
    * This method will insert records
    * @param tableName to use to insert into that specified TableName
    * @param content  this map contains the initial column values for the row.
    *                 The keys should be the column names and the values the column values
    *
    * */

    public long insertContentVals(String tableName, ContentValues content) {
        long id = 0;
        try {
            db.beginTransaction();
            id = db.insert(tableName, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return id;
    }

     /*
    *
    * This method will Get count of all records in a table as per the condition
    * @param table to use to search that specified TableName
    * @param where this is optional WHERE clause to apply when get records.
    *            Passing null will get all rows.
    *
    * */

    public int getFullCount(String table, String where) {
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }

    /*
    *
    * This will get all records from task_table
    *
    * */

    public List <Task> getTaskList() throws SQLException { // Creating method
        List <Task> taskList = new ArrayList <Task>();

        //Cursor exposes results from a query on a SQLiteDatabase.
        Cursor cursor = db.query(true, Constants.TASK_TABLE, new String[]{Constants.ID, Constants.TASK_TITLE, Constants.TASK_DESCRIPTION,
                Constants.TASK_DATE, Constants.TASK_STATUS}, null, null, null, null, Constants.TASK_DATE + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int taskId = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                String taskTitle = cursor.getString(cursor.getColumnIndex(Constants.TASK_TITLE));
                String taskDescription = cursor.getString(cursor.getColumnIndex(Constants.TASK_DESCRIPTION));
                String taskDate = cursor.getString(cursor.getColumnIndex(Constants.TASK_DATE));
                int taskStatus = cursor.getInt(cursor.getColumnIndex(Constants.TASK_STATUS));
                taskList.add(new Task(taskId, taskTitle, taskDescription, taskDate, taskStatus));
            } while (cursor.moveToNext());
            cursor.close();
            return taskList;
        }
        cursor.close();
        return taskList; // Return statement
    }

    /*
    *
    * This will get all Tasks which status is 1 ( completed)
    *
    * */

    public List <Task> getCompletedTaskList() throws SQLException {
        List <Task> completedTaskList = new ArrayList <Task>();
        Cursor cursor = db.query(Constants.TASK_TABLE, new String[]{Constants.ID, Constants.TASK_TITLE, Constants.TASK_DESCRIPTION,
                Constants.TASK_DATE, Constants.TASK_STATUS}, Constants.TASK_STATUS + " = 1", null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int taskId = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                    String taskTitle = cursor.getString(cursor.getColumnIndex(Constants.TASK_TITLE));
                    String taskDescription = cursor.getString(cursor.getColumnIndex(Constants.TASK_DESCRIPTION));
                    String taskDate = cursor.getString(cursor.getColumnIndex(Constants.TASK_DATE));
                    int taskStatus = cursor.getInt(cursor.getColumnIndex(Constants.TASK_STATUS));
                    completedTaskList.add(new Task(taskId, taskTitle, taskDescription, taskDate, taskStatus));
                } while (cursor.moveToNext());
                cursor.close();
                return completedTaskList;
            }
            // Return statement
        }
        cursor.close();
        return completedTaskList;
    }

    /*
    *
    * This method will update records from TABLE : task_table
    *
    * @param id is the task id will use to update records
    * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
    * */

    public int updateRecords(int id, ContentValues values) {
        Log.d(TAG, "updateRecords: ");
        int a = 0;
        try {
            db.beginTransaction();
            a = db.update(Constants.TASK_TABLE, values, Constants.ID + "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return a;
    }

    /*
    *
    * This method will update task Status
    * @param taskid is used to update status
    * @param iscompleted is used to check whether task is completed or not
    *                   true - then task is completed ( set as 1)
    *                   false - then task is notCompleted ( set as 0)
    *
    * */
    public void changeTaskStatus(int taskId, boolean isCompleted) {
        Log.d(TAG, "changeTaskStatus: ");
        ContentValues contentValues = new ContentValues();
        if (isCompleted)
            contentValues.put(Constants.TASK_STATUS, 1);
        else
            contentValues.put(Constants.TASK_STATUS, 0);

        updateRecords(taskId, contentValues);
    }

    /*
    *
    *This method will delete task
    * @param id will use to delete
    *
    * */
    public boolean deleteTask(int id) {
        return db.delete(Constants.TASK_TABLE, Constants.ID + "=" + id, null) > 0;
    }

    /*
    *
    * This method will get distinctdates based on order by 'DESC'
    *
    * */
    public List <String> getDistinctDates() {
        List <String> dates = new ArrayList <String>();
        Cursor cursor = db.query(true, Constants.TASK_TABLE, new String[]{Constants.TASK_DATE}, null, null, Constants.TASK_DATE, null, Constants.TASK_DATE + " DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String taskDate = cursor.getString(cursor.getColumnIndex(Constants.TASK_DATE));
                    dates.add(taskDate);
                    Log.d(TAG, "getDistinctDates: " + taskDate);
                } while (cursor.moveToNext());
                cursor.close();
                return dates;
            }
            // Return statement
        }
        cursor.close();
        return dates;
    }
}