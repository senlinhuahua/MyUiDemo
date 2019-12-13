package com.forest.myuidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.forest.myuidemo.douyinview.DouYinActivity;
import com.forest.myuidemo.handrecycleview.RecycleActivity;
import com.forest.myuidemo.liushiLayout.LiuShiActivity;
import com.forest.myuidemo.materialdesign.MaterialActivity;
import com.forest.myuidemo.pathmeasure.PathMeasureActivity;
import com.forest.myuidemo.pathmeasure.PathMeasureView;
import com.forest.myuidemo.qbehavior.BeHaviorActivity;
import com.forest.myuidemo.qzoneview.QQHeaderSrollView;
import com.forest.myuidemo.qzoneview.QzoneActivity;
import com.forest.myuidemo.svg.SVGActivity;
import com.forest.myuidemo.svganime.SVGAnimeActivity;
import com.forest.myuidemo.vlayout.VLayoutActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    private int[] mImgIds = new int[]{ //7个
            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline,

            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline
    };
    private int[] mImgIds_active = new int[]{
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active,
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active
    };

    private GallaryHorizonalScrollView scrollView;

    private Drawable[] revealDrawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        revealDrawables = new Drawable[mImgIds.length];

        for (int i = 0; i < mImgIds.length; i++) {
            RevealDrawable rd = new RevealDrawable(
                    getResources().getDrawable(mImgIds[i]),
                    getResources().getDrawable(mImgIds_active[i]),
                    RevealDrawable.HORIZONTAL);
            revealDrawables[i] = rd;
        }
        scrollView.addImageViews(revealDrawables);

    }

    private void initView() {
        scrollView = findViewById(R.id.imgv);

    }

    public void jumpMaterialActivity(View view) {
        startActivity(new Intent(MainActivity.this, MaterialActivity.class));

    }

    public void jumpDouyinActivity(View view) {
        startActivity(new Intent(MainActivity.this, DouYinActivity.class));
    }

    public void jumpHandRecycleView(View view) {
        startActivity(new Intent(MainActivity.this, RecycleActivity.class));
    }

    public void jumpLiushi(View view) {
        startActivity(new Intent(MainActivity.this, LiuShiActivity.class));
    }

    public void jumpPathMeasure(View view) {
        startActivity(new Intent(MainActivity.this, PathMeasureActivity.class));

    }

    public void jumpBehavior(View view) {
        startActivity(new Intent(MainActivity.this, BeHaviorActivity.class));
    }

    public void jumpQzone(View view) {
        startActivity(new Intent(MainActivity.this, QzoneActivity.class));
    }

    public void jumpSVG(View view) {
        startActivity(new Intent(MainActivity.this, SVGActivity.class));
    }

    public void jumpSVGAnime(View view) {
        startActivity(new Intent(MainActivity.this, SVGAnimeActivity.class));
    }

    public void jumpVLayout(View view) {
        startActivity(new Intent(MainActivity.this, VLayoutActivity.class));
    }
}
