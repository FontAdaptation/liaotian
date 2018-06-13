package com.ezreal.ezchat.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ezreal.ezchat.R;
import com.ezreal.ezchat.activity.AccountInfoActivity;
import com.ezreal.ezchat.activity.ContentActivity;
import com.ezreal.ezchat.activity.CreateActivity;
import com.ezreal.ezchat.bean.LocalAccountBean;
import com.ezreal.ezchat.handler.NimUserHandler;
import com.ezreal.ezchat.handler.NimUserHandler.OnInfoUpdateListener;
import com.joooonho.SelectableRoundedImageView;
import com.ezreal.ezchat.commonlibrary.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 张静
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.iv_head_picture)
    SelectableRoundedImageView mHeadView;
    @BindView(R.id.tv_user_name)
    TextView mTvName;
    @BindView(R.id.tv_account)
    TextView mTvAccount;
    private LocalAccountBean mAccountBean;

    @Override
    public int setLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView(View rootView) {
        ButterKnife.bind(this,rootView);
        NimUserHandler.getInstance().setUpdateListeners(new OnInfoUpdateListener() {
            @Override
            public void myInfoUpdate() {
                showOrRefreshView();
            }
        });
        showOrRefreshView();
    }


    @OnClick(R.id.layout_account)
    public void openAccountInfo(){
        Intent intent = new Intent(getContext(), AccountInfoActivity.class);
        startActivity(intent);
    }


    //进入字体库
    @OnClick(R.id.layout_user_words_input)
    public void openUserWordsAlbum(){
        Intent intent=new Intent(getContext(), ContentActivity.class);
        startActivity(intent);
    }

    private void showOrRefreshView(){
        mAccountBean = NimUserHandler.getInstance().getLocalAccount();
        if (mAccountBean != null){
            ImageUtils.setImageByUrl(getContext(),mHeadView,
                    mAccountBean.getHeadImgUrl(),R.mipmap.app_logo_main);
            mTvName.setText(mAccountBean.getNick());
            mTvAccount.setText(mAccountBean.getAccount());
        }
    }
}
