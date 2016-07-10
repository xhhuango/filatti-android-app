package com.fotro.activities.effect;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fotro.R;

public class EffectItemView extends LinearLayout {
    private LinearLayout mRoot;
    private TextView mNameTextView;
    private ImageView mIconImageView;

    @StringRes
    private int mName = 0;

    @DrawableRes
    private int mIcon = 0;

    public EffectItemView(Context context) {
        super(context);
        initViews(context);
    }

    public EffectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public EffectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = (LinearLayout) inflater.inflate(R.layout.view_effect_item, this, true);
        mNameTextView = (TextView) mRoot.findViewById(R.id.effectItemName);
        mIconImageView = (ImageView) mRoot.findViewById(R.id.effectItemIcon);

        setName(mName);
        setIcon(mIcon);
    }

    void setName(@StringRes int name) {
        mName = name;
        if (mNameTextView != null) {
            if (name == 0) {
                mNameTextView.setText(null);
            } else {
                mNameTextView.setText(name);
            }
        }
    }

    void setIcon(@DrawableRes int icon) {
        mIcon = icon;
        if (mIconImageView != null) {
            if (icon == 0) {
                mIconImageView.setImageBitmap(null);
            } else {
                mIconImageView.setImageResource(icon);
            }
        }
    }
}
