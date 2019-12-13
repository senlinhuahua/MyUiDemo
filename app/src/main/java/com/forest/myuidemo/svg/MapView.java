package com.forest.myuidemo.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.forest.myuidemo.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {

    private Context context;
    private Paint paint;

    //所有省份的集合
    private List<ProviceItem> itemList;
    //绘制地图的颜色
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    //适配比例
    private float scale=1.0f;
    //地图的矩形对象
    private RectF totalRect;
    //被选中的省
    private ProviceItem select;
    //是否已经解析完毕
    private boolean isEnd = false;

    public MapView(Context context) {
        super(context);

    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
//开启解析XML文件的线程
        loadThread.start();

    }


    //创建线程解析xml
    private Thread loadThread = new Thread(){
        @Override
        public void run() {
            try {
                InputStream inputStream = context.getResources().openRawResource(R.raw.china);
                List<ProviceItem> list = new ArrayList<>();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                builder = factory.newDocumentBuilder();

                Document doc = builder.parse(inputStream);
                //获取Xml文件的根目录
                Element rootElement = doc.getDocumentElement();
                //获取根节点下面的某些节点
                NodeList items = rootElement.getElementsByTagName("path");
                //首先 定义四个点
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;
                //遍历所有的path节点
                for (int x = 0; x < items.getLength(); x++) {
                    Element element = (Element) items.item(x);
                    String pathData = element.getAttribute("android:pathData");
                    String provice = element.getAttribute("android:provice");
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProviceItem proviceItem = new ProviceItem(path,provice);
                    list.add(proviceItem);

                    //获取控件的宽高
                    RectF rect = new RectF();
                    //获取到每个省份的边界
                    path.computeBounds(rect,true);
                    //遍历取出每个path中的left取所有的最小值
                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    //遍历取出每个path中的right取所有的最大值
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    //遍历取出每个path中的top取所有的最小值
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    //遍历取出每个path中的bottom取所有的最大值
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                }
                totalRect = new RectF(left,top,right,bottom);
                itemList = list;
                handler.sendEmptyMessage(1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * 设置颜色
     */
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (itemList == null) {
                return;
            }
            int totalNumber = itemList.size();
            for (int i = 0; i < totalNumber; i++) {
                int color = Color.WHITE;
                int flag = i % 4;
                switch (flag) {
                    case 1:
                        color = colorArray[0];
                        break;
                    case 2:
                        color = colorArray[1];
                        break;
                    case 3:
                        color = colorArray[2];
                        break;
                    default:
                        color = Color.CYAN;
                        break;
                }
                //将颜色设置给每个省份的封装对象
                itemList.get(i).setDrawColor(color);
            }
            isEnd = true;
            //requestLayout();
            measure(getMeasuredWidth(),getMeasuredHeight());
            postInvalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isEnd){
            return;
        }
        if (itemList != null && itemList.size()>0){
            canvas.save();
            canvas.scale(scale,scale);
            for (ProviceItem proviceItem : itemList) {
                if (select != proviceItem){
                    proviceItem.drawItem(canvas,paint,false);
                }
            }
            if (select != null){
                select.drawItem(canvas,paint,true);
            }
        }
    }

    /**
     * 重新测量，做适配
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取控件的宽高
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (!isEnd){
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(w,MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY));
            return;
        }

        if (totalRect != null){
            //获取到地图的宽高
            double mapWidth = totalRect.width();
            //获取到比例
            scale = (float) (w / mapWidth);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w,MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handTouch(event.getX(),event.getY());

        return super.onTouchEvent(event);

    }

    private void handTouch(float x, float y) {
        if (itemList == null || itemList.size() == 0){
            return;
        }
        ProviceItem selectItem = null;
        for (ProviceItem item : itemList) {
            //如果点击的是省份的范围之内
            if (item.isTouch(x/scale,y/scale)){
                selectItem = item;
            }
        }
        if (selectItem != null){
            select = selectItem;
            postInvalidate();
        }


    }


}
