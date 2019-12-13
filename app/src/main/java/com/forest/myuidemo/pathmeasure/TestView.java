package com.forest.myuidemo.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;


public class TestView extends View {

    private static final String TAG = "MyView";
    private int mViewWidth;
    private int mViewHeight;
    private Paint mDeafultPaint;

    private Paint mPaint;

    public TestView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mDeafultPaint = new Paint();
        mDeafultPaint.setColor(Color.RED);
        mDeafultPaint.setStrokeWidth(5);
        mDeafultPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 平移坐标系
        canvas.translate(mViewWidth/2,mViewHeight/2);
        // 画坐标线
        canvas.drawLine(-canvas.getWidth(),0,canvas.getWidth(),0,mPaint);
        canvas.drawLine(0,-canvas.getHeight(),0,canvas.getHeight(),mPaint);

//        testForceClosed(canvas);
//        testNextContour(canvas);
//        testGetSegment(canvas);
        testGetSegmentMoveTo(canvas);
    }

    /**
     *
     * @param canvas
     */
    private void testForceClosed(Canvas canvas) {
        Path path = new Path();

        path.lineTo(0,200);
        path.lineTo(200,200);
        path.lineTo(200,0);

        PathMeasure measure1 = new PathMeasure(path,false);
        //一但测量如果是闭合状态，那么不管原型的Path是怎样的，
        //这边在进行测量的时候回默认闭合
        //Path的测量不会影响原本的Path
        PathMeasure measure2 = new PathMeasure(path,true);

        Log.e(TAG, "forceClosed=false length = "+measure1.getLength());
        Log.e(TAG, "forceClosed=true length = "+measure2.getLength());

        canvas.drawPath(path,mDeafultPaint);

    }

    private void testNextContour(Canvas canvas) {
        Path path = new Path();
        Path path1 = new Path();
        Path path2 = new Path();
        // 添加小矩形
        path1.addRect(-100, -100, 100, 100, Path.Direction.CW);
        PathMeasure measure2 = new PathMeasure(path1, false);
        Log.i(TAG,"path1:"+measure2.getLength());

        // 添加大矩形
        //path.addRect(-200, 200, 200, 600, Path.Direction.CW);
        path2.addRect(-200, -200, 200, 200, Path.Direction.CW);

        PathMeasure measure3 = new PathMeasure(path2, false);
        Log.i(TAG,"path2:"+measure3.getLength());

        /**
         * 对两个Path进行布尔运算
         * XOR保留path1与path2不共同的部分;
         */
        path.op(path1,path2, Path.Op.XOR);
        canvas.drawPath(path,mDeafultPaint);

        PathMeasure measure = new PathMeasure(path, false);
        Log.i(TAG,"path:"+measure.getLength());
        float[] tan = new float[2];
        float[] pos = new float[2];



        //三个参数
        //distance 指定Path路径上的长度
//        两个长度为2的浮点数组，这个数组时用来接收数据
        //tan 当前位置的正切点XY
        //pos 当前Path路径点的XY
        measure.getPosTan(50f,pos,tan);
        Log.i(TAG,"----------------------pos[0] = " + pos[0] + "---pos[1] = " +pos[1]);
        Log.i(TAG,"----------------------tan[0] = " + tan[0] + "---tan[1] = " +tan[1]);
        canvas.drawLine(tan[0],tan[1],pos[0],pos[1],mPaint);

        float len1 = measure.getLength();
        // 跳转到下一条路径
        measure.nextContour();

        measure.getPosTan(0f,pos,tan);
        Log.i(TAG,"----------------------pos[0] = " + pos[0] + "pos[1] = " +pos[1]);
        Log.i(TAG,"----------------------tan[0] = " + tan[0] + "tan[1] = " +tan[1]);
        canvas.drawLine(tan[0],tan[1],pos[0],pos[1],mDeafultPaint);
        float len2 = measure.getLength();
        Log.i(TAG,"len1 = "+len1);
        Log.i(TAG,"len2 = "+len2);
    }

    /**
     * 路径截取，且不移动开始点
     * @param canvas
     */
    private void testGetSegment(Canvas canvas) {
        Path path = new Path();
        // 创建Path并添加了一个矩形(顺时针)
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();
        // 将 Path 与 PathMeasure 关联
        PathMeasure measure = new PathMeasure(path, false);

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        measure.getSegment(200, 600, dst, true);

        canvas.drawPath(path,mPaint);
        // 绘制 dst
        canvas.drawPath(dst, mDeafultPaint);
    }

    /**
     * 路径截取，移动开始点
     * @param canvas
     */
    private void testGetSegmentMoveTo(Canvas canvas) {
        Path path = new Path();
        // 创建Path并添加了一个矩形
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();
        dst.lineTo(-300, -300);
        // 将 Path 与 PathMeasure 关联
        PathMeasure measure = new PathMeasure(path, false);

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        measure.getSegment(200, 600, dst, false);
        //measure.getSegment(200, 600, dst, true);

        canvas.drawPath(path,mPaint);
        // 绘制 dst
        canvas.drawPath(dst, mDeafultPaint);

    }

}
