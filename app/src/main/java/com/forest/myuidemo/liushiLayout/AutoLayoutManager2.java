package com.forest.myuidemo.liushiLayout;

import android.graphics.Rect;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

class AutoLayoutManager2 extends RecyclerView.LayoutManager {
    //保存所有item的高度和偏移度的总和
    private int totalHeight = 0;
    //所有item的位置保存容器
    private SparseArray<Rect> allItemframs = new SparseArray<>();
    /**
     * 滑动偏移量
     * 如果是正的就是在向上滑，展现上面的view
     * 如果是负的向下
     */
    private int verticalScrollOffset = 0;



    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        //直接返回RecyclerView的LayoutParams
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 摆放子元素
     * @param recycler  回收池对象
     * @param state 对当前RecyclerView的状态管理类
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(getItemCount()<=0){
            return;
        }
        //是否在动画过程中给
        if(state.isPreLayout()){
            return;
        }
        //将视图分离放入到缓存中  recycler   Scrap
        detachAndScrapAttachedViews(recycler);
        //Y轴的累加值
        int offsetY = 0;
        //X轴的累加值
        int offsetX = 0;
        //当前所有行的高度
        int viewH = 0;
        for(int x=0;x<getItemCount();x++){
            //先去缓存中获取
            View view = recycler.getViewForPosition(x);
            //现将它添加进recyclerView中
            addView(view);
            //测量ietmView
            measureChildWithMargins(view,0,0);
            //获取到itemView的宽度
            int w = getDecoratedMeasuredWidth(view);
            int h = getDecoratedMeasuredHeight(view);
            viewH = h;
            //把view的位置存入到
            Rect fram = allItemframs.get(x);
            if(fram == null){
                fram = new Rect();
            }
            //什么时候换行  什么时候不换行
            //需要换行
            if(offsetX +w > getWidth()){
                offsetY+=h;
                offsetX = w;
                //保存位置
                fram.set(0,offsetY,w,offsetY+h);
            }else{
                //不需要换行
                fram.set(offsetX,offsetY,offsetX+w,offsetY+h);
                offsetX+=w;
            }
            //将位置对象放入到集合中
            allItemframs.put(x,fram);
        }
        totalHeight = offsetY+viewH;
        //回收不可见的itemView
        recyclerViewFillView(recycler,state);
    }

    /**
     * 回收不可见的itemView
     * @param recycler
     * @param state
     */
    private void recyclerViewFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //解绑所有的RecyclerView的ziview
        detachAndScrapAttachedViews(recycler);
        //获取到RecyclerView当前的可见区域
        Rect phoneFrame = new Rect(0, verticalScrollOffset, getWidth(), verticalScrollOffset + getHeight());
        //清楚当前显示的itemView  getChildCount当前recyclerView中的ieMvIEW
        for (int i=0;i<getChildCount();i++){
            //获取到ItemView
            View childView = getChildAt(i);
            //相对应的获取到它的位置对象
            Rect child=allItemframs.get(i);
            //然后看可见区域是否包含控件的区域
            if (!Rect.intersects(phoneFrame, child)) {
                //如果不包含  就进行回收
                removeAndRecycleView(childView, recycler);
            }
        }
        //可见区域出现在屏幕上的子view getItemCount数据源的数量
        for (int j = 0;j<getItemCount();j++){
            //判断是否包含子View的区域
            if (Rect.intersects(phoneFrame,allItemframs.get(j))){
                //如果包含 scrap回收池里面拿的
                View scrap = recycler.getViewForPosition(j);
                //重绘子View
                measureChildWithMargins(scrap,0,0);
                //添加进控件
                addView(scrap);
                //获取到位置信息
                Rect frame = allItemframs.get(j);
                //进行子View布局
                layoutDecorated(scrap, frame.left, frame.top - verticalScrollOffset,
                        frame.right, frame.bottom - verticalScrollOffset);
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
