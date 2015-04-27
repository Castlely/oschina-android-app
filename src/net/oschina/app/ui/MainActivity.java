package net.oschina.app.ui;

import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.bean.MyInformation;
import net.oschina.app.bean.Notice;
import net.oschina.app.common.AnimUtil;
import net.oschina.app.common.BitmapManager;
import net.oschina.app.common.UIHelper;
import net.oschina.app.fragment.BuMenFragment;
import net.oschina.app.fragment.DingYueFragment;
import net.oschina.app.fragment.GaiKuangFragment;
import net.oschina.app.fragment.IndexFragment;
import net.oschina.app.fragment.LingDaoFragment;
import net.oschina.app.inteface.FooterViewVisibility;
import net.oschina.designapp.R;
import net.tsz.afinal.FinalHttp;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class MainActivity extends BaseActionBarActivity implements FooterViewVisibility,
                                                       OnClickListener, BackHandledInterface {
    public static final String  TAG = MainActivity.class.getSimpleName();
    private int footbar_selected = 0xff1056e2;
    private int footbar_unselected = 0xff686868;
    private AppContext          appContext;
    private LinearLayout        footerLayout;
    private BackHandledFragment mBackHandedFragment;
    private RadioButton         fbGaiKuang;
    private RadioButton         fbLingDao;
    private RadioButton         fbBuMen;
    private RadioButton         fbDingYue;
    private FinalHttp           finalHttp;
    private BitmapManager       bitmapManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        finalHttp = new FinalHttp();
        bitmapManager = new BitmapManager();
        appContext = (AppContext) getApplication();

        initFirstFragment();
        setFooterView();

        // 网络连接判断
        if (!appContext.isNetworkConnected())
            UIHelper.ToastMessage(this, R.string.network_not_connected);
        // 初始化登录
        appContext.initLoginInfo();
        // 检查新版本
        if (appContext.isCheckUp()) {
            // UpdateManager.getUpdateManager().checkAppUpdate(this, false);
        }
        // 启动轮询通知信息
        this.foreachUserNotice();

    }

    private void initFirstFragment() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }

    private void setFootBarImageSize(RadioButton radioButton, int actionBarHeight) {
        int size = actionBarHeight / 10 * 14;
        Drawable[] drawables = radioButton.getCompoundDrawables();//左上右下
        radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0],
            zoomDrawable(drawables[1], size, size), drawables[2], drawables[3]);
    }

    private void setFootBarButtonState(int position, boolean checked) {
        int actionBarHeight = getActionBar().getHeight();
        Drawable[] drawables;//左上右下
        if (checked == true && position == 1) {
            drawables = fbGaiKuang.getCompoundDrawables();
            fbGaiKuang.setTextColor(footbar_selected);
            drawables[1] = getResources().getDrawable(R.drawable.jieshao_selected);
            fbGaiKuang.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbGaiKuang, actionBarHeight);
        }
        if (checked == false && position == 1) {
            drawables = fbGaiKuang.getCompoundDrawables();
            fbGaiKuang.setTextColor(footbar_unselected);
            drawables[1] = getResources().getDrawable(R.drawable.jieshao_unselected);
            fbGaiKuang.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbGaiKuang, actionBarHeight);
        }
        if (checked == true && position == 2) {
            drawables = fbLingDao.getCompoundDrawables();
            fbLingDao.setTextColor(footbar_selected);
            drawables[1] = getResources().getDrawable(R.drawable.lingdao_selected);
            fbLingDao.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbLingDao, actionBarHeight);
        }
        if (checked == false && position == 2) {
            drawables = fbLingDao.getCompoundDrawables();
            fbLingDao.setTextColor(footbar_unselected);
            drawables[1] = getResources().getDrawable(R.drawable.lingdao_unselected);
            fbLingDao.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbLingDao, actionBarHeight);
        }
        if (checked == true && position == 3) {
            drawables = fbBuMen.getCompoundDrawables();
            fbBuMen.setTextColor(footbar_selected);
            drawables[1] = getResources().getDrawable(R.drawable.bumen_selected);
            fbBuMen.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbBuMen, actionBarHeight);

        }
        if (checked == false && position == 3) {
            drawables = fbBuMen.getCompoundDrawables();
            fbBuMen.setTextColor(footbar_unselected);
            drawables[1] = getResources().getDrawable(R.drawable.bumen_unselected);
            fbBuMen.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbBuMen, actionBarHeight);
        }
        if (checked == true && position == 4) {
            drawables = fbDingYue.getCompoundDrawables();
            fbDingYue.setTextColor(footbar_selected);
            drawables[1] = getResources().getDrawable(R.drawable.dingyue_selected);
            fbDingYue.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbDingYue, actionBarHeight);
        }
        if (checked == false && position == 4) {
            drawables = fbDingYue.getCompoundDrawables();
            fbDingYue.setTextColor(footbar_unselected);
            drawables[1] = getResources().getDrawable(R.drawable.dingyue_unselected);
            fbDingYue.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1],
                drawables[2], drawables[3]);
            setFootBarImageSize(fbDingYue, actionBarHeight);
        }

    }

    private void setFooterView() {
        footerLayout = (LinearLayout) findViewById(R.id.main_layout_footer);
        footerLayout.post(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.  
                int actionBarHeight = getActionBar().getHeight();
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) footerLayout
                    .getLayoutParams();
                lParams.height = actionBarHeight;
                footerLayout.setLayoutParams(lParams);

                setFootBarImageSize(fbGaiKuang, actionBarHeight);
                setFootBarImageSize(fbLingDao, actionBarHeight);
                setFootBarImageSize(fbBuMen, actionBarHeight);
                setFootBarImageSize(fbDingYue, actionBarHeight);
            }
        });
        /* int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
         int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
         getActionBar().getCustomView().measure(w, h);
         int height = getActionBar().getCustomView().getMeasuredHeight();
         RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) footerLayout
             .getLayoutParams();
         lParams.height = height;
         footerLayout.setLayoutParams(lParams);*/
        fbGaiKuang = (RadioButton) findViewById(R.id.main_footbar_gaikuang);
        fbLingDao = (RadioButton) findViewById(R.id.main_footbar_lingdao);
        fbBuMen = (RadioButton) findViewById(R.id.main_footbar_bumen);
        fbDingYue = (RadioButton) findViewById(R.id.main_footbar_dingyue);

        fbGaiKuang.setOnClickListener(onClickListener);
        fbLingDao.setOnClickListener(onClickListener);
        fbBuMen.setOnClickListener(onClickListener);
        fbDingYue.setOnClickListener(onClickListener);

    }

    private Handler       mHandler;
    private MyInformation user;

    private void loadUserInfoThread(final boolean isRefresh) {
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    MyInformation user = ((AppContext) getApplication())
                        .getMyInformation(isRefresh);
                    msg.what = 1;
                    msg.obj = user;
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         resetRadioButton();
                                                         FragmentManager fManager = getSupportFragmentManager();
                                                         FragmentTransaction fTransaction = fManager
                                                             .beginTransaction();
                                                         switch (v.getId()) {
                                                             case R.id.main_footbar_gaikuang:
                                                                 setFootBarButtonState(1, true);
                                                                 setFootBarButtonState(2, false);
                                                                 setFootBarButtonState(3, false);
                                                                 setFootBarButtonState(4, false);
                                                                 fTransaction
                                                                     .replace(
                                                                         R.id.main_activity_linearlayout,
                                                                         new GaiKuangFragment())
                                                                     .commit();
                                                                 break;
                                                             case R.id.main_footbar_lingdao:
                                                                 setFootBarButtonState(1, false);
                                                                 setFootBarButtonState(2, true);
                                                                 setFootBarButtonState(3, false);
                                                                 setFootBarButtonState(4, false);
                                                                 fTransaction
                                                                     .replace(
                                                                         R.id.main_activity_linearlayout,
                                                                         new LingDaoFragment())
                                                                     .commit();
                                                                 break;
                                                             case R.id.main_footbar_bumen:
                                                                 setFootBarButtonState(1, false);
                                                                 setFootBarButtonState(2, false);
                                                                 setFootBarButtonState(3, true);
                                                                 setFootBarButtonState(4, false);
                                                                 fTransaction
                                                                     .replace(
                                                                         R.id.main_activity_linearlayout,
                                                                         new BuMenFragment())
                                                                     .commit();
                                                                 break;
                                                             case R.id.main_footbar_dingyue:
                                                                 setFootBarButtonState(1, false);
                                                                 setFootBarButtonState(2, false);
                                                                 setFootBarButtonState(3, false);
                                                                 setFootBarButtonState(4, true);
                                                                 fTransaction
                                                                     .replace(
                                                                         R.id.main_activity_linearlayout,
                                                                         new DingYueFragment())
                                                                     .commit();
                                                                 break;

                                                             default:
                                                                 break;
                                                         }
                                                     }
                                                 };

    private void resetRadioButton() {
        setFootBarButtonState(1, false);
        setFootBarButtonState(2, false);
        setFootBarButtonState(3, false);
        setFootBarButtonState(4, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 轮询通知信息
     */
    private void foreachUserNotice() {
        final int uid = appContext.getLoginUid();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    UIHelper.sendBroadCast(MainActivity.this, (Notice) msg.obj);
                }
                foreachUserNotice();// 回调
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    sleep(60 * 1000);
                    if (uid > 0) {
                        Notice notice = appContext.getUserNotice(uid);
                        msg.what = 1;
                        msg.obj = notice;
                    } else {
                        msg.what = 0;
                    }
                } catch (AppException e) {
                    e.printStackTrace();
                    msg.what = -1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void setFooterViewVisibility(int v) {
        AnimUtil.FooterViewAnim(MainActivity.this, footerLayout, v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onClick(final View v) {
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
            : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
