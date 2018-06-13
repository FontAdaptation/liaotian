package com.ezreal.ezchat.emojilibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.ezreal.ezchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情包控件
 * Created by 仝心
 */

public class ExpressLayout extends LinearLayout {

    private ViewPager mViewPager;
    private List<View> mExpressViews;
    private EmojiLayout mEmojiLayout;
    private OnExpressSelListener mOnExpressSelListener;

    public ExpressLayout(Context context) {
        this(context,null);
    }

    public ExpressLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExpressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_express,
                this, true);
        mViewPager = (ViewPager) rootView.findViewById(R.id.view_page);
        init();
    }

    private void init(){
        mExpressViews = new ArrayList<>();
        mEmojiLayout = new EmojiLayout(getContext());
        mEmojiLayout.setSelectListener(new EmojiLayout.OnEmojiSelectListener() {
            @Override
            public void emojiSelect(EmojiBean emojiBean) {
                if (mOnExpressSelListener != null){
                    mOnExpressSelListener.onEmojiSelect(emojiBean);
                }
            }

            @Override
            public void emojiDelete() {
                if (mOnExpressSelListener != null){
                    mOnExpressSelListener.onEmojiDelete();
                }
            }
        });

        mExpressViews.add(mEmojiLayout);
        mViewPager.setAdapter(new ViewPageAdapter(mExpressViews));
    }

    public void setOnExpressSelListener(OnExpressSelListener listener){
        this.mOnExpressSelListener = listener;
    }

    public interface OnExpressSelListener{
        void onEmojiSelect(EmojiBean emojiBean);
        void onEmojiDelete();
    }
}
