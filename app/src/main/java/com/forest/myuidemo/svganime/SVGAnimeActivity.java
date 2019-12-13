package com.forest.myuidemo.svganime;

import android.annotation.TargetApi;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.forest.myuidemo.R;


//
public class SVGAnimeActivity extends AppCompatActivity {

    private ImageView viewSvg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svganime);
        viewSvg = findViewById(R.id.imgsvg);
        ImageView viewSvg1 = findViewById(R.id.imgsvg1);
        ImageView viewSvg2 = findViewById(R.id.imgsvg2);
        ImageView viewSvg3 = findViewById(R.id.imgsvg3);
        final ImageView [] imageViewss= {viewSvg1, viewSvg2, viewSvg3};
        viewSvg.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawable vectorDrawable = (AnimatedVectorDrawable) viewSvg.getDrawable();
                vectorDrawable.registerAnimationCallback(new MyAnimationCallback(imageViewss));
                vectorDrawable.start();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    class MyAnimationCallback extends Animatable2.AnimationCallback{

        private ImageView[] animeViews;

        public MyAnimationCallback(ImageView[] animeViews) {
            this.animeViews = animeViews;
        }

        @Override
        public void onAnimationStart(Drawable drawable) {
            super.onAnimationStart(drawable);
        }

        @Override
        public void onAnimationEnd(Drawable drawable) {
            super.onAnimationEnd(drawable);
            AnimatedVectorDrawable vectorDrawable1 = (AnimatedVectorDrawable) animeViews[0].getDrawable();
            vectorDrawable1.start();
            vectorDrawable1.registerAnimationCallback(new Animatable2.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    AnimatedVectorDrawable vectorDrawable2 = (AnimatedVectorDrawable) animeViews[1].getDrawable();
                    vectorDrawable2.start();

                    vectorDrawable2.registerAnimationCallback(new Animatable2.AnimationCallback() {
                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            AnimatedVectorDrawable vectorDrawable3 = (AnimatedVectorDrawable) animeViews[2].getDrawable();
                            vectorDrawable3.start();
                        }
                    });
                }
            });


        }
    }
}
