package com.forest.myuidemo.douyinview;

import android.view.View;

public interface OnViewPagerListener {
    //停止播放的监听
    void onPageRelease(boolean isNest, View position);

    //播放的监听
    void onPageSelected(boolean isButten,View position);


}
