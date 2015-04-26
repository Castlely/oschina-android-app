package net.oschina.app.bean;

/**
 * 坪山API访问得到的实体类
 * @author gengchen.ggc
 * @version $Id: Information.java, v 0.1 2015年4月1日 下午6:21:29 gengchen.ggc Exp $
 */
public class Information extends Entity {
    private String createtime;

    private String last_update;

    private String title;

    private int    module_form;

    private String pubdate;

    private String description;

    private String link;

    private int    type;

    public void setId(int id) {
        super.id = id;
    }

    /**
     * Getter method for property <tt>createtime</tt>.
     * 
     * @return property value of createtime
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * Setter method for property <tt>createtime</tt>.
     * 
     * @param createtime value to be assigned to property createtime
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * Getter method for property <tt>last_update</tt>.
     * 
     * @return property value of last_update
     */
    public String getLast_update() {
        return last_update;
    }

    /**
     * Setter method for property <tt>last_update</tt>.
     * 
     * @param last_update value to be assigned to property last_update
     */
    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    /**
     * Getter method for property <tt>title</tt>.
     * 
     * @return property value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method for property <tt>title</tt>.
     * 
     * @param title value to be assigned to property title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter method for property <tt>module_form</tt>.
     * 
     * @return property value of module_form
     */
    public int getModule_form() {
        return module_form;
    }

    /**
     * Setter method for property <tt>module_form</tt>.
     * 
     * @param module_form value to be assigned to property module_form
     */
    public void setModule_form(int module_form) {
        this.module_form = module_form;
    }

    /**
     * Getter method for property <tt>pubdate</tt>.
     * 
     * @return property value of pubdate
     */
    public String getPubdate() {
        return pubdate;
    }

    /**
     * Setter method for property <tt>pubdate</tt>.
     * 
     * @param pubdate value to be assigned to property pubdate
     */
    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    /**
     * Getter method for property <tt>description</tt>.
     * 
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for property <tt>description</tt>.
     * 
     * @param description value to be assigned to property description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method for property <tt>link</tt>.
     * 
     * @return property value of link
     */
    public String getLink() {
        return link;
    }

    /**
     * Setter method for property <tt>link</tt>.
     * 
     * @param link value to be assigned to property link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Getter method for property <tt>type</tt>.
     * 
     * @return property value of type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     * 
     * @param type value to be assigned to property type
     */
    public void setType(int type) {
        this.type = type;
    }

}
