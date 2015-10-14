package cn.gov.psxq.fragment;

import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;
import cn.gov.psxq.common.HtmlRegexpUtils;
import cn.gov.psxq.common.StringUtils;
import cn.gov.psxq.common.UIHelper;
import cn.gov.psxq.ui.BackHandledFragment;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * home2
 * @author andye
 *
 */
public class LingDaoFragment extends BackHandledFragment implements OnClickListener {
    private static final String TAG = "LingDaoFragment";
    LayoutInflater              inflater;
    ActionBar                   mActionBar;
    private FinalHttp           finalHttp;

    private void initActionBar(LayoutInflater inflater, String titleString) {
        mActionBar = LingDaoFragment.this.getActivity().getActionBar();
        //mActionBar.setHomeButtonEnabled(true);
        //mActionBar.setDisplayHomeAsUpEnabled(true);
        //mActionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        View viewTitleBar = inflater.inflate(R.layout.action_bar_menu, null);
        TextView title = (TextView) viewTitleBar.findViewById(R.id.action_bar_title);
        title.setText(titleString);
        ImageView leftImageView = (ImageView) viewTitleBar.findViewById(R.id.left_btn);
        leftImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                IndexFragment indexFragment = new IndexFragment();
                FragmentManager fragmentManager = LingDaoFragment.this.getActivity()
                    .getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
                fragmentTransaction.commit();
            }
        });
        ImageView rightImageView = (ImageView) viewTitleBar.findViewById(R.id.right_btn);
        rightImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

            }
        });
        mActionBar.setCustomView(viewTitleBar, lp);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        this.getActivity().getActionBar().setCustomView(viewTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = (View) inflater.inflate(R.layout.lingdao_list, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.LingdaoListView);
        finalHttp = new FinalHttp();
        finalHttp.get(AppData.urlList.get("领导成员"), new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String gsonString) {
                String tmpString = StringUtils.getSubString(gsonString, "{\"dataList\":",
                    ",\"pageNo\":null,");
                List<Map<String, Object>> gsonList = AppData.gsonBuilder.create().fromJson(
                    tmpString, new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
                listView.setAdapter(new ListViewAdapter(gsonList));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map<String, Object> obj = null;
                        obj = (Map<String, Object>) view.getTag();
                        if (obj == null)
                            return;
                        // 跳转到博客详情
                        String lingdaoName = (String) obj.get("title");
                        String lingdaoZhiwu = (String) obj.get("x_headship");
                        String lingdaoWebSrc = (String) obj.get("description");
                        String lingdaoImg = (String) obj.get("x_imgpath");

                        /*UIHelper.showLingdaoDetail(LingDaoFragment.this.getActivity(), lingdaoName,
                            lingdaoZhiwu, lingdaoImg, lingdaoWebSrc);*/
                        UIHelper.showWebDetail(LingDaoFragment.this.getActivity(),
                            "http://" + (String) obj.get("link"), "领导成员", "领导成员");
                    }
                });
            }
        });

        initActionBar(inflater, "领导成员");
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ListItemView { //自定义控件集合  
        public TextView  lingdaoName;
        public TextView  lingdaoZhiwu;
        public TextView  lingdaoFengong;
        public ImageView lingdaoImage;
    }

    class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<Map<String, Object>> list) {
            itemViews = new View[list.size()];

            for (int i = 0; i < list.size(); ++i) {
                String nameStr = (String) list.get(i).get("title");
                String zhiWuStr = (String) list.get(i).get("x_headship");
                String fenGongStr = (String) list.get(i).get("x_duty");
                zhiWuStr = HtmlRegexpUtils.filterHtml(zhiWuStr).trim();
                fenGongStr = HtmlRegexpUtils.filterHtml(fenGongStr).trim();
                zhiWuStr = zhiWuStr.replaceAll(" ", "").replaceAll("　", "").replaceAll("\r\n", "");
                fenGongStr = fenGongStr.replaceAll(" ", "").replaceAll("　", "")
                    .replaceAll("\r\n", "");
                //String zhiWuStr = "";
                //String fenGongStr = "";
                String imgUrl = (String) list.get(i).get("link_picture");
                imgUrl = "http://" + imgUrl;
                if (fenGongStr.length() > 30)
                    fenGongStr = fenGongStr.substring(0, 30);
                itemViews[i] = makeItemView(nameStr, zhiWuStr, fenGongStr, imgUrl, list.get(i));
            }
        }

        public int getCount() {
            return itemViews.length;
        }

        public View getItem(int position) {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }

        private View makeItemView(String nameStr, String zhiWuStr, String fenGongStr,
                                  String imgUrl, Map<String, Object> obj) {

            // 使用View的对象itemView与R.layout.item关联  
            View itemView = inflater.inflate(R.layout.lingdao_listitem, null);

            // 通过findViewById()方法实例R.layout.item内各组件  
            TextView name = (TextView) itemView.findViewById(R.id.lingdaoName);
            name.setText(nameStr);
            TextView zhiWu = (TextView) itemView.findViewById(R.id.lingdaoZhiwu);
            zhiWu.setText(zhiWuStr);
            //            TextView fenGong = (TextView) itemView.findViewById(R.id.lingdaoFengong);
            //            fenGong.setText(fenGongStr);
            ImageView image = (ImageView) itemView.findViewById(R.id.lingdaoImage);
            LayoutParams para;
            para = image.getLayoutParams();
            para.height = 200;
            para.width = 150;
            image.setLayoutParams(para);
            ImageLoader.getInstance().displayImage(imgUrl, image);
            itemView.setTag(obj);
            return itemView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return itemViews[position];
        }
    }

    @Override
    protected boolean onBackPressed() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = LingDaoFragment.this.getActivity()
            .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.commit();
        return true;
    }
}
