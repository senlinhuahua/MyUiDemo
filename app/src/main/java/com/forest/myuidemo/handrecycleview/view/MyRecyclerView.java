package com.forest.myuidemo.handrecycleview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.forest.myuidemo.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerView extends ViewGroup {

    //当前RecyclerView的适配器
    private Adapter adapter;
    //当前显示的的View
    private List<View> viewList;
    //当前滑动的Y值
    private int currentY;
    //总行数
    private int rowCount;
    //显示的的第一行是占内容的第几行
    private int firstRow;
    //y偏移量
    private int scrollY;
    //初始化  是否是第一屏
    private boolean needRelayout;
    //当前RecyclerView的宽度
    private int width;
    //当前RecyclerView的高度
    private int height;
    //所有ItemView的高度数组
    private int[] heights;
    //View对象回收池
    private Recycler recycler;
    //最小滑动距离
    private int touchSlop;

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
        viewList = new ArrayList<>();
        //是否需要重新布局
        needRelayout = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y2 = Math.abs(currentY - ev.getRawY());
                //如果滑动的距离 小于 最小滑动距离就不滑动，否则就滑动
                if(y2 > touchSlop){
                    intercept = true;
                }
                break;
        }

        return intercept;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float y2 = event.getRawY();
                float diffY = currentY - y2;
                currentY = (int) y2;
                //滑动的方法
                scrollBy(0, (int) diffY);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollBy(int x, int y) {
        scrollY += y;
        scrollY = scrollBounds(scrollY);
        if (scrollY >0){
            //向上滑动
            //1,将上面的移除掉
            while (scrollY > heights[firstRow]){
                removeView(viewList.remove(0));
                //scrollY减去这一行的高度
                scrollY -= heights[firstRow];
                //当前的行标
                firstRow++;
            }
            //给下面添加新的
            //判断当前所显示的view的高度是不是小于RecycleView的高度，如果小于就添加新的item
            while (getFillHeight() < height){
                int addlast = firstRow+viewList.size();
                View view = obtainView(addlast,width,heights[addlast]);
                viewList.add(view);
            }
        }else if (scrollY<0){
            while (scrollY < 0) {
                //当前显示的itemview的第一行的行标减去1
                int firstaddrow = firstRow - 1;
                View view = obtainView(firstaddrow, width, heights[firstaddrow]);
                //添加到最上面
                viewList.add(0, view);
                firstRow--;
                scrollY += heights[firstaddrow];
            }

            //判断当前显示的view总高度是否大于recyclerView高度。大于的话就最下面的itemview移除
            while (sumArray(heights,firstRow,viewList.size())-scrollY-heights[firstRow+viewList.size()-1] >= height){
                removeView(viewList.remove(viewList.size()-1));
            }

        }else {

        }
        //重新摆放位置
        rePositionView();
    }

    private void rePositionView() {
        int top = 0,right,bottom,left = 0,i;
        top =- scrollY;
        //将当前第一行的行标赋值给i
        i = firstRow;
        for (View view : viewList) {
            bottom = top + heights[i++];
            view.layout(0,top,width,bottom);
            top = bottom;
        }

    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        int key = (int) view.getTag(R.id.tag_type_view);
        //将view添加进回收池中
        recycler.put(view,key);
    }

    //纠正
    private int scrollBounds(int scrollY){
        if(scrollY>0){
            //判断上滑的极限值  防止滚动的距离 大于当前所有内容的高度
            scrollY = Math.min(scrollY,sumArray(heights,firstRow,heights.length-firstRow)-height);
        }else{
            //判断下滑的极限值  防止滚动的距离 小于第0个item的高度
            scrollY = Math.max(scrollY,-sumArray(heights,0,firstRow));
        }
        return scrollY;
    }


    //获取显示控件中的view总高度
    private int getFillHeight() {
        return sumArray(heights,firstRow,viewList.size()) - scrollY;
    }

    /**
     *
     * @param heights
     * @param i 从哪一个item拿起
     * @param count 一共要拿多少个item的高度
     * @return
     */
    private int sumArray(int[] heights, int i, int count) {
        int sum = 0;
        count += i;
        for (int x  = i;x<count;x++){
            sum += heights[x];
        }
        return sum;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取recyclerView在当前窗体的宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        //当前内容的高度
        int h = 0;
        if (adapter != null){
            //获取当前数据条数
            rowCount = adapter.getCount();
            heights = new int[rowCount];
            //所有的item高度
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight(i);
            }
        }
        //获取所有数据的高度
        int tempHeight = sumArray(heights,0,heights.length);
        h = Math.min(tempHeight,hightSize);
        setMeasuredDimension(widthSize,h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed){
            needRelayout = false;
            //清楚所有itemview
            removeAllViews();
            viewList.clear();
            if (adapter != null){
                width = r - l;
                height = b - t;
                //定义布局itemview的四个变量
                int top =0,right,bottom,left = 0;
                for (int x = 0; x < rowCount; x++) {
                    //获取绘制的起点
                    right = width;
                    bottom = top+heights[x];
                    View view = makeAndStep(x,left,top,right,bottom);
                    viewList.add(view);
                    top = bottom;
                }
            }
        }
    }

    private View makeAndStep(int x, int left, int top, int right, int bottom) {
        //生成view
        View view = obtainView(x,width,right);
//布局itemView
        view.layout(left,top,right,bottom);
        return view;

    }

    private View obtainView(int row, int width, int right) {
        //去栈拿
        int itemViewType = adapter.getItemViewType(row);
        View view = recycler.get(itemViewType);

        View itemView = null;
        if (view == null){
            itemView = adapter.onCreateViewHolder(row,itemView,this);
            if (itemView == null){
                throw new RuntimeException("onCreateViewHolder,没有布局");
            }
        }else {
            itemView = adapter.onBinderViewHolder(row,view,this);
        }
        //给每个itemview设置一个tga
        itemView.setTag(R.id.tag_type_view,itemViewType);
        itemView.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
//每次生成一个Item 将添加进RecyclerView
        addView(itemView);
        return itemView;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if(adapter!=null){
            recycler = new Recycler(adapter.getViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            //重新测量  然后重新摆放
            requestLayout();
        }
    }

    public interface Adapter{
        //创建ViewHolder的接口
        View onCreateViewHolder(int position,View convertView,ViewGroup parent);
        //绑定ViewHolder的接口
        View onBinderViewHolder(int position,View convertView,ViewGroup parent);

        //获取到当前row item的控件类型
        int getItemViewType(int row);

        //获取当前控件类型的总数量
        int getViewTypeCount();

        //获取当前item的总数量
        int getCount();

        //获取index item的高度
        int getHeight(int index);
    }
}
