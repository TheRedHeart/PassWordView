package com.lxj.passView.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.lxj.passView.utils.DensityUtil;


/**
 * 密码框
 * Created by lxj on 2017-03-04.
 */

public class PassWordView extends View {

    private Paint mPaint;   //绘制对象

    private Handler mHandler; //用于指示输入提示

    private boolean isDrawText;//是否绘制文本

    private boolean isInputState = false;//是否输入状态

    private boolean mDrawRemindLineState;  //竖线状态控制  true 显示

    private int mInputStateBoxColor;  //输入状态下框颜色
    private int mNoInputStateBoxColor;//未输入状态下框颜色

    private int mRemindLineColor;  //提示输入线颜色
    private int mInputStateTextColor;  //输入后文字图案提示颜色


    private int mBoxDrawType = 0;//盒子图画类型 0 矩形 2圆形 3横线
    private int mShowPassType = 0;// 0 提示图案为实心圆 1提示图案为*

    private boolean isShowRemindLine = true;// true 显示提示光标 默认显示

    private int mWidth = 40;
    private int mheight = 40;

    private String mPassText = "";//要绘制的文字

    private Context mContext;

    private int mDrawTxtSize = 18;

    private int mDrawBoxLineSize = 4;

    public void setInputStateColor(int inputColor) {
        this.mInputStateBoxColor = inputColor;
    }

    public void setmPassText(String mPassText) {
        this.mPassText = mPassText;
    }

    public void setNoinputColor(int noinputColor) {
        this.mNoInputStateBoxColor = noinputColor;
    }

    public void setInputState(boolean input) {
        isInputState = input;
    }

    public void setRemindLineColor(int line_color) {
        this.mRemindLineColor = line_color;
    }


    public void setInputStateTextColor(int drc_color) {
        this.mInputStateTextColor = drc_color;
    }


    public PassWordView(Context context) {
        this(context, null);
    }

    public PassWordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setmBoxDrawType(int mBoxDrawType) {
        this.mBoxDrawType = mBoxDrawType;
    }


    public void setmShowPassType(int mShowPassType) {
        this.mShowPassType = mShowPassType;
    }

    public void setmDrawTxtSize(int mDrawTxtSize) {
        this.mDrawTxtSize = mDrawTxtSize;
    }


    public void setmIsShowRemindLine(boolean mIsShowShan) {
        this.isShowRemindLine = mIsShowShan;
    }

    public void setmDrawBoxLineSize(int mDrawBoxLineSize) {
        this.mDrawBoxLineSize = mDrawBoxLineSize;
    }

