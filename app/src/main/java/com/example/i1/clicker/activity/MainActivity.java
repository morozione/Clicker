package com.example.i1.clicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.i1.clicker.R;
import com.example.i1.clicker.Saver;
import com.example.i1.clicker.dialog.DialogRecord;

public class MainActivity extends AppCompatActivity implements DialogRecord.OnNewRecordListener {

    private int record;
    private int newRecord;
    private int coins;

    private TextView tvRecord, tvCoins;
    private Button bStart;

    private Intent intent;
    private Saver saver;
    private DialogRecord dialogRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        loadData();
        initView();
    }

    private void initView() {
        tvRecord = (TextView) findViewById(R.id.tv_record);
        tvCoins = (TextView) findViewById(R.id.tv_coins);
        bStart = (Button) findViewById(R.id.b_start);

        tvRecord.setText("Record: " + record);
        tvCoins.setText(coins + " ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                newRecord = data.getIntExtra(DialogRecord.KEY_NEW_RECORD, 0);
                dialogRecord = new DialogRecord();
                Bundle args = new Bundle();
                args.putInt(DialogRecord.KEY_NEW_RECORD, newRecord);
                args.putInt(DialogRecord.KEY_RECORD, record);
                dialogRecord.setArguments(args);
                dialogRecord.setCancelable(false);
                dialogRecord.show(getFragmentManager(), "dialog_fragment");
            }
        } else if (resultCode == RESULT_CANCELED) {
            updateView();
        }
    }

    private void updateView() {
        loadData();
        tvRecord.setText("Record: " + record);
        tvCoins.setText(coins + " ");
        bStart.setClickable(true);
    }

    private void loadData() {
        saver = Saver.getInstance(this);
        record = saver.loadInt(Saver.KEY_RECORD);
        coins = saver.loadInt(Saver.KEY_COINS);
    }

    public void onClick(View v) {
        intent = new Intent(this, ClickerActivity.class);
        bStart.setClickable(false);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onNewRecord(int newRecord, int coins) {
        loadData();
        this.record = newRecord;
        this.coins += coins;
        saver.saveInt(newRecord, Saver.KEY_RECORD);
        saver.saveInt(this.coins, Saver.KEY_COINS);

        updateView();
    }
}
