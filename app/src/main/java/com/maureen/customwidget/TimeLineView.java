package com.maureen.customwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Function: 时间线控件
 * Create:   2020/7/31
 *
 * @author Liaml
 */
public class TimeLineView extends View {
    private static final String TAG = "TimeLineView";

    /**
     * 中心圆画笔
     */
    private Paint mCenterCirclePaint;
    /**
     * 第一层圆环画笔
     */
    private Paint mFirstArcPaint;
    /**
     * 最外层圆环画笔
     */
    private Paint mSecondArcPaint;
    /**
     * 竖线画笔
     */
    private Paint mLinePaint;
    /**
     * 节点的中心圆半径
     */
    private int mNodeCenterRadius;
    /**
     * 节点的第一层圆环半径
     */
    private float mNodeFirstRingRadius;

    /**
     * 节点的最外层圆环半径
     */
    private float mNodeSecondRingRadius;
    /**
     * 节点的中心圆颜色
     */
    private int mNodeCenterColor;
    /**
     * 节点的第二层圆环颜色
     */
    private int mNodeEdgeColor;
    /**
     * 节点最外层圆环颜色
     */
    private int mNodeEdgeColorLight;
    /**
     * 时间线的节点数
     */
    private int mNodeCount;

    /**
     * 时间节点间距离，从上一个节点最外层圆环正下方到下一个节点最外层圆环正上方
     */
    private int mNodeDistance;

    /**
     * 时间轴竖线的长度
     */
    private int mLineLength;
    /**
     * 时间轴竖线的宽度
     */
    private int mLineWidth;

    /**
     * 时间轴竖线的颜色
     */
    private int mLineColor;

    /**
     * 一个节点所包含内容的View的高度
     */
    private int mNodeViewHeight;

    private final static int SHIFT_PX = 5;


    public TimeLineView(Context context) {
        this(context, null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TimeLineView);
        mNodeCenterRadius = (int) a.getDimension(R.styleable.TimeLineView_timeCenterNodeRadius, convertDp2Px(context, 2));
        mNodeFirstRingRadius = a.getDimension(R.styleable.TimeLineView_timeNodeFistRingRadius, convertDp2Px(context, 5));
        mNodeSecondRingRadius = a.getDimension(R.styleable.TimeLineView_timeNodeSecondRingRadius, convertDp2Px(context, 8));

        mNodeCenterColor = a.getColor(R.styleable.TimeLineView_timeNodeColor, Color.parseColor("#0060ff"));
        mNodeEdgeColor = a.getColor(R.styleable.TimeLineView_timeNodeEdgeColorLight, Color.parseColor("#330060ff"));
        mNodeEdgeColorLight = a.getColor(R.styleable.TimeLineView_timeNodeEdgeColor, Color.parseColor("#1a0060ff"));
        mLineColor = a.getColor(R.styleable.TimeLineView_timeLineColor, Color.parseColor("#e1e2e5"));

        mNodeCount = a.getInteger(R.styleable.TimeLineView_timeNodeCount, 0);
        mLineWidth = (int) a.getDimension(R.styleable.TimeLineView_timelineWidth, convertDp2Px(context, 1));
        mNodeDistance = (int) a.getDimension(R.styleable.TimeLineView_timeNodeDistance, convertDp2Px(context, 100));
        mNodeViewHeight = (int) a.getDimension(R.styleable.TimeLineView_timeNodeViewHeight, convertDp2Px(context, 116));
        mLineLength = (int) a.getDimension(R.styleable.TimeLineView_timelineLength, mNodeDistance + mNodeSecondRingRadius - mNodeCenterRadius);

        // 设置第一个节点的颜色
        mCenterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterCirclePaint.setColor(mNodeCenterColor);
        mCenterCirclePaint.setStyle(Paint.Style.FILL);
        // 设置第一个圆环
        mFirstArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFirstArcPaint.setStrokeWidth(convertDp2Px(context, 2));
        mFirstArcPaint.setColor(mNodeEdgeColor);
        // 设置第二个圆环
        mSecondArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondArcPaint.setColor(mNodeEdgeColorLight);
        mSecondArcPaint.setStrokeWidth(convertDp2Px(context, 2));
        mSecondArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置竖线画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mLineColor);
        a.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 默认设置时间轴的位置位于view的中间
        for (int j = 0; j <= mNodeCount - 1; j++) {
            // 画节点中心圆
            canvas.drawCircle(mNodeSecondRingRadius + SHIFT_PX, j * mNodeViewHeight + mNodeSecondRingRadius + SHIFT_PX, mNodeCenterRadius, mCenterCirclePaint);
            // 画节点第一层圆环
            canvas.drawCircle(mNodeSecondRingRadius + SHIFT_PX, j * mNodeViewHeight + mNodeSecondRingRadius + SHIFT_PX, mNodeFirstRingRadius, mFirstArcPaint);
            // 画节点最外层圆环
            canvas.drawCircle(mNodeSecondRingRadius + SHIFT_PX, j * mNodeViewHeight + mNodeSecondRingRadius + SHIFT_PX, mNodeSecondRingRadius, mSecondArcPaint);

            if (j == mNodeCount - 1) {
                // 最后一个节点不画竖线
                break;
            }
            // 画竖线
            canvas.drawRect(new Rect((int) (mNodeSecondRingRadius - mLineWidth / 2 + SHIFT_PX),
                    (int) (j * mNodeViewHeight + mNodeSecondRingRadius + mNodeCenterRadius + SHIFT_PX),
                    (int) (mNodeSecondRingRadius + mLineWidth / 2 + SHIFT_PX),
                    (int) ((j * mNodeViewHeight + mNodeSecondRingRadius) + mLineLength + mNodeSecondRingRadius + SHIFT_PX)), mLinePaint);
        }
    }


    /**
     * 转换dp为px
     */
    private int convertDp2Px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
