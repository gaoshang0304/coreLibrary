package com.daydream.corelibrary.app.dialog;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.daydream.corelibrary.R;


/**
 * 通用两个按钮dialog
 * 默认不显示title
 * 如果使用setTitle方法，则显示title
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-05-05
 */

public class ConfirmDialog extends AbsDialog {


    private TextView tv_content;
    private TextView tv_btnLeft;
    private TextView tv_btnRight;
    private OnClickButtonListener mOnClickButtonListener;
    private TextView tv_title;

    public ConfirmDialog(Context context) {
        super(context);
        initView();
    }

    protected ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_confirm);

        tv_title = (TextView) findViewById(R.id.tv_dialog_title);
        tv_content = (TextView) findViewById(R.id.tv_dialog_content);
        tv_btnLeft = (TextView) findViewById(R.id.tv_dialog_left_button);
        tv_btnRight = (TextView) findViewById(R.id.tv_dialog_right_button);
        addListener();
    }

    private void addListener() {
        tv_btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mOnClickButtonListener) {
                    dismiss();
                    mOnClickButtonListener.onClickButtonLeft();
                }
            }
        });
        tv_btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mOnClickButtonListener) {
                    dismiss();
                    mOnClickButtonListener.onClickButtonRight();
                }
            }
        });
    }

    public ConfirmDialog setTitle(String title) {
        tv_title.setText(title);
        tv_title.setVisibility(View.VISIBLE);
        return this;
    }

    public void setTitle(int resId) {
        tv_title.setText(resId);
        tv_title.setVisibility(View.VISIBLE);
    }

    public ConfirmDialog setContent(String content) {
        tv_content.setText(content);
        return this;
    }

    public ConfirmDialog setContent(SpannableString content) {
        tv_content.setText("");
        tv_content.append(content);
        return this;
    }

    public ConfirmDialog setContent(int resId) {
        tv_content.setText(resId);
        return this;
    }

    public ConfirmDialog setContentVisible(boolean visible) {
        tv_content.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ConfirmDialog setButtonText(String left, String right) {
        tv_btnLeft.setText(left);
        tv_btnRight.setText(right);
        return this;
    }

    public ConfirmDialog setButtonTextColor(int left, int right) {
        tv_btnLeft.setTextColor(left);
        tv_btnRight.setTextColor(right);
        return this;
    }

    public ConfirmDialog setButtonText(int left, int right) {
        tv_btnLeft.setText(left);
        tv_btnRight.setText(right);
        return this;
    }

    public ConfirmDialog setOnClickButtonListener(OnClickButtonListener listener) {
        mOnClickButtonListener = listener;
        return this;
    }

    public interface OnClickButtonListener {
        void onClickButtonLeft();
        void onClickButtonRight();
    }
}
