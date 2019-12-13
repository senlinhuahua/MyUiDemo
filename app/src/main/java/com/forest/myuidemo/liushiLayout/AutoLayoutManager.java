package com.forest.myuidemo.liushiLayout;

import android.graphics.Rect;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

class AutoLayoutManager extends RecyclerView.LayoutManager {
    //保存所有item的高度及偏移度的总和
    private int totalHeight = 0;
    //所有item的位置保存容器
    private SparseArray<Rect> allItemframs = new SparseArray<>();

    private int verticalScrollOffset = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * 摆放子元素
     * @param recycler 回收池
     * @param state 对当前recyclerView的状态管理类
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0){
            return;
        }
        //是否在播放动画
        if (state.isPreLayout()){
            return;
        }
        //将视图分离到缓存中
        detachAndScrapAttachedViews(recycler);
        int offsetY = 0;
        int offsetX = 0;
        //当前所有行的高度
        int viewH = 0;
        for (int x = 0; x < getItemCount(); x++) {
            //先去缓存中获取
            View viewForPosition = recycler.getViewForPosition(x);
            addView(viewForPosition);
            //测量itemView
            measureChildWithMargins(viewForPosition,0,0);
            int w = getDecoratedMeasuredWidth(viewForPosition);
            int h = getDecoratedMeasuredHeight(viewForPosition);
            viewH = h;
            Rect fram = allItemframs.get(x);
            if (fram == null){
                fram = new Rect();
            }
            //需要换行
            if (offsetX + w > getWidth()){
                offsetY+=h;
                offsetX = w;
                //保存位置
                fram.set(0,offsetY,w,offsetY+h);
            }else {
                fram.set(offsetX,offsetY,offsetX+w,offsetY+h);
                offsetX += w;

            }
            allItemframs.put(x,fram);
        }
        totalHeight = offsetY+viewH;
        //回收不可见的item
        recyclerViewFillView(recycler,state);



    }

    private void recyclerViewFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //解绑所有的子view
        detachAndScrapAttachedViews(recycler);
        //获取可见区域
        Rect phoneFram = new Rect(0,verticalScrollOffset,getWidth(),verticalScrollOffset+getHeight());
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Rect child = allItemframs.get(i);
            //可见区域是否包含控件区域
            if (!Rect.intersects(phoneFram,child)){
                //回收
                removeAndRecycleView(childAt,recycler);
            }
        }

        //可见区域出现在屏幕上的子view
        for (int j = 0; j < getItemCount(); j++) {
            if (Rect.intersects(phoneFram,allItemframs.get(j))){
                View scrap = recycler.getViewForPosition(j);
                //重绘子view
                measureChildWithMargins(scrap,0,0);
                addView(scrap);
                Rect fram = allItemframs.get(j);
                //进行子view布局
                //进行子View布局
                layoutDecorated(scrap, fram.left, fram.top - verticalScrollOffset,
                        fram.right, fram.bottom - verticalScrollOffset);
            }
        }


    }

    /**
     * 垂直滑动的方法
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //解绑所有的RecyclerView的ziview
        detachAndScrapAttachedViews(recycler);
        //得到实际滑动距离
        int trval = dy;
        //如果滑动到最顶部  往下滑   verticalScrollOffset   -
        //第一个坐标值 减 以前最后一个坐标值  //记死
        if (verticalScrollOffset + dy < 0) {
            trval = -verticalScrollOffset;
        }else if(verticalScrollOffset+dy>totalHeight-getHeight()){
            //如果滑动到最底部  往上滑   verticalScrollOffset   +
            trval = totalHeight - getHeight() - verticalScrollOffset;
        }
        verticalScrollOffset+=trval;
        //平移内容
        offsetChildrenVertical(trval);
        //重新摆放itemView
        recyclerViewFillView(recycler,state);
        return trval;
    }
    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