    public PassWordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        // 设置“空心”的外框的宽度
        mPaint.setStrokeWidth(mDrawBoxLineSize);
        mPaint.setPathEffect(new CornerPathEffect(1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (modeWidth == MeasureSpec.EXACTLY) {//如果是精确测量 则直接返回值
            width = sizeWidth;
        } else {//指定宽度的大小
            width = DensityUtil.dip2px(mContext, mWidth);
            if (modeWidth == MeasureSpec.AT_MOST) {//如果是最大值模式  取当中的小值  防止超出父类控件的最大值
                width = Math.min(width, sizeWidth);
            }
        }

        if (modeHeight == MeasureSpec.EXACTLY) {//如果是精确测量 则直接返回值
            height = sizeHeight;
        } else {//指定高度的大小
            height = DensityUtil.dip2px(mContext, mheight);
            if (modeHeight == MeasureSpec.AT_MOST) {//如果是最大值模式  取当中的小值  防止超出父类控件的最大值
                height = Math.min(height, sizeHeight);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawInputBox(canvas);    //绘制输入框

        drawRemindLine(canvas);  //绘制提醒线

        drawInputTextOrPicture(canvas);         //绘制输入文本或密码图案

    }

    /**
     *  //绘制输入文本或密码图案
     *
     * @param canvas
     */
    private void drawInputTextOrPicture(Canvas canvas) {
        if (isDrawText) {            //是否需要进行绘制

            mPaint.setColor(ContextCompat.getColor(mContext, mInputStateTextColor));
            mPaint.setStyle(Paint.Style.FILL);//实心
            switch (mShowPassType) {
                case 0:                                     //绘制圆心
                    canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 12, mPaint);
                    break;
                case 1:                                      //绘制*
                    mPaint.setTextSize(getMeasuredWidth() / 2 + 10);
                    float stringWidth = mPaint.measureText("*");

                    float baseY = (getMeasuredHeight() / 2 - ((mPaint.descent() + mPaint.ascent()) / 2)) + stringWidth / 3;  //实现y轴居中方法
                    float baseX = getMeasuredWidth() / 2 - stringWidth / 2;  //实现X轴居中方法
                    canvas.drawText("*", baseX, baseY, mPaint);
                    break;
                case 2:                                     //绘制输入数据
                    mPaint.setTextSize(DensityUtil.sp2px(mContext, mDrawTxtSize));//绘制字体大小
                    float stringWidth2 = mPaint.measureText(mPassText);

                    float baseY2 = (getMeasuredHeight() / 2 - ((mPaint.descent() + mPaint.ascent()) / 2)) + stringWidth2 / 5;  //实现y轴居中方法
                    float baseX2 = getMeasuredWidth() / 2 - stringWidth2 / 2;  //实现X轴居中方法
                    canvas.drawText(mPassText, baseX2, baseY2, mPaint); //文字
                    break;
            }


        }
    }

    /**
     * 绘制提示线
     *
     * @param canvas
     */
    private void drawRemindLine(Canvas canvas) {
        if (mDrawRemindLineState && isShowRemindLine) {  // mDrawRemindLineState 控制闪烁情况  //isShowRemindLine 是否绘制提示线
            int line_height = getMeasuredWidth() / 2 - 10;

            line_height = line_height < 0 ? getMeasuredWidth() / 2 : line_height;

            mPaint.setStyle(Paint.Style.FILL);//实心
            mPaint.setColor(ContextCompat.getColor(mContext, mRemindLineColor));
            canvas.drawLine(getMeasuredWidth() / 2, getMeasuredHeight() / 2 - line_height / 2, getMeasuredWidth() / 2, getMeasuredHeight() / 2 + line_height / 2, mPaint);
        }
    }

    /**
     * 绘制输入框
     *
     * @param canvas
     */
    private void drawInputBox(Canvas canvas) {
        if (isInputState) { //是否是输入状态  输入状态和未输入状态颜色区分
            mPaint.setColor(ContextCompat.getColor(mContext, mInputStateBoxColor));
        } else {
            mPaint.setColor(ContextCompat.getColor(mContext, mNoInputStateBoxColor));
        }
        mPaint.setStyle(Paint.Style.STROKE);//空心
        switch (mBoxDrawType) {
            case 1:         //绘制圆形
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 - 5, mPaint);
                break;
            case 2:        //绘制横线
                canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), mPaint);
                break;
            default:// 绘制矩形
                RectF rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawRoundRect(rect, 6, 6, mPaint);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 刷新状态
     *
     * @param isinput 是否已经输入过密码
     */
    public void updateInputState(boolean isinput) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (isinput) {
            isInputState = true;
            isDrawText = true;
        } else {
            isInputState = false;
            isDrawText = false;
        }
        mDrawRemindLineState = false;
        invalidate();
    }

    /**
     * 设置为选中输入状态
     */
    public void startInputState() {
        isInputState = true;
        isDrawText = false;

        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.removeCallbacksAndMessages(null);

        if (!isShowRemindLine) {
            invalidate();
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {                 //循环绘制 造成闪动状态
                mDrawRemindLineState = !mDrawRemindLineState;
                invalidate();
                mHandler.postDelayed(this, 800);

            }
        });
    }

}
