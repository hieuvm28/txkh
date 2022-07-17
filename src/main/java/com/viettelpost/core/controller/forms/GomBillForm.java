package com.viettelpost.core.controller.forms;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GomBillForm implements Serializable {
    @JsonProperty("id_phancong")
    Long idPhancong;
    @JsonProperty("ma_phieugui")
    String maPhieugui;
    @JsonProperty("noi_dung")
    String noiDung;

    public Long getIdPhancong() {
        return idPhancong;
    }

    public void setIdPhancong(Long idPhancong) {
        this.idPhancong = idPhancong;
    }

    public String getMaPhieugui() {
        return maPhieugui;
    }

    public void setMaPhieugui(String maPhieugui) {
        this.maPhieugui = maPhieugui;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
