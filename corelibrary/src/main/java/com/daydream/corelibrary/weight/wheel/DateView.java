package com.daydream.corelibrary.weight.wheel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.weight.wheel.widget.OnWheelChangedListener;
import com.daydream.corelibrary.weight.wheel.widget.OnWheelScrollListener;
import com.daydream.corelibrary.weight.wheel.widget.WheelView;
import com.daydream.corelibrary.weight.wheel.widget.adapters.ArrayWheelAdapter;
import com.daydream.corelibrary.weight.wheel.widget.adapters.NumericWheelAdapter;

import java.util.Calendar;

/**
 * 日期选择View
 *
 * @author wangheng
 */
public class DateView extends LinearLayout implements OnWheelScrollListener {

    private WheelView mYearView, mMonthView, mDayView;
    private int mYear,mMonth,mDay;
    private OnDateChangedListener mOnDateChangedListener;

    public DateView(Context context) {
        super(context);
        init(context,null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        setOrientation(HORIZONTAL);

        Calendar calendar = Calendar.getInstance();

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(mYearView, mMonthView, mDayView);
            }
        };

        mYearView = new WheelView(context);
        LayoutParams yearParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        yearParams.weight = 1;
        mYearView.setLayoutParams(yearParams);

        mMonthView = new WheelView(context);
        LayoutParams monthParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        monthParams.weight = 1;
        mMonthView.setLayoutParams(monthParams);

        mDayView = new WheelView(context);
        LayoutParams dayParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        dayParams.weight = 1;
        mDayView.setLayoutParams(dayParams);

        // mMonthView
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        mMonthView.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
        mMonthView.setCurrentItem(0);
        mMonthView.addChangingListener(listener);


        // mYearView
        int curYear = calendar.get(Calendar.YEAR);
        String yearPostfix = context.getString(R.string.dateview_year);
        mYearView.setViewAdapter(new DateNumericAdapter(context, curYear - 100, curYear, 100,yearPostfix));
        int current = 100 - (curYear - 1980);
        if(current < 0){
            mYearView.setCurrentItem(100);
        }else {
            mYearView.setCurrentItem(current);
        }
        mYearView.addChangingListener(listener);

        //mDayView
        updateDays(mYearView, mMonthView, mDayView);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        mDayView.setCurrentItem(0);

        mYearView.addScrollingListener(this);
        mMonthView.addScrollingListener(this);
        mDayView.addScrollingListener(this);


        mYearView.setWheelBackground(R.drawable.transparent);
        mMonthView.setWheelBackground(R.drawable.transparent);
        mDayView.setWheelBackground(R.drawable.transparent);

        mYearView.setWheelForeground(R.drawable.date_view_foreground);
        mMonthView.setWheelForeground(R.drawable.date_view_foreground);
        mDayView.setWheelForeground(R.drawable.date_view_foreground);

        int startColor = Color.parseColor("#AFFFFFFF");
        int endColor = Color.parseColor("#3FFFFFFF");

        mYearView.setShadowColor(startColor,startColor,endColor);
        mMonthView.setShadowColor(startColor,startColor,endColor);
        mDayView.setShadowColor(startColor,startColor,endColor);

        addView(mYearView);
        addView(mMonthView);
        addView(mDayView);

        receiveYearMonthDay();
    }


    /**
     * Updates mDayView wheel. Sets max days according to selected mMonthView and mYearView
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dayPostfix = getContext().getString(R.string.dateview_day);
        day.setViewAdapter(new DateNumericAdapter(getContext(),
                1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1,dayPostfix));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }


    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        receiveYearMonthDay();

    }

    private void receiveYearMonthDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + (mYearView.getCurrentItem() - 100));
        calendar.set(Calendar.MONTH, mMonthView.getCurrentItem());
        calendar.set(Calendar.DAY_OF_MONTH, mDayView.getCurrentItem() + 1);

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(mOnDateChangedListener != null){
            mOnDateChangedListener.onDateChanged(mYear,mMonth,mDay);
        }
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        String postfix;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current,String postfix) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(18);
            this.postfix = postfix;
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
//            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
//            }else{
//                view.setTextColor(Color.parseColor("#FF336699"));
//            }
            view.setTextColor(Color.BLACK);
            view.setText(view.getText().toString() + postfix);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            View view = super.getItem(index, cachedView, parent);
            view.setPadding(view.getPaddingLeft(),5,view.getPaddingRight(),5);
            return view;
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        String postfix;
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(18);
            postfix = context.getString(R.string.dateview_month);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
//            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
//            }else{
//                view.setTextColor(Color.RED);
//            }

            view.setTextColor(Color.BLACK);
            view.setText(view.getText().toString() + postfix);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            View view = super.getItem(index, cachedView, parent);
            view.setPadding(view.getPaddingLeft(),5,view.getPaddingRight(),5);
            return view;
        }
    }



    public void setOnDateChangedListener(OnDateChangedListener listener){
        this.mOnDateChangedListener = listener;
    }


    public interface OnDateChangedListener{
        void onDateChanged(int year, int month, int day);
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }
}
