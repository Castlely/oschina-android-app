package net.oschina.app.bean;

/**
 * 坪山API访问得到的实体类
 * @author gengchen.ggc
 * @version $Id: Information.java, v 0.1 2015年4月1日 下午6:21:29 gengchen.ggc Exp $
 */
public class Information extends Entity {
    private String  createtime;

    private String  last_update;

    private String  title;

    private Integer module_form;

    private String  pubdate;

    private String  description;

    private String  link;

    private Integer type;

    private Integer is_picture;

    private String  link_picture;

    public void setId(Integer id) {
        super.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getModule_form() {
        return module_form;
    }

    public void setModule_form(Integer module_form) {
        this.module_form = module_form;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIs_picture() {
        return is_picture;
    }

    public void setIs_picture(Integer is_picture) {
        this.is_picture = is_picture;
    }

    public String getLink_picture() {
        return link_picture;
    }

    public void setLink_picture(String link_picture) {
        this.link_picture = link_picture;
    }

}
