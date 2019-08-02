package com.spark.newbitrade.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class BannerEntity {

    /**
     * picture : [{"language":"zh","url":"http://oss.bitaccept.newbtc.online/oss/1.jpg"},{"language":"en","url":"https://bitrade.oss-cn-hongkong.aliyuncs.com/logo/bitrade/app.png"}]
     * link : index
     * index : 0
     * sort : 0
     */

    private String link;
    private int index;
    private int sort;
    private List<PictureBean> picture;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }

    public static class PictureBean {
        /**
         * language : zh
         * url : http://oss.bitaccept.newbtc.online/oss/1.jpg
         */

        private String language;
        private String url;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
