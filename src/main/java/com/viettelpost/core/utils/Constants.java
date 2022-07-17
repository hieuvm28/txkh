package com.viettelpost.core.utils;

/**
 * Created by Oto on 16/10/2018.
 */
public class Constants {
    public final static Long expired = 1000L * 60 * 60 * 24 * 30;
    public final static String PREFIX = "Bearer ";

    public enum TpKafka {
        DONG_BO_TON("DONG_BO_TON_001", "Tồn ca, xóa nhóm gom bill"),
        PHAN_CONG("PHAN_CONG_001", "Phân công mới"),
        CHUYEN_PHAN_CONG("CHUYEN_PHAN_CONG_001", "Chuyển phân công"),
        CHUYEN_PHAN_CONG_NHOM("CHUYEN_PHAN_CONG_002", "Chuyển phân công theo group bill"),
        PHIEU_GUI_NHAN("PHIEU_GUI_NHAN_001", "Lưu thông tin phiếu gửi nhận"),
        GACH_BAO_PHAT("GACH_BAO_PHAT_001", "Gạch báo phát");
        private String code;
        private String name;

        TpKafka(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum Status {
        E200(200, "Success"),
        E201(201, "Token invalid"),
        E202(202, "Header invalid"),
        E203(203, "Data invalid(validate input)"),
        E204(204, "Data invalid(db raise, business error)"),
        E205(205, "Application error");
        private int code;
        private String name;

        Status(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static final String DATE_DEFAULT_FORMAT = "dd/MM/yyyy";
    public static final String YEAR_DEFAULT_FORMAT = "yyyy";

}
