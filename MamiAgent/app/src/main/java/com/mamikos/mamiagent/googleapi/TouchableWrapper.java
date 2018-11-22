package com.mamikos.mamiagent.googleapi;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by angga on 6/25/2015.
 *
 */
public class TouchableWrapper extends FrameLayout
{
    private TouchAction mTouchAction;
    private SparseArray<PointF> mActivePointers = new SparseArray<>();

    public TouchableWrapper(Context context)
    {
        super(context);
        // Force the host activity to implement the TouchAction Interface
        try
        {
            mTouchAction = (TouchAction) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                                                 + " must implement TouchAction");
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event)
    {
        int pointerIndex = event.getActionIndex();
        //Log.i("Pointer index", "" + pointerIndex);

        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                //Log.i("oldval " + pointerIndex, "" + f.x + f.y);
                mActivePointers.put(pointerId, f);
                mTouchAction.onTouchDown();
                break;
            case MotionEvent.ACTION_MOVE:
                for (int size = event.getPointerCount(), i = 0; i < size; i++)
                {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null)
                    {
                        float oldval = point.x + point.y;
                        float newval = event.getX(i) + event.getY(i);
                        if (oldval - newval > 10 || oldval - newval < -10)
                        {
                            //Log.i("newval " + i, "" + event.getX(i) + event.getY(i));
                            mTouchAction.onTouchMove();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //Log.i("Pointer count", "" + event.getPointerCount());
                if (event.getPointerCount() < 2)
                {
                    mTouchAction.onTouchUp();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointers.remove(pointerId);
                break;
        }
        invalidate();
        return super.dispatchTouchEvent(event);
    }

    public interface TouchAction
    {
        void onTouchUp();

        void onTouchDown();

        void onTouchMove();
    }

}
