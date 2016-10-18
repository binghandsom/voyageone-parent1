package com.voyageone.service.bean.cms.cn;

/**
 * 独立域名类目Bean
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
public class CnCategoryBean {
    private String Id; // 类目Id
    private String ParentId; // 父类目Id
    private String CategoryPath; // 类目的path
    private int DisplayOrder; // 类目的显示顺序
    private String Name; // 类目名称
    private String IsPublished; // 类目是否激活 0:否 1:是
    private String IsVisibleOnMenu; // 类目是否作为一个菜单项 0:否 1:是
    private String UrlKey; // 唯一（暂定用类目Id）
    private String HeaderTitle; // 类目的描述(画面上显示内容)
    private String SEO_Title;
    private String SEO_Keywords;
    private String SEO_Description;
    private String SEO_Canonical;
    private String IsSneakerheadOnly;
    private String HighlightedProductCode;
    private String IsEnableFilter;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getCategoryPath() {
        return CategoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        CategoryPath = categoryPath;
    }

    public int getDisplayOrder() {
        return DisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        DisplayOrder = displayOrder;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIsPublished() {
        return IsPublished;
    }

    public void setIsPublished(String isPublished) {
        IsPublished = isPublished;
    }

    public String getIsVisibleOnMenu() {
        return IsVisibleOnMenu;
    }

    public void setIsVisibleOnMenu(String isVisibleOnMenu) {
        IsVisibleOnMenu = isVisibleOnMenu;
    }

    public String getUrlKey() {
        return UrlKey;
    }

    public void setUrlKey(String urlKey) {
        UrlKey = urlKey;
    }

    public String getHeaderTitle() {
        return HeaderTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        HeaderTitle = headerTitle;
    }

    public String getSEO_Title() {
        return SEO_Title;
    }

    public void setSEO_Title(String SEO_Title) {
        this.SEO_Title = SEO_Title;
    }

    public String getSEO_Keywords() {
        return SEO_Keywords;
    }

    public void setSEO_Keywords(String SEO_Keywords) {
        this.SEO_Keywords = SEO_Keywords;
    }

    public String getSEO_Description() {
        return SEO_Description;
    }

    public void setSEO_Description(String SEO_Description) {
        this.SEO_Description = SEO_Description;
    }

    public String getSEO_Canonical() {
        return SEO_Canonical;
    }

    public void setSEO_Canonical(String SEO_Canonical) {
        this.SEO_Canonical = SEO_Canonical;
    }

    public String getIsSneakerheadOnly() {
        return IsSneakerheadOnly;
    }

    public void setIsSneakerheadOnly(String isSneakerheadOnly) {
        IsSneakerheadOnly = isSneakerheadOnly;
    }

    public String getHighlightedProductCode() {
        return HighlightedProductCode;
    }

    public void setHighlightedProductCode(String highlightedProductCode) {
        HighlightedProductCode = highlightedProductCode;
    }

    public String getIsEnableFilter() {
        return IsEnableFilter;
    }

    public void setIsEnableFilter(String isEnableFilter) {
        IsEnableFilter = isEnableFilter;
    }
}
