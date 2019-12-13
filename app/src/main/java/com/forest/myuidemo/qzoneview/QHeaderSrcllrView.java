package com.forest.myuidemo.qzoneview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.forest.myuidemo.R;

public class QHeaderSrcllrView extends ListView {

    private ImageView mImageView;
    private int mImageHeight;

    public QHeaderSrcllrView(Context context) {
        super(context);
    }

    public QHeaderSrcllrView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageHeight = context.getResources().getDimensionPixelOffset(R.dimen.size_default_height);
    }

    public QHeaderSrcllrView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setZoomImageView(ImageView iv){
        this.mImageView = iv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
//            mImageView.getLayoutParams().height = mImageHeight;
//            mImageView.requestLayout();
            RestAnimetion restAnimetion = new RestAnimetion(mImageHeight);
            restAnimetion.setInterpolator(new OvershootInterpolator());
            restAnimetion.setDuration(700);
            mImageView.startAnimation(restAnimetion);
        }

        return super.onTouchEvent(ev);

    }

    public class RestAnimetion extends Animation{

        //1nImageView 的增加的高度
        private int extraHeight;
        //ImageView原始的高度
        private int currentHeight;

        public RestAnimetion(int targetHeight) {
            currentHeight = mImageView.getHeight();
            extraHeight = mImageView.getHeight() - targetHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mImageView.getLayoutParams().height = (int) (currentHeight - extraHeight*interpolatedTime);
            mImageView.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0){
            mImageView.getLayoutParams().height = mImageView.getHeight() - deltaY/3;
            mImageView.requestLayout();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
