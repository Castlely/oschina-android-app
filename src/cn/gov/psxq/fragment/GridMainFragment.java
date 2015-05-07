package cn.gov.psxq.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;

public class GridMainFragment extends MainFragment {
    Map<String, Object> data;
    String              catalogName;
    ActionBar           mActionBar;
    String              parentString = null;

    PagerAdapter getPagerAdapter() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        List<CharSequence> titles = new ArrayList<CharSequence>();

        for (String key : data.keySet()) {
            if (!AppData.isLink.get(key)) {
                Fragment fragment = addBundle(new InformationListFragment(), key, catalogName,
                    AppData.isLink.get(key));
                fragments.add(fragment);
                titles.add(key);
            } else {
                Fragment fragment = addBundle(new WebFragment(), key, catalogName,
                    AppData.isLink.get(key));
                fragments.add(fragment);
                titles.add(key);
            }
        }

        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).toString().equals(catalogName)) {
                super.setPageSelect(i);
            }
        }
        return new ActivePagerAdapter(getChildFragmentManager(), fragments, titles);
    }

    private void initActionBar(LayoutInflater inflater, String titleString) {
        mActionBar = GridMainFragment.this.getActivity().getActionBar();
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
                FragmentManager fragmentManager = GridMainFragment.this.getFragmentManager();
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catalogName = this.getArguments().getString("catalogName");
        //判断是否是二级目录

        Map<String, Object> menuMap = AppData.getMenu(getResources());
        for (String parentKey : menuMap.keySet()) {
            if (menuMap.get(parentKey) instanceof Double) {
                continue;
            }
            ;
            Map<String, Object> subMenuMap = (Map<String, Object>) menuMap.get(parentKey);
            if (subMenuMap.containsKey(catalogName)) {
                parentString = parentKey;
            }
        }
        if (parentString == null) {
            data = (Map<String, Object>) AppData.getMenu(this.getResources()).get(catalogName);
            initActionBar(this.getLayoutInflater(savedInstanceState), catalogName);
        } else {
            data = (Map<String, Object>) AppData.getMenu(this.getResources()).get(parentString);
            initActionBar(this.getLayoutInflater(savedInstanceState), parentString);
        }

    }

    @Override
    public void setProgressBarVisibility(int v) {
    }

    @Override
    protected boolean onBackPressed() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = GridMainFragment.this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.commit();
        return true;
    }

}
