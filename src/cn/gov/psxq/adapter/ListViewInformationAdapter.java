package cn.gov.psxq.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.gov.psxq.R;
import cn.gov.psxq.bean.Information;
import cn.gov.psxq.common.HtmlRegexpUtils;
import cn.gov.psxq.common.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

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
        /*        if (information.getDescription().indexOf("/uploadfiles/") != -1) {
                    String img = information.getDescription().substring(
                        information.getDescription().indexOf("/uploadfiles/"));
                    img = img.substring(0, img.indexOf("\">"));
                    img = "http://www.psxq.gov.cn" + img;
                    bitmapManager.loadBitmap(img, listItemView.img);
                } else {
                    listItemView.img.setVisibility(View.GONE);
                }*/
        if (information.getIs_picture() != null
            && !StringUtils.isEmpty(information.getLink_picture())) {
            ImageLoader.getInstance().displayImage("http://" + information.getLink_picture(),
                listItemView.img);
            listItemView.img.setVisibility(View.VISIBLE);
        } else {
            if (StringUtils.isEmpty(information.getX_imgpath()))
                listItemView.img.setVisibility(View.GONE);
            else {
                ImageLoader.getInstance().displayImage("http://" + information.getX_imgpath(),
                    listItemView.img);
            }
        }
        String dateString = information.getLast_update();
        dateString = dateString.substring(0, 19);
        listItemView.lastUpdate.setText(StringUtils.friendly_time(dateString));
        if (!StringUtils.isEmpty(information.getDescription())) {
            String descriptionString = delHTMLTag(information.getDescription());
            descriptionString = descriptionString.replaceAll("　", "");
            descriptionString = descriptionString.replaceAll("\r\n", "");
            descriptionString = descriptionString.replaceAll("&nbsp;", "");
            descriptionString = descriptionString.trim();
            listItemView.description.setText(descriptionString);
        } else {
            listItemView.description.setText("");
        }
        convertView.setId(information.getId());
        return convertView;
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签 

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签 

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    }
}