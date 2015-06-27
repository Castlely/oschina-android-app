package cn.gov.psxq.ui;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;

public class BaseActionBarActivity extends ActionBarActivity {
    // 是否允许全屏
    private boolean allowFullScreen = false;
    // 是否允许销毁
    private boolean allowDestroy    = true;

    private View    view;

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
       super.onActivityResult(requestCode, resultCode, intent);
       
    }
    /**
     * 设置是否可以全屏
     * 
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    public void setAllowDestroy(boolean allowDestroy) {
        this.allowDestroy = allowDestroy;
    }

    public void setAllowDestroy(boolean allowDestroy, View view) {
        this.allowDestroy = allowDestroy;
        this.view = view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
            view.onKeyDown(keyCode, event);
            if (!allowDestroy) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
