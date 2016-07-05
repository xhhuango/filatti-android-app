package com.fotro.activities.effect;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fotro.utils.ThreadPool;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

class EffectItemListAdapter extends BaseAdapter {
    private List<EffectItem> mEffectItemList = new ArrayList<>();

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
    public Object getItem(int i) {
        return mEffectItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        return null;
    }
}
