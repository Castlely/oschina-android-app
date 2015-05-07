package cn.gov.psxq.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;
import cn.gov.psxq.common.StringUtils;
import cn.gov.psxq.common.UIHelper;
import cn.gov.psxq.ui.BackHandledFragment;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;

public class BuMenFragment extends BackHandledFragment implements OnClickListener {
    private static final String              TAG     = "GaiKuangFragment";
    private static final int                 catalog = 10;
    private Map<String, Map<String, Object>> dataMap;
    ActionBar                                mActionBar;

    private void initActionBar(LayoutInflater inflater, String titleString) {
        mActionBar = BuMenFragment.this.getActivity().getActionBar();
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
                FragmentManager fragmentManager = BuMenFragment.this.getActivity()
                    .getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
                fragmentTransaction.addToBackStack("tag");
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
        //View v = inflater.inflate(R.layout.index_1, null);
        dataMap = Maps.newHashMap();
        final ListView listview = new ListView(this.getActivity());
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
            android.R.layout.simple_list_item_1);
        //init(v);
        InputStream in = getResources().openRawResource(R.raw.gaikuang);
        String gsonString = null;
        try {
            gsonString = StringUtils.getStrFromInputSteam(in);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        List<Map<String, Object>> gsonList = AppData.gsonBuilder.create().fromJson(gsonString,
            new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        for (Map<String, Object> map : gsonList) {
            String name = (String) map.get("name");
            dataMap.put(name, map);
            Double parentIdDouble = (Double) map.get("parentId");
            Long parentId = parentIdDouble.longValue();
            if (parentId == catalog) {
                adapter.add(name);
            }
        }
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Log.d("ID", String.valueOf(parent.getItemAtPosition(position)));
                Map<String, Object> map = dataMap.get(parent.getItemAtPosition(position));
                Boolean hasChild = (Boolean) map.get("hasChild");
                if (hasChild == true) {
                    //更新列表
                    Double idDouble = (Double) map.get("id");
                    adapter.clear();
                    Long id = idDouble.longValue();
                    for (String key : dataMap.keySet()) {
                        Map<String, Object> valueMap = dataMap.get(key);
                        Double parentIdDoubleTmp = (Double) valueMap.get("parentId");
                        Long parentIdTmp = parentIdDoubleTmp.longValue();
                        if (id == parentIdTmp) {
                            adapter.add((String) valueMap.get("name"));
                        }
                        listview.setAdapter(adapter);
                    }

                } else {
                    UIHelper.showWebDetail(BuMenFragment.this.getActivity(),
                        (String) map.get("iconName"));
                }

            }

        });
        initActionBar(inflater, "部门");
        return listview;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected boolean onBackPressed() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = BuMenFragment.this.getActivity()
            .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
        return true;
    }

}
