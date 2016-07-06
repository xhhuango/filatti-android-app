package com.fotro.activities.effect;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.fotro.utils.ThreadPool;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class EffectItemListAdapter extends BaseAdapter {
    private static final int PADDING = 1;

    private List<EffectItem> mEffectItemList = new ArrayList<>();

    private final EffectActivity mActivity;
    private final int mItemWidth;

    EffectItemListAdapter(EffectActivity activity, int itemViewWidth) {
        Preconditions.checkNotNull(activity);
        Preconditions.checkArgument(itemViewWidth > 0);

        mActivity = activity;
        mItemWidth = itemViewWidth - (PADDING * 2);
    }

    void setEffectItemList(List<EffectItem> effectItemList) {
        Preconditions.checkNotNull(effectItemList);

        mEffectItemList = effectItemList;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return mEffectItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEffectItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        EffectItemView effectItemView;

        if (convertView == null) {
            effectItemView = new EffectItemView(mActivity);
            effectItemView.setLayoutParams(new GridView.LayoutParams(mItemWidth, mItemWidth));
            effectItemView.setPadding(PADDING, PADDING, PADDING, PADDING);
        } else {
            effectItemView = (EffectItemView) convertView;
            effectItemView.setName(0);
            effectItemView.setIcon(0);
        }

        EffectItem effectItem = mEffectItemList.get(position);
        effectItemView.setName(effectItem.getDisplayName());
        effectItemView.setIcon(effectItem.getIcon());

        return effectItemView;
    }
}
