package com.daydream.corelibrary.weight.wheel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daydream.corelibrary.R;
import com.daydream.corelibrary.app.App;
import com.daydream.corelibrary.utils.Logger;
import com.daydream.corelibrary.weight.wheel.widget.OnWheelScrollListener;
import com.daydream.corelibrary.weight.wheel.widget.WheelView;
import com.daydream.corelibrary.weight.wheel.widget.adapters.NumericWheelAdapter;

import java.util.Calendar;

/**
 * 日期选择View
 *
 * @author wangheng
 */
public class DateTimeView extends LinearLayout implements OnWheelScrollListener {

    private WheelView mDateView, mHourView, mMinutesView;
    private int mYear,mMonth,mDay;
    private OnDateChangedListener mOnDateChangedListener;

    public DateTimeView(Context context) {
        super(context);
        init(context,null);
    }

    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DateTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private DateAdapter mDateAdapter;

    private void init(Context context, AttributeSet attrs) {

        setOrientation(HORIZONTAL);


        mDateView = new WheelView(getContext());
        LayoutParams dateParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        dateParams.weight = 2;
        mDateView.setLayoutParams(dateParams);

        mHourView = new WheelView(getContext());
        LayoutParams hourParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        hourParams.weight = 1;
        mHourView.setLayoutParams(hourParams);

        mMinutesView = new WheelView(getContext());
        LayoutParams minutesParams = new LayoutParams(0,LayoutParams.WRAP_CONTENT);
        minutesParams.weight = 1;
        mMinutesView.setLayoutParams(minutesParams);

        addView(mDateView);
        addView(mHourView);
        addView(mMinutesView);
    }

    private int mDateOffset;

    public void setData(int dateOffset){

        mDateOffset = dateOffset;

        Calendar calendar = Calendar.getInstance();
        // mDateView
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        mDateAdapter = new DateAdapter(getContext(),mDateOffset, 0, 365, 0);
        mDateView.setViewAdapter(mDateAdapter);
        mDateView.setCurrentItem(0);
//        mDateView.addChangingListener(listener);

        // mHourView
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        mHourView.setViewAdapter(new NumberAdapter(getContext(),
                0, 23,
                0, App.getInstance().getString(R.string.dateview_hour)));

        mHourView.setCurrentItem(currentHour);
//        mHourView.addChangingListener(listener);

        //mMinutesView
        int currentMinute = calendar.get(Calendar.MINUTE);
        mMinutesView.setViewAdapter(new NumberAdapter(getContext(),
                0, 59,
                0,App.getInstance().getString(R.string.dateview_minutes)));

        mMinutesView.setCurrentItem(currentMinute);

        mDateView.addScrollingListener(this);
        mHourView.addScrollingListener(this);
        mMinutesView.addScrollingListener(this);


        mDateView.setWheelBackground(R.drawable.transparent);
        mHourView.setWheelBackground(R.drawable.transparent);
        mMinutesView.setWheelBackground(R.drawable.transparent);

        mDateView.setWheelForeground(R.drawable.date_view_foreground);
        mHourView.setWheelForeground(R.drawable.date_view_foreground);
        mMinutesView.setWheelForeground(R.drawable.date_view_foreground);

        int startColor = Color.parseColor("#AFFFFFFF");
        int endColor = Color.parseColor("#3FFFFFFF");

        mDateView.setShadowColor(startColor,startColor,endColor);
        mHourView.setShadowColor(startColor,startColor,endColor);
        mMinutesView.setShadowColor(startColor,startColor,endColor);



        receiveYearMonthDay();
    }



    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        receiveYearMonthDay();

    }

    public Calendar receiveYearMonthDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,mDateOffset);


        Logger.i("wangheng","day month current:" + mDateView.getCurrentItem()
                + ",hour:" + mHourView.getCurrentItem()
                + ",minute:" + mMinutesView.getCurrentItem());

        calendar.add(Calendar.DAY_OF_MONTH,mDateView.getCurrentItem());
        calendar.set(Calendar.HOUR_OF_DAY,mHourView.getCurrentItem());
        calendar.set(Calendar.MINUTE,mMinutesView.getCurrentItem());

        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,0);

        Logger.i("wangheng",calendar.getTimeInMillis() + " ## "
                + (calendar.get(Calendar.YEAR))
                + "-" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        return calendar;
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        String format;

        private Calendar mCalendar;

        public Calendar getCalendar(){
            return mCalendar;
        }

        /**
         * Constructor
         */
        public DateAdapter(Context context,int dateOffset, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(18);
            format = App.getInstance().getString(R.string.dateview_month_date);
            mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DAY_OF_MONTH,dateOffset);
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
//            view.setText(view.getText().toString() + postfix);
//            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            View view = super.getItem(index, cachedView, parent);
            view.setPadding(view.getPaddingLeft(),5,view.getPaddingRight(),5);
            return view;
        }

        @Override
        public CharSequence getItemText(int index) {
            mCalendar.add(Calendar.DAY_OF_MONTH,index);

            String text = String.format(format,
                    mCalendar.get(Calendar.MONTH) + 1,
                    mCalendar.get(Calendar.DAY_OF_MONTH));

            mCalendar.add(Calendar.DAY_OF_MONTH,-index);
            return text;
        }
    }

    private class NumberAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        private String postfix;

        private Calendar mCalendar;

        /**
         * Constructor
         */
        public NumberAdapter(Context context, int minValue, int maxValue, int current,String postfix) {
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
//            view.setText(view.getText().toString() + postfix);
//            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            View view = super.getItem(index, cachedView, parent);
            view.setPadding(view.getPaddingLeft(),5,view.getPaddingRight(),5);
            return view;
        }

        @Override
        public CharSequence getItemText(int index) {

            return (index + minValue) + postfix;
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
