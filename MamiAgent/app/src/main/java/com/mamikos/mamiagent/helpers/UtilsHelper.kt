package com.mamikos.mamiagent.helpers

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.R
import android.support.v7.app.AlertDialog
import android.view.View
import com.mamikos.mamiagent.views.LockableScrollView
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import android.provider.MediaStore
import android.support.v4.content.CursorLoader


/**
 * Created by Dedi Dot on 9/21/2018.
 * Happy Coding!
 */


class UtilsHelper {

    companion object {

        val KEY_FORMAT_PRICE = "#,###"

        fun log(message: String) {
            if (BuildConfig.SHOW_LOG) {

                val desc = "[" + Exception().stackTrace[1].className + "] " + "[" + Exception().stackTrace[1].lineNumber + "] " + "{" + Exception().stackTrace[1].methodName + "} "

                if (message.length > 500) {
                    try {
                        val maxLogSize = 500
                        for (i in 0..message.length / maxLogSize) {
                            val start = i * maxLogSize
                            var end = (i + 1) * maxLogSize
                            end = if (end > message.length) message.length else end
                            Log.e(":", desc + " => " + message.substring(start, end))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("log Exception", desc + " => " + e.message)
                    }

                } else {
                    Log.e(":", "$desc => $message")
                }
            }
        }

        fun makeHandler(duration: Long, runnable: Runnable) {
            val handler = Handler()
            handler.postDelayed(runnable, duration)
        }

        fun getFontRegular(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_regular)
        }

        fun getFontBold(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_bold)
        }

        fun getFontSemiBold(context: Context): Typeface? {
            return ResourcesCompat.getFont(context, R.font.font_semi_bold)
        }


        fun showDialogYesNo(context: Context, title: String, message: String, runnable: Runnable,
                            icon: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)

            if (!title.isEmpty()) {
                builder.setTitle(title)
            }

            if (icon != 0) {
                builder.setIcon(icon)
            }

            builder.setCancelable(false)
            builder.setPositiveButton(context.getString(R.string.msg_yes)) { dialog, _ ->
                runnable.run()
                dialog.dismiss()
            }
            builder.setNegativeButton(context.getString(R.string.msg_no)) { dialog, _ -> dialog.dismiss() }
            builder.show()

        }

        fun autoFocusScroll(view: View, scrollView: LockableScrollView? = null) {
            scrollView?.post {
                scrollView.scrollTo(0, view.top - 100)
            }
        }

        fun showSnackbar(rootView: View, str: String) {
            Snackbar.make(rootView, str, Snackbar.LENGTH_SHORT).show()
        }

        fun getTimestamp(): String {
            val timeStamp = SimpleDateFormat("HH_mm_ss_SSS")
            val now = Date()
            return timeStamp.format(now)+""
        }

        fun getPathFromURI(context: Context, contentUri: Uri): String? {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            var result: String? = null

            val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
            val cursor = cursorLoader.loadInBackground()
            //log("cek cursorX " + cursor!!) // null di device oppo
            log("cek cursorXX " + cursorLoader.uri.path)

            if (cursor != null) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                result = cursor.getString(column_index)
                cursor.close()
            } else {

                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    return cursorLoader.uri.path
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    Toast.makeText(context, "getPathSDKBaru null bro", Toast.LENGTH_SHORT).show()
                    return ""
                }

                /*  } else {
                Cursor cursorx = context.getContentResolver().query(contentUri, proj, null, null, null);
                if (cursorx != null) {
                    if (cursorx.moveToFirst()) {
                        int column_index = cursorx.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        result = cursorx.getString(column_index);
                    }
                    cursorx.close();
                }
            }*/
            }

            return result
        }

    }
}