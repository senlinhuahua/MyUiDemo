package com.forest.myuidemo.qbehavior;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.forest.myuidemo.GallaryHorizonalScrollView;
import com.forest.myuidemo.R;
import com.forest.myuidemo.RevealDrawable;
import com.forest.myuidemo.materialdesign.MaterialActivity;

public class BeHaviorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //setContentView(new PathMeasureView(this));


    }



    public void jumpMaterialActivity(View view) {
        startActivity(new Intent(BeHaviorActivity.this, MaterialActivity.class));
    }
}
