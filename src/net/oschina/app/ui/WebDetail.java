package net.oschina.app.ui;

import net.oschina.app.AppConfig;
import net.oschina.app.AppContext;
import net.oschina.app.AppData;
import net.oschina.app.AppException;
import net.oschina.app.bean.Information;
import net.oschina.app.bean.Notice;
import net.oschina.app.common.HtmlRegexpUtils;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.fragment.InformationListFragment;
import net.oschina.app.widget.BadgeView;
import net.oschina.designapp.R;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 博客详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class WebDetail extends BaseActivity {

    public final static String TAG                    = "InformationDetail";

    private FrameLayout        mHeader;
    private ImageView          mBack;
    private ImageView          mFavorite;
    private ImageView          mRefresh;
    private TextView           mHeadTitle;
    private ProgressBar        mProgressbar;
    private ScrollView         mScrollView;
    private ViewSwitcher       mViewSwitcher;

    private WebView            mWebView;
    private Handler            mHandler;
    private String             url;

    private final static int   VIEWSWITCH_TYPE_DETAIL = 0x001;

    private final static int   DATA_LOAD_ING          = 0x001;
    private final static int   DATA_LOAD_COMPLETE     = 0x002;
    private final static int   DATA_LOAD_FAIL         = 0x003;

    private ProgressDialog     mProgress;
    private InputMethodManager imm;
    private ActionBar          mActionBar;
    private GestureDetector    gd;
    private boolean            isFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_detail);

        this.initView();
        this.initData();

        //注册双击全屏事件
        this.regOnDoubleEvent();
    }

    //初始化视图控件
    private void initView() {

        mHeader = (FrameLayout) findViewById(R.id.information_detail_header);
        mBack = (ImageView) findViewById(R.id.information_detail_back);
        mRefresh = (ImageView) findViewById(R.id.information_detail_refresh);
        mProgressbar = (ProgressBar) findViewById(R.id.information_detail_head_progress);
        mHeadTitle = (TextView) findViewById(R.id.information_detail_head_title);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.web_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.web_detail_scrollview);

        mWebView = (WebView) findViewById(R.id.web_detail_webview);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, mWebView);

        mBack.setOnClickListener(UIHelper.finish(this));

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    //初始化控件数据
    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    headButtonSwitch(DATA_LOAD_COMPLETE);
                    mWebView.loadUrl(url);
                    mWebView.setWebViewClient(UIHelper.getWebViewClient());

                    //发送通知广播
                    if (msg.obj != null) {
                        UIHelper.sendBroadCast(WebDetail.this, (Notice) msg.obj);
                    }
                } else if (msg.what == 0) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    UIHelper.ToastMessage(WebDetail.this, R.string.msg_load_is_null);
                } else if (msg.what == -1 && msg.obj != null) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    ((AppException) msg.obj).makeToast(WebDetail.this);
                }
            }
        };

        initData(false);
    }

    private void initData(final boolean isRefresh) {
        headButtonSwitch(DATA_LOAD_ING);

        new Thread() {
            public void run() {
                Message msg = new Message();
                //informationDetail = ((AppContext) getApplication()).getInformation(
                //    information_id, isRefresh);
                url = getIntent().getStringExtra("url");
                msg.what = 1;
                msg.obj = null;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 底部栏切换
     * @param type
     */
    private void viewSwitch(int type) {
        switch (type) {
            case VIEWSWITCH_TYPE_DETAIL:
                mHeadTitle.setText("详细信息");
                mViewSwitcher.setDisplayedChild(0);
                break;
        }
    }

    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) {
        switch (type) {
            case DATA_LOAD_ING:
                mScrollView.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.VISIBLE);
                mRefresh.setVisibility(View.GONE);
                break;
            case DATA_LOAD_COMPLETE:
                mScrollView.setVisibility(View.VISIBLE);
                mProgressbar.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_FAIL:
                mScrollView.setVisibility(View.GONE);
                mProgressbar.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                break;
        }
    }

    private View.OnClickListener detailClickListener = new View.OnClickListener() {
                                                         public void onClick(View v) {
                                                             //切换到详情
                                                             viewSwitch(VIEWSWITCH_TYPE_DETAIL);
                                                         }
                                                     };

    /**
     * 注册双击全屏事件
     */
    private void regOnDoubleEvent() {
        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                isFullScreen = !isFullScreen;
                if (!isFullScreen) {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().setAttributes(params);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    mHeader.setVisibility(View.VISIBLE);
                } else {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(params);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    mHeader.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isAllowFullScreen()) {
            gd.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

}
