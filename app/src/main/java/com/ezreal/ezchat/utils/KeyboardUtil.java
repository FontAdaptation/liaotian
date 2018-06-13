package com.ezreal.ezchat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.ezreal.ezchat.emojilibrary.EmojiUtils;
import com.ezreal.ezchat.R;
import com.ezreal.ezchat.widget.ChatInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 仝心
 */

public class KeyboardUtil {
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard k1;// 字母键盘
    private Keyboard k2;// 数字键盘
    private Keyboard k3;//符号键盘
    public boolean isnun = false;// 是否数据键盘
    public boolean isupper = false;// 是否大写
    private boolean isSymbol = false;// 是否是符号

    Resources res;
    int[][][] imageBmp;
    int[][] A;
    private Canvas show_canvas;
    private Paint paint;
    Bitmap showBitmap;
    //ImageView iv1;
    float sensorData;//抖动参数
    int millisecond;//触键时间 毫秒
    float skewData;//倾斜程度
    public static final int MY_ROW = 100;
    public static final int MY_COL = 75;
    public static List<Bitmap> inputBmpList = new ArrayList<Bitmap>();//存放文本框中已输入的图片list
    public static int count;//文本框中已输入的图片个数
    public static int curPos=0;//文本框当前光标所在的图片位置数
    private Toast mToast;

    private EditText ed;

