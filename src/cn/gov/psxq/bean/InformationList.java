package cn.gov.psxq.bean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.gov.psxq.AppData;
import cn.gov.psxq.AppException;

/**
 * 坪山返回的数据结构的List
 * @author gengchen.ggc
 * @version $Id: InformationList.java, v 0.1 2015年4月1日 下午6:24:13 gengchen.ggc Exp $
 */
public class InformationList extends Entity {
    private List<Information> dataList;

    private int               pageNo;

    private int               pageSize;

    /**
     * Getter method for property <tt>informationList</tt>.
     * 
     * @return property value of informationList
     */
    public List<Information> getDataList() {
        return dataList;
    }

    /**
     * Setter method for property <tt>informationList</tt>.
     * 
     * @param informationList value to be assigned to property informationList
     */
    public void setDataList(List<Information> dataList) {
        this.dataList = dataList;
    }

    /**
     * Getter method for property <tt>pageNo</tt>.
     * 
     * @return property value of pageNo
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * Setter method for property <tt>pageNo</tt>.
     * 
     * @param pageNo value to be assigned to property pageNo
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * Getter method for property <tt>pageSize</tt>.
     * 
     * @return property value of pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Setter method for property <tt>pageSize</tt>.
     * 
     * @param pageSize value to be assigned to property pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static InformationList parse(String source) throws IOException, AppException {
        InformationList informationList = new InformationList();
        if (source.charAt(0) == '[')
            source = source.substring(1, source.length() - 1);
        Gson gson = AppData.gsonBuilder.create();
        Map<String, Object> map = gson.fromJson(source, new TypeToken<Map<String, Object>>() {
        }.getType());
        if (map.containsKey("list")) {
            map.put("dataList", map.get("list"));
        }
        informationList = gson.fromJson(gson.toJson(map), InformationList.class);
        if (!map.containsKey("pageSize")) {
            informationList.setPageSize(informationList.getDataList().size());
        }
        return informationList;
    }

    public static InformationList parseBanShiZhiNan(String source) throws IOException,
                                                                   AppException {
        InformationList informationList = new InformationList();
        List<Information> informations = Lists.newArrayList();
        Gson gson = AppData.gsonBuilder.create();
        List<TmpInfo> tmpInfos = gson.fromJson(source,
            new TypeToken<List<InformationList.TmpInfo>>() {
            }.getType());
        for (TmpInfo tmpInfo : tmpInfos) {
            Information information = new Information();
            information.setMenu(true);
            information.setCreatetime("2015-10-12 10:00:00.0");
            information.setPubdate("2015-10-12 10:00:00.0");
            information.setLast_update("2015-10-12 10:00:00.0");
            information.setId(tmpInfo.getId());
            information.setLink_picture(tmpInfo.getImg());
            information.setIs_picture(1);
            information.setX_imgpath(tmpInfo.getImg());
            information.setId(tmpInfo.getId());
            information.setTitle(tmpInfo.getName());
            information.setLink("www.psxq.gov.cn/app/bsdt/getDataJson.jsp?id=" + tmpInfo.getId()
                                + "&pageSize=20&pageNo=1");
            informations.add(information);
        }
        informationList.setDataList(informations);
        informationList.setPageSize(100);
        informationList.setPageNo(1);
        return informationList;
    }

    class TmpInfo {
        private Integer id;
        private String  name;
        private String  img;

        /**
         * Getter method for property <tt>id</tt>.
         * 
         * @return property value of id
         */
        public Integer getId() {
            return id;
        }

        /**
         * Setter method for property <tt>id</tt>.
         * 
         * @param id value to be assigned to property id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * Getter method for property <tt>name</tt>.
         * 
         * @return property value of name
         */
        public String getName() {
            return name;
        }

        /**
         * Setter method for property <tt>name</tt>.
         * 
         * @param name value to be assigned to property name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Getter method for property <tt>img</tt>.
         * 
         * @return property value of img
         */
        public String getImg() {
            return img;
        }

        /**
         * Setter method for property <tt>img</tt>.
         * 
         * @param img value to be assigned to property img
         */
        public void setImg(String img) {
            this.img = img;
        }
    }

}
