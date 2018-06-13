package com.ezreal.ezchat.chat;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by 李晨晨
 */

public interface OnItemClickListener {
    void onItemClick(RViewHolder holder, IMMessage message);
}
