package com.mamikos.mamiagent.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.mamikos.mamiagent.BuildConfig;
import com.mamikos.mamiagent.activity.FormKostActivity;

import java.io.PrintWriter;
import java.io.StringWriter;



/**
 * Created by Dedi Android on 4/24/2018.
 * Happy Codding!
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String TAG = ExceptionHandler.class.getSimpleName();
    private final Context mContext;

    public ExceptionHandler(Context myContext) {
        this.mContext = myContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("====> CAUSE OF ERROR <====\n");
        errorReport.append(stackTrace.toString());
        errorReport.append("\n\n====> MAMI AGENT <====\n");

        String LINE_SEPARATOR = "\n";

        errorReport.append(LINE_SEPARATOR);

        try {
            errorReport.append("Version:").append(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "errorMessage : " + e.getMessage());
                e.printStackTrace();
            }
        }

        errorReport.append("\n\n====> DEVICE INFORMATION <====\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n\n====> FIRMWARE <====\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        Intent intent = new Intent(mContext, FormKostActivity.class);
        intent.putExtra("error", errorReport.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}