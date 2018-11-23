package com.mamikos.mamiagent.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mamikos.mamiagent.R
import kotlinx.android.synthetic.main.view_kos_type.view.*
import kotlinx.android.synthetic.main.view_room_size.view.*
import org.jetbrains.anko.backgroundResource

/**
 * Created by Dedi Dot on 10/9/2018.
 * Happy Coding!
 */

class RoomSizeView : FrameLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_room_size, this)
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setTextRoomSize(str: String) {
        genderTypeTextView.text = str
    }

    fun setCanEdit() {
        leftRoomSizeEditText.isFocusable = true
        leftRoomSizeEditText.isFocusableInTouchMode = true
    }

    fun setCheckList(isOkay: Boolean) {
        if (isOkay) {
            checkListGenderImageView.visibility = View.VISIBLE
            contentRoomSizeLinearLayout.backgroundResource = R.drawable.border_radius_type_kos_green
            leftRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            centerRoomSizeTextView.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            RightRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.apptheme_color))
            leftRoomSizeEditText.setBackgroundResource(0)
            RightRoomSizeEditText.setBackgroundResource(0)
        } else {
            checkListGenderImageView.visibility = View.GONE
            contentRoomSizeLinearLayout.backgroundResource = R.drawable.border_radius_type_kos_grey
            leftRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
            centerRoomSizeTextView.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
            RightRoomSizeEditText.setTextColor(ContextCompat.getColor(context, R.color.silver_approx_2))
        }
    }

}