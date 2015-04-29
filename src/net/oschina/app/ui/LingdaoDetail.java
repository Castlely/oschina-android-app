package net.oschina.app.ui;

import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.bean.Notice;
import net.oschina.app.common.HtmlRegexpUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.designapp.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 博客详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class LingdaoDetail extends BaseActivity {

    public final static String TAG                    = "InformationDetail";

    private FrameLayout        mHeader;
    private ImageView          mBack;
    private ImageView          mRefresh;
    private TextView           mHeadTitle;
    private ProgressBar        mProgressbar;
    private ViewSwitcher       mViewSwitcher;

    private ImageView          mImage;
    private TextView           mName;
    private TextView           mZhiwu;

    private WebView            mWebView;
    private Handler            mHandler;
    private String             lingdaoName;
    private String             lingdaoZhiwu;
    private String             lingdaoWebSrc;
    private String             lingdaoImg;

    private final static int   VIEWSWITCH_TYPE_DETAIL = 0x001;

    private final static int   DATA_LOAD_ING          = 0x001;
    private final static int   DATA_LOAD_COMPLETE     = 0x002;
    private final static int   DATA_LOAD_FAIL         = 0x003;

    private GestureDetector    gd;
    private boolean            isFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lingdao_detail);
        this.initView();
        this.initData();

        //注册双击全屏事件
        this.regOnDoubleEvent();
    }

    //初始化视图控件
    private void initView() {
        lingdaoName = getIntent().getStringExtra("lingdaoName");
        lingdaoImg = getIntent().getStringExtra("lingdaoImg");
        lingdaoZhiwu = getIntent().getStringExtra("lingdaoZhiwu");
        lingdaoWebSrc = getIntent().getStringExtra("lingdaoWebSrc");
        mHeader = (FrameLayout) findViewById(R.id.information_detail_header);
        mBack = (ImageView) findViewById(R.id.information_detail_back);
        mRefresh = (ImageView) findViewById(R.id.information_detail_refresh);
        mProgressbar = (ProgressBar) findViewById(R.id.information_detail_head_progress);
        mHeadTitle = (TextView) findViewById(R.id.information_detail_head_title);

        mImage = (ImageView) findViewById(R.id.lingdao_detail_image);
        LayoutParams para;
        para = mImage.getLayoutParams();
        para.height = 220;
        para.width = 170;
        mImage.setLayoutParams(para);
        mName = (TextView) findViewById(R.id.lingdao_detail_name);
        mZhiwu = (TextView) findViewById(R.id.lingdao_detail_zhiwu);

        mWebView = (WebView) findViewById(R.id.lingdao_detail_webview);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, mWebView);

        mBack.setOnClickListener(UIHelper.finish(this));

    }

    //初始化控件数据
    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    headButtonSwitch(DATA_LOAD_COMPLETE);
                    mName.setText(lingdaoName);
                    mZhiwu.setText(lingdaoZhiwu);

                    ImageLoader.getInstance().displayImage(lingdaoImg, mImage);
                    String body = UIHelper.WEB_STYLE + lingdaoWebSrc;
                    body = HtmlRegexpUtils.z(body);
                    //读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
                    boolean isLoadImage;
                    AppContext ac = (AppContext) getApplication();
                    if (AppContext.NETTYPE_WIFI == ac.getNetworkType()) {
                        isLoadImage = true;
                    } else {
                        isLoadImage = ac.isLoadImage();
                    }
                    if (isLoadImage) {
                        body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
                        body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

                        // 添加点击图片放大支持
                        body = body
                            .replaceAll("(<img[^>]+src=\")(\\S+)\"",
                                "$1$2\" onclick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
                    } else {
                        body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
                    }

                    mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                    mWebView.setWebViewClient(UIHelper.getWebViewClient());
                    //发送通知广播
                    if (msg.obj != null) {
                        UIHelper.sendBroadCast(LingdaoDetail.this, (Notice) msg.obj);
                    }
                } else if (msg.what == 0) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    UIHelper.ToastMessage(LingdaoDetail.this, R.string.msg_load_is_null);
                } else if (msg.what == -1 && msg.obj != null) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    ((AppException) msg.obj).makeToast(LingdaoDetail.this);
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
                mProgressbar.setVisibility(View.VISIBLE);
                mRefresh.setVisibility(View.GONE);
                break;
            case DATA_LOAD_COMPLETE:
                mProgressbar.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_FAIL:
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
