package net.oschina.app.fragment;

import java.util.List;
import java.util.Map;

import net.oschina.app.AppData;
import net.oschina.app.common.BitmapUtil;
import net.oschina.app.fragment.DingYueFragment.MyAdapter.ViewHolder;
import net.oschina.app.ui.BackHandledFragment;
import net.oschina.designapp.R;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DingYueFragment extends BackHandledFragment {
    ActionBar mActionBar;

    private void initActionBar(LayoutInflater inflater, String titleString) {
        mActionBar = DingYueFragment.this.getActivity().getActionBar();
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
                FragmentManager fragmentManager = DingYueFragment.this.getFragmentManager();
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
        View v = inflater.inflate(R.layout.dingyue_listview, null);
        final ListView listView = (ListView) v.findViewById(R.id.dingyue_list);
        // 获取Resources对象  

        List<Map<String, Object>> mData = Lists.newArrayList();

        
        Map<String, Object> menusMap = AppData.getMenu(getResources());
        for(String key:menusMap.keySet())
        {
            Map<String, Object> item = Maps.newHashMap();
            item.put("img", AppData.ico.get(key));
            item.put("title", key);
            mData.add(item);
        }
        
        /*if (!StringUtils.isEmpty(AppData.get("isShortCut", DingYueFragment.this.getActivity()))) {
            AppData.set("isShortCut", AppData.gsonBuilder.create().toJson(AppData.isShortCut),
                DingYueFragment.this.getActivity());
        } else {
            AppData.isShortCut = AppData.gsonBuilder.create().fromJson(
                AppData.get("isShortCut", DingYueFragment.this.getActivity()),
                new TypeToken<Map<String, Boolean>>() {
                }.getType());
        }*/
        /*Map<String, Object> dataMap = (Map<String, Object>) AppData.getMenu(
            DingYueFragment.this.getResources()).get(getResources());
        mData.add(dataMap);*/
        final MyAdapter adapter = new MyAdapter(this.getActivity(), mData);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder vHollder = (ViewHolder) view.getTag();
                Map<String, Object> menusMap = AppData.getMenu(getResources());

                if (!menusMap.containsKey(vHollder.title.getText().toString())
                    || vHollder.title.getText().toString().equals("服务地图")) {
                    //在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。  
                    vHollder.cBox.toggle();
                    AppData.isShortCut.put(vHollder.title.getText().toString(),
                        vHollder.cBox.isChecked());
                    AppData.set("isShortCut",
                        AppData.gsonBuilder.create().toJson(AppData.isShortCut),
                        DingYueFragment.this.getActivity());
                } else {
                    //更新目录
                    adapter.clear();
                    Map<String, Object> dataMap = (Map<String, Object>) AppData.getMenu(
                        DingYueFragment.this.getResources()).get(
                        vHollder.title.getText().toString());
                    for (String key : dataMap.keySet()) {
                        Map<String, Object> item = Maps.newHashMap();
                        item = Maps.newHashMap();
                        item.put("img", AppData.ico.get(key));
                        item.put("title", key);
                        adapter.add(item);
                    }
                    listView.setAdapter(adapter);
                }

            }
        });
        initActionBar(inflater, "订阅");
        return v;
    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater            mInflater;
        private List<Map<String, Object>> mData;

        public void clear() {
            mData.clear();
        }

        public void add(Map<String, Object> data) {
            mData.add(data);
        }

        public MyAdapter(Context context, List<Map<String, Object>> mData) {
            this.mData = mData;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //convertView为null的时候初始化convertView。  
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.dingyue_listitem, null);
                holder.img = (ImageView) convertView.findViewById(R.id.dingyue_image);
                holder.title = (TextView) convertView.findViewById(R.id.dingyue_title);
                holder.cBox = (CheckBox) convertView.findViewById(R.id.dingyue_cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> menusMap = AppData.getMenu(getResources());
            if ((Integer) mData.get(position).get("img") != null) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    (Integer) mData.get(position).get("img"));
                holder.img.setBackground(BitmapUtil.bitmapToDrawable(BitmapUtil.getResizedBitmap(
                    bitmap, 150, 150)));
            }

            holder.title.setText(mData.get(position).get("title").toString());
            if (menusMap.containsKey(mData.get(position).get("title").toString())
                && !mData.get(position).get("title").toString().equals("服务地图"))//主目录
            {
                holder.cBox.setVisibility(View.GONE);
            } else {
                holder.cBox.setVisibility(View.VISIBLE);
            }
            holder.cBox.setChecked(AppData.isShortCut.get(holder.title.getText().toString()));
            /*
            if (position % 2 == 1) {
                convertView.setBackgroundColor(R.color.even_color);
            }
            */
            return convertView;
        }

        public final class ViewHolder {
            public ImageView img;
            public TextView  title;
            public CheckBox  cBox;
        }
    }

    @Override
    protected boolean onBackPressed() {
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager fragmentManager = DingYueFragment.this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_linearlayout, indexFragment);
        fragmentTransaction.commit();
        return true;
    }
}