    public KeyboardUtil(Activity act, EditText editText, KeyboardView keyboardViews, Resources resourses, int[][][] bitmap) {
        this.act = act;
        this.ed = editText;
        this.res = resourses;
        this.imageBmp = bitmap;
        this.A = bitmap[0];
        k1 = new Keyboard(act.getApplication(), R.xml.qwerty);
        k2 = new Keyboard(act.getApplication(), R.xml.number);
        k3 = new Keyboard(act.getApplication(), R.xml.symbol);
        keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view_my);//***********************
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        mToast = Toast.makeText(act, "", Toast.LENGTH_SHORT);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            Bitmap bmp;

            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                        inputBmpList.remove(start-1);
                        count --;
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
                keyboardView.setKeyboard(k1);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                if (isnun) {
                    isnun = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isnun = true;
                    keyboardView.setKeyboard(k2);
                }
            }else if (primaryCode == 90001) {
//                  字母与符号切换
                if (isSymbol) {
                    isSymbol = false;
                    keyboardView.setKeyboard(k1);
                } else {
                    isSymbol = true;
                    keyboardView.setKeyboard(k3);
                }
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else if(primaryCode == 32){//空格符
                bmp = Bitmap.createBitmap(MY_COL/2,MY_ROW, Bitmap.Config.ARGB_8888);
                System.out.println(sensorData);
                //iv1.setImageBitmap(bmp);
                SpannableString ss=getBitmapMime(bmp);
                insertPhotoToEditText(ss);
                count++;

                inputBmpList.add(start,bmp);
            }
            else {
                if(imageBmp[primaryCode-33]!=null){
                    bmp = paintNewImage(imageBmp[primaryCode-33],sensorData,millisecond,skewData);
                }else{
                    bmp = Bitmap.createBitmap(MY_COL/2,MY_ROW, Bitmap.Config.ARGB_8888);
                    //提示字体库不全
                    mToast.setText("个人字体库不完整！");
                    mToast.show();
                }
                System.out.println(sensorData);
                SpannableString ss=getBitmapMime(bmp);
                insertPhotoToEditText(ss);
                inputBmpList.add(start,bmp);
                count++;
            }
        }
    };

    //获得抖动参数
    public void getSensorData(float data){
        sensorData = data;
    }
    //获得按键时间
    public void getTouchTime(int time)
    {
        millisecond = time;
    }
    //获得倾斜角度
    public void getSkewData(float angle){
        skewData = angle;
    }
    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        List<Keyboard.Key> keylist = k1.getKeys();
        if (isupper) {//大写切换小写
            isupper = false;
            for(Keyboard.Key key:keylist){
                if (key.label!=null && isword(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0]+32;
                }
            }
        } else {//小写切换大写
            isupper = true;
            for(Keyboard.Key key:keylist){
                if (key.label!=null && isword(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0]-32;
                }
            }
        }
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isword(String str){
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        if (wordstr.indexOf(str.toLowerCase())>-1) {
            return true;
        }
        return false;
    }


    /**
     * 利用数据重绘字母图片
     * @param arr 从数据库中拿出的字母数组
     * @param tense 抖动程度
     * @param level 画笔粗细
     * @param angle 倾斜角度
     * @return 新的Bitmap
     */
    protected Bitmap paintNewImage(int[][] arr,float tense,int level,float angle) {//抖动程度、画笔粗细、倾斜角度

        int row = arr.length;
        int col = arr[0].length;
        int min=0;
        int max=10;
        Random random = new Random();
        int num1 = random.nextInt(max)%(max-min+1) -5;
        int num2 = random.nextInt(max)%(max-min+1) -5;
        int[][] arr1 = distort1(arr,num1,num2,row,col);//异形文字
        arr = trembleImage(arr1,tense);//抖动
        paint = new Paint();
        //if (showBitmap == null) {
        //}

        int left = 0,right = 0;
        boolean flag = true;
        //判断最左和最右的像素
        for(int i = 0;i<col&&flag;i++)
        {
            for(int j = 0;j<row;j++)
            {
                if(arr[j][i] == 0&&j<0.95*row)
                {
                    left = i;
                    flag = false;
                    System.out.println(i+ "   "+ j +"                          ////////////////////////////////");
                    break;
                }
            }
        }
        flag = true;


        for(int i = col-1;i>0&&flag;i--)
        {
            for(int j = 0;j<row;j++)
            {
                if(arr[j][i] == 0&&j<0.95*row)
                {
                    right = i;
                    flag = false;
                    System.out.println(right+"                          ////////////////////////////////");
                    break;
                }
            }
        }

        showBitmap = Bitmap.createBitmap(right - left +11,
                MY_ROW, Bitmap.Config.ARGB_8888);
        show_canvas = new Canvas(showBitmap);
        show_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        show_canvas.skew((float)Math.tan(angle*3.14/180),0f);
        show_canvas.translate(-50*(float)Math.tan(angle*3.14/180),0);
        System.out.println(row+"   "+ col+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for (int i = 0; i < row; i++) {
            for (int j = left; j < right; j++) {
                paint.setARGB(255, 0, 0, 0);
                paint.setStrokeWidth(level);
                if (arr[i][j] == 0&&i<0.95*row)
                    show_canvas.drawPoint(j-left+6, i, paint);
            }
        }
        return  showBitmap;
    }

    /**
    * 获得抖动图像的矩阵
    */
    protected int[][] trembleImage(int[][] arr,float tense){

        int row = arr.length;
        int col  = arr[0].length;
        for(int i = 0;i<row;i++)
        {
            for(int j = 0;j<col;j++)
                System.out.print(arr[i][j]);
            System.out.println();
        }
        int width = 19;//几行为一组
        int height = 19;//几列为一组
        int half_width = 10;
        int half_height = 10;
        int row_block = row/width;//行被分成了几组
        int col_block = col/height;//列被分成了几组
        int[][] new_arr = new int[MY_ROW][MY_COL];
        //tense = 3;//抖动程度(1-5)，数值越小抖得越厉害
        paint = new Paint();
        showBitmap = Bitmap.createBitmap(MY_COL,
                MY_ROW, Bitmap.Config.ARGB_8888);
        show_canvas = new Canvas(showBitmap);
        show_canvas.drawColor(Color.WHITE);

        for(int i = 0;i < row_block;i++)
        {
            for(int j = 0;j < height;j++)
            {
                for(int k = 0;k < col;k++) {
                    if(k<col - half_height)
                        if (j < half_height)
                            new_arr[j + i * height][k] = arr[j + i * height][(int)(k + j/tense)];
                        else
                            new_arr[j + i * height][k] = arr[j + i * height][(int)(k - j/tense + height/tense)];
                    else
                        new_arr[j + i * height][k] = arr[j + i * height][k];
                }
            }
        }

        int[][] new_arr_2 = new_arr;

        for(int i = 0;i < col_block;i++)
        {
            for(int j = 0;j < width;j++)
            {
                for(int k = 0;k < row;k++) {
                    if(k<row - half_width)
                        if (j < half_width)
                            new_arr_2[k][j + i * width] = new_arr[(int)(k + j/tense)][j + i * width];
                        else
                            new_arr_2[k][j + i * width] = new_arr[(int)(k - j/tense + width/tense)][j + i * width];
                    else
                        new_arr_2[k][j + i * width] = new_arr[k][j + i * width];
                }
            }
        }
        for(int i = 0;i<row;i++)
        {
            for(int j = 0;j<col;j++)
                System.out.print(arr[i][j]);
            System.out.println();
        }
        return (new_arr_2);
    }

    /**
    *绘制异形文字
    */
    protected int[][] distort1(int[][] pixels_copy, int dx, int dy, int row, int col) {
        //往左往上拉伸，dx，dy为负
        int[][] result_pixels1 = new int[row][col];
        int[][] result_pixels2 = new int[row][col];

        int currentY, currentX;//当前要拉伸的行数
        int xAdd1, yAdd1, xAdd, yAdd;


        //逐行拉伸
        for (currentY = 0; currentY < row; currentY++) {
            //对于每一行要扩展的像素数
            if (currentY <= row/2+dy) {
                xAdd = currentY * Math.abs(dx) / (row/2+dy);
            } else {
                xAdd = (row - currentY) * Math.abs(dx) / (row/2 - dy);
            }
            //判断往左还是往右拉伸
            if (dx <= 0) {
                //往左拉伸
                xAdd1 = xAdd;
            } else {
                xAdd1 = 0;
            }
            //反向映射当前像素点的颜色
            for (int x = 0; x < col; x++) {
                int x0 = (x + xAdd1) * col / (col + xAdd);//对应的原图x坐标
                result_pixels1[currentY][x] = pixels_copy[currentY][x0];
            }
        }

        //逐列拉伸
        for (currentX = 0; currentX < col; currentX++) {
            //对于每一列要扩展的像素数
            if (currentX <= col/2+dx) {
                yAdd = currentX * Math.abs(dy) / (col/2+dx);
            } else {
                yAdd = (col - currentX) * Math.abs(dy) / (col/2 - dx);
            }
            //判断往上还是往下拉伸
            if (dy <= 0) {
                //往上拉伸
                yAdd1 = yAdd;
            } else {
                yAdd1 = 0;
            }
            //反向映射颜色值
            for (int y = 0; y < row; y++) {
                int y0 = (y + yAdd1) * row / (row + yAdd);
                result_pixels2[y][currentX] = result_pixels1[y0][currentX];
            }
        }

        return result_pixels2;

    }


    /**
     *利用给定bitmap得到SpannableString
     */
    private SpannableString getBitmapMime(Bitmap bmp)
    {
        SpannableString ss=new SpannableString(" ");
        Drawable drawable = new BitmapDrawable(bmp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(imageSpan,0," ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    /**
     * 将ss插入文本框当前光标位置，并移动光标
     * */
    private void insertPhotoToEditText(SpannableString ss)
    {
        Editable et=ed.getText();
        int start=ed.getSelectionStart();
        et.insert(start,ss);
        ed.setText(et);
        ed.setSelection(start+ss.length());
        ed.setFocusableInTouchMode(true);
        ed.setFocusable(true);

    }



}
