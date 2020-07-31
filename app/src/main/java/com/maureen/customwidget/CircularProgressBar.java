package com.maureen.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Function: 波纹效果圆形进度条
 * Date:   2019/11/30
 *
 * @author Lianml
 */
public class CircularProgressBar extends View {
    private static final String TAG = CircularProgressBar.class.getSimpleName();
    /**
     * view宽度
     */
    private int width;
    /**
     * view高度
     */
    private int height;

    /**
     * 外层圆环画笔
     */
    private Paint mBackgroundArcPaint;
    /**
     * 进度圆环画笔
     */
    private Paint mProgressArcPaint;

    /**
     * 中心实心圆画笔
     */
    private Paint mCenterCirclePaint;

    /**
     * 命中数量文本画笔
     */
    private Paint matchNumTextPaint;

    /**
     * 命中数量提示文本画笔
     */
    private Paint tipTextPaint;

    /**
     * 计时器文字画笔
     */
    private Paint timeTextPaint;

    private int center;

    /**
     * 半径
     */
    private int radius;

    /**
     * 外层矩形
     */
    private RectF mMiddleRect;

    /**
     * 进度矩形
     */
    private RectF mMiddleProgressRect;

    /**
     * 当前进度
     */
    private float mCurrentAngle = 0f;

    /**
     * 总进度
     */
    private float mTotalAngle = 360f;

    /**
     * 进度头画笔
     */
    private Paint mBitmapPaint;

    private Context mContext;
    private int barSize;
    private int barWidth;
    private int barBackgroundColor;
    private int barActionColor;
    private int maxWidth;
    private int maxHeight;

    private int matchNumTextSize;
    private int matchNumTextColor;
    private String matchNumText;

    private int tipTextSize;
    private int tipTextColor;
    private String tipText;


    private int timeTextSize;
    private int timeTextColor;
    private String timeText;
    /**
     * 圆环中心填充颜色
     */
    private int centerColor;


