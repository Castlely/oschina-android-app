package cn.gov.psxq.fragment;

import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.sqk.viewpager.widget.CircleFlowIndicator;
import org.sqk.viewpager.widget.ViewFlow;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;
import cn.gov.psxq.bean.Information;
import cn.gov.psxq.bean.InformationList;
import cn.gov.psxq.inteface.ActionBarProgressBarVisibility;
import cn.gov.psxq.ui.BackHandledFragment;
import cn.gov.psxq.widget.TabButton;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.sqk.viewpager.ImageAdapter;

public abstract class MainFragment extends BackHandledFragment implements
                                                              ActionBarProgressBarVisibility {
    private ViewPager   viewPager;
    private FinalHttp   fh;
    private ViewFlow    viewFlow;
    private int         pageSelect   = 0;
    private FrameLayout optional;
    View                view;
    String              parentString = null;
    ActionBar           mActionBar;

    public void setPageSelect(int pageSelect) {
        this.pageSelect = pageSelect;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_main_viewpager);
        viewPager.setAdapter(getPagerAdapter());
        viewPager.setCurrentItem(pageSelect, true);
        TabButton tabsButton = (TabButton) view.findViewById(R.id.fragment_main_tabsbutton);
        tabsButton.setViewPager(viewPager);
        tabsButton.pageSelect = pageSelect;
        optional = (FrameLayout) view.findViewById(R.id.optional);
        fh = new FinalHttp();
        setOptionalSlider();
        return view;
    }

    public void setOptionalSlider() {
        if (AppData.optionSet.contains(this.getArguments().getString("catalogName"))) {
            String url = "http://www.psxq.gov.cn/app/opendata/getData/68432?pageSize=30&pageNo=1";
            fh.get(url, new AjaxCallBack<String>() {

                @Override
                public void onSuccess(String resultString) {

                    Gson gson = AppData.gsonBuilder.create();
                    InformationList informationList = gson
                        .fromJson(resultString, InformationList.class);
                    Map<String, Information> imgMap = Maps.newHashMap();
                    int count = 0;
                    for (int n = 0; n < 30; n++) {
                        /*AppData.set("information_" + informationList.getDataList().get(n).getId(), gson
                            .toJson(informationList.getDataList().get(n)), IndexFragment.this
                            .getActivity().getApplication());
                        String img = informationList
                            .getDataList()
                            .get(n)
                            .getDescription()
                            .substring(
                                informationList.getDataList().get(n).getDescription()
                                    .indexOf("/uploadfiles/"));
                        img = img.substring(0, img.indexOf("\">"));
                        img = "http://www.psxq.gov.cn" + img;*/
                        //imgMap.put(img, informationList.getDataList().get(n));
                        if (informationList.getDataList().get(n).getIs_picture() != null
                            && informationList.getDataList().get(n).getIs_picture() == 1) {
                            imgMap.put("http://"
                                       + informationList.getDataList().get(n).getLink_picture(),
                                informationList.getDataList().get(n));
                            count++;
                            if (count == 3)
                                break;
                        }

                    }
                    viewFlow.setAdapter(new ImageAdapter(MainFragment.this.getActivity(), imgMap));
                }

            });
            viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
            viewFlow.setmSideBuffer(3); // 实际图片张数， 我的ImageAdapter实际图片张数为3
            CircleFlowIndicator indic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
            viewFlow.setFlowIndicator(indic);
            viewFlow.setTimeSpan(4500);
            viewFlow.setSelection(3 * 1000); // 设置初始位置
            viewFlow.startAutoFlowTimer(); // 启动自动播放
            optional.setVisibility(View.VISIBLE);
        } else {
            optional.setVisibility(View.GONE);
        }
    }

    abstract PagerAdapter getPagerAdapter();

    protected Fragment addBundle(Fragment fragment, String catlogName, String title,Boolean web) {
        Bundle bundle = new Bundle();
        bundle.putString("catalogName", catlogName);
        bundle.putString("title", title);
        bundle.putBoolean("web", web);
        fragment.setArguments(bundle);
        return fragment;
    }
}
