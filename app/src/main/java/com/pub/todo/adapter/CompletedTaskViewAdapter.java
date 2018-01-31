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

import com.pub.todo.R;
import com.pub.todo.activity.CompletedTaskActivity;
import com.pub.todo.activity.HomeScreenActivity;
import com.pub.todo.database.DBHelper;
import com.pub.todo.model.Task;
import com.pub.todo.utils.CommonUtilities;
import com.pub.todo.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prabhu on 25/1/18.
 */

public class CompletedTaskViewAdapter extends RecyclerView.Adapter <CompletedTaskViewAdapter.MyViewAdapter> {
    private final String TAG = CompletedTaskViewAdapter.class.toString();
    Context context;
    private DBHelper dbHelper;
    private List <Task> taskList = new ArrayList <Task>();
    private int position;

    public CompletedTaskViewAdapter(Context context) {
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
        holder.mTitleTv.setText(currentTask.getTitle());    // set Title
        holder.mDescriptionTv.setText(currentTask.getDescription());  // set Decripton
        holder.mDateTv.setText(currentTask.getDate());  // set Date
        holder.mDateHeaderTv.setText(currentTask.getDate()); // set DateHeader

        // check whether the task is completed or not
        if (currentTask.isTaskCompleted()) {
            holder.mStatusBtn.setImageResource(R.drawable.complete);  // if completed set (R.drawable.complete) this image
        } else {
            holder.mStatusBtn.setImageResource(R.drawable.incomplete); // if incompleted set (R.drawable.incomplete) this image
        }

        // Set longclick listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                setPosition(holder.getAdapterPosition());
                // When long click event happen Delete that Task
                dbHelper.deleteTask(currentTask.getId());
                // After Deleting refresh Records and UI
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
        private TextView mTitleTv, mDescriptionTv, mDateTv, mDateHeaderTv;
        private ImageButton mStatusBtn;

        public MyViewAdapter(View itemView) {
            super(itemView);
            // init all views
            mTitleTv = (TextView) itemView.findViewById(R.id.text_view_title);
            mDescriptionTv = (TextView) itemView.findViewById(R.id.text_view_description);
            mDateTv = (TextView) itemView.findViewById(R.id.text_view_date);
            mDateHeaderTv = (TextView) itemView.findViewById(R.id.date_header);
            mStatusBtn = (ImageButton) itemView.findViewById(R.id.image_button_status);

            // set Custom fonts
            Typeface Roboto_Thin = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(Roboto_Thin);
        }
    }

    /*
    *
    * This method will Delete current records and get fresh records from the database
    * and refresh the UI
    *
    * */
    public void refreshUI() {
        taskList.clear();
        taskList = dbHelper.getCompletedTaskList();
        if (taskList.size() == 0) {
            ((CompletedTaskActivity) context).showStatusText(true);
        } else {
            ((CompletedTaskActivity) context).showStatusText(false);
        }

        ((CompletedTaskActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

}
