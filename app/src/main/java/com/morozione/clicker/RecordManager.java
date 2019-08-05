package com.morozione.clicker;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by morozione on 07/19/17.
 */

public class RecordManager {

    public static void textForRecordDialog(int record, int newResult, TextView tvMessage, TextView tvResult) {
        if (newResult > record) {
            tvMessage.setText("New Record!!!");
            tvMessage.setTextColor(Color.parseColor("#FF3AA63A"));
            tvResult.setTextColor(Color.parseColor("#FF3AA63A"));
        } else {
            if (newResult > (record / 4 * 3)) {
                tvMessage.setText("Not Bad");
                tvMessage.setTextColor(Color.parseColor("#FF989608"));
                tvResult.setTextColor(Color.parseColor("#FF989608"));
            } else if (newResult > (record / 4 * 2)) {
                tvMessage.setText("So-So");
                tvMessage.setTextColor(Color.parseColor("#FF989608"));
                tvResult.setTextColor(Color.parseColor("#FF989608"));
            } else if (newResult > (record / 4)) {
                tvMessage.setText("Bad");
                tvMessage.setTextColor(Color.parseColor("#FFB01111"));
                tvResult.setTextColor(Color.parseColor("#FFB01111"));
            } else {
                tvMessage.setText("You Loser!!");
                tvMessage.setTextColor(Color.parseColor("#FFB01111"));
                tvResult.setTextColor(Color.parseColor("#FFB01111"));
            }
        }
        tvResult.setText(newResult + "");
    }

}
