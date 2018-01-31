package com.pub.todo.utils;

import android.content.Context;
import android.util.Log;

import com.pub.todo.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by prabhu on 24/1/18.
 */

public class CommonUtilities {
    private final String TAG = CommonUtilities.class.toString();

    /**
     * Check if singleton object of DB is null and not open; in the case
     * reinitialize and open DB.
     *
     * @param mContext
     */
    public static DBHelper getDBObject(Context mContext) {
        DBHelper dbhelper = DBHelper.getInstance(mContext);
        return dbhelper;
    }

}
