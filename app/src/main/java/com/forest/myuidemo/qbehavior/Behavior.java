package com.forest.myuidemo.qbehavior;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.forest.myuidemo.R;

//动作的执行者
public class Behavior {
    public Behavior(Context context) {
        //super();
    }

    /***
     * 用来筛选被观察者
     * @param parent 观察者的父亲
     * @param child 观察者
     * @param dependency 被观察者
     * @return
     */
    public boolean layoutDependsOn(@NonNull View parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof NestedScrollView && dependency.getId() == R.id.scrollView;
    }


    public void onNestedScroll(@NonNull View parent, View child,@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed < 0){
            //向下滑动
            Log.d("forest", "onNestedScroll: 向下滑动");
            if (child.getY() <= 0 && target.getY() <= child.getHeight()){
                child.setTranslationY(-(target.getScrollY() > child.getHeight()?
                        child.getHeight():target.getScrollY()));
                target.setTranslationY(-(target.getScrollY() > child.getHeight()?
                        child.getHeight():target.getScrollY()));

                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight()-child.getHeight()-child.getTranslationY());
                target.setLayoutParams(layoutParams);

            }
        }else {
            Log.d("forest", "onNestedScroll: 向上滑动");
            //当观察者的Y坐标大于0的时候，才能向上滑
            if (target.getY() > 0){
                child.setTranslationY(-(target.getScrollY() > child.getHeight()?
                        child.getHeight():target.getScrollY()));
                target.setTranslationY(-(target.getScrollY() > child.getHeight()?
                        child.getHeight():target.getScrollY()));

                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight()-child.getHeight()-child.getTranslationY());
                target.setLayoutParams(layoutParams);

            }

        }

    }

        public void aa(View view) {
        TextView textView = (TextView) view;
        textView.setText("aaaaa");
        //Log.d("forest", "22222222222222");
    }
}
