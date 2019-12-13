package com.forest.myuidemo.qbehavior.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingParent2;

import com.forest.myuidemo.R;
import com.forest.myuidemo.qbehavior.Behavior;

import java.lang.reflect.Constructor;

public class MyNestedLinerLayout extends LinearLayout implements NestedScrollingParent2 {
    private static final String TAG = "forest";

    public MyNestedLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 接收开始嵌套滑动的方法
     * @param child
     * @param target
     * @param axes
     * @param type
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        //Log.d(TAG, "onStartNestedScroll: aaaaaaaaaaaaaa");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        //Log.d(TAG, "11111111111111111");
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        //Log.d(TAG, "22222222222222");
    }

    /**
     * 接收滚动的通知 子控件一直滚动，这个方法就一直被调用
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     * @param type
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //遍历子控件，然后看看子控件有没有设置behavior，behavior就是去操作子控件做出动作
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.getChildAt(i);
            MyLayouParams layoutParams = (MyLayouParams) childAt.getLayoutParams();
            Behavior behavior = layoutParams.behavior;
            if (behavior != null){
                if (behavior.layoutDependsOn(this,childAt,target)){
                    behavior.onNestedScroll(this,childAt,target,dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed);
                    behavior.aa(childAt);
                }

            }



        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //Log.d(TAG, "4444444444444444444");
    }



    /**
     * 定义当前这个控件下所有的子控件使用的layoutParams类
     * @param
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayouParams(getContext(),attrs);
    }

    class MyLayouParams extends LayoutParams {
        private Behavior behavior;

        public MyLayouParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //将自定义的属性交个一个TypeArry来管理
            TypedArray t = c.obtainStyledAttributes(attrs, R.styleable.MyNestedLinerLayout);
            //获取自定义属性的值
            String className = t.getString(R.styleable.MyNestedLinerLayout_layout_behavior);
            //根据类名实例化
            behavior = parseBehavior(c,attrs,className);
            t.recycle();
        }

        private Behavior parseBehavior(Context c, AttributeSet attrs, String className) {
            Behavior behavior = null;
            if (TextUtils.isEmpty(className)){
                return null;
            }
            try {
                Class aClass = Class.forName(className);
                if (!Behavior.class.isAssignableFrom(aClass)){
                    return null;
                }
                Constructor<? extends Behavior> constructor = aClass.getConstructor(Context.class);
                constructor.setAccessible(true);
                behavior = constructor.newInstance(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return behavior;
        }

        public MyLayouParams(int width, int height) {
            super(width, height);
        }

        public MyLayouParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public MyLayouParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public MyLayouParams(MarginLayoutParams source) {
            super(source);
        }

        public MyLayouParams(LayoutParams source) {
            super(source);
        }
    }


}
