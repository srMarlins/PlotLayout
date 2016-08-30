package com.srmarlins.plotlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.srmarlins.plotlayout.R;
import com.srmarlins.plotlayout.model.Point;

/**
 * Created by JaredFowler on 8/18/2016.
 */

public class PlotLayout extends ViewGroup {

    protected static final int MAX_GRID_SIZE = 1000;

    protected double graphCoefficient = .1;
    protected int xRowSize;
    protected int yRowSize;
    protected double distance;
    private Rect tempContainer = new Rect();

    public PlotLayout(Context context) {
        super(context);
    }

    public PlotLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlotLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                if (layoutParams.x > xRowSize) {
                    throw new IllegalArgumentException("X value of " + layoutParams.x + " is greater than the x row size of " + xRowSize);
                } else if (layoutParams.y > yRowSize) {
                    throw new IllegalArgumentException("Y value of " + layoutParams.y + " is greater than the y row size of " + yRowSize);
                }

                tempContainer.left = (int) (layoutParams.x * distance + layoutParams.leftMargin - paddingStart);
                tempContainer.right = (int) (layoutParams.x * distance + childWidth - layoutParams.rightMargin - paddingEnd);
                tempContainer.top = (int) (layoutParams.y * distance + layoutParams.topMargin - paddingTop);
                tempContainer.bottom = (int) (layoutParams.y * distance + childHeight - layoutParams.bottomMargin - paddingBottom);

                child.layout(tempContainer.left, tempContainer.top, tempContainer.right, tempContainer.bottom);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();
        int childState = 0;
        int maxHeight = 0;
        int maxWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                maxWidth += child.getMeasuredWidth() + layoutParams.getMarginStart() + layoutParams.getMarginEnd();
                maxHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        calculateGridSize(MeasureSpec.getSize(getMeasuredWidth()), MeasureSpec.getSize(getMeasuredHeight()));
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PlotLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    private void placePoint(Point point) {
        point.getMatrix().preTranslate((float) (point.getxCoordinate() * distance), (float) (point.getyCoordinate() * distance));
    }

    private void calculateGridSize(int viewWidth, int viewHeight) {
        distance = (viewWidth > viewHeight ? viewWidth : viewHeight) / getMaxRows();
        xRowSize = (int) (viewWidth / distance);
        yRowSize = (int) (viewHeight / distance);
    }

    private double getMaxRows() {
        return MAX_GRID_SIZE * graphCoefficient;
    }

    public int sizeOfX() {
        return xRowSize;
    }

    public int sizeOfY() {
        return yRowSize;
    }

    public int coordToPx(int coord) {
        return (int) (coord * distance);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public int x;
        public int y;
        public String pathTag;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.PlotLayoutParams);
            x = a.getInteger(R.styleable.PlotLayoutParams_layout_positionX, 0);
            y = a.getInteger(R.styleable.PlotLayoutParams_layout_positionY, 0);
            pathTag = a.getString(R.styleable.PlotLayoutParams_animation_pathTag);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
