package com.ezreal.ezchat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezreal.ezchat.R;
import com.ezreal.ezchat.utils.ImageSQLiteHelper;

/**
 * Created by 张静
 */

public class ContentActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6,mTvTitle;
    ImageView iv0,iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9,iv10,iv11,iv12,iv13,iv14,iv15, iv16,iv17,iv18,iv19,iv20,iv21,iv22,iv23,iv24
            ,iv25,iv26,iv27,iv28,iv29,iv30,iv31,iv32,iv33,iv34,iv35,iv36,iv37,iv38,iv39,iv40,iv41,iv42,iv43,iv44,iv45,iv46,iv47,iv48,iv49
            ,iv50,iv51,iv52,iv53,iv54,iv55,iv56,iv57,iv58,iv59,iv60,iv61,iv62,iv63,iv64,iv65,iv66,iv67,iv68,iv69,iv70,iv71,iv72,iv73,iv74,iv75
            ,iv76,iv77,iv78,iv79,iv80,iv81,iv82,iv83,iv84,iv85,iv86,iv87,iv88,iv89,iv90,iv91,iv92,iv93,mIvBack,mIvMenu;
    String[] imageStr;
    public static final int MY_ROW = 100;
    public static final int MY_COL = 75;
    int[][] myImageArray;
    Paint paint;
    private Bitmap showBitmap;
    private Canvas show_canvas;
    int ImageNum;//记录下点击的图片的编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_album_layout);

        setTitleBar("字体库列表",true,false);

        paint = new Paint();
        tv1 = (TextView)findViewById(R.id.asc034);//双引号"
        tv2 = (TextView)findViewById(R.id.asc038);//and &号
        tv3 = (TextView)findViewById(R.id.asc063);//?
        tv4 = (TextView)findViewById(R.id.asc064);//@
        tv5 = (TextView)findViewById(R.id.asc60);//<
        tv6 = (TextView)findViewById(R.id.asc092);//|

        iv0 = (ImageView)findViewById(R.id.asc_033);//!
        iv0.setTag(0);
        iv1 = (ImageView)findViewById(R.id.asc_034);//"
        iv1.setTag(1);
        iv2 = (ImageView)findViewById(R.id.asc_035);//#
        iv2.setTag(2);
        iv3 = (ImageView)findViewById(R.id.asc_036);//$
        iv3.setTag(3);
        iv4 = (ImageView)findViewById(R.id.asc_037);//%
        iv4.setTag(4);
        iv5 = (ImageView)findViewById(R.id.asc_038);//&
        iv5.setTag(5);
        iv6 = (ImageView)findViewById(R.id.asc_039);
        iv6.setTag(6);
        iv7 = (ImageView)findViewById(R.id.asc_040);
        iv7.setTag(7);
        iv8 = (ImageView)findViewById(R.id.asc_041);
        iv8.setTag(8);
        iv9 = (ImageView)findViewById(R.id.asc_042);
        iv9.setTag(9);
        iv10 = (ImageView)findViewById(R.id.asc_043);
        iv10.setTag(10);
        iv11 = (ImageView)findViewById(R.id.asc_044);
        iv11.setTag(11);
        iv12 = (ImageView)findViewById(R.id.asc_045);
        iv12.setTag(12);
        iv13 = (ImageView)findViewById(R.id.asc_046);
        iv13.setTag(13);
        iv14 = (ImageView)findViewById(R.id.asc_047);
        iv14.setTag(14);
        iv15 = (ImageView)findViewById(R.id.asc_048);
        iv15.setTag(15);
        iv16 = (ImageView)findViewById(R.id.asc_049);
        iv16.setTag(16);
        iv17 = (ImageView)findViewById(R.id.asc_050);
        iv17.setTag(17);
        iv18 = (ImageView)findViewById(R.id.asc_051);
        iv18.setTag(18);
        iv19 = (ImageView)findViewById(R.id.asc_052);
        iv19.setTag(19);
        iv20 = (ImageView)findViewById(R.id.asc_053);
        iv20.setTag(20);
        iv21 = (ImageView)findViewById(R.id.asc_054);
        iv21.setTag(21);
        iv22 = (ImageView)findViewById(R.id.asc_055);
        iv22.setTag(22);
        iv23 = (ImageView)findViewById(R.id.asc_056);
        iv23.setTag(23);
        iv24 = (ImageView)findViewById(R.id.asc_057);
        iv24.setTag(24);
        iv25 = (ImageView)findViewById(R.id.asc_058);
        iv25.setTag(25);
        iv26 = (ImageView)findViewById(R.id.asc_059);
        iv26.setTag(26);
        iv27 = (ImageView)findViewById(R.id.asc_060);
        iv27.setTag(27);
        iv28 = (ImageView)findViewById(R.id.asc_061);
        iv28.setTag(28);
        iv29 = (ImageView)findViewById(R.id.asc_062);
        iv29.setTag(29);
        iv30 = (ImageView)findViewById(R.id.asc_063);
        iv30.setTag(30);
        iv31 = (ImageView)findViewById(R.id.asc_064);
        iv31.setTag(31);
        iv32 = (ImageView)findViewById(R.id.asc_065);
        iv32.setTag(32);
        iv33 = (ImageView)findViewById(R.id.asc_066);
        iv33.setTag(33);
        iv34 = (ImageView)findViewById(R.id.asc_067);
        iv34.setTag(34);
        iv35 = (ImageView)findViewById(R.id.asc_068);
        iv35.setTag(35);
        iv36 = (ImageView)findViewById(R.id.asc_069);
        iv36.setTag(36);
        iv37 = (ImageView)findViewById(R.id.asc_070);
        iv37.setTag(37);
        iv38 = (ImageView)findViewById(R.id.asc_071);
        iv38.setTag(38);
        iv39 = (ImageView)findViewById(R.id.asc_072);
        iv39.setTag(39);
        iv40 = (ImageView)findViewById(R.id.asc_073);
        iv40.setTag(40);
        iv41 = (ImageView)findViewById(R.id.asc_074);
        iv41.setTag(41);
        iv42 = (ImageView)findViewById(R.id.asc_075);
        iv42.setTag(42);
        iv43 = (ImageView)findViewById(R.id.asc_076);
        iv43.setTag(43);
        iv44 = (ImageView)findViewById(R.id.asc_077);
        iv44.setTag(44);
        iv45 = (ImageView)findViewById(R.id.asc_078);
        iv45.setTag(45);
        iv46 = (ImageView)findViewById(R.id.asc_079);
        iv46.setTag(46);
        iv47 = (ImageView)findViewById(R.id.asc_080);
        iv47.setTag(47);
        iv48 = (ImageView)findViewById(R.id.asc_081);
        iv48.setTag(48);
        iv49 = (ImageView)findViewById(R.id.asc_082);
        iv49.setTag(49);
        iv50 = (ImageView)findViewById(R.id.asc_083);
        iv50.setTag(50);
        iv51 = (ImageView)findViewById(R.id.asc_084);
        iv51.setTag(51);
        iv52 = (ImageView)findViewById(R.id.asc_085);
        iv52.setTag(52);
        iv53 = (ImageView)findViewById(R.id.asc_086);
        iv53.setTag(53);
        iv54 = (ImageView)findViewById(R.id.asc_087);
        iv54.setTag(54);
        iv55 = (ImageView)findViewById(R.id.asc_088);
        iv55.setTag(55);
        iv56 = (ImageView)findViewById(R.id.asc_089);
        iv56.setTag(56);
        iv57 = (ImageView)findViewById(R.id.asc_090);
        iv57.setTag(57);
        iv58 = (ImageView)findViewById(R.id.asc_091);
        iv58.setTag(58);
        iv59 = (ImageView)findViewById(R.id.asc_092);
        iv59.setTag(59);
        iv60 = (ImageView)findViewById(R.id.asc_093);
        iv60.setTag(60);
        iv61 = (ImageView)findViewById(R.id.asc_094);
        iv61.setTag(61);
        iv62 = (ImageView)findViewById(R.id.asc_095);
        iv62.setTag(62);
        iv63 = (ImageView)findViewById(R.id.asc_096);
        iv63.setTag(63);
        iv64 = (ImageView)findViewById(R.id.asc_097);
        iv64.setTag(64);
        iv65 = (ImageView)findViewById(R.id.asc_098);
        iv65.setTag(65);
        iv66 = (ImageView)findViewById(R.id.asc_099);
        iv66.setTag(66);
        iv67 = (ImageView)findViewById(R.id.asc_100);
        iv67.setTag(67);
        iv68 = (ImageView)findViewById(R.id.asc_101);
        iv68.setTag(68);
        iv69 = (ImageView)findViewById(R.id.asc_102);
        iv69.setTag(69);
        iv70 = (ImageView)findViewById(R.id.asc_103);
        iv70.setTag(70);
        iv71 = (ImageView)findViewById(R.id.asc_104);
        iv71.setTag(71);
        iv72 = (ImageView)findViewById(R.id.asc_105);
        iv72.setTag(72);
        iv73 = (ImageView)findViewById(R.id.asc_106);
        iv73.setTag(73);
        iv74 = (ImageView)findViewById(R.id.asc_107);
        iv74.setTag(74);
        iv75 = (ImageView)findViewById(R.id.asc_108);
        iv75.setTag(75);
        iv76 = (ImageView)findViewById(R.id.asc_109);
        iv76.setTag(76);
        iv77 = (ImageView)findViewById(R.id.asc_110);
        iv77.setTag(77);
        iv78 = (ImageView)findViewById(R.id.asc_111);
        iv78.setTag(78);
        iv79 = (ImageView)findViewById(R.id.asc_112);
        iv79.setTag(79);
        iv80 = (ImageView)findViewById(R.id.asc_113);
        iv80.setTag(80);
        iv81 = (ImageView)findViewById(R.id.asc_114);
        iv81.setTag(81);
        iv82 = (ImageView)findViewById(R.id.asc_115);
        iv82.setTag(82);
        iv83 = (ImageView)findViewById(R.id.asc_116);
        iv83.setTag(83);
        iv84 = (ImageView)findViewById(R.id.asc_117);
        iv84.setTag(84);
        iv85 = (ImageView)findViewById(R.id.asc_118);
        iv85.setTag(85);
        iv86 = (ImageView)findViewById(R.id.asc_119);
        iv86.setTag(86);
        iv87 = (ImageView)findViewById(R.id.asc_120);
        iv87.setTag(87);
        iv88 = (ImageView)findViewById(R.id.asc_121);
        iv88.setTag(88);
        iv89 = (ImageView)findViewById(R.id.asc_122);
        iv89.setTag(89);
        iv90 = (ImageView)findViewById(R.id.asc_123);
        iv90.setTag(90);
        iv91 = (ImageView)findViewById(R.id.asc_124);
        iv91.setTag(91);
        iv92 = (ImageView)findViewById(R.id.asc_125);
        iv92.setTag(92);
        iv93 = (ImageView)findViewById(R.id.asc_126);
        iv93.setTag(93);

        iv0.setOnClickListener(new OnImageClickListener());
        iv1.setOnClickListener(new OnImageClickListener());
        iv2.setOnClickListener(new OnImageClickListener());
        iv3.setOnClickListener(new OnImageClickListener());
        iv4.setOnClickListener(new OnImageClickListener());
        iv5.setOnClickListener(new OnImageClickListener());
        iv6.setOnClickListener(new OnImageClickListener());
        iv7.setOnClickListener(new OnImageClickListener());
        iv8.setOnClickListener(new OnImageClickListener());
        iv9.setOnClickListener(new OnImageClickListener());
        iv10.setOnClickListener(new OnImageClickListener());
        iv11.setOnClickListener(new OnImageClickListener());
        iv12.setOnClickListener(new OnImageClickListener());
        iv13.setOnClickListener(new OnImageClickListener());
        iv14.setOnClickListener(new OnImageClickListener());
        iv15.setOnClickListener(new OnImageClickListener());
        iv16.setOnClickListener(new OnImageClickListener());
        iv17.setOnClickListener(new OnImageClickListener());
        iv18.setOnClickListener(new OnImageClickListener());
        iv19.setOnClickListener(new OnImageClickListener());
        iv20.setOnClickListener(new OnImageClickListener());
        iv21.setOnClickListener(new OnImageClickListener());
        iv22.setOnClickListener(new OnImageClickListener());
        iv23.setOnClickListener(new OnImageClickListener());
        iv24.setOnClickListener(new OnImageClickListener());
        iv25.setOnClickListener(new OnImageClickListener());
        iv26.setOnClickListener(new OnImageClickListener());
        iv27.setOnClickListener(new OnImageClickListener());
        iv28.setOnClickListener(new OnImageClickListener());
        iv29.setOnClickListener(new OnImageClickListener());
        iv30.setOnClickListener(new OnImageClickListener());
        iv31.setOnClickListener(new OnImageClickListener());
        iv32.setOnClickListener(new OnImageClickListener());
        iv33.setOnClickListener(new OnImageClickListener());
        iv34.setOnClickListener(new OnImageClickListener());
        iv35.setOnClickListener(new OnImageClickListener());
        iv36.setOnClickListener(new OnImageClickListener());
        iv37.setOnClickListener(new OnImageClickListener());
        iv38.setOnClickListener(new OnImageClickListener());
        iv39.setOnClickListener(new OnImageClickListener());
        iv40.setOnClickListener(new OnImageClickListener());
        iv41.setOnClickListener(new OnImageClickListener());
        iv30.setOnClickListener(new OnImageClickListener());
        iv31.setOnClickListener(new OnImageClickListener());
        iv32.setOnClickListener(new OnImageClickListener());
        iv33.setOnClickListener(new OnImageClickListener());
        iv34.setOnClickListener(new OnImageClickListener());
        iv35.setOnClickListener(new OnImageClickListener());
        iv36.setOnClickListener(new OnImageClickListener());
        iv37.setOnClickListener(new OnImageClickListener());
        iv38.setOnClickListener(new OnImageClickListener());
        iv39.setOnClickListener(new OnImageClickListener());
        iv40.setOnClickListener(new OnImageClickListener());
        iv41.setOnClickListener(new OnImageClickListener());
        iv42.setOnClickListener(new OnImageClickListener());
        iv43.setOnClickListener(new OnImageClickListener());
        iv44.setOnClickListener(new OnImageClickListener());
        iv45.setOnClickListener(new OnImageClickListener());
        iv46.setOnClickListener(new OnImageClickListener());
        iv47.setOnClickListener(new OnImageClickListener());
        iv48.setOnClickListener(new OnImageClickListener());
        iv49.setOnClickListener(new OnImageClickListener());
        iv50.setOnClickListener(new OnImageClickListener());
        iv51.setOnClickListener(new OnImageClickListener());
        iv52.setOnClickListener(new OnImageClickListener());
        iv53.setOnClickListener(new OnImageClickListener());
        iv54.setOnClickListener(new OnImageClickListener());
        iv55.setOnClickListener(new OnImageClickListener());
        iv56.setOnClickListener(new OnImageClickListener());
        iv57.setOnClickListener(new OnImageClickListener());
        iv58.setOnClickListener(new OnImageClickListener());
        iv59.setOnClickListener(new OnImageClickListener());
        iv60.setOnClickListener(new OnImageClickListener());
        iv61.setOnClickListener(new OnImageClickListener());
        iv62.setOnClickListener(new OnImageClickListener());
        iv63.setOnClickListener(new OnImageClickListener());
        iv64.setOnClickListener(new OnImageClickListener());
        iv65.setOnClickListener(new OnImageClickListener());
        iv66.setOnClickListener(new OnImageClickListener());
        iv67.setOnClickListener(new OnImageClickListener());
        iv68.setOnClickListener(new OnImageClickListener());
        iv69.setOnClickListener(new OnImageClickListener());
        iv70.setOnClickListener(new OnImageClickListener());
        iv71.setOnClickListener(new OnImageClickListener());
        iv72.setOnClickListener(new OnImageClickListener());
        iv73.setOnClickListener(new OnImageClickListener());
        iv74.setOnClickListener(new OnImageClickListener());
        iv75.setOnClickListener(new OnImageClickListener());
        iv76.setOnClickListener(new OnImageClickListener());
        iv77.setOnClickListener(new OnImageClickListener());
        iv78.setOnClickListener(new OnImageClickListener());
        iv79.setOnClickListener(new OnImageClickListener());
        iv80.setOnClickListener(new OnImageClickListener());
        iv81.setOnClickListener(new OnImageClickListener());
        iv82.setOnClickListener(new OnImageClickListener());
        iv83.setOnClickListener(new OnImageClickListener());
        iv84.setOnClickListener(new OnImageClickListener());
        iv85.setOnClickListener(new OnImageClickListener());
        iv86.setOnClickListener(new OnImageClickListener());
        iv87.setOnClickListener(new OnImageClickListener());
        iv88.setOnClickListener(new OnImageClickListener());
        iv89.setOnClickListener(new OnImageClickListener());
        iv90.setOnClickListener(new OnImageClickListener());
        iv91.setOnClickListener(new OnImageClickListener());
        iv92.setOnClickListener(new OnImageClickListener());
        iv93.setOnClickListener(new OnImageClickListener());

        mIvBack.setOnClickListener(new mBackListener());

        setTvs();
        setIvs();
    }

    public void setTvs(){
        tv1.setText("\"");
        tv2.setText("&");
        tv3.setText("?");
        tv4.setText("@");
        tv5.setText("<");
        tv6.setText("|");

    }

    public void setIvs(){
        ImageSQLiteHelper dbHelper = new ImageSQLiteHelper(ContentActivity.this,"my_nn_database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("array",new String[]{"id","content","name"},null,null,null,null,"id",null);
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String num = cursor.getString(cursor.getColumnIndex("name"));//!的name是0
            System.out.println("" + num+"   "+ content + "------------" + id);

            imageStr = content.split("[,]");
            myImageArray = new int[MY_ROW][MY_COL];
            for(int j = 0;j<MY_ROW;j++)
            {
                for(int k = 0;k<MY_COL;k++)
                {
                    myImageArray[j][k] = Integer.parseInt(imageStr[MY_COL*j+k]);
                }
            }

            int order = Integer.parseInt(num);//转换成int

            if(order == 0)
                paintNewImage(myImageArray,iv0);
            if(order == 1)
                paintNewImage(myImageArray,iv1);
            if(order == 2)
                paintNewImage(myImageArray,iv2);
            if(order == 3)
                paintNewImage(myImageArray,iv3);
            if(order == 4)
                paintNewImage(myImageArray,iv4);
            if(order == 5)
                paintNewImage(myImageArray,iv5);
            if(order == 6)
                paintNewImage(myImageArray,iv6);
            if(order == 7)
                paintNewImage(myImageArray,iv7);
            if(order == 8)
                paintNewImage(myImageArray,iv8);
            if(order == 9)
                paintNewImage(myImageArray,iv9);
            if(order == 10)
                paintNewImage(myImageArray,iv10);
            if(order == 11)
                paintNewImage(myImageArray,iv11);
            if(order == 12)
                paintNewImage(myImageArray,iv12);
            if(order == 13)
                paintNewImage(myImageArray,iv13);
            if(order == 14)
                paintNewImage(myImageArray,iv14);
            if(order == 15)
                paintNewImage(myImageArray,iv15);
            if(order == 16)
                paintNewImage(myImageArray,iv16);
            if(order == 17)
                paintNewImage(myImageArray,iv17);
            if(order == 18)
                paintNewImage(myImageArray,iv18);
            if(order == 19)
                paintNewImage(myImageArray,iv19);
            if(order == 20)
                paintNewImage(myImageArray,iv20);
            if(order == 21)
                paintNewImage(myImageArray,iv21);
            if(order == 22)
                paintNewImage(myImageArray,iv22);
            if(order == 23)
                paintNewImage(myImageArray,iv23);
            if(order == 24)
                paintNewImage(myImageArray,iv24);
            if(order == 25)
                paintNewImage(myImageArray,iv25);
            if(order == 26)
                paintNewImage(myImageArray,iv26);
            if(order == 27)
                paintNewImage(myImageArray,iv27);
            if(order == 28)
                paintNewImage(myImageArray,iv28);
            if(order == 29)
                paintNewImage(myImageArray,iv29);
            if(order == 30)
                paintNewImage(myImageArray,iv30);
            if(order == 31)
                paintNewImage(myImageArray,iv31);
            if(order == 32)
                paintNewImage(myImageArray,iv32);
            if(order == 33)
                paintNewImage(myImageArray,iv33);
            if(order == 34)
                paintNewImage(myImageArray,iv34);
            if(order == 35)
                paintNewImage(myImageArray,iv35);
            if(order == 36)
                paintNewImage(myImageArray,iv36);
            if(order == 37)
                paintNewImage(myImageArray,iv37);
            if(order == 38)
                paintNewImage(myImageArray,iv38);
            if(order == 39)
                paintNewImage(myImageArray,iv39);
            if(order == 40)
                paintNewImage(myImageArray,iv40);
            if(order == 41)
                paintNewImage(myImageArray,iv41);
            if(order == 42)
                paintNewImage(myImageArray,iv42);
            if(order == 43)
                paintNewImage(myImageArray,iv43);
            if(order == 44)
                paintNewImage(myImageArray,iv44);
            if(order == 45)
                paintNewImage(myImageArray,iv45);
            if(order == 46)
                paintNewImage(myImageArray,iv46);
            if(order == 47)
                paintNewImage(myImageArray,iv47);
            if(order == 48)
                paintNewImage(myImageArray,iv48);
            if(order == 49)
                paintNewImage(myImageArray,iv49);
            if(order == 50)
                paintNewImage(myImageArray,iv50);
            if(order == 51)
                paintNewImage(myImageArray,iv51);
            if(order == 52)
                paintNewImage(myImageArray,iv52);
            if(order == 53)
                paintNewImage(myImageArray,iv53);
            if(order == 54)
                paintNewImage(myImageArray,iv54);
            if(order == 55)
                paintNewImage(myImageArray,iv55);
            if(order == 56)
                paintNewImage(myImageArray,iv56);
            if(order == 57)
                paintNewImage(myImageArray,iv57);
            if(order == 58)
                paintNewImage(myImageArray,iv58);
            if(order == 59)
                paintNewImage(myImageArray,iv59);
            if(order == 60)
                paintNewImage(myImageArray,iv60);
            if(order == 61)
                paintNewImage(myImageArray,iv61);
            if(order == 62)
                paintNewImage(myImageArray,iv62);
            if(order == 63)
                paintNewImage(myImageArray,iv63);
            if(order == 64)
                paintNewImage(myImageArray,iv64);
            if(order == 65)
                paintNewImage(myImageArray,iv65);
            if(order == 66)
                paintNewImage(myImageArray,iv66);
            if(order == 67)
                paintNewImage(myImageArray,iv67);
            if(order == 68)
                paintNewImage(myImageArray,iv68);
            if(order == 69)
                paintNewImage(myImageArray,iv69);
            if(order == 70)
                paintNewImage(myImageArray,iv70);
            if(order == 71)
                paintNewImage(myImageArray,iv71);
            if(order == 72)
                paintNewImage(myImageArray,iv72);
            if(order == 73)
                paintNewImage(myImageArray,iv73);
            if(order == 74)
                paintNewImage(myImageArray,iv74);
            if(order == 75)
                paintNewImage(myImageArray,iv75);
            if(order == 76)
                paintNewImage(myImageArray,iv76);
            if(order == 77)
                paintNewImage(myImageArray,iv77);
            if(order == 78)
                paintNewImage(myImageArray,iv78);
            if(order == 79)
                paintNewImage(myImageArray,iv79);
            if(order == 80)
                paintNewImage(myImageArray,iv80);
            if(order == 81)
                paintNewImage(myImageArray,iv81);
            if(order == 82)
                paintNewImage(myImageArray,iv82);
            if(order == 83)
                paintNewImage(myImageArray,iv83);
            if(order == 84)
                paintNewImage(myImageArray,iv84);
            if(order == 85)
                paintNewImage(myImageArray,iv85);
            if(order == 86)
                paintNewImage(myImageArray,iv86);
            if(order == 87)
                paintNewImage(myImageArray,iv87);
            if(order == 88)
                paintNewImage(myImageArray,iv88);
            if(order == 89)
                paintNewImage(myImageArray,iv89);
            if(order == 90)
                paintNewImage(myImageArray,iv90);
            if(order == 91)
                paintNewImage(myImageArray,iv91);
            if(order == 92)
                paintNewImage(myImageArray,iv92);
            if(order == 93)
                paintNewImage(myImageArray,iv93);
        }
    }

    protected void paintNewImage(int[][] arr,ImageView iv) {//二维数组的行数和列数

        int row = arr.length;
        int col = arr[0].length;

        showBitmap = Bitmap.createBitmap(MY_COL,
                MY_ROW, Bitmap.Config.ARGB_8888);
        show_canvas = new Canvas(showBitmap);
        show_canvas.drawColor(Color.WHITE);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                paint.setARGB(255, 0, 0, 0);
                paint.setStrokeWidth(4f);
                if (arr[i][j] == 0)
                    show_canvas.drawPoint(j, i, paint);
            }
        }

        // 把图片展示到ImageView中
        iv.setImageBitmap(showBitmap);
    }

    private class OnImageClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(ContentActivity.this,CreateActivity.class);
            CreateActivity.count=(Integer)view.getTag();
            //ImageNum =(Integer)view.getTag();
            //intent.putExtra("myIntent",ImageNum+"");
            ContentActivity.this.startActivityForResult(intent,100);
        }
    }

    private class mBackListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            ContentActivity.this.finish();
        }
    }
    public void onBackPressed() {
        ContentActivity.this.finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //可以根据多个请求代码来作相应的操作
        if(20==resultCode)
        {
            setIvs();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
