package com.mamikos.mamiagent.helpers

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import com.mamikos.mamiagent.BuildConfig
import com.mamikos.mamiagent.R
import android.support.v7.app.AlertDialog
import android.view.View
import com.mamikos.mamiagent.views.LockableScrollView


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

    }
}