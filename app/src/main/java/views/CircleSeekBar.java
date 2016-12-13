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
    private static final double RADIAN=180/Math.PI;
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
    private int mArcWidth=20;
    //起始角度,绘制的时候会用到
    private int mStartAngle =135;

    //初始角度
    private int mCalculateStartAngle;
    //最大角度
    private int mMaxAngle =270;
    //当前的角度
    private int mCurrentAngle=0;


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

        mCalculateStartAngle = mStartAngle%90;
        mArcPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(Color.BLUE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStyle(Paint.Style.FILL);
        setBackgroundColor(Color.GREEN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=measureWidth(widthMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
//        //取宽度和高度的最小值
//        int diameter=Math.min(width,height);
        setMeasuredDimension(width,height);
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
            result=30;
            if(specMode==MeasureSpec.AT_MOST){
                result=Math.min(result,specSize);
            }
        }
        return result;
    }
    private int measureHeight(int heightMeasureSpec){
        return measureWidth(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
         mRadius= Math.min(w,h)/2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        RectF reactF=new RectF(0+mArcWidth,0+mArcWidth,2*mRadius-mArcWidth,2*mRadius-mArcWidth);
        canvas.drawArc(reactF, mStartAngle,mCurrentAngle,false, mArcPaint);
        canvas.drawCircle(mPointX,mPointY,mArcWidth/2, mPointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int x= (int) event.getX();
        int y= (int) event.getY();

       switch (event.getAction()){
           case MotionEvent.ACTION_DOWN:
                if(!isValid(x,y)){
                    return false;
                }
               calculateAngle(x,y);
               break;
           case MotionEvent.ACTION_MOVE:
               calculateAngle(x,y);
               break;
           case MotionEvent.ACTION_UP:

               break;
       }
        invalidate();
        return true;
    }
    private void calculateAngle(int x,int y){
        int angle;
        //斜边
        int hypotenuse;
        hypotenuse= (int) Math.sqrt(Math.pow(x-mRadius,2)+Math.pow(y-mRadius,2));
        double sin=(double)(y-mRadius)/hypotenuse;
        float pointX;
        float pointY;
        if(x-mRadius<0){
            angle= (int) (90-Math.asin(sin)*RADIAN);
            //计算小圆点坐标
            pointX=(float) (mRadius- (mRadius-mArcWidth)*Math.sqrt(1-sin*sin));
            pointY= (float) (mRadius+(mRadius-mArcWidth)*sin);
            Log.d("c_angle","left:"+angle);
        }else{
            angle= (int) (180+90+Math.asin(sin)*RADIAN);
            //计算小圆点坐标
            pointX= (float) (mRadius+(mRadius-mArcWidth)*Math.sqrt(1-sin*sin));
            pointY= (float) (mRadius+(mRadius-mArcWidth)*sin);
            Log.d("c_angle","right:"+angle);
        }
        if(angle>= mCalculateStartAngle &&angle<=mMaxAngle+ mCalculateStartAngle){
            mCurrentAngle= angle- mCalculateStartAngle;
            mPointX=pointX;
            mPointY=pointY;
        }


    }


    /**
     * 判断点是否在弧形的半径内
     * @param x
     * @param y
     * @return true在   false不在
     */
    private boolean isValid(int x,int y){
        return Math.pow(x-mRadius,2)+Math.pow(y-mRadius,2)<=mRadius*mRadius;
    }
}
