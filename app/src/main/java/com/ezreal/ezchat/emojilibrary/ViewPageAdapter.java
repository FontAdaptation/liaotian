package com.ezreal.ezchat.emojilibrary;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 仝心
 */

public class ViewPageAdapter extends PagerAdapter {

    private List<View> mViews;

    public ViewPageAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews != null ? mViews.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
