package cn.gov.psxq.bean;

import java.io.IOException;
import java.util.List;

import cn.gov.psxq.AppData;
import cn.gov.psxq.AppException;

import com.google.gson.Gson;

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
        Gson gson = AppData.gsonBuilder.create();
        informationList = gson.fromJson(source, InformationList.class);
        return informationList;
    }

}
