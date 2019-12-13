package com.forest.myuidemo.materialdesign;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.forest.myuidemo.R;

public class MaterialActivity extends AppCompatActivity {


    Button button;
    TextView textView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        button = findViewById(R.id.bottom1);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    v.setX(event.getRawX() - v.getWidth()/2);
                    v.setY(event.getRawY() - v.getHeight()/2);
                }
                return true;
            }
        });
    }
}
