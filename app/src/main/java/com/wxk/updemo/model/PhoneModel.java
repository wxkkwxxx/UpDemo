package com.wxk.updemo.model;

/**
 * Created by Administrator on 2017/4/2
 */

public class PhoneModel {

    public String resultcode;
    public String reason;
    public PhoneResultModel result;

    public PhoneModel(String resultcode, String reason, PhoneResultModel result) {
        this.resultcode = resultcode;
        this.reason = reason;
        this.result = result;
    }

    @Override
    public String toString() {
        return "PhoneModel{" +
                "resultcode='" + resultcode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

    public static class PhoneResultModel{

        public String province;
        public String city;
        public String areacode;
        public String zip;

        public PhoneResultModel(String province, String city, String areacode, String zip) {
            this.province = province;
            this.city = city;
            this.areacode = areacode;
            this.zip = zip;
        }

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
