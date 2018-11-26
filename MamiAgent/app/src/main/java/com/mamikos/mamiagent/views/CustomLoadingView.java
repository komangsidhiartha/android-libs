package com.mamikos.mamiagent.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.mamikos.mamiagent.R;


/**
 * Created by Dedi Android on 4/30/2018.
 * Happy Codding!
 */

public class CustomLoadingView {

    private Dialog dialogLoading;
    private Context context;
    private View view;

    public CustomLoadingView(Context mContext) {
        this.context = mContext;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        view = View.inflate(context, R.layout.view_custom_loading, null);
        builder.setView(view);
        dialogLoading = builder.create();
        dialogLoading.setCancelable(true);
        dialogLoading.setCanceledOnTouchOutside(false);
    }

    public void hideTextStatus() {
        view.findViewById(R.id.text_status_loading).setVisibility(View.GONE);
    }

    public Dialog getDialogLoading() {
        return dialogLoading;
    }

    public void show() {
        dialogLoading.show();
    }

    public void hide() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }
}
