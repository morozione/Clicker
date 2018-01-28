package com.example.i1.clicker;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by morozione on 07/19/17.
 */

public class Saver {

    public static final String KEY_RECORD = "record";
    public static final String KEY_COINS = "coins";
    public static final String KEY_COUNT_MINUTES = "count_minutes";

    private SharedPreferences dataRecord;
    private static Saver saver;

    private Saver(Context context) {
        dataRecord = context.getSharedPreferences(KEY_RECORD, MODE_PRIVATE);
    }

    public static Saver getInstance(Context context) {
        if (saver == null)
            saver = new Saver(context);
        return saver;
    }

    public void saveInt(int record, String key) {
        SharedPreferences.Editor edit = dataRecord.edit();
        edit.putInt(key, record);
        edit.commit();
    }

    public int loadInt(String key) {
        int record = dataRecord.getInt(key, 0);
        return record;
    }
}
