package com.blingbling.widget.videoprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by BlingBling on 2017/12/8.
 */

public class VideoProgressView extends View {

    private static final long DEFAULT_PROGRESS_MAX = 100;
    private static final int DEFAULT_CIRCLE_RADIUS = 20;
    private static final int DEFAULT_CIRCLE_WIDTH = 2;
    private static final int DEFAULT_CIRCLE_PROGRESS_WIDTH = 4;

    private float mDensity;

    private long mProgress = 0;
    private long mMax = DEFAULT_PROGRESS_MAX;

    private int mCircleRadius;
    private int mCircleWidth;
    private int mCircleProgressWidth;
    private int mViewSize;
    private RectF mProgressRect;

    private Paint mBackgroundPaint;
    private Paint mProgressPaint;

    public VideoProgressView(Context context) {
        this(context, null);
    }

    public VideoProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDensity = getResources().getDisplayMetrics().density;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoProgressView);
        int backgroundColor = a.getColor(R.styleable.VideoProgressView_vpv_backgroundColor, Color.parseColor("#d6d6d8"));
        int progressColor = a.getColor(R.styleable.VideoProgressView_vpv_progressColor, Color.parseColor("#626165"));
        mCircleRadius = a.getDimensionPixelOffset(R.styleable.VideoProgressView_vpv_circleRadius, dp2px(DEFAULT_CIRCLE_RADIUS));
        mCircleWidth = a.getDimensionPixelOffset(R.styleable.VideoProgressView_vpv_circleWidth, dp2px(DEFAULT_CIRCLE_WIDTH));
        mCircleProgressWidth = a.getDimensionPixelOffset(R.styleable.VideoProgressView_vpv_circleProgressWidth, dp2px(DEFAULT_CIRCLE_PROGRESS_WIDTH));
        a.recycle();

        mViewSize = mCircleRadius * 2 + mCircleWidth;

        mBackgroundPaint = getPaint(mCircleWidth, backgroundColor);
        mProgressPaint = getPaint(mCircleProgressWidth, progressColor);
        initProgressRect();
    }

    private int dp2px(int dp) {
        return (int) (dp * mDensity);
    }

    private Paint getPaint(int stokeWidth, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stokeWidth);
        paint.setColor(color);
        return paint;
    }

    private void initProgressRect() {
        if (mProgressRect == null) {
            mProgressRect = new RectF();
        }
        final float move = (mCircleProgressWidth) / 2.0f;
        mProgressRect.left = getPaddingLeft() + move;
        mProgressRect.top = getPaddingTop() + move;
        mProgressRect.right = getPaddingLeft() + mCircleRadius * 2 + mCircleWidth - move;
        mProgressRect.bottom = getPaddingTop() + mCircleRadius * 2 + mCircleWidth - move;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getPaddingLeft() + mViewSize + getPaddingRight(),
                getPaddingTop() + mViewSize + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float half = mViewSize / 2.0f;
        final float x = getPaddingLeft() + half;
        final float y = getPaddingTop() + half;
        canvas.drawCircle(x, y, mCircleRadius, mBackgroundPaint);

        final float sweepAngle = 1.0f * mProgress / mMax * 360;
        canvas.drawArc(mProgressRect, -90, sweepAngle, false, mProgressPaint);
    }

    /**
     * 设置进度最大值
     *
     * @param max
     */
    public void setMax(long max) {
        mMax = max;
        postInvalidate();
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;
        postInvalidate();
    }
}
