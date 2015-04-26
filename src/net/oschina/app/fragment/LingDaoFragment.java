package net.oschina.app.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.oschina.app.AppData;
import net.oschina.app.common.BitmapManager;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.ui.BackHandledFragment;
import net.oschina.designapp.R;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.google.gson.reflect.TypeToken;

/**
 * home2
 * @author andye
 *
 */
public class LingDaoFragment extends BackHandledFragment implements OnClickListener {
    private static final String TAG = "LingDaoFragment";
    LayoutInflater              inflater;
    BitmapManager               bitmapManager;
    ActionBar                   mActionBar;

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
                // TODO Auto-generated method stub

                IndexFragment indexFragment = new IndexFragment();
                FragmentManager fragmentManager = LingDaoFragment.this.getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
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
        bitmapManager = new BitmapManager();
        View view = (View) inflater.inflate(R.layout.lingdao_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.LingdaoListView);
        InputStream in = getResources().openRawResource(R.raw.lingdao);
        String gsonString = null;
        try {
            gsonString = StringUtils.getStrFromInputSteam(in);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        List<Map<String, Object>> gsonList = AppData.gsonBuilder.create().fromJson(gsonString,
            new TypeToken<List<Map<String, Object>>>() {
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

                UIHelper.showLingdaoDetail(LingDaoFragment.this.getActivity(), lingdaoName,
                    lingdaoZhiwu, lingdaoImg, lingdaoWebSrc);
            }
        });
        initActionBar(inflater, "领导成员");
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<Map<String, Object>> list) {
            itemViews = new View[list.size()];

            for (int i = 0; i < itemViews.length; ++i) {
                String nameStr = (String) list.get(i).get("title");
                String zhiWuStr = (String) list.get(i).get("x_headship");
                String fenGongStr = "分工：" + (String) list.get(i).get("x_duty");
                String imgUrl = (String) list.get(i).get("x_imgpath");
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
            TextView fenGong = (TextView) itemView.findViewById(R.id.lingdaoFengong);
            fenGong.setText(fenGongStr);
            ImageView image = (ImageView) itemView.findViewById(R.id.lingdaoImage);
            LayoutParams para;
            para = image.getLayoutParams();
            para.height = 200;
            para.width = 150;
            image.setLayoutParams(para);
            bitmapManager.loadBitmap(imgUrl, image);
            itemView.setTag(obj);
            return itemView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
    }

    @Override
    protected boolean onBackPressed() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = LingDaoFragment.this.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.commit();
        return true;
    }
}