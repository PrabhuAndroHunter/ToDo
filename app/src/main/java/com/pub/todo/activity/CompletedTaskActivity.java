package com.pub.todo.activity;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pub.todo.R;
import com.pub.todo.adapter.CompletedTaskViewAdapter;
import com.pub.todo.adapter.TaskViewAdapter;
import com.pub.todo.database.DBHelper;
import com.pub.todo.model.Task;
import com.pub.todo.utils.CommonUtilities;
import com.pub.todo.utils.RecyclerViewItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class CompletedTaskActivity extends AppCompatActivity {
    private final String TAG = CompletedTaskActivity.class.toString();
    private RecyclerView mRecyclerView;
    private DBHelper dbHelper;
    private CompletedTaskViewAdapter completedTaskViewAdapter;
    private TextView mStatusTv;
    private List <Task> taskList = new ArrayList <Task>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialise layout file (.xml)
        setContentView(R.layout.activity_completed_task);
        Log.d(TAG, "onCreate: ");
        // init Recyclerview And Textview
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_View_employee);
        mStatusTv = (TextView) findViewById(R.id.textview_no_result);
        // get database reference
        dbHelper = CommonUtilities.getDBObject(this);
        getSupportActionBar().setTitle("Completed Task"); //  set actionbar tittle
        // create CompletedTaskViewAdapter  instance
        completedTaskViewAdapter = new CompletedTaskViewAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(this, 0));
        mRecyclerView.setAdapter(completedTaskViewAdapter); // set adapter to recycleview
    }

    @Override
    protected void onStart() {
        super.onStart();
        // refresh UI
        completedTaskViewAdapter.refreshUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /*
    *
    * This method will Show/Hide "NO COMPLETED TASK" Text based on record count
    * Show snackbar with 1 sec delay so that user can understand clearly
    * This Snack bar will show only if there is completed tasks list
    *
    * */
    public void showStatusText(boolean show) {
        if (show)
            mStatusTv.setVisibility(View.VISIBLE);
        else {
            mStatusTv.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.layout_completed_task), "Long Press to Delete Task",
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            }, 1000);
        }
    }
}
