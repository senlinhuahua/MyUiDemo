package com.forest.myuidemo.materialdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class FollowBehavier extends CoordinatorLayout.Behavior<TextView> {

    private boolean isone = true;

    public FollowBehavier(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        if (!(dependency instanceof Button)){
            return false;
        }
        return true;

    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        if (!isone){
            child.setX(dependency.getX()+200);
            child.setY(dependency.getY()+200);
        }
        isone = false;

        return true;
    }
}
