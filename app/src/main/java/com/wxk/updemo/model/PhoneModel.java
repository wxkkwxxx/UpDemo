package com.wxk.updemo.model;

/**
 * Created by Administrator on 2017/4/2
 */

public class PhoneModel {

    public String resultcode;
    public String reason;
    public PhoneResultModel result;

    @Override
    public String toString() {
        return "PhoneModel{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public class PhoneResultModel{

        public String province;
        public String city;
        public String areacode;
        public String zip;

        @Override
        public String toString() {
            return "PhoneResultModel{" +
                    "province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", areacode='" + areacode + '\'' +
                    ", zip='" + zip + '\'' +
                    '}';
        }
    }
}
