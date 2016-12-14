package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CircleSeekBar extends View
{
    private static final double RADIAN = 180 / Math.PI;
    //半径
    private int mRadius;
    //画弧的笔
    private Paint mArcPaint;
    //弧尽头的小圆点
    private Paint mPointPaint;
    //小圆点的X坐标
    private float mPointX;
    //小圆点的Y坐标
    private float mPointY;
    //弧线宽度
    private int mArcWidth = 20;
    //起始角度,绘制的时候会用到
    private int mStartAngle = 135;

    //初始角度
    private int mCalculateStartAngle;
    //最大角度
    private int mMaxAngle = 270;
    //当前的角度
    private float mCurrentAngle = 0;


    public CircleSeekBar(Context context)
    {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {

        mCalculateStartAngle = mStartAngle % 90;
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(Color.BLUE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStyle(Paint.Style.FILL);
        setBackgroundColor(Color.GREEN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
//        //取宽度和高度的最小值
//        int diameter=Math.min(width,height);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec)
    {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        } else
        {
            result = 30;
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec)
    {
        return measureWidth(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        RectF reactF = new RectF(getPaddingLeft() + mArcWidth, getPaddingTop() + mArcWidth, getPaddingLeft() + 2 * mRadius - mArcWidth, getPaddingTop() + 2 * mRadius - mArcWidth);
        canvas.drawArc(reactF, mStartAngle, mCurrentAngle, false, mArcPaint);
        if (mCurrentAngle != 0)
        {
            canvas.drawCircle(mPointX, mPointY, mArcWidth / 2, mPointPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (!isValid(x, y))
                {
                    return false;
                }
                calculateAngle(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                calculateAngle(x, y);
                break;
            case MotionEvent.ACTION_UP:
                calculateAngle(x, y);
                //添加粘性效果
                if (mCurrentAngle <= 5)
                {
                    mCurrentAngle = 0;
                }
                break;
        }
        invalidate();
        return true;
    }

    private void calculateAngle(float x, float y)
    {
        float angle;
        //斜边
        double hypotenuse;
        hypotenuse = Math.sqrt(Math.pow(x - mRadius, 2) + Math.pow(y - mRadius, 2));
        double sin = (double) (y - mRadius) / hypotenuse;
        float pointX;
        float pointY;
        boolean isLeft = x - mRadius < 0;
        if (isLeft)
        {
            angle = (float) (90 - Math.asin(sin) * RADIAN);
            //计算小圆点坐标
            pointX = calculatePointX(isLeft, sin);
            pointY = calculatePointY(sin);
            Log.d("c_angle", "left:" + angle);
        } else
        {
            angle = (float) (180 + 90 + Math.asin(sin) * RADIAN);
            //计算小圆点坐标
            pointX = calculatePointX(isLeft, sin);
            pointY = calculatePointY(sin);
            Log.d("c_angle", "right:" + angle);
        }
        if (angle >= mCalculateStartAngle && angle <= mMaxAngle + mCalculateStartAngle)
        {
            mCurrentAngle = Math.round(angle - mCalculateStartAngle);
            Log.d("cur_angle", "mCurrentAngle:" + mCurrentAngle);
            mPointX = pointX;
            mPointY = pointY;
        }
    }

    /**
     * 计算小圆点的X坐标
     *
     * @param isLeft 判断点是否位于弧形的左半部分
     * @param sin    #calculateAngle计算出来的sin值
     * @return 小圆点的X坐标
     */
    private float calculatePointX(boolean isLeft, double sin)
    {
        return isLeft ? (float) (mRadius - (mRadius - mArcWidth) * Math.sqrt(1 - sin * sin)) + getPaddingLeft() :
                (float) (mRadius + (mRadius - mArcWidth) * Math.sqrt(1 - sin * sin)) + getPaddingLeft();
    }

    private float calculatePointY(double sin)
    {
        return (float) (mRadius + (mRadius - mArcWidth) * sin) + getPaddingTop();
    }


    /**
     * 判断点是否在弧形的半径内
     *
     * @param x
     * @param y
     * @return true在   false不在
     */
    private boolean isValid(float x, float y)
    {
        return Math.pow(x - mRadius - getPaddingLeft(), 2) + Math.pow(y - mRadius - getPaddingTop(), 2) <= mRadius * mRadius;
    }
}
