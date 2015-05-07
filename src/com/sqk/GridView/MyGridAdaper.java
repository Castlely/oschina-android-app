package com.sqk.GridView;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.gov.psxq.AppData;
import cn.gov.psxq.R;
import cn.gov.psxq.common.UIHelper;
import cn.gov.psxq.fragment.GridMainFragment;

import com.google.common.collect.Lists;

public class MyGridAdaper extends BaseAdapter {
    private List<Grid_Item> Grid_Items;
    private LayoutInflater  inflater;
    private FragmentManager fManager;
    private List<Bundle>    bundles;
    private Context         context;

    public MyGridAdaper(Context context, List<Grid_Item> menus, FragmentManager fManager) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.Grid_Items = menus;
        this.fManager = fManager;
        bundles = Lists.newArrayList();
        for (Grid_Item gi : menus) {
            Bundle args = new Bundle();
            args.putString("catalogName", gi.Item_title);
            args.putBoolean("web", AppData.isLink.get(gi.Item_title));
            bundles.add(args);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Grid_Items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Grid_Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int width = parent.getWidth() / 3;
        int height = parent.getHeight() / 910 * 500 / 2;
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width, (int) (width * 1.2));
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Grid_Item item = Grid_Items.get(position);
        holder.image.setImageResource(item.imageID);
        holder.title.setText(item.Item_title);
        convertView.setLayoutParams(lp);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Boolean) bundles.get(position).get("web")) {
                    GridMainFragment gridMainFragment = new GridMainFragment();
                    gridMainFragment.setArguments(bundles.get(position));
                    FragmentManager fragmentManager = fManager;
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_linearlayout, gridMainFragment);
                    //fragmentTransaction.addToBackStack(bundles.get(position).getString("catalogName"));
                    fragmentTransaction.commit();
                } else {
                    UIHelper.showWebDetail(context,
                        AppData.urlList.get(bundles.get(position).get("catalogName")));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView  title;

    }
}
