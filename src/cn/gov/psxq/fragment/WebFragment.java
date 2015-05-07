package cn.gov.psxq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;
import cn.gov.psxq.common.UIHelper;

public class WebFragment extends Fragment {
    WebView webView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化Handler
        View view = inflater.inflate(R.layout.web_fragment, null);
        webView = (WebView) view.findViewById(R.id.web_detail_webview);
        String catalogName = this.getArguments().getString("catalogName");
        final String title = this.getArguments().getString("title");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(15);
        webView.loadUrl(AppData.urlList.get(catalogName));
        webView.setWebViewClient(UIHelper.getWebViewClient());
        return view;
    }

}
