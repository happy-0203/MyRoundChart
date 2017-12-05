package com.example.cc.mypiechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2017/12/5.
 */

public class MyPieChart extends View {

    private List<PieEntry> mPieEntries;
    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private float mSRadius;
    private float mRadius;

    public MyPieChart(Context context) {
        this(context, null);
    }

    public MyPieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        mPieEntries = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(DensityUtils.sp2px(getContext(), 12));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算总值
        float total = 0;

        for (int i = 0; i < mPieEntries.size(); i++) {
            total += mPieEntries.get(i).getNumber();
        }

        //获取View中心点的坐标
        mCenterX = getPivotX();
        mCenterY = getPivotY();


        //设置半径
        //选中的半径是View宽高中较小值的一半
        if (mSRadius == 0) {
            mSRadius = (getWidth() > getHeight() ? getHeight() / 2 : getWidth() / 2);
        }
        //未选择中扇形的半径
        mRadius = mSRadius - DensityUtils.dp2px(getContext(), 5);

        //设置起始角度为0
        float startC = 0;

        //遍历换扇形
        for (int i = 0; i < mPieEntries.size(); i++) {
            //计算当前扇形扫过的角度
            float sweep = 0;
            if (total <= 0) {
                //没有设置数值
                sweep = 360 / mPieEntries.size();
            } else {
                sweep = 360 * (mPieEntries.get(i).getNumber() / total);
            }

            //设置当前扇形的颜色
            mPaint.setColor(getResources().getColor(mPieEntries.get(i).getColorRes()));

            //判断当前那个扇形被选中,确定半径
            float radiusT;
            if (mPieEntries.get(i).isSelected()) {
                radiusT = mSRadius;
            } else {
                radiusT = mRadius;
            }
            //画扇形
            RectF rectF = new RectF(mCenterX - radiusT, mCenterY - radiusT, mCenterX + radiusT, mCenterY + radiusT);
            canvas.drawArc(rectF, startC, sweep, true, mPaint);

            //画文字和直线
            if ((mPieEntries.get(i).getNumber() > 0 && total > 0) || (mPieEntries.get(i).getNumber() <= 0 && total <= 0)) {
                float arcCenterC = startC + sweep/2;//当前扇形弧线的中间点与圆心的连线与起始角度的夹角
                //短线起始点的坐标
                float arcCenterX = 0;
                float arcCenterY = 0;

                //短线终点坐标
                float arcCenterX2 = 0;
                float arcCenterY2 = 0;

                //格式化数字
                DecimalFormat numberFormat = new DecimalFormat("00.00");
                mPaint.setColor(Color.BLACK);
                //分象限 利用三角函数 来求出每个短线的起始点和结束点，并画出短线和百分比。
                //具体的计算方法看下面图示介绍

                if (arcCenterC>=0 && arcCenterC < 90){
                    arcCenterX = (float) (mCenterX+radiusT*Math.cos(arcCenterC * Math.PI/180));
                    arcCenterY = (float) (mCenterY +radiusT*Math.sin(arcCenterC *Math.PI/180));
                    arcCenterX2 = (float) (arcCenterX+DensityUtils.dp2px(getContext(), 10)*Math.cos(arcCenterC*Math.PI/180));
                    arcCenterY2 = (float) (arcCenterY+DensityUtils.dp2px(getContext(), 10)*Math.sin(arcCenterC*Math.PI/180));
                    canvas.drawLine(arcCenterX,arcCenterY,arcCenterX2,arcCenterY2,mPaint);

                    //画文字
                    if (total <= 0){
                        canvas.drawText(numberFormat.format(0)+"%",arcCenterX2,arcCenterY2+mPaint.getTextSize()/2,mPaint);
                    }else {
                        canvas.drawText(numberFormat.format((mPieEntries.get(i).getNumber()/total)*100),arcCenterX2,arcCenterY2+mPaint.getTextSize()/2,mPaint);
                    }
                }else if (arcCenterC >= 90 && arcCenterC < 180){
                    //在第二象限
                    arcCenterC = 180 - arcCenterC;

                    arcCenterX = (float) (mCenterX - radiusT*Math.cos(arcCenterC*Math.PI/180));
                    arcCenterY = (float) (mCenterY+radiusT*Math.sin(arcCenterC*Math.PI/180));

                    arcCenterX2 = (float) (arcCenterX-DensityUtils.dp2px(getContext(), 10)*Math.cos(arcCenterC*Math.PI/180));
                    arcCenterY2 = (float) (arcCenterY+DensityUtils.dp2px(getContext(), 10)*Math.sin(arcCenterC*Math.PI/180));

                    canvas.drawLine(arcCenterX,arcCenterY,arcCenterX2,arcCenterY2,mPaint);

                    //画文字
                    if (total <= 0){
                        canvas.drawText(numberFormat.format(0)+"%",arcCenterX2,arcCenterY2+mPaint.getTextSize()/2,mPaint);
                    }else {
                        canvas.drawText(numberFormat.format((mPieEntries.get(i).getNumber()/total)*100),(float) (arcCenterX2 - mPaint.getTextSize() * 3.5),arcCenterY2+mPaint.getTextSize()/2,mPaint);
                    }
                } else if (arcCenterC >= 180 && arcCenterC < 270) {
                    arcCenterC = 270 - arcCenterC;
                    arcCenterX = (float) (mCenterX - radiusT * Math.sin(arcCenterC * Math.PI / 180));
                    arcCenterY = (float) (mCenterY - radiusT * Math.cos(arcCenterC * Math.PI / 180));
                    arcCenterX2 = (float) (arcCenterX - DensityUtils.dp2px(getContext(), 10) * Math.sin(arcCenterC * Math.PI / 180));
                    arcCenterY2 = (float) (arcCenterY - DensityUtils.dp2px(getContext(), 10) * Math.cos(arcCenterC * Math.PI / 180));
                    canvas.drawLine(arcCenterX, arcCenterY, arcCenterX2, arcCenterY2, mPaint);
                    if (total <= 0) {
                        canvas.drawText(numberFormat.format(0) + "%", (float) (arcCenterX2 - mPaint.getTextSize() * 3.5), arcCenterY2, mPaint);
                    } else {
                        canvas.drawText(numberFormat.format(mPieEntries.get(i).getNumber() / total * 100) + "%", (float) (arcCenterX2 - mPaint.getTextSize() * 3.5), arcCenterY2, mPaint);
                    }
                } else if (arcCenterC >= 270 && arcCenterC < 360) {
                    arcCenterC = 360 - arcCenterC;
                    arcCenterX = (float) (mCenterX + radiusT * Math.cos(arcCenterC * Math.PI / 180));
                    arcCenterY = (float) (mCenterY- radiusT * Math.sin(arcCenterC * Math.PI / 180));
                    arcCenterX2 = (float) (arcCenterX + DensityUtils.dp2px(getContext(), 10) * Math.cos(arcCenterC * Math.PI / 180));
                    arcCenterY2 = (float) (arcCenterY - DensityUtils.dp2px(getContext(), 10) * Math.sin(arcCenterC * Math.PI / 180));
                    canvas.drawLine(arcCenterX, arcCenterY, arcCenterX2, arcCenterY2, mPaint);
                    if (total <= 0) {
                        canvas.drawText(numberFormat.format(0) + "%", arcCenterX2, arcCenterY2, mPaint);
                    } else {
                        canvas.drawText(numberFormat.format(mPieEntries.get(i).getNumber() / total * 100) + "%", arcCenterX2, arcCenterY2, mPaint);
                    }
                }
            }


            //将每个扇形的起始角度 和 结束角度 放入对应的对象
            mPieEntries.get(i).setStartC(startC);
            mPieEntries.get(i).setEndC(startC + sweep);
            //将当前扇形的结束角度作为下一个扇形的起始角度
            startC += sweep;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //按下
            float downX = event.getX();
            float downY = event.getY();
            //如果点击的地方在扇形区域内
            if (Math.pow(downX-mCenterX,2)+Math.pow(downY-mCenterY,2) <= Math.pow(mRadius,2)){
                //计算 touch点和圆心的连线 与 x轴正方向的夹角
                float touchC = getSweep(downX, downY);
                //遍历 List<PieEntry> 判断touch点在哪个扇形中
                for (int i = 0; i < mPieEntries.size(); i++) {
                    if (touchC >= mPieEntries.get(i).getStartC() && touchC < mPieEntries.get(i).getEndC()) {
                        mPieEntries.get(i).setSelected(true);
                        if (listener != null)
                            listener.onItemClick(i); //将被点击的扇形id回调出去
                    } else {
                        mPieEntries.get(i).setSelected(false);
                    }
                }
                invalidate();//刷新画布
            }
        }

        return super.onTouchEvent(event);
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    private OnItemClickListener listener; //点击事件的回调

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    /**
     * 获取  touch点/圆心连线  与  x轴正方向 的夹角
     *
     * @param touchX
     * @param touchY
     */
    private float getSweep(float touchX, float touchY) {
        float xZ = touchX - mCenterX;
        float yZ = touchY - mCenterY;
        float a = Math.abs(xZ);
        float b = Math.abs(yZ);
        double c = Math.toDegrees(Math.atan(b / a));
        if (xZ >= 0 && yZ >= 0) {//第一象限
            return (float) c;
        } else if (xZ <= 0 && yZ >= 0) {//第二象限
            return 180 - (float) c;
        } else if (xZ <= 0 && yZ <= 0) {//第三象限
            return (float) c + 180;
        } else {//第四象限
            return 360 - (float) c;
        }
    }





    public void setPieEntries(List<PieEntry> entries) {
        this.mPieEntries = entries;
        invalidate();
    }

    /**
     * 设置半径
     * @param radius
     */
    public void setRadius(float radius) {
        this.mSRadius = radius;
    }
}
