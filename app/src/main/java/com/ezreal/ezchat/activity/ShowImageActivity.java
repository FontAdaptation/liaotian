package com.ezreal.ezchat.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.ezreal.ezchat.R;
import com.ezreal.ezchat.utils.ConvertUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.ezreal.ezchat.commonlibrary.utils.TextUtils;
import com.ezreal.ezchat.commonlibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李晨晨
 */

public class ShowImageActivity extends BaseActivity {

    private static final String TAG = ShowImageActivity.class.getSimpleName();

    @BindView(R.id.image_view)
    PhotoView mIvImage;

    private IMMessage mMessage;
    private Bitmap bitmap;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);
        initImage();

    }


    private void initImage() {
        mIvImage.setMaxScale(4);
        mIvImage.enable();

        mMessage = (IMMessage) getIntent().getSerializableExtra("IMMessage");

        if (mMessage == null) {
            ToastUtils.showMessage(this, "图片无法显示，请重试~");
            finish();
            return;
        }

        String path = ((ImageAttachment) mMessage.getAttachment()).getPath();

        // 原图已经下载，显示原图
        if (mMessage.getAttachStatus() == AttachStatusEnum.transferred
                && !TextUtils.isEmpty(path)) {
            Bitmap origin_bitmap = BitmapFactory.decodeFile(path);
            int height = (int) (origin_bitmap.getHeight() * (512.0 / origin_bitmap.getWidth()));
            bitmap = Bitmap.createScaledBitmap(origin_bitmap, 512, height, true);
            if (bitmap != null) {
                mIvImage.setImageBitmap(bitmap);
            } else {
                ToastUtils.showMessage(ShowImageActivity.this, "原图 下载/显示 失败，请重试~");
                finish();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();//防止图片过大造成java.lang.OutOfMemoryError
        super.onDestroy();
    }
}





