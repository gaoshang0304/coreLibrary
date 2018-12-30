package com.daydream.corelibrary.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daydream.corelibrary.R;


/**
 * 只有一个确定按钮
 * 默认Title不显示
 * 如果使用setTitle，则显示；
 *
 * @author daydream
 */
public class RemindDialog extends AbsDialog implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_button;

    public RemindDialog(Context context) {
        super(context);
    }

    protected RemindDialog(Context context,
                           boolean cancelable,
                           OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remind);

        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_content = (TextView) findViewById(R.id.tv_dialog_content);
        tv_button = (TextView) findViewById(R.id.tv_dialog_button);

        tv_button.setOnClickListener(RemindDialog.this);
    }


    public RemindDialog setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
            tv_title.setVisibility(View.VISIBLE);
        }
        return RemindDialog.this;
    }

    public void setTitle(int resId) {
        if (tv_title != null) {
            tv_title.setText(resId);
            tv_title.setVisibility(View.VISIBLE);
        }
    }

    public RemindDialog setContent(String message) {
        if (tv_content != null) {
            tv_content.setText(message);
        }
        return RemindDialog.this;
    }

    public RemindDialog setContent(int message) {
        if (tv_content != null) {
            tv_content.setText(message);
        }
        return RemindDialog.this;
    }

    public RemindDialog setConfirmText(String text) {
        if (tv_button != null) {
            tv_button.setText(text);
        }
        return RemindDialog.this;
    }

    public RemindDialog setConfirmText(int resId) {
        if (tv_button != null) {
            tv_button.setText(resId);
        }
        return RemindDialog.this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dialog_button) {
            if (mOnButtonClickListener != null) {
                mOnButtonClickListener.onButtonClick(RemindDialog.this, v);
            }
            dismiss();
        }
    }

    private OnButtonClickListener mOnButtonClickListener;

    public RemindDialog setOnButtonClickListener(OnButtonClickListener listener) {
        mOnButtonClickListener = listener;
        return RemindDialog.this;
    }

    public interface OnButtonClickListener {
        void onButtonClick(RemindDialog dialog, View view);
    }
}
