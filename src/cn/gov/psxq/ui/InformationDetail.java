package cn.gov.psxq.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import cn.gov.psxq.AppConfig;
import cn.gov.psxq.AppData;
import cn.gov.psxq.AppException;
import cn.gov.psxq.R;
import cn.gov.psxq.bean.Information;
import cn.gov.psxq.bean.Notice;
import cn.gov.psxq.common.HtmlRegexpUtils;
import cn.gov.psxq.common.StringUtils;
import cn.gov.psxq.common.UIHelper;
import cn.gov.psxq.widget.BadgeView;

/**
 * 博客详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class InformationDetail extends BaseActivity {

    public final static String TAG                    = "InformationDetail";

    private FrameLayout        mHeader;
    private LinearLayout       mFooter;
    private ImageView          mBack;
    private ImageView          mFavorite;
    private ImageView          mRefresh;
    private TextView           mHeadTitle;
    private ProgressBar        mProgressbar;
    private ScrollView         mScrollView;
    private ViewSwitcher       mViewSwitcher;

    private BadgeView          bv_comment;
    private ImageView          mDetail;
    private ImageView          mCommentList;
    private ImageView          mShare;

    private ImageView          mDocTYpe;
    private TextView           mTitle;
    private TextView           mAuthor;
    private TextView           mPubDate;
    private TextView           mCommentCount;

    private WebView            mWebView;
    private Handler            mHandler;
    private Information        informationDetail;

    private final static int   VIEWSWITCH_TYPE_DETAIL = 0x001;

    private final static int   DATA_LOAD_ING          = 0x001;
    private final static int   DATA_LOAD_COMPLETE     = 0x002;
    private final static int   DATA_LOAD_FAIL         = 0x003;

    private ViewSwitcher       mFootViewSwitcher;
    private ImageView          mFootEditebox;
    private EditText           mFootEditer;
    private Button             mFootPubcomment;
    private ProgressDialog     mProgress;
    private InputMethodManager imm;
    private String             tempCommentKey         = AppConfig.TEMP_COMMENT;

    private int                _id;
    private int                _uid;
    private String             _content;

    private GestureDetector    gd;
    private boolean            isFullScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_detail);

        this.initView();
        this.initData();

        //注册双击全屏事件
        this.regOnDoubleEvent();
    }

    // 隐藏输入发表回帖状态
    private void hideEditor(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (mFootViewSwitcher.getDisplayedChild() == 1) {
            mFootViewSwitcher.setDisplayedChild(0);
            mFootEditer.clearFocus();
            mFootEditer.setVisibility(View.GONE);
        }
    }

    //初始化视图控件
    private void initView() {

        mHeader = (FrameLayout) findViewById(R.id.information_detail_header);
        mHeader.post(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.  
                int actionBarHeight = mFooter.getHeight();
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) mHeader
                    .getLayoutParams();
                lParams.height = actionBarHeight;
                mHeader.setLayoutParams(lParams);
            }
        });
        mFooter = (LinearLayout) findViewById(R.id.information_detail_footer);
        mBack = (ImageView) findViewById(R.id.information_detail_back);
        mRefresh = (ImageView) findViewById(R.id.information_detail_refresh);
        mProgressbar = (ProgressBar) findViewById(R.id.information_detail_head_progress);
        mHeadTitle = (TextView) findViewById(R.id.information_detail_head_title);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.information_detail_viewswitcher);
        mScrollView = (ScrollView) findViewById(R.id.information_detail_scrollview);

        mDetail = (ImageView) findViewById(R.id.information_detail_footbar_detail);
        mCommentList = (ImageView) findViewById(R.id.information_detail_footbar_commentlist);
        mShare = (ImageView) findViewById(R.id.information_detail_footbar_share);
        mFavorite = (ImageView) findViewById(R.id.information_detail_footbar_favorite);

        mDocTYpe = (ImageView) findViewById(R.id.information_detail_documentType);
        mTitle = (TextView) findViewById(R.id.information_detail_title);
        mAuthor = (TextView) findViewById(R.id.information_detail_author);
        mPubDate = (TextView) findViewById(R.id.information_detail_date);
        mCommentCount = (TextView) findViewById(R.id.information_detail_commentcount);

        mDetail.setEnabled(false);

        mWebView = (WebView) findViewById(R.id.information_detail_webview);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, mWebView);

        mBack.setOnClickListener(UIHelper.finish(this));
        mDetail.setOnClickListener(detailClickListener);

        bv_comment = new BadgeView(this, mCommentList);
        bv_comment.setBackgroundResource(R.drawable.widget_count_bg2);
        bv_comment.setIncludeFontPadding(false);
        bv_comment.setGravity(Gravity.CENTER);
        bv_comment.setTextSize(8f);
        bv_comment.setTextColor(Color.WHITE);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mFootViewSwitcher = (ViewSwitcher) findViewById(R.id.information_detail_foot_viewswitcher);
        mFootPubcomment = (Button) findViewById(R.id.information_detail_foot_pubcomment);
        mFootEditebox = (ImageView) findViewById(R.id.information_detail_footbar_editebox);
        mFootEditebox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFootViewSwitcher.showNext();
                mFootEditer.setVisibility(View.VISIBLE);
                mFootEditer.requestFocus();
                mFootEditer.requestFocusFromTouch();
            }
        });
        mFootEditer = (EditText) findViewById(R.id.information_detail_foot_editer);
        mFootEditer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(v, 0);
                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    hideEditor(v);
                }
            }
        });
        mFootEditer.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    hideEditor(v);
                    return true;
                }
                return false;
            }
        });
        // 编辑器添加文本监听
        mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(this, tempCommentKey));

       

    }

    //初始化控件数据
    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    headButtonSwitch(DATA_LOAD_COMPLETE);

                    mTitle.setText(informationDetail.getTitle());
                    mPubDate.setText(StringUtils.friendly_time(informationDetail.getLast_update()));

                    String body = UIHelper.WEB_STYLE + informationDetail.getDescription();
                    body = HtmlRegexpUtils.z(body);

                    body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
                    body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

                    // 添加点击图片放大支持
                    body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                        "$1$2\" onclick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");

                    mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                    mWebView.setWebViewClient(UIHelper.getWebViewClient());

                    //发送通知广播
                    if (msg.obj != null) {
                        UIHelper.sendBroadCast(InformationDetail.this, (Notice) msg.obj);
                    }
                } else if (msg.what == 0) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    UIHelper.ToastMessage(InformationDetail.this, R.string.msg_load_is_null);
                } else if (msg.what == -1 && msg.obj != null) {
                    headButtonSwitch(DATA_LOAD_FAIL);

                    ((AppException) msg.obj).makeToast(InformationDetail.this);
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
                String informationJsonString = getIntent().getStringExtra("information");
                informationDetail = AppData.gsonBuilder.create().fromJson(informationJsonString,
                    Information.class);
                msg.what = (informationDetail != null && informationDetail.getId() > 0) ? 1 : 0;
                msg.obj = (informationDetail != null) ? informationDetail.getNotice() : null;
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
                mDetail.setEnabled(false);
                mCommentList.setEnabled(true);
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
                    mFooter.setVisibility(View.VISIBLE);
                } else {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getWindow().setAttributes(params);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    mHeader.setVisibility(View.GONE);
                    mFooter.setVisibility(View.GONE);
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
