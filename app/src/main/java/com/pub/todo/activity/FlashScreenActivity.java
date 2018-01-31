package com.pub.todo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pub.todo.R;

public class FlashScreenActivity extends AppCompatActivity {
    private final String TAG = FlashScreenActivity.class.toString();
    private Intent intent;
    private View mLayout;
    private TextView mAppNameTv;
    private static final int PERMISSION_REQUEST_STORAGE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        // initialise layout file (.xml)
        setContentView(R.layout.activity_flash_screen);
        mLayout = findViewById(R.id.layout_todo);
        // init views
        mAppNameTv = (TextView) findViewById(R.id.text_view_app_name);
        // Create a new typeface from the specified font data.
        Typeface face = Typeface.createFromAsset(this.getAssets(), "fonts/DragonForcE.ttf");
        mAppNameTv.setTypeface(face); // set custom font
        intent = new Intent(this, HomeScreenActivity.class);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // wait for 2 sec after that check for storage permission
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForUserPermitions();
            }
        }, 2000);
    }

    @Override
    public void finish() {
        super.finish();
    }

    /*
    *
    * This method will check whether storage permission is granted or not
    * if Granted -> Start HomeScreenActivity
    * if not Granted ->Request for permission
    *
    * */

    private void checkForUserPermitions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            goToHomeScreen(); // if granted go to next screen
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_STORAGE);
    }

    /*
    *
    * This method will handle runtime permission request output( User selection )
    *
    * */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // if user granted Storage permission start HomeScreenActivity
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToHomeScreen();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Storage permission request was denied please Restart the Application",
                        Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        }
    }

    /*
    *
    * This method will start HomeScreenActivity with Sliding Animation
    *
    * */
    private void goToHomeScreen() {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        FlashScreenActivity.this.finish();
    }

}