    public CircularProgressBar(Context context) {
        this(context, null);
        mContext = context;
    }


    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }


    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);
        barSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_barSize, dp2px(100));
        barWidth = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_barWidth, dp2px(3));
        barBackgroundColor = typedArray.getColor(R.styleable.CircularProgressBar_barBackgroundColor, ContextCompat.getColor(mContext, R.color.blue_dart));
        barActionColor = typedArray.getColor(R.styleable.CircularProgressBar_barActionColor, ContextCompat.getColor(mContext, android.R.color.white));

        matchNumTextSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_matchNumTextSize, 50);
        matchNumTextColor = typedArray.getColor(R.styleable.CircularProgressBar_matchNumTextColor, ContextCompat.getColor(mContext, (android.R.color.white)));
        matchNumText = typedArray.getString(R.styleable.CircularProgressBar_matchNumText);

        tipTextSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_tipTextSize, 17);
        tipTextColor = typedArray.getColor(R.styleable.CircularProgressBar_tipTextColor, ContextCompat.getColor(mContext, android.R.color.white));
        tipText = typedArray.getString(R.styleable.CircularProgressBar_tipText);

        timeTextSize = typedArray.getDimensionPixelSize(R.styleable.CircularProgressBar_timeTextSize, 15);
        timeTextColor = typedArray.getColor(R.styleable.CircularProgressBar_timeTextColor, ContextCompat.getColor(mContext, android.R.color.white));
        timeText = typedArray.getString(R.styleable.CircularProgressBar_timeText);

        centerColor = typedArray.getResourceId(R.styleable.CircularProgressBar_centerColor, ContextCompat.getColor(mContext, R.color.blue));

        if (TextUtils.isEmpty(matchNumText)) {
            matchNumText = "0";
        }

        if (TextUtils.isEmpty(tipText)) {
            tipText = "命中数量";
        }

        if (TextUtils.isEmpty(timeText)) {
            timeText = "00:00:00";
        }

        //中心实心圆画笔
        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setColor(centerColor);

        //外层圆环画笔
        mBackgroundArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundArcPaint.setStrokeWidth(barWidth);
        mBackgroundArcPaint.setColor(barBackgroundColor);
        mBackgroundArcPaint.setStyle(Paint.Style.STROKE);

        //外层进度画笔
        mProgressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置槽宽度
        mProgressArcPaint.setStrokeWidth(barWidth);
        //槽填充颜色
        mProgressArcPaint.setColor(barActionColor);
        mProgressArcPaint.setStyle(Paint.Style.STROKE);
        mProgressArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //命中数量文字画笔
        matchNumTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matchNumTextPaint.setTextSize(matchNumTextSize);
        matchNumTextPaint.setColor(matchNumTextColor);
        matchNumTextPaint.setTextAlign(Paint.Align.CENTER);

        //命中数量提示文字画笔
        tipTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tipTextPaint.setColor(tipTextColor);
        tipTextPaint.setTextSize(tipTextSize);
        tipTextPaint.setTextAlign(Paint.Align.CENTER);

        //计时器文本画笔
        timeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timeTextPaint.setColor(timeTextColor);
        timeTextPaint.setTextSize(timeTextSize);
        timeTextPaint.setTextAlign(Paint.Align.CENTER);
        typedArray.recycle();
    }

    public void setMatchNumText(String matchNumText) {
        this.matchNumText = matchNumText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public void setCenterColor(int centerColor) {
        mCenterCirclePaint.setColor(ContextCompat.getColor(mContext, centerColor));
    }

    public void setBackgroundArcColor(int backgroundArcColor) {
        mBackgroundArcPaint.setColor(ContextCompat.getColor(mContext, backgroundArcColor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int computedWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int computedHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        setMeasuredDimension(computedWidth, computedHeight);
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxWidth = w;
        maxHeight = h;
        width = barSize;
        height = barSize;
        radius = width / 2;
        mMiddleRect = new RectF((maxWidth / 2) - radius, (maxHeight / 2) - radius, (maxWidth / 2)
                + radius, (maxHeight / 2) + radius);

        mMiddleProgressRect = new RectF((maxWidth / 2) - radius, (maxHeight / 2) - radius, (maxWidth / 2)
                + radius, (maxHeight / 2) + radius);
        mMaxRadius = Math.min(w, h) * 1.0f / 2.0f - barWidth;
        mInitialRadius = radius * 1.0f + barWidth;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        center = getWidth() / 2;
        radius = barSize / 2;
        canvas.drawCircle(center, center, radius, mCenterCirclePaint);
        canvas.drawCircle(center, center, radius, mBackgroundArcPaint);
        drawText(canvas);

        //*******************波纹圆*********************
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mCirclePaint.setAlpha(circle.getAlpha());
                mCirclePaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(center, center, radius, mCirclePaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    private void drawText(Canvas canvas) {
        //正常绘制命中数量文本
        canvas.drawText(matchNumText, (maxWidth >> 1), (maxHeight / 2) - dp2px(8), matchNumTextPaint);
        //绘制命中数量提示文本
        canvas.drawText(tipText, (maxWidth >> 1), (maxHeight / 2) + dp2px(32), tipTextPaint);
        //绘制计时器文本
        canvas.drawText(timeText, (maxWidth >> 1), (maxHeight / 2) + dp2px(54), timeTextPaint);
    }


    public int dp2px(int values) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    //*************************************波纹圆*****************************************

    private Interpolator mInterpolator = new LinearInterpolator();

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 初始波纹半径
     */
    private float mInitialRadius = radius;
    /**
     * 最大波纹半径
     */
    private float mMaxRadius = (getWidth() - 30) / 2;
    /**
     * 一个波纹从创建到消失的持续时间
     */
    private long mDuration = 2000;
    /**
     * 波纹的创建速度，每500ms创建一个
     */
    private int mSpeed = 500;

    private boolean mIsRunning;
    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<Circle>();

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    public void setWaveColor(int color) {
        mCirclePaint.setColor(color);
    }

    public void setWaveWidth(int width) {
        mCirclePaint.setStrokeWidth(dp2px(width));
    }

    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setMaxRadius(float maxRadius) {
        mMaxRadius = maxRadius;
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }
}
