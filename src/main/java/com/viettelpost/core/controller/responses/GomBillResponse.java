package com.viettelpost.core.controller.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GomBillResponse implements Serializable {
    @JsonProperty("ma_phieugui")
    String maPhieugui;
    @JsonProperty("thu_ho")
    Long thuHo;
    @JsonProperty("ghi_chu")
    String ghiChu;

    public String getMaPhieugui() {
        return maPhieugui;
    }

    public void setMaPhieugui(String maPhieugui) {
        this.maPhieugui = maPhieugui;
    }

    public Long getThuHo() {
        return thuHo;
    }

    public void setThuHo(Long thuHo) {
        this.thuHo = thuHo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
