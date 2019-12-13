package com.forest.myuidemo.douyinview;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DouYinLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener{

    //判断是否上滑还是下滑
    private int mDrift;

    private OnViewPagerListener onViewPagerListener;
    //吸顶，吸底
    private PagerSnapHelper pagerSnapHelper;

    public OnViewPagerListener getOnViewPagerListener() {
        return onViewPagerListener;
    }

    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.onViewPagerListener = onViewPagerListener;
    }

    public DouYinLayoutManager(Context context) {
        super(context);
    }

    public DouYinLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        pagerSnapHelper = new PagerSnapHelper();
    }

    /**
     * 当manager完全添加到recycleview中是会被调用
     * @param view
     */
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        pagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);

    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (mDrift > 0){
            //向上滑
            if (onViewPagerListener != null && Math.abs(mDrift) == view.getHeight()){
                onViewPagerListener.onPageSelected(false,view);
            }
        }else {
            //向下滑
            if (onViewPagerListener != null && Math.abs(mDrift) == view.getHeight()){
                onViewPagerListener.onPageSelected(true,view);
            }
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (mDrift >= 0){
            //向上滑
            if (onViewPagerListener != null){
                onViewPagerListener.onPageRelease(true,view);
            }
        }else {
            //向下滑
            if (onViewPagerListener != null){
                onViewPagerListener.onPageRelease(false,view);
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state){
            case RecyclerView.SCROLL_STATE_IDLE:
                //当前显示的item
                View snapView = pagerSnapHelper.findSnapView(this);
                if (onViewPagerListener != null){
                    onViewPagerListener.onPageSelected(false,snapView);
                }
                break;
        }
        super.onScrollStateChanged(state);

    }
}
