package com.example.xposedtest.bean;

import java.util.List;

public class FastLoadBean {

    /**
     * func : downloadPageDataForFastLoad
     * params : {"itemList":[{"item_show_type":"0","url":"http://mp.weixin.qq.com/s?__biz=MzAxNTE2MjgyNw==&mid=2650640029&idx=1&sn=96713f7cd68d9092cb7861054e637feb&chksm=83816239b4f6eb2f18342782877feba2fd3f42a274eb477225a037a79184265a7cd7c796b842&scene=4&subscene=126#wechat_redirect"}]}
     * __msg_type : call
     * __callback_id : 1021
     */

    private String func;
    private ParamsBean params;
    private String __msg_type;
    private String __callback_id;

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String get__msg_type() {
        return __msg_type;
    }

    public void set__msg_type(String __msg_type) {
        this.__msg_type = __msg_type;
    }

    public String get__callback_id() {
        return __callback_id;
    }

    public void set__callback_id(String __callback_id) {
        this.__callback_id = __callback_id;
    }

    public static class ParamsBean {
        private List<ItemListBean> itemList;

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListBean> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListBean {
            /**
             * item_show_type : 0
             * url : http://mp.weixin.qq.com/s?__biz=MzAxNTE2MjgyNw==&mid=2650640029&idx=1&sn=96713f7cd68d9092cb7861054e637feb&chksm=83816239b4f6eb2f18342782877feba2fd3f42a274eb477225a037a79184265a7cd7c796b842&scene=4&subscene=126#wechat_redirect
             */

            private String item_show_type;
            private String url;

            public String getItem_show_type() {
                return item_show_type;
            }

            public void setItem_show_type(String item_show_type) {
                this.item_show_type = item_show_type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
