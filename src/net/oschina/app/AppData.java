package net.oschina.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.oschina.app.common.StringUtils;
import net.oschina.designapp.R;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class AppData {
    public static final String              TAG         = "AppData";
    public static final Map<String, String> urlList;
    public static Map<String, Boolean>      isShortCut;
    public static Map<String, Boolean>      isLink;
    public static Map<String, Integer>      ico;

    public static GsonBuilder               gsonBuilder = new GsonBuilder();
    public static final String              WEATHERAPI  = "http://api.map.baidu.com/telematics/v3/weather?location=%E6%B7%B1%E5%9C%B3&output=json&ak=BPGPMj8GameGHglMDsbgUILZ";

    public static void set(String key, String value, Application context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("data",
            Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(String key, AppContext context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("data",
            Activity.MODE_PRIVATE);
        return mySharedPreferences.getString(key, "");
    }

    public static Map<String, Object> getMenu(Resources resource) {
        InputStream in = resource.openRawResource(R.raw.menu);
        String gsonString = null;
        try {
            gsonString = StringUtils.getStrFromInputSteam(in);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        Map<String, Object> gsonMap = gsonBuilder.create().fromJson(gsonString,
            new TypeToken<Map<String, Object>>() {
            }.getType());
        try {
            in.close();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        return gsonMap;
    }

    static {
        urlList = Maps.newHashMap();
        isShortCut = Maps.newHashMap();
        isLink = Maps.newHashMap();
        ico = Maps.newHashMap();
        urlList.put("了解坪山", "");
        urlList.put("坪山概况", "");
        urlList.put("文化坪山", "");
        urlList.put("产业坪山", "");
        urlList.put("信息公开", "");
        urlList
            .put("政务动态", "http://www.psxq.gov.cn/app/opendata/getData/63764?pageSize=10&pageNo=");
        urlList.put("通知公告", "");
        urlList.put("领导成员", "");
        urlList.put("规范性文件", "");
        urlList.put("财政资金", "");
        urlList.put("发展规划", "");
        urlList.put("政府工作报告", "");
        urlList.put("名单名录", "");
        urlList.put("直属部门", "");
        urlList.put("驻区单位", "");
        urlList.put("办事处", "");
        urlList.put("学校", "");
        urlList.put("医院", "");
        urlList.put("药店", "");
        urlList.put("诊所", "");
        urlList.put("社康中心", "");
        urlList.put("WIFI热点", "");
        urlList.put("办事服务", "");
        urlList.put("办事指南", "");
        urlList.put("个人办事", "");
        urlList.put("企业办事", "");
        urlList.put("部门服务", "");
        urlList.put("镇街服务", "");
        urlList.put("在线服务", "");
        urlList.put("挂号预约", "");
        urlList.put("婚姻登记预约", "");
        urlList.put("社保查询", "");
        urlList.put("公积金查询", "");
        urlList.put("地铁查询", "");
        urlList.put("信息自主申报", "");
        urlList.put("服务地图", "");
        urlList.put("互动交流", "");
        urlList.put("网上民声", "");
        urlList.put("微调查", "");
        urlList.put("乐在坪山", "");
        urlList.put("吃在坪山", "");
        urlList.put("游在坪山", "");

        isShortCut.put("了解坪山", false);
        isShortCut.put("坪山概况", false);
        isShortCut.put("文化坪山", false);
        isShortCut.put("产业坪山", true);
        isShortCut.put("信息公开", true);
        isShortCut.put("政务动态", true);
        isShortCut.put("通知公告", true);
        isShortCut.put("领导成员", true);
        isShortCut.put("规范性文件", false);
        isShortCut.put("财政资金", false);
        isShortCut.put("发展规划", false);
        isShortCut.put("政府工作报告", false);
        isShortCut.put("名单名录", false);
        isShortCut.put("直属部门", false);
        isShortCut.put("驻区单位", false);
        isShortCut.put("办事处", false);
        isShortCut.put("学校", false);
        isShortCut.put("医院", false);
        isShortCut.put("药店", false);
        isShortCut.put("诊所", false);
        isShortCut.put("社康中心", false);
        isShortCut.put("WIFI热点", false);
        isShortCut.put("办事服务", false);
        isShortCut.put("办事指南", false);
        isShortCut.put("个人办事", false);
        isShortCut.put("企业办事", false);
        isShortCut.put("部门服务", false);
        isShortCut.put("镇街服务", false);
        isShortCut.put("在线服务", false);
        isShortCut.put("挂号预约", false);
        isShortCut.put("婚姻登记预约", false);
        isShortCut.put("社保查询", false);
        isShortCut.put("公积金查询", false);
        isShortCut.put("地铁查询", false);
        isShortCut.put("信息自主申报", false);
        isShortCut.put("服务地图", false);
        isShortCut.put("互动交流", false);
        isShortCut.put("网上民声", false);
        isShortCut.put("微调查", false);
        isShortCut.put("乐在坪山", false);
        isShortCut.put("吃在坪山", false);
        isShortCut.put("游在坪山", false);

        isLink.put("了解坪山", false);
        isLink.put("坪山概况", false);
        isLink.put("文化坪山", false);
        isLink.put("产业坪山", false);
        isLink.put("信息公开", false);
        isLink.put("政务动态", false);
        isLink.put("通知公告", false);
        isLink.put("领导成员", false);
        isLink.put("规范性文件", false);
        isLink.put("财政资金", false);
        isLink.put("发展规划", false);
        isLink.put("政府工作报告", false);
        isLink.put("名单名录", false);
        isLink.put("直属部门", false);
        isLink.put("驻区单位", false);
        isLink.put("办事处", false);
        isLink.put("学校", false);
        isLink.put("医院", false);
        isLink.put("药店", false);
        isLink.put("诊所", false);
        isLink.put("社康中心", false);
        isLink.put("WIFI热点", false);
        isLink.put("办事服务", false);
        isLink.put("办事指南", false);
        isLink.put("个人办事", false);
        isLink.put("企业办事", false);
        isLink.put("部门服务", false);
        isLink.put("镇街服务", false);
        isLink.put("在线服务", false);
        isLink.put("挂号预约", false);
        isLink.put("婚姻登记预约", false);
        isLink.put("社保查询", false);
        isLink.put("公积金查询", false);
        isLink.put("地铁查询", false);
        isLink.put("信息自主申报", false);
        isLink.put("服务地图", false);
        isLink.put("互动交流", false);
        isLink.put("网上民声", false);
        isLink.put("微调查", false);
        isLink.put("乐在坪山", false);
        isLink.put("吃在坪山", false);
        isLink.put("游在坪山", false);

        //ICO
        ico.put("了解坪山", R.drawable.grid);
        ico.put("坪山概况", R.drawable.grid);
        ico.put("文化坪山", R.drawable.grid);
        ico.put("产业坪山", R.drawable.grid);
        ico.put("信息公开", R.drawable.grid);
        ico.put("政务动态", R.drawable.grid);
        ico.put("通知公告", R.drawable.grid);
        ico.put("领导成员", R.drawable.grid);
        ico.put("规范性文件", R.drawable.grid);
        ico.put("财政资金", R.drawable.grid);
        ico.put("发展规划", R.drawable.grid);
        ico.put("政府工作报告", R.drawable.grid);
        ico.put("名单名录", R.drawable.grid);
        ico.put("直属部门", R.drawable.grid);
        ico.put("驻区单位", R.drawable.grid);
        ico.put("办事处", R.drawable.grid);
        ico.put("学校", R.drawable.grid);
        ico.put("医院", R.drawable.grid);
        ico.put("药店", R.drawable.grid);
        ico.put("诊所", R.drawable.grid);
        ico.put("社康中心", R.drawable.grid);
        ico.put("WIFI热点", R.drawable.grid);
        ico.put("办事服务", R.drawable.grid);
        ico.put("办事指南", R.drawable.grid);
        ico.put("个人办事", R.drawable.grid);
        ico.put("企业办事", R.drawable.grid);
        ico.put("部门服务", R.drawable.grid);
        ico.put("镇街服务", R.drawable.grid);
        ico.put("在线服务", R.drawable.grid);
        ico.put("挂号预约", R.drawable.grid);
        ico.put("婚姻登记预约", R.drawable.grid);
        ico.put("社保查询", R.drawable.grid);
        ico.put("公积金查询", R.drawable.grid);
        ico.put("地铁查询", R.drawable.grid);
        ico.put("信息自主申报", R.drawable.grid);
        ico.put("服务地图", R.drawable.grid);
        ico.put("互动交流", R.drawable.grid);
        ico.put("网上民声", R.drawable.grid);
        ico.put("微调查", R.drawable.grid);
        ico.put("乐在坪山", R.drawable.grid);
        ico.put("吃在坪山", R.drawable.grid);
        ico.put("游在坪山", R.drawable.grid);
        /*urlList
            .put("政务动态", "http://www.psxq.gov.cn/app/opendata/getData/63764?pageSize=10&pageNo=");
        urlList
            .put("通知公告", "http://www.psxq.gov.cn/app/opendata/getData/63765?pageSize=10&pageNo=");
        urlList.put("领导成员", "http://203.91.35.115:60085/pingshan/GetRecords?menuId=9");
        urlList.put("政府部门", "http://www.psxq.gov.cn/main/xxgk/gbmxxgk/");
        urlList
            .put("发展规划", "http://www.psxq.gov.cn/app/opendata/getData/63737?pageSize=10&pageNo=");
        urlList
            .put("专项规划", "http://www.psxq.gov.cn/app/opendata/getData/64165?pageSize=10&pageNo=");
        urlList.put("年度工作计划及总结",
            "http://www.psxq.gov.cn/app/opendata/getData/63738?pageSize=10&pageNo=");
        urlList
            .put("任前公示", "http://www.psxq.gov.cn/app/opendata/getData/63750?pageSize=10&pageNo=");
        urlList
            .put("人事任免", "http://www.psxq.gov.cn/app/opendata/getData/63751?pageSize=10&pageNo=");
        urlList.put("干部选拔交流",
            "http://www.psxq.gov.cn/app/opendata/getData/63753?pageSize=10&pageNo=");
        urlList.put("公务员招考",
            "http://www.psxq.gov.cn/app/opendata/getData/64169?pageSize=10&pageNo=");
        urlList.put("事业单位招聘",
            "http://www.psxq.gov.cn/app/opendata/getData/67601?pageSize=10&pageNo=");
        urlList
            .put("统计月报", "http://www.psxq.gov.cn/app/opendata/getData/63744?pageSize=10&pageNo=");
        urlList
            .put("统计分析", "http://www.psxq.gov.cn/app/opendata/getData/63746?pageSize=10&pageNo=");
        urlList
            .put("统计公报", "http://www.psxq.gov.cn/app/opendata/getData/63743?pageSize=10&pageNo=");
        urlList
            .put("政策法规", "http://www.psxq.gov.cn/app/opendata/getData/63736?pageSize=10&pageNo=");
        urlList
            .put("坪山概况", "http://www.psxq.gov.cn/app/opendata/getData/65075?pageSize=10&pageNo=");
        urlList.put("办事结果查询", "http://fwdt.psxq.gov.cn/jggsTest.jsp");
        urlList.put("政府信箱", "http://www.psxq.gov.cn/m/zmhd/zfxx/index.html");
        urlList.put("咨询投诉", "http://www.psxq.gov.cn/app/mailbox/toAdd");
        urlList
            .put(
                "腾讯微博",
                "http://e.t.qq.com/pingshanfabu?ADUIN=2535621029&ADSESSION=1353286790&ADTAG=CLIENT.QQ.4819_ADClick_0.0&ADPUBNO=26058");
        urlList.put("新浪微博",
            "http://weibo.com/p/1001061922783457/home?from=page_100106&mod=TAB#place");
        urlList.put("幼儿园", "http://www.psxq.gov.cn/app/opendata/getData/65386?pageSize=10&pageNo=");
        urlList.put("小学", "http://www.psxq.gov.cn/app/opendata/getData/65392?pageSize=10&pageNo=");
        urlList.put("中学", "http://www.psxq.gov.cn/app/opendata/getData/65398?pageSize=10&pageNo=");
        urlList.put("医院", "http://www.psxq.gov.cn/app/opendata/getData/65441?pageSize=10&pageNo=");
        urlList.put("药店", "http://www.psxq.gov.cn/app/opendata/getData/65446?pageSize=10&pageNo=");
        urlList.put("诊所", "http://www.psxq.gov.cn/app/opendata/getData/65443?pageSize=10&pageNo=");
        urlList
            .put("社康中心", "http://www.psxq.gov.cn/app/opendata/getData/65442?pageSize=10&pageNo=");
        urlList
            .put("图片新闻", "http://www.psxq.gov.cn/app/opendata/getData/65170?pageSize=10&pageNo=");
        urlList.put("婚姻登记预约", "http://210.76.66.108/hyyy/");
        urlList.put("挂号预约", "http://sz.91160.com/search/index/pid-2/cid-5/aid-3366.html");
        urlList.put("电子地图", "http://www.psxq.gov.cn/main/service/map/index.shtml");
        urlList.put("常见问题", "http://www.psxq.gov.cn/m/cjwt/index.shtml");
        urlList.put("办事指南", "http://www.psxq.gov.cn/wexin/menu/bszn/index.shtml");
        urlList.put("社保查询", "http://www.psxq.gov.cn/weixin/yanyun/social.html");
        urlList.put("公积金查询", "http://www.psxq.gov.cn/weixin/yanyun/fund.html");
        isShortCut.put("政务公开", false);
        isShortCut.put("政务动态", true);
        isShortCut.put("通知公告", true);
        isShortCut.put("领导成员", false);
        isShortCut.put("政府部门", false);
        isShortCut.put("规划计划", false);
        isShortCut.put("发展规划", false);
        isShortCut.put("专项规划", false);
        isShortCut.put("年度工作计划及总结", false);
        isShortCut.put("人事信息", false);
        isShortCut.put("任前公示", false);
        isShortCut.put("人事任免", false);
        isShortCut.put("干部选拔交流", false);
        isShortCut.put("公务员招考", true);
        isShortCut.put("事业单位招聘", true);
        isShortCut.put("统计信息", false);
        isShortCut.put("统计月报", false);
        isShortCut.put("统计分析", false);
        isShortCut.put("统计公报", false);
        isShortCut.put("政策法规", false);
        isShortCut.put("坪山概况", false);
        isShortCut.put("在线服务", false);
        isShortCut.put("办事结果查询", true);
        isShortCut.put("公众参与", false);
        isShortCut.put("政府信箱", true);
        isShortCut.put("咨询投诉", true);
        isShortCut.put("腾讯微博", false);
        isShortCut.put("新浪微博", false);
        isShortCut.put("学校名录", false);
        isShortCut.put("幼儿园", true);
        isShortCut.put("小学", true);
        isShortCut.put("中学", true);
        isShortCut.put("卫生服务机构名录", false);
        isShortCut.put("医院", true);
        isShortCut.put("药店", true);
        isShortCut.put("诊所", true);
        isShortCut.put("社康中心", true);
        isShortCut.put("办事指南", true);
        isShortCut.put("图片新闻", false);
        isShortCut.put("婚姻登记预约", false);
        isShortCut.put("挂号预约", false);
        isShortCut.put("电子地图", false);
        isShortCut.put("常见问题", false);
        isShortCut.put("社保查询", false);
        isShortCut.put("公积金查询", false);*/
    }

}
