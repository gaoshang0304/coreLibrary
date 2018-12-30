package com.daydream.corelibrary.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * FlowLayout
 *
 * @author wangheng
 */
public class AdapterFlowLayout extends FlowLayout {

    private OnItemClickListener mOnItemClickListener;

    public AdapterFlowLayout(Context context) {
        super(context);
    }

    public AdapterFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setAdapter(Adapter adapter){
        if(adapter == null || adapter.getCount() == 0){
            removeAllViews();
            return;
        }
        removeAllViews();

        int count = adapter.getCount();
        for(int i = 0;i < count;i++){
            final View view = adapter.getView(AdapterFlowLayout.this,i);
            if(view != null) {
                final int position = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mOnItemClickListener != null){
                            mOnItemClickListener.onItemClick(AdapterFlowLayout.this,view,position);
                        }
                    }
                });
                addView(view);
            }
        }
    }

    public interface Adapter{
        int getCount();
        View getView(AdapterFlowLayout parent, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(AdapterFlowLayout parent, View view, int position);
    }
}
