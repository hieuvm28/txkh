package com.viettelpost.core.services.domains;

import com.viettelpost.core.utils.Utils;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class UserInfo implements Serializable {
    JSONObject infos;

    public UserInfo() {
    }

    public UserInfo(JSONObject infos) {
        this.infos = infos;
    }

    public JSONObject getInfos() {
        return infos;
    }

    public void setInfos(JSONObject infos) {
        this.infos = infos;
    }

    public boolean verify() {
        long exp = infos.containsKey("exp") ? infos.getAsNumber("exp").longValue() : 0;
        return exp == 0 || exp > System.currentTimeMillis();
    }

    public Long getUserId() {
        return infos.getAsNumber("userid").longValue();
    }

    public String getPost() {
        return infos.getAsString("ma_buucuc");
    }

    public String getOrg() {
        return infos.getAsString("don_vi");
    }

    public String getName() {
        return infos.getAsString("name");
    }

    public String getPhone() {
        return infos.getAsString("phone");
    }

    public String getMaChucDanh() {
        return infos.getAsString("ma_chucdanh");
    }

    public Integer getSource() {
        String sourceTmp = infos.getAsString("source");
        if (Utils.isNullOrEmpty(sourceTmp) || !StringUtils.isNumeric(sourceTmp)) {
            return -2;
        }
        return Integer.valueOf(sourceTmp);
    }

}
