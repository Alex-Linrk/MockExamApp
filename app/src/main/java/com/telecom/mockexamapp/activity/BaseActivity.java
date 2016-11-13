package com.telecom.mockexamapp.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    private boolean canBack = false;
    private static final int EXIT_TEST = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT_TEST:
                    canBack = true;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !canBack) {
            Toast.makeText(this, "再点击一次BACK键退出练习！", Toast.LENGTH_SHORT).show();
            canBack = true;
            handler.sendEmptyMessageDelayed(EXIT_TEST, 2000);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
