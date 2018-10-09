package com.mamikos.mamiagent.views

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

import com.mamikos.mamiagent.R

import java.util.ArrayList

/**
 * FlowLayout will arrange child elements horizontally one next to another. If there is not enough
 * space for next view new line will be added.
 *
 * User: Blaz Solar
 * Date: 5/6/13
 * Time: 8:17 PM
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {

    var gravity = (if (isIcs) Gravity.START else Gravity.LEFT) or Gravity.TOP
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        set(gravity) {
            var gravity = gravity
            if (this.gravity != gravity) {
                if (gravity and Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK == 0) {
                    gravity = gravity or if (isIcs) Gravity.START else Gravity.LEFT
                }

                if (gravity and Gravity.VERTICAL_GRAVITY_MASK == 0) {
                    gravity = gravity or Gravity.TOP
                }

                field = gravity
                requestLayout()
            }
        }

    private val mLines = ArrayList<ArrayList<View>>()
    private val mLineHeights = ArrayList<Int>()
    private val mLineMargins = ArrayList<Int>()

    /**
     * @return `true` if device is running ICS or grater version of Android.
     */
    private val isIcs: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH

    init {

        val a = context.obtainStyledAttributes(attrs,
                R.styleable.FlowLayout_Layout, defStyle, 0)

        try {
            val index = a.getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1)
            if (index > 0) {
                gravity = index
            }
        } finally {
            a.recycle()
        }

    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val modeWidth = View.MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = View.MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = paddingTop + paddingBottom

        var lineWidth = 0
        var lineHeight = 0

        val childCount = childCount

        for (i in 0 until childCount) {

            val child = getChildAt(i)
            val lastChild = i == childCount - 1

            if (child.visibility == View.GONE) {

                if (lastChild) {
                    width = Math.max(width, lineWidth)
                    height += lineHeight
                }

                continue
            }

            measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height)

            val lp = child.layoutParams as LayoutParams

            var childWidthMode = View.MeasureSpec.AT_MOST
            var childWidthSize = sizeWidth

            var childHeightMode = View.MeasureSpec.AT_MOST
            var childHeightSize = sizeHeight

            if (lp.width == MATCH_PARENT) {
                childWidthMode = View.MeasureSpec.EXACTLY
                childWidthSize -= lp.leftMargin + lp.rightMargin
            } else if (lp.width >= 0) {
                childWidthMode = View.MeasureSpec.EXACTLY
                childWidthSize = lp.width
            }

            if (lp.height >= 0) {
                childHeightMode = View.MeasureSpec.EXACTLY
                childHeightSize = lp.height
            } else if (modeHeight == View.MeasureSpec.UNSPECIFIED) {
                childHeightMode = View.MeasureSpec.UNSPECIFIED
                childHeightSize = 0
            }

            child.measure(
                    View.MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                    View.MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode)
            )

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin

            if (lineWidth + childWidth > sizeWidth) {

                width = Math.max(width, lineWidth)
                lineWidth = childWidth

                height += lineHeight
                lineHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
            }

            if (lastChild) {
                width = Math.max(width, lineWidth)
                height += lineHeight
            }

        }

        width += paddingLeft + paddingRight

        setMeasuredDimension(
                if (modeWidth == View.MeasureSpec.EXACTLY) sizeWidth else width,
                if (modeHeight == View.MeasureSpec.EXACTLY) sizeHeight else height)
    }

    /**
     * {@inheritDoc}
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        mLines.clear()
        mLineHeights.clear()
        mLineMargins.clear()

        val width = width
        val height = height

        var linesSum = paddingTop

        var lineWidth = 0
        var lineHeight = 0
        var lineViews = ArrayList<View>()

        val horizontalGravityFactor: Float
        when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> horizontalGravityFactor = 0f
            Gravity.CENTER_HORIZONTAL -> horizontalGravityFactor = .5f
            Gravity.RIGHT -> horizontalGravityFactor = 1f
            else -> horizontalGravityFactor = 0f
        }

        for (i in 0 until childCount) {

            val child = getChildAt(i)

            if (child.visibility == View.GONE) {
                continue
            }

            val lp = child.layoutParams as LayoutParams

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.bottomMargin + lp.topMargin

            if (lineWidth + childWidth > width) {
                mLineHeights.add(lineHeight)
                mLines.add(lineViews)
                mLineMargins.add(((width - lineWidth) * horizontalGravityFactor).toInt() + paddingLeft)

                linesSum += lineHeight

                lineHeight = 0
                lineWidth = 0
                lineViews = ArrayList()
            }

            lineWidth += childWidth
            lineHeight = Math.max(lineHeight, childHeight)
            lineViews.add(child)
        }

        mLineHeights.add(lineHeight)
        mLines.add(lineViews)
        mLineMargins.add(((width - lineWidth) * horizontalGravityFactor).toInt() + paddingLeft)

        linesSum += lineHeight

        var verticalGravityMargin = 0
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> {
            }
            Gravity.CENTER_VERTICAL -> verticalGravityMargin = (height - linesSum) / 2
            Gravity.BOTTOM -> verticalGravityMargin = height - linesSum
            else -> {
            }
        }

        val numLines = mLines.size

        var left: Int
        var top = paddingTop

        for (i in 0 until numLines) {

            lineHeight = mLineHeights[i]
            lineViews = mLines[i]
            left = mLineMargins[i]

            val children = lineViews.size

            for (j in 0 until children) {

                val child = lineViews[j]

                if (child.visibility == View.GONE) {
                    continue
                }

                val lp = child.layoutParams as LayoutParams

                // if height is match_parent we need to remeasure child to line height
                if (lp.height == MATCH_PARENT) {
                    var childWidthMode = View.MeasureSpec.AT_MOST
                    var childWidthSize = lineWidth

                    if (lp.width == MATCH_PARENT) {
                        childWidthMode = View.MeasureSpec.EXACTLY
                    } else if (lp.width >= 0) {
                        childWidthMode = View.MeasureSpec.EXACTLY
                        childWidthSize = lp.width
                    }

                    child.measure(
                            View.MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                            View.MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin, View.MeasureSpec.EXACTLY)
                    )
                }

                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                var gravityMargin = 0

                if (Gravity.isVertical(lp.gravity)) {
                    when (lp.gravity) {
                        Gravity.TOP -> {
                        }
                        Gravity.CENTER_VERTICAL, Gravity.CENTER -> gravityMargin = (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2
                        Gravity.BOTTOM -> gravityMargin = lineHeight - childHeight - lp.topMargin - lp.bottomMargin
                        else -> {
                        }
                    }
                }

                child.layout(left + lp.leftMargin,
                        top + lp.topMargin + gravityMargin + verticalGravityMargin,
                        left + childWidth + lp.leftMargin,
                        top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin)

                left += childWidth + lp.leftMargin + lp.rightMargin

            }

            top += lineHeight
        }

    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
        return LayoutParams(p)
    }

    /**
     * {@inheritDoc}
     */
    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    /**
     * {@inheritDoc}
     */
    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(MATCH_PARENT, MATCH_PARENT) 
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return super.checkLayoutParams(p) && p is LayoutParams
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {

        var gravity = -1

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {

            val a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout)

            try {
                gravity = a.getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1)
            } finally {
                a.recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(source: ViewGroup.LayoutParams) : super(source) {}

    }

}
