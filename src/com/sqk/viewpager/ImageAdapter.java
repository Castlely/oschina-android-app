package com.sqk.viewpager;

import java.util.List;
import java.util.Map;

import net.oschina.app.AppData;
import net.oschina.app.bean.Information;
import net.oschina.app.common.BitmapManager;
import net.oschina.app.common.UIHelper;
import net.oschina.app.ui.MainActivity;
import net.oschina.designapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;

public class ImageAdapter extends BaseAdapter {
    private Context                  mContext;
    private LayoutInflater           mInflater;
    private Map<String, Information> bitmapMap;
    private List<String>             bitmapList;
    private BitmapManager            bitmapManager;

    public ImageAdapter(Context context, Map<String, Information> map) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bitmapMap = map;
        bitmapList = Lists.newArrayList(bitmapMap.keySet());
        bitmapManager = new BitmapManager();//初始化FinalBitmap模块
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE; //返回很大的值使得getView中的position不断增大来实现循环
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        textView = (TextView) ((MainActivity) mContext).findViewById(R.id.img_title);
        String titleString = bitmapMap.get(bitmapList.get(position % bitmapList.size())).getTitle();
        if (titleString.length() > 20)
            titleString = titleString.substring(0, 20);
        textView.setText(titleString);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.image_item, null);
            imageView = ((ImageView) convertView.findViewById(R.id.imgView));
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }

        bitmapManager.loadBitmap(bitmapList.get(position % bitmapList.size()), imageView);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showInformationDetail(
                    mContext,
                    AppData.gsonBuilder.create().toJson(
                        bitmapMap.get(bitmapList.get(position % bitmapList.size()))));
            }
        });
        return convertView;
    }
}
