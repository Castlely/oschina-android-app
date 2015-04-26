package net.oschina.app.adapter;

import java.util.List;

import net.oschina.app.bean.Information;
import net.oschina.app.common.BitmapManager;
import net.oschina.app.common.HtmlRegexpUtils;
import net.oschina.app.common.StringUtils;
import net.oschina.designapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Module_Form_0的数据展示
 * @author gengchen.ggc
 * @version $Id: ListViewInformationAdapter.java, v 0.1 2015年4月1日 下午9:02:56 gengchen.ggc Exp $
 */
public class ListViewInformationAdapter extends BaseAdapter {
    private Context           context;         //运行上下文
    private List<Information> listItems;       //数据集合
    private LayoutInflater    listContainer;   //视图容器
    private int               itemViewResource; //自定义项视图源 
    private BitmapManager     bitmapManager;

    public static class ListItemView { //自定义控件集合  
        public TextView  title;
        public TextView  description;
        public TextView  lastUpdate;
        public ImageView img;
    }

    /**
     * 实例化Adapter
     * @param context
     * @param data
     * @param resource
     */
    public ListViewInformationAdapter(Context context, List<Information> data, int resource) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context); //创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
        bitmapManager = new BitmapManager();
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("method", "getView");

        //自定义视图
        ListItemView listItemView = null;

        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(this.itemViewResource, null);

            listItemView = new ListItemView();
            //获取控件对象
            listItemView.img = (ImageView) convertView
                .findViewById(R.id.information_listitem_image);
            listItemView.title = (TextView) convertView
                .findViewById(R.id.information_listitem_title);
            listItemView.description = (TextView) convertView
                .findViewById(R.id.information_listitem_description);
            listItemView.lastUpdate = (TextView) convertView
                .findViewById(R.id.information_listitem_date);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        //设置文字和图片
        Information information = listItems.get(position);
        String titleString = HtmlRegexpUtils.filterHtml(information.getTitle());
        titleString = titleString.replaceAll(" ", "");
        titleString = titleString.trim();
        listItemView.title.setText(titleString);
        listItemView.title.setTag(information);//设置隐藏参数(实体类)
        if (information.getDescription().indexOf("/uploadfiles/") != -1) {
            String img = information.getDescription().substring(
                information.getDescription().indexOf("/uploadfiles/"));
            img = img.substring(0, img.indexOf("\">"));
            img = "http://www.psxq.gov.cn" + img;
            bitmapManager.loadBitmap(img, listItemView.img);
        } else {
            listItemView.img.setVisibility(View.GONE);
        }
        String dateString = information.getLast_update();
        dateString = dateString.substring(0, 19);
        listItemView.lastUpdate.setText(StringUtils.friendly_time(dateString));
        String descriptionString = HtmlRegexpUtils.filterHtml(information.getDescription());
        descriptionString = descriptionString.replaceAll(" ", "");
        descriptionString = descriptionString.trim();
        listItemView.description.setText(descriptionString);
        convertView.setId(information.getId());
        return convertView;
    }
}