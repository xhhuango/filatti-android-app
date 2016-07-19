package com.filatti.activities.effect;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.filatti.activities.effect.items.EffectItem;
import com.filatti.utils.ThreadPool;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class EffectItemListAdapter extends BaseAdapter {
    private static final int PADDING = 1;

    private List<EffectItem> mEffectItemList = new ArrayList<>();

    private final EffectPresenter mPresenter;
    private final int mItemWidth;

    EffectItemListAdapter(EffectPresenter presenter, int itemViewWidth) {
        Preconditions.checkNotNull(presenter);
        Preconditions.checkArgument(itemViewWidth > 0);

        mPresenter = presenter;
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
            effectItemView = new EffectItemView(mPresenter.getActivity());
            effectItemView.setLayoutParams(new GridView.LayoutParams(mItemWidth, mItemWidth));
            effectItemView.setPadding(PADDING, PADDING, PADDING, PADDING);
        } else {
            effectItemView = (EffectItemView) convertView;
            effectItemView.setName(0);
            effectItemView.setIcon(0);
        }

        final EffectItem effectItem = mEffectItemList.get(position);
        effectItemView.setName(effectItem.getDisplayName());
        effectItemView.setIcon(effectItem.getIcon());
        effectItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSelectEffectItem(effectItem);
            }
        });

        return effectItemView;
    }
}
