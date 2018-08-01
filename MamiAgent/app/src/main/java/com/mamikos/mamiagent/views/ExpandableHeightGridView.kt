package com.mamikos.mamiagent.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**
 * Created by foursixseven on 3/16/16.
 */
class ExpandableHeightGridView : GridView {

    var isExpanded = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded) {
            // Calculate entire height by providing a very large height hint.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            val expandSpec = View.MeasureSpec.makeMeasureSpec(View.MEASURED_SIZE_MASK,
                    View.MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, expandSpec)

            val params = layoutParams
            params.height = measuredHeight
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}