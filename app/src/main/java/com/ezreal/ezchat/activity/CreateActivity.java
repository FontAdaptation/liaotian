package com.ezreal.ezchat.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezreal.ezchat.R;
import com.ezreal.ezchat.utils.ImageSQLiteHelper;

/**
 * Created by 李晨晨
 */

public class CreateActivity extends AppCompatActivity {

    TextView tv,mTvTitle;
    ImageView iv1,mIvBack,mIvMenu;
    Button b1,b2,b3,b4,b5,b6;
    String[] str;
    public static int count = 0;//当前字母在数据库中的序号
    int[][] iarr;//用于存储从数据库取出之后的数组
    String s = "";
    private Bitmap baseBitmap, showBitmap;//位图
    private Canvas canvas,show_canvas;
    private Paint paint;

    private String[] imageStr;
    private int[][] myImageArray;
    public static int[][][] myImageArrays = new int[94][][];

    public static final int MY_ROW = 100;
    public static final int MY_COL = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_words_input);
        setTitleBar("字体库编辑",true,false);//设置标题栏

        paint = new Paint();
        paint.setAntiAlias(true);
        tv = (TextView)findViewById(R.id.letter);
        char letter = (char)('!'+ count);
        tv.setText(""+letter);
        iv1 = (ImageView)findViewById(R.id.Create_up_a);
        b1 = (Button)findViewById(R.id.save_button);
        b2 = (Button)findViewById(R.id.show_button);
        b3 = (Button)findViewById(R.id.delete_btn);
        b5 = (Button)findViewById(R.id.next_button);


        ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        b1.setOnClickListener(new insertListener());
        b2.setOnClickListener(new resumeListener());
        b3.setOnClickListener(new deleteListener());
        b5.setOnClickListener(new nextListener());
        mIvBack.setOnClickListener(new mIvBackListener());

        baseBitmap = Bitmap.createBitmap(MY_COL*4, MY_ROW*4, Bitmap.Config.ARGB_8888);
        //如果数据库已保存了，就显示出来
        if(searchExistence(count+"")){
            int[][] bitmapArray=getImageArry(count+"");
            Bitmap bb=arrayToBitmap(bitmapArray);
            baseBitmap=scaleBitmap(bb,4);
        }

        canvas = new Canvas(baseBitmap);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        canvas.drawLine(0,350,300,350,paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(0,250,300,250,paint);
        paint.setColor(Color.RED);
        canvas.drawLine(0,150,300,150,paint);
        canvas.drawLine(0,50,300,50,paint);
        iv1.setImageBitmap(baseBitmap);
        iv1.setOnTouchListener(touch);
    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
        // 定义手指开始触摸的坐标
        float startX;
        float startY;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            paint.setStrokeWidth(8);
            switch (event.getAction()) {
                // 用户按下动作
                case MotionEvent.ACTION_DOWN:
                    // 第一次绘图初始化内存图片，指定背景为白色
                    // 记录开始触摸的点的坐标
                    startX = event.getX();
                    startY = event.getY();
                    break;
                // 用户手指在屏幕上移动的动作
                case MotionEvent.ACTION_MOVE:
                    // 记录移动位置的点的坐标
                    float stopX = event.getX();
                    float stopY = event.getY();

                    //根据两点坐标，绘制连线
                    paint.setARGB(255, 0, 0, 0);
                    canvas.drawLine(startX, startY, stopX, stopY, paint);

                    // 更新开始点的位置
                    startX = event.getX();
                    startY = event.getY();

                    // 把图片展示到ImageView中
                    iv1.setImageBitmap(baseBitmap);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
    };



    protected void resumeCanvas() {
        // 手动清除画板的绘图，重新创建一个画板
        paint.setStrokeWidth(2);
        if (baseBitmap != null) {
            baseBitmap = Bitmap.createBitmap(iv1.getWidth(),
                   iv1.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmap);
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.RED);
            canvas.drawLine(0,350,300,350,paint);
            paint.setColor(Color.BLUE);
            canvas.drawLine(0,250,300,250,paint);
            paint.setColor(Color.RED);
            canvas.drawLine(0,150,300,150,paint);
            canvas.drawLine(0,50,300,50,paint);
            iv1.setImageBitmap(baseBitmap);
        }
    }


    //保存按钮监听器
    class insertListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            int[][] pixel = saveScaledBitmapArray(baseBitmap);
            int row = pixel.length;
            int col = pixel[0].length;

            for(int i = 0; i<row;i++)
            {
                for(int j = 0;j<col;j++)
                {
                    s += pixel[i][j]+",";
                }
            }

            ContentValues values = new ContentValues();
            char letter = (char)('!'+count);
            String str = ""+letter;
            values.put("id",str);
            values.put("content",s);
            values.put("name",count);
            ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert("array",null,values);
            s="";
        }
    }

    class resumeListener implements View.OnClickListener{//重画

        @Override
        public void onClick(View view) {
            ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("array","name=?",new String[]{count+""});
            resumeCanvas();
        }
    }

    /*控制字符的保存界面*/
    class nextListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            count++;
            char letter = (char)('!'+ count);
            tv.setText(""+letter);
            baseBitmap = Bitmap.createBitmap(MY_COL*4, MY_ROW*4, Bitmap.Config.ARGB_8888);
            //如果数据库已保存了，就显示出来
            if(searchExistence(count+"")){
                int[][] bitmapArray=getImageArry(count+"");
                Bitmap bb=arrayToBitmap(bitmapArray);
                baseBitmap=scaleBitmap(bb,4);
            }

            canvas = new Canvas(baseBitmap);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);
            paint.setColor(Color.RED);
            canvas.drawLine(0,350,300,350,paint);
            paint.setColor(Color.BLUE);
            canvas.drawLine(0,250,300,250,paint);
            paint.setColor(Color.RED);
            canvas.drawLine(0,150,300,150,paint);
            canvas.drawLine(0,50,300,50,paint);
            iv1.setImageBitmap(baseBitmap);

        }
    }

    class deleteListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("array",null,null);
        }
    }


    class mIvBackListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            getImageArry();
            Intent intent=new Intent();
            setResult(20,intent);
            finish();
        }
    }

    public void onBackPressed() {
        getImageArry();
        Intent intent=new Intent();
        setResult(20,intent);
        finish();
    }


    /*获得图片数组*/
    public void getImageArry() {
        ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this, "my_nn_database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("array", new String[]{"id", "content","name"}, null, null, null, null, "id", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String or = cursor.getString(cursor.getColumnIndex("name"));
            System.out.println(id+"------------------------------------"+name);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            imageStr = name.split("[,]");
            System.out.println(name);
            myImageArray = new int[MY_ROW][MY_COL];
            for(int j = 0;j<MY_ROW;j++)
            {
                for(int k = 0;k<MY_COL;k++)
                {
                    myImageArray[j][k] = Integer.parseInt(imageStr[MY_COL*j+k]);
                }
            }

            int order = Integer.parseInt(or);

            System.out.println(order+"++++++++++++++++++++)0000000000000000000000000000000000000");

            myImageArrays[order] = myImageArray;
        }
    }
    //查询数据库中是否存在该条目
    private boolean searchExistence(String value){
        ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("array",new String[]{"id","content","name"},"name=?",new String[]{value},null,null,"id",null);
        while (cursor.moveToNext()) {
            db.close();
            System.out.println("----------------------------数据库中有此条数据——————————————");
            return true;
        }
        db.close();
        System.out.println("----------------------------数据库中没没没此条数据——————————————");
        return false;
    }

    private int[][] getImageArry(String value){
        int[][] myImageArray1 = new int[MY_ROW][MY_COL];
        ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(CreateActivity.this,"my_nn_database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("array",new String[]{"id","content","name"},"name=?",new String[]{value},null,null,"id",null);
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String num = cursor.getString(cursor.getColumnIndex("name"));//!的name是0
            System.out.println("" + num + "   " + content + "------------" + id);

            String[] imageStr1 = content.split("[,]");
            for(int j = 0;j<MY_ROW;j++)
            {
                for(int k = 0;k<MY_COL;k++)
                {
                    myImageArray1[j][k] = Integer.parseInt(imageStr1[MY_COL*j+k]);
                    //System.out.println(myImageArray1[j][k]);
                }
                //System.out.println();
            }
        }
        return myImageArray1;
    }

    private Bitmap arrayToBitmap(int[][] arr) {//二维数组的行数和列数

        int row = arr.length;
        int col = arr[0].length;
        Bitmap bitmap = Bitmap.createBitmap(MY_COL, MY_ROW, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (arr[i][j] == 0)
                    bitmap.setPixel(j,i,Color.BLACK);
            }
        }
        return bitmap;
    }
    //按比例缩放图片
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
    //保存的为缩放后的bitmap（并且只保存黑色像素）
    protected int[][] saveScaledBitmapArray(Bitmap bmp) {
        Bitmap new_bmp = scaleBitmap(bmp,0.25f);
        int width = new_bmp.getWidth();
        int height = new_bmp.getHeight();
        //bmp.getPixels(pixels,0,width,0,0,width,height);
        int[][] ps = new int[height][width];//存成一个长*宽的矩阵
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int p = new_bmp.getPixel(j, i);//p是-1是白色，p是-16777216是黑色
                if (p == -16777216)
                    ps[i][j] = 0;
                else
                    ps[i][j] = 1;
            }
        }
        return ps;
    }


    /**
     * 设置标题栏 需确定 该页面的layout布局文件 include title_layout
     * @param titleName 标题
     * @param showBackIcon 是否显示返回按钮
     * @param showMenuIcon 是否显示菜单按钮
     */
    protected void setTitleBar(String titleName, boolean showBackIcon,boolean showMenuIcon){
        try {
            mTvTitle = (TextView) findViewById(R.id.tv_title);
            mTvTitle.setText(titleName);
            mIvBack = (ImageView) findViewById(R.id.iv_back_btn);
            mIvMenu = (ImageView) findViewById(R.id.iv_menu_btn);
            if (showBackIcon){
                mIvBack.setVisibility(View.VISIBLE);
            }
            if (showMenuIcon){
                mIvMenu.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
