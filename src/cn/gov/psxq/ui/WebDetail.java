package cn.gov.psxq.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.gov.psxq.AppException;
import cn.gov.psxq.R;
import cn.gov.psxq.bean.Notice;
import cn.gov.psxq.common.UIHelper;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMEvernoteHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.media.EvernoteShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.laiwang.media.LWDynamicShareContent;
import com.umeng.socialize.laiwang.media.LWShareContent;
import com.umeng.socialize.media.GooglePlusShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;

/**
 * 博客详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class WebDetail extends BaseActivity {

    public final static String    TAG                    = "InformationDetail";
    private final UMSocialService mController            = UMServiceFactory
                                                             .getUMSocialService("com.umeng.share");
    private FrameLayout           mHeader;
    private ImageView             mBack;
    private ImageView             mFavorite;
    private ImageView             mRefresh;
    private TextView              mHeadTitle;
    private ProgressBar           mProgressbar;

    private WebView               webView;
    private Handler               mHandler;
    private String                url;

    private final static int      VIEWSWITCH_TYPE_DETAIL = 0x001;

    private final static int      DATA_LOAD_ING          = 0x001;
    private final static int      DATA_LOAD_COMPLETE     = 0x002;
    private final static int      DATA_LOAD_FAIL         = 0x003;

    private ProgressDialog        mProgress;
    private InputMethodManager    imm;
    private ActionBar             mActionBar;
    private GestureDetector       gd;
    private boolean               isFullScreen;
    private String                title;
    private String                share;

    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加人人网SSO授权
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(this, "201874",
            "28401c0964f04a72a14c812d6132fcef", "3bf66e42db1e4fa9829b955cc300b737");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();
        addSMS();
        addEmail();
    }

    private void setShareContent() {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
            "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent(share + " " + url);

        // APP ID：201874, API
        // * KEY：28401c0964f04a72a14c812d6132fcef, Secret
        // * Key：3bf66e42db1e4fa9829b955cc300b737.
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(this, "201874",
            "28401c0964f04a72a14c812d6132fcef", "3bf66e42db1e4fa9829b955cc300b737");
        mController.getConfig().setSsoHandler(renrenSsoHandler);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent("来自坪山新区政府在线");
        weixinContent.setTitle(share + " " + url);
        weixinContent.setTargetUrl(url);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自坪山新区政府在线");
        circleMedia.setTitle(share + " " + url);
        circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);

        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(share + " " + url);
        renrenShareContent.setAppWebSite(url);
        mController.setShareMedia(renrenShareContent);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(share + " " + url);
        qzone.setTargetUrl(url);
        qzone.setTitle(share + " " + url);
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        //        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
        qqShareContent.setTitle(share + " " + url);
        qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);

        TencentWbShareContent tencent = new TencentWbShareContent();
        tencent.setShareContent(share + " " + url + " " + url);
        tencent.setTitle(share + " " + url);
        tencent.setTargetUrl(url);
        // 设置tencent分享内容
        mController.setShareMedia(tencent);

        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
        MailShareContent mail = new MailShareContent();
        mail.setTitle(share + " " + url);
        mail.setShareContent(url);
        // 设置tencent分享内容
        mController.setShareMedia(mail);

        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(share + " " + url);
        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(share + " " + url);
        mController.setShareMedia(sinaContent);

        TwitterShareContent twitterShareContent = new TwitterShareContent();
        twitterShareContent.setShareContent(share + " " + url);
        mController.setShareMedia(twitterShareContent);

        GooglePlusShareContent googlePlusShareContent = new GooglePlusShareContent();
        googlePlusShareContent.setShareContent(share + " " + url);
        mController.setShareMedia(googlePlusShareContent);

        // 来往分享内容
        LWShareContent lwShareContent = new LWShareContent();
        // lwShareContent.setShareImage(urlImage);
        // lwShareContent.setShareMedia(uMusic);
        lwShareContent.setTitle(share + " " + url);
        lwShareContent.setMessageFrom("坪山新区政府在线");
        lwShareContent.setShareContent(share + " " + url);
        mController.setShareMedia(lwShareContent);

        // 来往动态分享内容
        LWDynamicShareContent lwDynamicShareContent = new LWDynamicShareContent();
        // lwDynamicShareContent.setShareImage(urlImage);
        // lwDynamicShareContent.setShareMedia(uMusic);
        lwDynamicShareContent.setTitle(share + " " + url);
        lwDynamicShareContent.setMessageFrom(share + " " + url);
        lwDynamicShareContent.setShareContent("来自坪山新区政府在线");
        lwDynamicShareContent.setTargetUrl(url);
        mController.setShareMedia(lwDynamicShareContent);

    }

    /**
     * 添加短信平台</br>
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }

    /**
     * 添加Email平台</br>
     */
    private void addEmail() {
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }

    /**
     * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享</br>
     */
    private void addYNote() {
        UMYNoteHandler yNoteHandler = new UMYNoteHandler(this);
        yNoteHandler.addToSocialSDK();
        YNoteShareContent yNoteShareContent = new YNoteShareContent();
        yNoteShareContent.setShareContent(share + " " + url);
        yNoteShareContent.setTitle(share);
        mController.setShareMedia(yNoteShareContent);
    }

    /**
     * 添加印象笔记平台
     */
    private void addEverNote() {
        UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(this);
        evernoteHandler.addToSocialSDK();

        // 设置evernote的分享内容
        EvernoteShareContent shareContent = new EvernoteShareContent(share + " " + url);
        mController.setShareMedia(shareContent);
    }

    /**
     * 添加来往和来往动态平台</br>
     */
    private void addLaiWang() {

        String appToken = "laiwangd497e70d4";
        String secretID = "d497e70d4c3e4efeab1381476bac4c5e";
        // laiwangd497e70d4:来往appToken,d497e70d4c3e4efeab1381476bac4c5e:来往secretID
        // 添加来往的支持
        UMLWHandler umlwHandler = new UMLWHandler(this, appToken, secretID);
        umlwHandler.addToSocialSDK();

        // 添加来往动态的支持
        UMLWHandler lwDynamicHandler = new UMLWHandler(this, appToken, secretID);
        lwDynamicHandler.setToCircle(true);
        lwDynamicHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx967daebe835fbeac";
        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
        qqSsoHandler.setTargetUrl(url);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @Title: addYXPlatform
     * @Description:
     * @throws
     */
    private void addYXPlatform() {

        // 添加易信平台
        UMYXHandler yixinHandler = new UMYXHandler(this, "yxc0614e80c9304c11b0391514d09f13bf");
        // 关闭分享时的等待Dialog
        yixinHandler.enableLoadingDialog(false);
        // 设置target Url, 必须以http或者https开头
        yixinHandler.setTargetUrl(url);
        yixinHandler.addToSocialSDK();

        // 易信朋友圈平台
        UMYXHandler yxCircleHandler = new UMYXHandler(this, "yxc0614e80c9304c11b0391514d09f13bf");
        yxCircleHandler.setToCircle(true);
        yxCircleHandler.addToSocialSDK();

    }

    private ValueCallback<Uri> mUploadMessage;
    private final static int   FILECHOOSER_RESULTCODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent
                .getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_detail);
        getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        share = getIntent().getStringExtra("share");
        this.initView();
        // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享的内容
        setShareContent();
        title = getIntent().getExtras().getString("title");
        url = getIntent().getExtras().getString("url");
        share = getIntent().getExtras().getString("share");
        mHeadTitle.setText(title);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebDetail.this.startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);

            }

            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            // For Android 3.0+  
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebDetail.this.startActivityForResult(Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1  
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType,
                                        String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebDetail.this.startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);

            }
        });
        webView.setWebViewClient(UIHelper.getWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setSavePassword(true);

        webView.getSettings().setTextZoom(100);

        int minimumFontSize = 1;
        webView.getSettings().setMinimumFontSize(minimumFontSize);
        webView.getSettings().setMinimumLogicalFontSize(minimumFontSize);

        CookieManager.getInstance().setAcceptCookie(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setEnableSmoothTransition(true);

        // HTML5 API flags
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // HTML5 configuration settings.
        webView.getSettings().setAppCacheMaxSize(3 * 1024 * 1024);

        webView.getSettings().setAppCachePath(
            WebDetail.this.getApplicationContext().getDir("appcache", Context.MODE_PRIVATE)
                .getPath());
        webView.getSettings().setDatabasePath(
            WebDetail.this.getApplicationContext().getDir("databases", Context.MODE_PRIVATE)
                .getPath());
        webView.getSettings().setGeolocationDatabasePath(
            WebDetail.this.getApplicationContext().getDir("geolocation", Context.MODE_PRIVATE)
                .getPath());
        webView.setDownloadListener(new MyDownloadListener());
        this.regOnDoubleEvent();
    }

    private class MyDownloadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    //初始化视图控件
    private void initView() {

        mHeader = (FrameLayout) findViewById(R.id.information_detail_header);
        mBack = (ImageView) findViewById(R.id.information_detail_back);
        mRefresh = (ImageView) findViewById(R.id.information_detail_refresh);
        mProgressbar = (ProgressBar) findViewById(R.id.information_detail_head_progress);
        mHeadTitle = (TextView) findViewById(R.id.information_detail_head_title);

        webView = (WebView) findViewById(R.id.web_detail_webview);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, webView);

        mBack.setOnClickListener(UIHelper.finish(this));
        mRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mController.getConfig().setPlatforms(SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                    SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
                mController.openShare(WebDetail.this, false);
            }
        });

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    //初始化控件数据
    private void initData() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    headButtonSwitch(DATA_LOAD_COMPLETE);

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
                title = getIntent().getStringExtra("title");
                share = getIntent().getStringExtra("share");
                msg.what = 1;
                msg.obj = null;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 底部栏切换
     * @param type
     */
    private void viewSwitch(int type) {
        switch (type) {
            case VIEWSWITCH_TYPE_DETAIL:
                mHeadTitle.setText(title);
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
