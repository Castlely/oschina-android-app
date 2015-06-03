package cn.gov.psxq.wxapi;

import cn.gov.psxq.common.UIHelper;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onReq(BaseReq arg0) {
        UIHelper.ToastMessage(this, "正在分享");
    }

    @Override
    public void onResp(BaseResp arg0) {
        UIHelper.ToastMessage(this, "分享成功");
    }
}