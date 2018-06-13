package com.ezreal.ezchat.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.IDNA;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezreal.ezchat.audiorecordbutton.AudioRecordButton;
import com.ezreal.ezchat.emojilibrary.EmojiBean;
import com.ezreal.ezchat.emojilibrary.EmojiUtils;
import com.ezreal.ezchat.emojilibrary.ExpressLayout;
import com.ezreal.ezchat.R;
import com.ezreal.ezchat.activity.CreateActivity;
import com.ezreal.ezchat.utils.Constant;
import com.ezreal.ezchat.utils.KeyboardUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.ezreal.ezchat.commonlibrary.utils.SharedPreferencesUtil;
import com.ezreal.ezchat.commonlibrary.utils.SystemUtils;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ezreal.ezchat.utils.KeyboardUtil.inputBmpList;
import static com.ezreal.ezchat.utils.KeyboardUtil.count;
/**
 * 聊天界面 输入相关组合布局
 * Created by 仝心
 */

public class ChatInputLayout extends RelativeLayout {

    @BindView(R.id.iv_input_type)
    ImageView mIvInputType;
    @BindView(R.id.et_chat_message)
    EditText mEtInput;
    @BindView(R.id.btn_audio_record)
    AudioRecordButton mBtnAudioRecord;
    @BindView(R.id.iv_expression)
    ImageView mIvExpress;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.tv_btn_send)
    TextView mBtnSend;

    @BindView(R.id.layout_extension)
    RelativeLayout mExtensionLayout;
    @BindView(R.id.layout_express)
    ExpressLayout mExpressLayout;

    @BindView(R.id.layout_sound_recog)
    RelativeLayout mSoundRecogLayout;
    @BindView(R.id.layout_change_mode)
    LinearLayout mChangeModeLayout;
    @BindView(R.id.layout_myKeyboard)
    RelativeLayout mKeyboardLayout;
    @BindView(R.id.keyboard_view_my)
    KeyboardView mKv;


    private InputMethodManager mInputManager;
    private OnInputLayoutListener mLayoutListener;
    private Activity mActivity;
    private View mContentView;
    private KeyboardUtil myKeyboard;

    SensorManager sensorManager;//定义传感器的管理器
    Sensor accelerateSensor,magneticSensor;//获取其中一种传感器
    int second1,second2,millisecond1,millisecond2;//用来记录按下和抬起的秒和毫秒
    int totalMilliSecond;//用来记录按下和抬起一共用了多少毫秒
    int[][][] myImageArrays = CreateActivity.myImageArrays;
    float tempTense;
    public static final int MY_ROW = 100;
    public static final int MY_COL = 75;
    public static List<Bitmap> currentBmpList = new ArrayList<Bitmap>();


    float[] accelerometerValues = new float[3];
    float[] magneticValues = {0f,0f,0f};
    float[] usedValues = {0f,0f,0f};//存上一次传感器的数据

    //讯飞语音识别相关****************
    private Context mContext;
    public static final String PREFER_NAME = "com.voice.recognition";
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Toast mToast;
    @BindView(R.id.bt_start)
    Button btStart;
    private SharedPreferences mSharedPreferences;
    private int ret = 0; // 函数调用返回值
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String new_piece="";
    private String result_string="";
    private int sound=0,soundCount=0;



    public ChatInputLayout(Context context) {
        this(context, null);
    }

    public ChatInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.layout_chat_input,
                this, true);
        ButterKnife.bind(root);
        mInputManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        initListener();

        //讯飞语音识别相关
        SpeechUtility.createUtility(context,"appid="+getResources().getString(R.string.app_id));
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(context, mInitListener);
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mSharedPreferences = context.getSharedPreferences(this.PREFER_NAME,
                Activity.MODE_PRIVATE);
        setOnclickListener();
        mContext = context;
        mEtInput.setSingleLine(true);
        mEtInput.setHorizontallyScrolling(true);
    }

    /**
     * 根据Activity和内容区域，设置隐藏显示时的控件高度
     */
    public void bindInputLayout(Activity activity, View contentView) {
        mActivity = activity;
        mContentView = contentView;
        myKeyboard = new KeyboardUtil(activity, mEtInput,mKv,getResources(),myImageArrays);
        int keyboardHeight = SharedPreferencesUtil.getIntSharedPreferences(activity, Constant.OPTION_TABLE
                , Constant.OPTION_KEYBOARD_HEIGHT);
        if (keyboardHeight == 0) {
            keyboardHeight = 787;
        }

        ViewGroup.LayoutParams layoutParams = mExpressLayout.getLayoutParams();
        layoutParams.height = keyboardHeight;
        mExpressLayout.setLayoutParams(layoutParams);

        layoutParams = mExtensionLayout.getLayoutParams();
        layoutParams.height = keyboardHeight;
        mExtensionLayout.setLayoutParams(layoutParams);

        layoutParams=mKeyboardLayout.getLayoutParams();
        layoutParams.height=keyboardHeight;
        mKeyboardLayout.setLayoutParams(layoutParams);

        mKv.setOnTouchListener(new touchKeyListener());

        sensorManager = (SensorManager)mActivity.getSystemService(Context.SENSOR_SERVICE);//实例化传感器的管理
        accelerateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//加速度传感
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);//磁力传感
        sensorManager.registerListener(sensorListener, accelerateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);

        //getImageArry();
        //myKeyboardListener();
    }


    //文本框触摸、输入监听；录音按钮初始化和录音监听；表情输入监听
    private void initListener() {
        // 文本输入框触摸监听
        mEtInput.setOnClickListener(new MyOnClickListener());
        // 文本输入框输入监听
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    mIvMore.setVisibility(View.GONE);
                    mBtnSend.setVisibility(View.VISIBLE);
                } else {
                    mIvMore.setVisibility(View.VISIBLE);
                    mBtnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 录音按钮初始化和录音监听
        mBtnAudioRecord.init(Constant.APP_CACHE_AUDIO);
        mBtnAudioRecord.setRecordingListener(new AudioRecordButton.OnRecordingListener() {
            @Override
            public void recordFinish(String audioFilePath, long recordTime) {
                if (mLayoutListener != null) {
                    mLayoutListener.audioRecordFinish(audioFilePath, recordTime);
                }
            }

            @Override
            public void recordError(String message) {
                if (mLayoutListener != null) {
                    mLayoutListener.audioRecordError(message);
                }
            }
        });

        //语音识别按钮监听


        //表情输入监听
        mExpressLayout.setOnExpressSelListener(new ExpressLayout.OnExpressSelListener() {
            @Override
            public void onEmojiSelect(EmojiBean emojiBean) {
                // 如果点击了表情,则添加到输入框中

                int curPosition = mEtInput.getSelectionStart();
                String tag=mChangeModeLayout.getTag().toString();
                switch(tag){
                    case "1":
                        // 获取当前光标位置,在指定位置上添加表情图片文本
                        StringBuilder sb = new StringBuilder(mEtInput.getText().toString());
                        sb.insert(curPosition, emojiBean.getEmojiName());
                        // 特殊文字处理,将表情等转换一下
                        SpannableString spannableString = EmojiUtils.text2Emoji(getContext(),
                                sb.toString(), mEtInput.getTextSize());
                        mEtInput.setText(spannableString);
                        // 将光标设置到新增完表情的右侧
                        mEtInput.setSelection(curPosition + emojiBean.getEmojiName().length());
                        break;
                    case "2":
                        //Drawable drawable = getResources().getDrawable(emojiBean.getResIndex());

                        Bitmap bmp = BitmapFactory.decodeResource(getResources(),emojiBean.getResIndex());
                        SpannableString ss=getBitmapMime(bmp);

                        insertPhotoToEditText(ss);
                        count++;
                        inputBmpList.add(curPosition,bmp);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onEmojiDelete() {
                // 调用系统的删除操作
                mEtInput.dispatchKeyEvent(new KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });

        mEtInput.setFocusable(true);
        mEtInput.requestFocus();//否则第一次点文本框时会弹出系统键盘
        hideSoftInput(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h > oldh){
            if (mLayoutListener != null){
                mLayoutListener.exLayoutShow();
            }
        }
    }

    /************ 按钮点击事件 ***********/


    @OnClick(R.id.iv_input_type)
    public void clickInputTypeBtn(){
        String tag=mIvInputType.getTag().toString();
        switch(tag){
            case "1":
                //说明现在为键盘输入界面，要更改为语音识别界面
                hideSoftInput(3);
                mExtensionLayout.setVisibility(GONE);
                mExpressLayout.setVisibility(GONE);
                mIvExpress.setImageResource(R.mipmap.expression);

                mIvInputType.setImageResource(R.mipmap.sound);
                mSoundRecogLayout.setVisibility(VISIBLE);
                mIvInputType.setTag("2");
                break;
            case "2":
                //说明现在为语音识别界面，要更改为录音界面
                hideSoftInput(3);
                mExtensionLayout.setVisibility(GONE);
                mExpressLayout.setVisibility(GONE);
                mIvExpress.setImageResource(R.mipmap.expression);
                mSoundRecogLayout.setVisibility(GONE);
                mEtInput.setVisibility(GONE);

                mBtnAudioRecord.setVisibility(VISIBLE);
                mIvInputType.setImageResource(R.mipmap.keyboard);
                mIvInputType.setTag("3");
                break;
            case "3":
                //说明现在为录音界面，要更改为键盘输入界面
                mBtnAudioRecord.setVisibility(GONE);
                mEtInput.setVisibility(VISIBLE);
                mIvInputType.setImageResource(R.mipmap.sound_recognise);

                //hideSoftInput(3);
                showSoftInput();
                mIvInputType.setTag("1");
            default:
                break;

        }
    }

    @OnClick(R.id.iv_more)
    public void clickMoreBtn(){
        String tag=mIvInputType.getTag().toString();
        switch(tag){
            case "1":
                //现为键盘输入模式
                if(mExtensionLayout.isShown()){
                    lockContentHeight();
                    mExtensionLayout.setVisibility(GONE);
                    showSoftInput();
                    unLockContentHeight();
                }else{
                    if(isSoftInputShow())
                    {
                        lockContentHeight();
                        hideSoftInput(3);
                    }
                    mExpressLayout.setVisibility(GONE);
                    mIvExpress.setImageResource(R.mipmap.expression);
                    mExtensionLayout.setVisibility(VISIBLE);
                    unLockContentHeight();
                }
                break;
            case "2":
                if(mExtensionLayout.isShown())
                {
                    mExtensionLayout.setVisibility(GONE);
                    mSoundRecogLayout.setVisibility(VISIBLE);
                }else{
                    mExtensionLayout.setVisibility(VISIBLE);
                    mSoundRecogLayout.setVisibility(GONE);
                    mExpressLayout.setVisibility(GONE);
                    mIvExpress.setImageResource(R.mipmap.expression);
                }
                break;
            case "3":
                if(mExtensionLayout.isShown())
                {
                    mExtensionLayout.setVisibility(GONE);
                }else{
                    mExtensionLayout.setVisibility(VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.iv_expression)
    public void clickExpressBtn(){
        String tag=mIvInputType.getTag().toString();
        switch(tag){
            case "1":
                if(mExpressLayout.isShown())
                {
                    lockContentHeight();
                    mExpressLayout.setVisibility(GONE);
                    mIvExpress.setImageResource(R.mipmap.expression);
                    showSoftInput();
                    unLockContentHeight();
                }else{
                    if(isSoftInputShow()){
                        lockContentHeight();
                        hideSoftInput(3);
                    }
                    mExtensionLayout.setVisibility(GONE);
                    mExpressLayout.setVisibility(VISIBLE);
                    mIvExpress.setImageResource(R.mipmap.keyboard);
                    unLockContentHeight();
                }
                break;
            case "2":
                if(mExpressLayout.isShown()){
                    mExpressLayout.setVisibility(GONE);
                    mIvExpress.setImageResource(R.mipmap.expression);
                    mSoundRecogLayout.setVisibility(VISIBLE);
                }else{
                    mExtensionLayout.setVisibility(GONE);
                    mSoundRecogLayout.setVisibility(GONE);
                    mExpressLayout.setVisibility(VISIBLE);
                    mIvExpress.setImageResource(R.mipmap.keyboard);
                }
                break;
            case "3":

                break;
            default:
                break;
        }
    }

    //mEtInput文本框的触摸监听器
    class MyOnClickListener implements  OnClickListener{

        @Override
        public void onClick(View v) {
            String tag=mIvInputType.getTag().toString();
            switch(tag){
                case "1":
                    //lockContentHeight();
                    if (mExpressLayout.isShown()) {
                        lockContentHeight();
                        mExpressLayout.setVisibility(GONE);
                        mIvExpress.setImageResource(R.mipmap.expression);
                        showSoftInput();
                        unLockContentHeight();
                    } else if (mExtensionLayout.isShown()) {
                        System.out.println("从extensionLayout进去的————————————————————————");
                        lockContentHeight();
                        mExtensionLayout.setVisibility(GONE);
                        showSoftInput();
                        unLockContentHeight();
                    } else {
                        hideSoftInput(3);
                        showSoftInput();
                    }
                    break;
                case "2":
                    //lockContentHeight();
                    hideSoftInput(1);
                    if (mExpressLayout.isShown()) {
                        mExpressLayout.setVisibility(GONE);
                        mIvExpress.setImageResource(R.mipmap.expression);
                        mSoundRecogLayout.setVisibility(VISIBLE);
                    } else if (mExtensionLayout.isShown()) {
                        mExtensionLayout.setVisibility(GONE);
                        mSoundRecogLayout.setVisibility(VISIBLE);
                    }
                    break;
                case "3":

                    break;
                default:
                    break;
            }
        }
    }


    //发送键，发送键
    @OnClick(R.id.tv_btn_send)
    public void sendTextMessage() {
        String tag=mChangeModeLayout.getTag().toString();
        switch (tag){
            case "1":
                //正常文本模式，系统键盘
                String text = mEtInput.getText().toString();
                mEtInput.getText().clear();
                //语音识别相关清空
                mIatResults.clear();
                result_string="";
                new_piece="";
                if (mLayoutListener != null) {
                    mLayoutListener.sendBtnClick(text);
                }
                break;
            case "2":
                //个性化文本模式，自定义键盘*****************************************************************************************************************************
                Bitmap bitmap;//文本框所有图片合成的bitmap
                if(inputBmpList.size() == 0)
                    break;
                else if(inputBmpList.size() == 1){
                    bitmap = newBitmap(inputBmpList.get(0));
                }else {
                    bitmap = newBitmap(inputBmpList.get(0));
                    for (int i = 0; i < inputBmpList.size() - 1; i++) {
                        if(bitmap.getWidth()<800)
                        {
                            bitmap = newBitmap(bitmap, inputBmpList.get(i + 1));
                        }
                        else{
                            currentBmpList.add(bitmap);
                            bitmap = null;
                            bitmap = newBitmap(inputBmpList.get(i + 1));
                        }
                    }
                    currentBmpList.add(bitmap);

                }
                int longest;
                if(currentBmpList.size()>0){
                    bitmap = currentBmpList.get(0);
                }
                for(int i = 0;i< currentBmpList.size() - 1;i++){
                    if(bitmap.getWidth()>currentBmpList.get(i+1).getWidth())
                        longest = bitmap.getWidth();
                    else
                        longest = currentBmpList.get(i+1).getWidth();
                    bitmap = newBitmap(bitmap,currentBmpList.get(i+1),longest);
                }
                bitmap = scaleBitmap(bitmap,0.6f);
                currentBmpList.clear();
                //将bitmap的背景色改为蓝色
                bitmap=changeBackground(bitmap);


                //将合成的图保存到指定路径，用于图片发送
                Bitmap bb;
                bb=bitmap.copy(Bitmap.Config.ARGB_8888,true);
                String pathName=Constant.APP_CACHE_IMAGE+System.currentTimeMillis()+".jpg";
                File dirFile=new File(pathName);
                if(!dirFile.exists()){
                    dirFile.getParentFile().mkdir();
                    try{
                        dirFile.createNewFile();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                FileOutputStream out=null;
                try{
                    out = new FileOutputStream(dirFile);
                    bb.compress(Bitmap.CompressFormat.JPEG,90,out);
                    System.out.println("-------------保存到sd指定目录文件下--------------");

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                try{
                    out.flush();
                    out.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                mEtInput.getText().clear();
                if (mLayoutListener != null) {
                    mLayoutListener.photoTextBtnClick(pathName);
                }
                count = 0;
                inputBmpList.clear();
                break;
            default:
                break;

        }



    }

    @OnClick(R.id.layout_image)
    public void selectPhoto() {
        if (mLayoutListener != null) {
            mLayoutListener.photoBtnClick();
        }
    }

    @OnClick(R.id.layout_location)
    public void selectLocation() {
        if (mLayoutListener != null) {
            mLayoutListener.locationBtnClick();
        }
    }


    @OnClick(R.id.layout_change_mode)
    public void changeMode(){
        String tag=mChangeModeLayout.getTag().toString();
        switch (tag){
            case "1":
                mChangeModeLayout.setTag("2");
                break;
            case "2":
                mChangeModeLayout.setTag("1");
                break;
            default:
                break;
        }
        System.out.println("键盘输入模式，自定义为2，系统为1，实际值为:"+mChangeModeLayout.getTag().toString()+"----------------------------");
    }



    /**
     * 锁定内容 View 的高度，解决闪屏问题
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                mContentView.getLayoutParams();
        layoutParams.height = mContentView.getHeight();
        layoutParams.weight = 0.0f;
    }

    /**
     * 释放内容 View 高度
     */
    private void unLockContentHeight() {
        mEtInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                        mContentView.getLayoutParams();
                layoutParams.height = 0;
                layoutParams.weight = 1.0f;
            }
        }, 200);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mEtInput.requestFocus();
        String tag=mChangeModeLayout.getTag().toString();
        switch(tag){
            case "1":
                hideSoftInput(2);
                mInputManager.showSoftInput(mEtInput,0);
                System.out.println("现在进入了showSoftInput的模式1——————————————————————————————");
                break;
            case "2":
                hideSoftInput(1);
                //mInputManager.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
                System.out.println("现在进入了showSoftInput的模式2——————————————————————————————");
                mKeyboardLayout.setVisibility(VISIBLE);
                myKeyboard.showKeyboard();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput(int i) {
        switch(i){
            case 1:
                mInputManager.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
                System.out.println("隐藏系统键盘隐藏系统键盘隐藏系统键盘隐藏系统键盘隐藏系统键盘隐藏系统键盘！！！！！！！！！！！！！！！！！！");
                break;
            case 2:
                mKeyboardLayout.setVisibility(GONE);
                myKeyboard.hideKeyboard();
                break;
            case 3:
                mInputManager.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
                mKeyboardLayout.setVisibility(GONE);
                myKeyboard.hideKeyboard();
                break;
            default:
                break;
        }

    }


    private boolean isSoftInputShow() {
        boolean isShow;
        if(SystemUtils.getKeyBoardHeight(mActivity) != 0||mKv.getVisibility()==VISIBLE){
            isShow=true;
        }else{
            isShow=false;
        }
        return isShow;
    }

    public void setLayoutListener(OnInputLayoutListener layoutListener) {
        this.mLayoutListener = layoutListener;
    }

    /**
     * 隐藏所有已显示的布局（键盘，表情，扩展）,在p2pChatActivity的消息列表触摸事件调用
     */
    public void hideOverView(){
        if (isSoftInputShow()){
            hideSoftInput(3);
        }

        if (mExpressLayout.isShown()){
            mExpressLayout.setVisibility(GONE);
            mIvExpress.setImageResource(R.mipmap.expression);
        }

        if (mExtensionLayout.isShown()){
            mExtensionLayout.setVisibility(GONE);
        }

        if(mSoundRecogLayout.isShown())
        {
            mSoundRecogLayout.setVisibility(GONE);
        }
    }

    public interface OnInputLayoutListener {
        void sendBtnClick(String textMessage);

        void photoTextBtnClick(String path);

        void photoBtnClick();

        void locationBtnClick();

        void audioRecordFinish(String audioFilePath, long recordTime);

        void audioRecordError(String message);

        void exLayoutShow();
    }


    /**
     * 传感器监听器
     * */
    SensorEventListener sensorListener = new SensorEventListener() {
        //传感器改变时,一般是通过这个方法里面的参数确定传感器状态的改变
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                accelerometerValues = sensorEvent.values.clone();
                float[] values = sensorEvent.values;//获取传感器的数据
                //float max = sensor.getMaximumRange();//获取最大值范围
                if (values.length >= 3) {
                    float x = values[0];//右侧面向上时为g(9.8)
                    float y = values[1];//上侧面向上时为g(9.8)
                    float z = values[2];

                    //抖动
                    float xx = values[0] - usedValues[0];
                    float yy = values[1] - usedValues[1];
                    float zz = values[2] - usedValues[2];
                    float Tense = Math.abs(xx)+Math.abs(yy)+Math.abs(zz);

                    //System.out.println(values[2]+"  " +usedValues[2]+"                "+Tense);
                    usedValues[0] = x;
                    usedValues[1] = y;
                    usedValues[2] = z;
                    if(Tense >=15)
                        tempTense = 1;
                    else if(Tense >=6&&Tense < 15)
                        tempTense = 2;
                    else if(Tense>=4&&Tense < 6)
                        tempTense = 6;
                    else if(Tense >=2&&Tense<4)
                        tempTense = 7;
                    else if(Tense>=1&&Tense<2)
                        tempTense = 8;
                    else
                        tempTense = 10;
                    //倾斜
                    float angle;
                    if(y>0)
                    {
                        angle = x*8;
                        if(angle>25)
                            angle = 25;
                    }
                    else
                        angle = 0;
                    //System.out.println(angle);
                    myKeyboard.getSkewData(angle);
                    //抖动
                    //float Tense = Math.abs(x)+Math.abs(y)+Math.abs(z);
                }
                //System.out.println(accelerometerValues[0]+" "+accelerometerValues[1]+" "+accelerometerValues[2]);
                myKeyboard.getSensorData(tempTense);
            }else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {// 注意赋值时要调用clone()方法
                magneticValues = sensorEvent.values.clone();
                //System.out.println(magneticValues[0]+" "+magneticValues[1]+" "+magneticValues[2]);
            }
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            SensorManager.getOrientation(R, values);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    /**
     * 获得触键时间
     * */
    class touchKeyListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //timer = new Chronometer(MainActivity.this);
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Calendar calendar = Calendar.getInstance();
                    int YY = calendar.get(Calendar.YEAR);
                    int MM = calendar.get(Calendar.MONTH)+1;
                    int DD = calendar.get(Calendar.DATE);
                    int HH = calendar.get(Calendar.HOUR_OF_DAY);
                    int mm = calendar.get(Calendar.MINUTE);
                    int SS = calendar.get(Calendar.SECOND);
                    int MI = calendar.get(Calendar.MILLISECOND);
                    String string = YY+" "+MM+" "+DD+" "+HH+" "+mm+" "+SS+" "+MI+"";

                    second1 = SS;
                    millisecond1 = MI;
                    //timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                    //timer.start();
                    System.out.println("down");
                    System.out.println(string);
                    break;
                case MotionEvent.ACTION_UP:
                    //timer.stop();
                    Calendar c = Calendar.getInstance();
                    int Y = c.get(Calendar.YEAR);
                    int M = c.get(Calendar.MONTH)+1;
                    int D = c.get(Calendar.DATE);
                    int H = c.get(Calendar.HOUR_OF_DAY);
                    int m = c.get(Calendar.MINUTE);
                    int S = c.get(Calendar.SECOND);
                    int mi = c.get(Calendar.MILLISECOND);
                    String s = Y+" "+M+" "+D+" "+H+" "+m+" "+S+" "+mi+"";
                    second2 = S;
                    millisecond2 = mi;
                    totalMilliSecond = (second2-second1)*1000+millisecond2-millisecond1;
                    int thick;
                    if(totalMilliSecond<300)
                        thick = 2;
                    else if(totalMilliSecond>=300&&totalMilliSecond<600)
                        thick = 5;
                    else if(totalMilliSecond>=600&&totalMilliSecond<800)
                        thick = 6;
                    else if(totalMilliSecond>=800&&totalMilliSecond<1000)
                        thick = 7;
                    else  thick = 8;
                    myKeyboard.getTouchTime(thick);
                    System.out.println("up");
                    System.out.println(totalMilliSecond+"~6666666666666666666666666666666666666666666666");
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                default:
                    break;
            }
            return false;
        }
    }



    //左右拼接
    private Bitmap newBitmap(Bitmap bit1,Bitmap bit2){

        int width = bit1.getWidth()+bit2.getWidth();
        int height = bit1.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, bit1.getWidth(), 0, null);
        return bitmap;
    }
    //上下拼接
    private Bitmap newBitmap(Bitmap bit1,Bitmap bit2,int length){

        int width = length;
        int height = bit1.getHeight()+bit2.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        return bitmap;
    }
    //用于bitmap赋值
    private Bitmap newBitmap(Bitmap bit1){

        int width = bit1.getWidth();
        int height = bit1.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        return bitmap;
    }

    //按比例缩放图片，将拼接好的要发的图片缩小
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
    //用于把要发的大图改蓝
    protected Bitmap changeBackground(Bitmap bmp) {//二维数组的行数和列数
        int width=bmp.getWidth();
        int height=bmp.getHeight();
        Bitmap bmp1=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                int p = bmp.getPixel(i, j);//p是-1是白色，p是-16777216是黑色
                int a = Color.alpha(p);
                if(a!=255){
                    bmp1.setPixel(i,j,getResources().getColor(R.color.msg_back_color));
                }else{
                    bmp1.setPixel(i,j,bmp.getPixel(i,j));
                }
            }
        }
        return bmp1;
    }

    private SpannableString getBitmapMime(Bitmap bmp)
    {
        SpannableString ss=new SpannableString(" ");
        Drawable drawable = new BitmapDrawable(bmp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(imageSpan,0," ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    private void insertPhotoToEditText(SpannableString ss)
    {
        Editable et=mEtInput.getText();
        int start=mEtInput.getSelectionStart();
        et.insert(start,ss);

        mEtInput.setText(et);
        mEtInput.setSelection(start+ss.length());
        mEtInput.setFocusableInTouchMode(true);
        mEtInput.setFocusable(true);

    }

    //语音识别相关***********************************************************************************************

    protected Bitmap paintNewImage(int[][] arr,float tense,int level,float angle) {//抖动程度、画笔粗细、倾斜角度、num1，num2为distort异形文字的x，y偏移

        int row = arr.length;
        int col = arr[0].length;
        int min=0;
        int max=10;
        Random random = new Random();
        int num1 = random.nextInt(max)%(max-min+1) -5;
        int num2 = random.nextInt(max)%(max-min+1) -5;
        int[][] arr1 = distort1(arr,num1,num2,row,col);//异形文字
        arr = trembleImage(arr1,tense);//抖动
        Paint paint = new Paint();
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
            // System.out.println("                          66666666666666666666666666666////////////////////////////////");
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

        Bitmap showBitmap = Bitmap.createBitmap(right - left +11,
                MY_ROW, Bitmap.Config.ARGB_8888);
        Canvas show_canvas = new Canvas(showBitmap);
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
        Paint paint = new Paint();
        Bitmap showBitmap = Bitmap.createBitmap(MY_COL,
                MY_ROW, Bitmap.Config.ARGB_8888);
        Canvas show_canvas = new Canvas(showBitmap);
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
        //paint.setStrokeWidth(2f);
        return (new_arr_2);
    }
    private SpannableString getBitmapMime1(Bitmap bmp) {
        SpannableString ss=new SpannableString(" ");
        Drawable drawable = new BitmapDrawable(bmp);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(imageSpan,0," ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    //语音识别相关********************************************************************************************
    //讯飞语音识别相关
    private void checkSoIsInstallSucceed() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
    }
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
    public void setParam() {
        //参数设置
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.LANGUAGE,"en_us");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            //初始化监听器
            //LogUtil.L("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    private void setOnclickListener() {
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSoIsInstallSucceed();
                //mEtInput.setText("");
                //mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getResources().getString(R.string.pref_key_iat_show), true);
                if(isShowDialog){
                    ret=mIat.startListening(mRecognizerListener);
                    if(ret!= ErrorCode.SUCCESS)
                    {
                        showTip("听写失败，错误码："+ret);
                    }else{
                        showTip("请开始说话");
                    }
                }
            }
        });
    }
    //听写监听器。用他给的ui不会调这个，会用上面的监听器
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            //Log.L(results.getResultString());
            System.out.println("间断结果——————————————————————————————————");
            printResult(results);
            if(isLast){
                //因为是last，所以new_piece稳定到了最终结果，不再变化。
                result_string+=new_piece;
                //针对不同的文本显示方式，系统字体，手写个性化字体
                String tag=mChangeModeLayout.getTag().toString();
                switch (tag){
                    case "1":
                        //系统字体
                        mEtInput.setText(result_string);
                        break;
                    case "2":
                        //手写个性化字体
                        //音量
                        sound=sound/soundCount;
                        showTip("平均音量"+sound);
                        if(sound<=18){
                            sound=2;
                        }else{
                            sound=sound/3;
                        }

                        for(int i=0;i<new_piece.length();i++){
                            int start=mEtInput.getSelectionStart();
                            char singleLetter=new_piece.charAt(i);
                            int pos=singleLetter-33;
                            Bitmap bmp;
                            if(pos==-1){
                                //空格键
                                bmp = Bitmap.createBitmap(MY_COL/2,MY_ROW, Bitmap.Config.ARGB_8888);//空白图片
                            }else if(myImageArrays[pos]!=null){
                                bmp = paintNewImage(myImageArrays[pos],10,sound,0);

                            }else{
                                bmp = Bitmap.createBitmap(MY_COL/2,MY_ROW, Bitmap.Config.ARGB_8888);//空白图片
                                showTip("个人字体库不完整！");
                            }
                            SpannableString ss=getBitmapMime1(bmp);
                            insertPhotoToEditText(ss);
                            count++;
                            inputBmpList.add(start,bmp);
                        }
                        sound=0;
                        soundCount=0;
                        break;
                    default:
                        break;
                }

                System.out.println("最后的结果**********************************************************");
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            //LogUtil.L("返回音频数据："+data.length);
            System.out.println(data+"jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjqqqqqqqqqqqqqqqqqqqqqqq");
            if(volume>5){
                sound+=volume;
                soundCount+=1;
            }

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        new_piece=resultBuffer.toString();
    }

}
