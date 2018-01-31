package com.pub.todo.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pub.todo.activity.HomeScreenActivity;
import com.pub.todo.R;
import com.pub.todo.database.DBHelper;
import com.pub.todo.model.Task;
import com.pub.todo.utils.CommonUtilities;
import com.pub.todo.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prabhu on 25/1/18.
 */

public class TaskViewAdapter extends RecyclerView.Adapter <TaskViewAdapter.MyViewAdapter> {
    private final String TAG = TaskViewAdapter.class.toString();
    Context context;
    private EditText mtitleEdt, mdescriptionEdt;
    private Button mUpdateBtn, mCancelBtn;
    private DatePicker mDatePicker;
    private DBHelper dbHelper;
    private String title, description, date;
    private List <Task> taskList = new ArrayList <Task>();
    private List <Task> unSortedTaskList = new ArrayList <Task>();
    private List <String> datesList;
    private int position;

    public TaskViewAdapter(Context context) {
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(final MyViewAdapter holder, final int position) {
        final Task currentTask = taskList.get(position);
        // set values to all views
        holder.mTitleTv.setText(currentTask.getTitle());    // set title
        holder.mDescriptionTv.setText(currentTask.getDescription());  // set Description
        holder.mDateTv.setText(currentTask.getDate());  // set date
        holder.mDateHeader.setText(currentTask.getDate());

        // check whether the task is completed or not
        if (currentTask.isTaskCompleted()) {
            holder.mStatusBtn.setImageResource(R.drawable.complete); // if completed set (R.drawable.complete) this image
        } else {
            holder.mStatusBtn.setImageResource(R.drawable.incomplete); // if incompleted set (R.drawable.incomplete) this image
        }

        // set on listitem click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when this event happen show dialog with prestored values
                showDialogue(currentTask);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                setPosition(holder.getAdapterPosition());
                if (currentTask.isTaskCompleted())
                    dbHelper.changeTaskStatus(currentTask.getId(), false);
                else
                    dbHelper.changeTaskStatus(currentTask.getId(), true);
                refreshUI();
                return true;
            }
        });

    }

    // return total record count
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + taskList.size());
        return taskList.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mTitleTv, mDescriptionTv, mDateTv, mDateHeader;
        private ImageButton mStatusBtn;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.text_view_title);
            mDescriptionTv = (TextView) itemView.findViewById(R.id.text_view_description);
            mDateTv = (TextView) itemView.findViewById(R.id.text_view_date);
            mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
            mStatusBtn = (ImageButton) itemView.findViewById(R.id.image_button_status);

            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(face);
        }
    }

    public void updateTaskList(List <Task> taskList) {
        this.taskList = taskList;
    }

    private void showDialogue(final Task task) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.add_dialog);
        // Set dialog title
        dialog.setTitle("Update Task");
        // Get all view references
        mtitleEdt = (EditText) dialog.findViewById(R.id.edit_text_title);
        mdescriptionEdt = (EditText) dialog.findViewById(R.id.edit_text_description);
        mUpdateBtn = (Button) dialog.findViewById(R.id.button_add);
        mUpdateBtn.setText("Update");
        mCancelBtn = (Button) dialog.findViewById(R.id.button_cancle);
        mDatePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        long now = System.currentTimeMillis() - 1000;
       /*
        * Note :  Restricting user to set min date as current date because
        *         New Tasks need to done in current date or in future days
        *         so There is no meaning to give past day option
        *
        *         "mDatePicker.setMinDate(now);"
        *
        * */
        mDatePicker.setMinDate(now);
        mtitleEdt.setHint(task.getTitle());
        mtitleEdt.setText(task.getTitle());   // Set Title
        mdescriptionEdt.setHint(task.getDescription());
        mdescriptionEdt.setText(task.getDescription()); // Set Description
        final String[] newDate = task.getDate().split("-");   // need to update date in dialog
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // set onclick listener to save button
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all values
                title = mtitleEdt.getText().toString();
                description = mdescriptionEdt.getText().toString();
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                month = month + 1;
                date = day + "-" + month + "-" + year;
                Log.d(TAG, "onClick: value" + title + description + date);
                if (validateAndSave(task)) {            // if all values are current and save in DB
//                            mStatusTv.setVisibility(View.INVISIBLE);
                    lightRefreshUI();
                    refreshUI();
                    dialog.dismiss();
                }
            }
        });

        // set onclick listener to Cancel button
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // This method will check all fields are having values or not
    private boolean validateAndSave(final Task task) {
        if (validate(task)) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TASK_TITLE, title);
            contentValues.put(Constants.TASK_DESCRIPTION, description);
            contentValues.put(Constants.TASK_DATE, date);
            new Thread(new Runnable() {
                @Override
                public void run() {
                     /*
                        * Update records to database with new Thread
                        * because database operation may take longer time
                        *
                        * */
                    dbHelper.updateRecords(task.getId(), contentValues);
                }
            }).start();
        }
        return true;
    }

    // This method will check whether the fields are having values or not if not return false
    private boolean validate(Task task) {
        if (title.equalsIgnoreCase("")) {  // If Title field is emplty the take old value
            title = task.getTitle(); //
        } else {
            task.setTitle(title);
        }

        if (description.equalsIgnoreCase("")) { // If Description field is emplty the take old value
            description = task.getDescription();
        } else {
            task.setDescription(description);
        }

        task.setDate(date);
        return true;
    }

    private void lightRefreshUI() {
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void refreshUI() {
        // clear current values
        taskList.clear();
        unSortedTaskList.clear();
        // get fresh recods from DB
        unSortedTaskList = dbHelper.getTaskList();
        datesList = dbHelper.getDistinctDates();
        // Sort dates in Ascending order
        Collections.sort(datesList, new DateAscComparator());

        // Sort Task records in Ascending order based on date
        for (String date : datesList) {
            for (Task task : unSortedTaskList) {
                if (date.equalsIgnoreCase(task.getDate()))
                    taskList.add(task);
            }
        }

        // refresh UI
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    /*
    *
    * This class helps to sort date in Ascending order
    *
    * */
    public class DateAscComparator implements Comparator <String> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int n = 0;

        public int compare(String lhs, String rhs) {
            try {
                n = dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
            } catch (Exception e) {
                Log.e(TAG, "compare: " + e.getMessage());
            }
            return n;
        }
    }
}
