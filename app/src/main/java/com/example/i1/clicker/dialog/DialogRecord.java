package com.example.i1.clicker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.i1.clicker.R;
import com.example.i1.clicker.RecordManager;
import com.example.i1.clicker.Saver;

/**
 * Created by morozione on 10/13/17.
 */

public class DialogRecord extends DialogFragment {
    public static final String KEY_RECORD = "key_record";
    public static final String KEY_NEW_RECORD = "key_new_record";

    int record;
    int newRecord;

    private OnNewRecordListener listener;
    private Saver saver;

    public interface OnNewRecordListener {
        void onNewRecord(int newRecord, int coins);
    }

    public DialogRecord() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnNewRecordListener) getActivity();

        Bundle args = getArguments();
        record = args.getInt(KEY_RECORD);
        newRecord = args.getInt(KEY_NEW_RECORD);
        saver = Saver.getInstance(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("mylog", "record: " + record + ", newRecord: " + newRecord);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        TextView tvMessage = (TextView) rootView.findViewById(R.id.tv_message);
        TextView tvResult = (TextView) rootView.findViewById(R.id.tv_result);
        TextView tvCoins = (TextView) rootView.findViewById(R.id.tv_coin);

        if (newRecord > record) {
            tvMessage.setText("New Record!!!");
        } else {
            if (newRecord > (record / 3 * 2)) {
                tvMessage.setText("Not Bad");
            } else if (newRecord > (record / 3)) {
                tvMessage.setText("Bad");
            } else {
                tvMessage.setText("You Loser!!!")
                ;
            }
        }

        final int coins = newRecord / 20;

        tvResult.setText(newRecord + "");
        tvCoins.setText("+" + coins);

        dialog.setTitle("Game Over");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newRecord > record)
                    record = newRecord;
                listener.onNewRecord(record, coins);
            }
        });
        dialog.setView(rootView);
        dialog.setCancelable(false);
        RecordManager.textForRecordDialog(record, newRecord, tvMessage, tvResult);
        Log.d("mylog", "record: " + record + ", newRecord: " + newRecord);
        return dialog.create();
    }
}
