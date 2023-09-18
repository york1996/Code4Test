package com.york1996.samplecode4varlens.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class AdjustProtractorView extends View {

    private static final String TAG = "AdjustProtractorView";

    public interface ProtractorAdjustViewChangeListener {
        void onValueChange(float currentValue);
    }

    private Paint mProtractorPaint;
    private Paint mTextPaint;
    private Rect mTextBounds;
    private RectF mArcRect;
    private ProtractorAdjustViewChangeListener mListener;

    private int mRadius;
    private int mCenterX;
    private int mCenterY;

    private float mStartAngle;
    private float mTouchDownViewRotation;

    private int mDivisionCount = 90; // 整个半圆刻度线的总数
    private int mMajorDivisionEvery = 5; // 每隔几个刻度显示数字

    private float mMinAngle = 0f; // 最小角度
    private float mMaxAngle = 180f; // 最大角度

    private float mMaxValue = 180;
    private float mCurrentValue = mMaxValue / 2f;
    private float mCurrentCustomRotation = 0;

    public AdjustProtractorView(Context context) {
        this(context, null);
    }

    public AdjustProtractorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustProtractorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO(xinyu.zhang): 半圆范围横向角度处理
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartAngle = (float) Math.toDegrees(Math.atan2(event.getY() - getPivotY(), event.getX() - getPivotX()));
                mTouchDownViewRotation = mCurrentCustomRotation;
                break;
            case MotionEvent.ACTION_MOVE:
                double currentAngle = Math.toDegrees(Math.atan2(event.getY() - getPivotY(), event.getX() - getPivotX()));
                float rotationAngle = (float) (currentAngle - mStartAngle);
                setViewRotation(mTouchDownViewRotation + rotationAngle);
                break;
        }
        return true;
    }

    public void setProtractorAdjustViewChangeListener(ProtractorAdjustViewChangeListener listener) {
        mListener = listener;
    }

    public float getValue() {
        return mCurrentValue;
    }

    public void setValue(float currentValue) {

    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    public void setDivisionCount(int count) {
        mDivisionCount = count;
    }

    private void init() {
        mProtractorPaint = new Paint();
        mProtractorPaint.setColor(Color.BLACK);
        mProtractorPaint.setStyle(Paint.Style.STROKE);
        mProtractorPaint.setStrokeWidth(2);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(24);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTextBounds = new Rect();
    }

    private void setViewRotation(float rotation) {
        if (getValue() > mMaxValue || getValue() < 0) {
            return;
        }
        rotation = (rotation + 360) % 360; // 角度范围0-360
        if (rotation > 90 && rotation < 270) {
            return;
        }
        mCurrentCustomRotation = rotation;
        Log.d(TAG, "setViewRotation = mCurrentCustomRotation = " + mCurrentCustomRotation + " Value = " + calculateValue());
        invalidate();
        notifyValueChange(calculateValue());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mRadius = h / 2 - 20;
        mCenterX = w;
        mCenterY = h / 2;
        mArcRect = new RectF(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // 画半圆
        canvas.drawArc(mArcRect, 0, 360, false, mProtractorPaint);
        // 旋转
        canvas.rotate(mCurrentCustomRotation, mCenterX, mCenterY);
        // 画刻度和数字
        for (int i = 0; i <= mDivisionCount; i++) {
            double currentAngle = i * (180f / mDivisionCount);
            if (currentAngle < mMinAngle || currentAngle > mMaxAngle) {
                continue;
            }
            double angleRadians = Math.toRadians(90 - currentAngle); // 求相对于垂直方向的角度
            float startX = (float) (mCenterX - mRadius * Math.cos(angleRadians));
            float startY = (float) (mCenterY + mRadius * Math.sin(angleRadians));

            float endX = (float) (mCenterX - (mRadius - 15) * Math.cos(angleRadians));
            float endY = (float) (mCenterY + (mRadius - 15) * Math.sin(angleRadians));

            mTextPaint.setColor(Color.RED);
            mProtractorPaint.setColor(Color.BLACK);

            // 分隔个数对刻度线和文字进行特殊绘制
            if (i % mMajorDivisionEvery == 0) {
                // 文字绘制
                float showingValue = i * (mMaxValue / mDivisionCount);
                String labelText = String.valueOf((int) Math.round(showingValue));
                mTextPaint.setColor(Color.RED);
                mTextPaint.getTextBounds(labelText, 0, labelText.length(), mTextBounds);

                // 计算数字位置的坐标并绘制数字
                double angleRotationRadians = Math.toRadians(90 - currentAngle - mCurrentCustomRotation);
                float textX = mCenterX - (mRadius - 50) * (float) Math.cos(angleRotationRadians);
                float textY = mCenterY + (mRadius - 50) * (float) Math.sin(angleRotationRadians) + mTextBounds.height() / 2f;
                canvas.rotate(-mCurrentCustomRotation, mCenterX, mCenterY);
                canvas.drawText(labelText, textX, textY, mTextPaint);
                canvas.rotate(mCurrentCustomRotation, mCenterX, mCenterY);

                // 刻度线延长
                endX = (float) (mCenterX - (mRadius - 30) * Math.cos(angleRadians));
                endY = (float) (mCenterY + (mRadius - 30) * Math.sin(angleRadians));
                mProtractorPaint.setColor(Color.RED);
            }
            canvas.drawLine(startX, startY, endX, endY, mProtractorPaint);
        }
        canvas.restore();
    }

    private void notifyValueChange(float value) {
        mCurrentValue = value;
        if (mListener != null) {
            mListener.onValueChange(value);
        }
    }

    private float calculateValue() {
        float baseValue = mMaxValue / 2;
        float protractorLength = (float) (mRadius * Math.PI);
        if (mCurrentCustomRotation > 270) {
            // 加
            float rotationLength = protractorLength * ((360 - mCurrentCustomRotation) / 180);
            return baseValue + mMaxValue * (rotationLength / protractorLength);
        } else if (mCurrentCustomRotation <= 90 && mCurrentCustomRotation >= 0) {
            // 减
            float rotationLength = protractorLength * (mCurrentCustomRotation / 180);
            return  baseValue - mMaxValue * (rotationLength / protractorLength);
        }
        return 0;
    }
}
