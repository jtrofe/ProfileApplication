package com.example.main.profileapplication.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.main.profileapplication.R;

public class parallaxScrollView extends ScrollView{

    private RelativeLayout mHeader;
    private Context mContext;


    public parallaxScrollView(Context context, AttributeSet attrs){
        super(context, attrs);

        mContext = context;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy){
        super.onScrollChanged(x, y, oldx, oldy);

        if(mHeader == null){
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

            mHeader = (RelativeLayout) layout.findViewById(R.id.header);
        }


        float percent = (mHeader.getHeight() - y)/ ((float) mHeader.getHeight());
        percent = Math.max(0, percent);
        percent = Math.min(1, percent);
        percent = (percent * 0.75f) + 0.25f;

        mHeader.setAlpha(percent);
        mHeader.setTranslationY((y/2f));


        LinearLayout content = (LinearLayout) findViewById(R.id.content);

        int height = mContext.getResources().getDisplayMetrics().heightPixels;

        content.setMinimumHeight(height);
    }
}