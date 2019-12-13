package com.forest.myuidemo.handrecycleview.view;

import android.view.View;

import java.util.Stack;

class Recycler {
    //回收池的容器  存储所有的回收了的view 它是一个数组   它存储的是每一个ItemView
    private Stack<View>[] views;

    /**
     * 初始化栈容器的数组
     * @param viewTypeCount
     */
    public Recycler(int viewTypeCount){
        //有几中类型的ItemView  就创建几个数组
        views = new Stack[viewTypeCount];
        //初始化数组  循环创建Stack对象
        for(int x=0;x<viewTypeCount;x++){
            //数组的下标 指向对象
            views[x] = new Stack<>();
        }
    }

    /**
     * 将view放入类型相对应的栈容器中--》当回收View的时候
     * @param view
     * @param viewType
     */
    public void put(View view,int viewType){
        //根据itemView的类型获取itemView
        views[viewType].push(view);
    }

    /**
     * 将View从栈中取出来 ---》当填充itemView的时候
     * @param viewType
     * @return
     */
    public View get(int viewType){
        try{
            return views[viewType].pop();
        }catch (Exception e){
            return null;
        }
    }


}
