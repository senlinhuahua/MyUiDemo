package com.forest.myuidemo.svg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class ProviceItem {
    private Path path;
    //绘制的颜色
    private int drawColor;
    private String provice;

    public ProviceItem(Path path,String provice) {
        this.path = path;
        this.provice = provice;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect){
            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
            canvas.drawPath(path,paint);

            int storkColor = Color.RED;
            paint.setColor(storkColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path,paint);

        }else {
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8,0,0,0xffffff);
            canvas.drawPath(path,paint);
            //填充
            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path,paint);
        }
        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        canvas.drawText(provice,rectF.centerX()-10,rectF.centerY(),paint);

    }

    public boolean isTouch(float x, float y) {
        //创建一个矩形
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        Region region= new Region();
        region.setPath(path, new Region((int) rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom));
        return region.contains((int)x,(int)y);
    }
}
