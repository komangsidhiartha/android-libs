package com.mamikos.mamiagent.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import kotlinx.android.synthetic.main.view_grey_square.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class SquareGreyView : FrameLayout, TextWatcher, View.OnClickListener {

    private var clicked: Runnable? = null
    var isChecked: Boolean = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_grey_square, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setString(str: String) {
        squareNameTextView.text = str
    }

    fun setImage(img: Int) {
        squareImageView.imageResource = img
    }

    fun setJustText() {
        squareNameTextView.visibility = View.VISIBLE
        squareImageView.visibility = View.GONE
        contentRoomSizeLinearLayout.visibility = View.GONE
        squareNameTextView.setPadding(20, 20, 20, 20)
        squareNameTextView.setAllCaps(false)
    }

    fun setRoomSize() {
        squareNameTextView.visibility = View.GONE
        squareImageView.visibility = View.GONE
        contentRoomSizeLinearLayout.visibility = View.VISIBLE
        leftRoomSizeEditText.addTextChangedListener(this)
        rightRoomSizeEditText.addTextChangedListener(this)
        setIsClick(true)
    }

    private fun setIsClick(b: Boolean) {
        leftRoomSizeEditText.setOnClickListener(this)
        centerRoomSizeTextView.setOnClickListener(this)
        rightRoomSizeEditText.setOnClickListener(this)
        contentSquareConstraintLayout.setOnClickListener(this)
        squareImageView.setOnClickListener(this)
        squareNameTextView.setOnClickListener(this)
        contentRoomSizeLinearLayout.setOnClickListener(this)
    }

    fun removeBackgroundRoomSize() {
        leftRoomSizeEditText.setBackgroundResource(0)
        leftRoomSizeEditText.isFocusable = false
        leftRoomSizeEditText.isFocusableInTouchMode = false
        centerRoomSizeTextView.setBackgroundResource(0)
        rightRoomSizeEditText.setBackgroundResource(0)
        rightRoomSizeEditText.isFocusable = false
        rightRoomSizeEditText.isFocusableInTouchMode = false
    }

    fun setTextRoomSize(left: String, right: String) {
        leftRoomSizeEditText.setText(left)
        rightRoomSizeEditText.setText(right)
    }

    fun getTextRoomSize(): String {
        return if (leftRoomSizeEditText.text.toString().isEmpty() || rightRoomSizeEditText.text.toString().isEmpty()) {
            ""
        } else {
            leftRoomSizeEditText.text.toString() + "," + rightRoomSizeEditText.text.toString()
        }
    }

    fun setCheckList(isOkay: Boolean) {
        setIsClick(true)
        if (isOkay) {
            isChecked = true
            checkListSquareImageView.visibility = View.VISIBLE
            contentSquareConstraintLayout.backgroundResource = R.drawable.border_radius_type_kos_green
            squareNameTextView.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            leftRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            centerRoomSizeTextView.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            rightRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
        } else {
            isChecked = false
            checkListSquareImageView.visibility = View.GONE
            contentSquareConstraintLayout.backgroundResource = R.drawable.border_radius_type_kos_grey
            squareNameTextView.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
            leftRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
            centerRoomSizeTextView.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
            rightRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
        }
    }

    fun setOnClick(click: Runnable) {
        clicked = click
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        clicked?.let { clicked?.run() }
    }

    override fun onClick(v: View?) {
        clicked?.let { clicked?.run() }
    }

}