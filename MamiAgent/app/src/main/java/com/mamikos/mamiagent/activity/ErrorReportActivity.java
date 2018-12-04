package com.mamikos.mamiagent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mamikos.mamiagent.R;
import com.mamikos.mamiagent.helpers.ExceptionHandler;

/**
 * Created by Dedi Android on 4/24/2018.
 * Happy Codding!
 */

public class ErrorReportActivity extends AppCompatActivity {

    String error = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.text_view_custom);

        String error = getIntent().getStringExtra("error");
        ((TextView) findViewById(R.id.txtViewCustom)).setText(error);
    }


    private void goReport() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, error);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(this, FormKostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("error", getIntent().getStringExtra("error"));
            startActivity(intent);
            finish();
        } catch (Exception ex) {
            finish();
        }
    }
}
