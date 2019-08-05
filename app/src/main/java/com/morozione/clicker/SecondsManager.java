package com.morozione.clicker;

import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

public class SecondsManager extends AsyncTask<Void, Integer, Integer> {
    private int seconds = 5;

    private OnSecondsListener listener;

    public interface OnSecondsListener {
        void countSecond(int seconds);
    }

    public SecondsManager(Context context) {
        listener = (OnSecondsListener) context;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        while (seconds >= 0) {
            publishProgress(seconds--);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isCancelled())
                return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        listener.countSecond(values[0]);
    }

    public void upperSecondsOnNewLevel() {
        seconds += 5;
        publishProgress(seconds);
    }
}
