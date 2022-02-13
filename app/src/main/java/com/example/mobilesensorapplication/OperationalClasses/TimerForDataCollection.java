package com.example.mobilesensorapplication.OperationalClasses;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.example.mobilesensorapplication.R;

public class TimerForDataCollection {

    Context mcontext;
    View mview;
    TextView timer;


    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Hours, Seconds, Minutes, MilliSeconds;

    public static String stopWatch = "00:00:00:000";
    int handlerDelay = 1; // In mili seconds

    public TimerForDataCollection(Context mcontext, View mview) {
        this.mcontext = mcontext;
        this.mview = mview;
    }

    public void startTimer() {
        stopWatch = "00:00:00:000";
        setupViews();
        StartTime = SystemClock.uptimeMillis();
        handler = new Handler();
        handler.postDelayed(runnableTimer, handlerDelay);
    }

    public void pauseTimer() {
        TimeBuff += MillisecondTime;
        if(handler!=null)
        handler.removeCallbacks(runnableTimer);
    }


    public void resetTimer() {
        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;
        Hours = 0;
        timer.setText("00:00:00:000");
        if (handler != null)
            handler.removeCallbacks(runnableTimer);
    }

    private void setupViews() {
        timer = (TextView) mview.findViewById(R.id.timer);
    }

    private Runnable runnableTimer = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime; // Deal with Pause Time

            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Seconds = Seconds % 60;
            Minutes = Minutes % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            stopWatch = "" + String.format("%02d", Hours) + ":" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":" + String.format("%03d", MilliSeconds);
            timer.setText(stopWatch);

            //  checkValidHRDate();
            handler.postDelayed(runnableTimer, handlerDelay);
        }


    };


}
