package com.ezreal.ezchat.emojilibrary;

import java.io.Serializable;

/**
 * Created by 仝心
 */

public class EmojiBean implements Serializable {

    private int mResIndex;
    private String mEmojiName;

    public int getResIndex() {
        return mResIndex;
    }

    public void setResIndex(int resIndex) {
        mResIndex = resIndex;
    }

    public String getEmojiName() {
        return mEmojiName;
    }

    public void setEmojiName(String emojiName) {
        mEmojiName = emojiName;
    }
}
