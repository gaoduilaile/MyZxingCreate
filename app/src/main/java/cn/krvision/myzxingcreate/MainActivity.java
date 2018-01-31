package cn.krvision.myzxingcreate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView img_test;
    private Button btn_test;

    /**
     * 传递进来的源图片
     */
    private Bitmap source;
    /**
     * 图片的配文
     */
    private String text;
    /**
     * 图片加上配文后生成的新图片
     */
    private Bitmap newBitmap;
    /**
     * 配文的颜色
     */
    private int textColor = Color.BLACK;
    /**
     * 配文的字体大小
     */
    private float textSize = 16;
    /**
     * 图片的宽度
     */
    private int bitmapWidth;
    /**
     * 图片的高度
     */
    private int bitmapHeight;
    /**
     * 画图片的画笔
     */
    private Paint bitmapPaint;
    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    /**
     * 配文与图片间的距离
     */
    private float padding = 0;
    /**
     * 配文行与行之间的距离
     */
    private float linePadding = 0;
    private Button btn_new;
    private ImageView img_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_test = findViewById(R.id.btn_test);
        img_test = findViewById(R.id.img_test);

        btn_new = findViewById(R.id.btn_new);
        img_new = findViewById(R.id.img_new);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                source = ZxingUtils.createBitmap("http://www.krvision.cn");
                img_test.setImageBitmap(source);


            }
        });


        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap newBitMap = getNewBitMap(source);
                img_new.setImageBitmap(newBitMap);
            }
        });


    }


    private Bitmap getNewBitMap(Bitmap source) {

        text = "视氪科技 ";

        bitmapWidth = source.getWidth();
        bitmapHeight = source.getHeight();

        Log.e("bitmap1  ", bitmapWidth + " " + bitmapHeight);

//一行可以显示文字的个数 文字距离边距为5
        int lineTextCount = (int) ((source.getWidth() -10) / textSize);
//一共要把文字分为几行
        int line = (int) Math.ceil(Double.valueOf(text.length()) / Double.valueOf(lineTextCount));

        Log.e("line  ", "line="+line+" "+"   lineTextCount="+lineTextCount);

//新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
        newBitmap = Bitmap.createBitmap(bitmapWidth,
                (int) (bitmapHeight + padding + textSize * line + linePadding * line), Bitmap.Config.ARGB_8888);

        Log.e("bitmap2  ", bitmapWidth + "  " + (int)(bitmapHeight + padding + textSize * line + linePadding * line));

        Canvas canvas = new Canvas(newBitmap);
//把图片画上来

        bitmapPaint = new Paint();
        canvas.drawBitmap(source, 0, 0, bitmapPaint);

//在图片下边画一个白色矩形块用来放文字，防止文字是透明背景，在有些情况下保存到本地后看不出来

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        canvas.drawRect(0, source.getHeight(), source.getWidth(),
                source.getHeight() + padding + textSize * line + linePadding * line, textPaint);

//把文字画上来

        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        Rect bounds = new Rect();

//开启循环直到画完所有行的文字
        for (int i = 0; i < line; i++) {
            String s;
            if (i == line - 1) {//如果是最后一行，则结束位置就是文字的长度，别下标越界哦
                s = text.substring(i * lineTextCount, text.length());
            } else {//不是最后一行
                s = text.substring(i * lineTextCount, (i + 1) * lineTextCount);
            }
            //获取文字的字宽高以便把文字与图片中心对齐
            textPaint.getTextBounds(s, 0, s.length(), bounds);
            //画文字的时候高度需要注意文字大小以及文字行间距
            canvas.drawText(s, source.getWidth() / 2 - bounds.width() / 2,
                    source.getHeight() + padding + i * textSize + i * linePadding + bounds.height() / 2, textPaint);
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();


        return newBitmap;

    }
}
