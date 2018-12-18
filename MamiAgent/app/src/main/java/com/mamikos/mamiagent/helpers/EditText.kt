package com.mamikos.mamiagent.helpers

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import org.jetbrains.anko.toast

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun TextInputLayout.validate(validator: (String) -> Boolean, message: String) {
    this.editText?.afterTextChanged {
        Log.i("EditText", "AfterTextChanged String `$it`")
        this.error = if (validator(it)) null else message
    }
    Log.i("EditText", "String `${this.editText?.text.toString()}`")
    //this.error = if (validator(this.editText?.text.toString())) null else message
}

fun TextInputLayout.isError(): Boolean = this.error != null

fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String.isValidPhone(): Boolean = this.isNotBlank() && this.matches(Regex("08+[0-9]+"))

fun EditText.validateAutoFocus(message: String, view: View): Boolean {
    return if (this.text.isNotEmpty()) {
        true
    } else {
        this.requestFocus()
        UtilsHelper.autoFocusScroll(this, view)
        this.context.toast(message)
        false
    }
}


