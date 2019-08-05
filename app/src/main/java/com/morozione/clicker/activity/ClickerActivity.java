package com.morozione.clicker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.morozione.clicker.LevelsManager;
import com.morozione.clicker.R;
import com.morozione.clicker.Saver;
import com.morozione.clicker.SecondsManager;
import com.morozione.clicker.dialog.DialogRecord;

public class ClickerActivity extends AppCompatActivity implements View.OnClickListener, SecondsManager.OnSecondsListener {

    private int activeClick;
    private int coins;

    private TextView tvResult, tvTime;
    private View iUpTime;
    private Button bClick;
    private ImageView ivUpSeconds;

    private Intent intent;
    private SecondsManager secondsManager;
    private LevelsManager levelsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clicker);

        initView();

        secondsManager = new SecondsManager(this);
        levelsManager = new LevelsManager();

        coins = Saver.getInstance(this).loadInt(Saver.KEY_COINS);
        changeOnCoinsUpSeconds();
    }

    private void initView() {
        bClick = (Button) findViewById(R.id.b_click);
        ivUpSeconds = (ImageView) findViewById(R.id.iv_up_second);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvTime = (TextView) findViewById(R.id.tv_time);

        iUpTime = findViewById(R.id.i_up_time);
    }

    private void changeOnCoinsUpSeconds() {
        if (coins >= 30) {
            ivUpSeconds.setVisibility(View.VISIBLE);
            iUpTime.setVisibility(View.VISIBLE);
        } else {
            ivUpSeconds.setVisibility(View.GONE);
            iUpTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b_click) {
            if (activeClick == 0) {
                secondsManager.execute();
            }
            bClick.setText((++activeClick) + "");
            checkedOnNewLevel();
        } else if (v.getId() == R.id.iv_up_second) {
            secondsManager.upperSecondsOnNewLevel();
            makeAnimation();

            coins -= 30;
            Saver.getInstance(this).saveInt(coins, Saver.KEY_COINS);

            changeOnCoinsUpSeconds();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        secondsManager.cancel(true);
    }

    private void checkedOnNewLevel() {
        if (activeClick >= levelsManager.getStartRecord()) {
            levelsManager.upperLevel();
            secondsManager.upperSecondsOnNewLevel();
            writeLevelText();
            makeAnimation();
        }
    }

    private void writeLevelText() {
        tvResult.setText("to next: " + levelsManager.getStartRecord());
    }

    private void makeAnimation() {
        Animation resize = AnimationUtils.loadAnimation(this, R.anim.resize);
        tvTime.setAnimation(resize);
    }

    @Override
    public void countSecond(int seconds) {
        if (seconds >= 5) {
            tvTime.setTextColor(Color.parseColor("#FF159731"));
        } else if (seconds == 3) {
            tvTime.setTextColor(Color.parseColor("#FFB4AD1C"));
        } else if (seconds == 2) {
            tvTime.setTextColor(Color.parseColor("#FFBA1D2C"));
        } else if (seconds == 0) {
            intent = new Intent();
            intent.putExtra(DialogRecord.KEY_NEW_RECORD, activeClick);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (seconds != 0) {
            tvTime.setText("Time: " + seconds);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
