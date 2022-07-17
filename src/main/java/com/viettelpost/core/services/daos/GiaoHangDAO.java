package com.viettelpost.core.services.daos;

import com.viettelpost.core.base.VtException;
import com.viettelpost.core.controller.forms.GomBillForm;
import com.viettelpost.core.controller.responses.GomBillResponse;
import com.viettelpost.core.services.domains.UserInfo;
import com.viettelpost.core.utils.BaseDAO;
import com.viettelpost.core.utils.LanguageUtils;
import com.viettelpost.core.utils.Utils;
import oracle.jdbc.internal.OracleTypes;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.*;

@Service
public class GiaoHangDAO extends BaseDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("coreFactory")
    SessionFactory sessionFactory;

    @Autowired
    LanguageUtils languageUtils;

    @Transactional
    public GomBillResponse gomBillTxxh(UserInfo info, GomBillForm gomBillForm) throws Exception {
        try {
            Object[] rsArr = getListResult(sessionFactory, "VTP.PKG_VTMAN_API_V2.CREATE_GROUP_BILL",
                    Arrays.asList(info.getUserId(), info.getPost(), gomBillForm.getIdPhancong(), gomBillForm.getMaPhieugui(), gomBillForm.getNoiDung()),
                    new int[]{OracleTypes.VARCHAR, OracleTypes.CURSOR});
            return getGomBillResponse(rsArr);
        } catch (Exception ex) {
            throwException(ex);
        }
        return null;
    }

    @Transactional
    public GomBillResponse xoaGomBillTxxh(UserInfo info, Long groupId, Long billId) throws Exception {
        try {
            Object[] rsArr = getListResult(sessionFactory, "VTP.PKG_VTMAN_API_V2.REMOVE_BILL",
                    Arrays.asList(info.getUserId(), info.getPost(), groupId, billId),
                    new int[]{OracleTypes.VARCHAR, OracleTypes.CURSOR});
            return getGomBillResponse(rsArr);
        } catch (Exception ex) {
            throwException(ex);
        }
        return null;
    }

    GomBillResponse getGomBillResponse(Object[] rsArr) throws Exception {
        String msg = (String) rsArr[0];
        if (msg == null || msg.equals("OK")) {
            ResultSet rs2 = (ResultSet) rsArr[1];
            while (rs2 != null && rs2.next()) {
                GomBillResponse response = new GomBillResponse();
                response.setMaPhieugui(rs2.getString("MA_PHIEUGUI"));
                response.setGhiChu(rs2.getString("GHI_CHU"));
                response.setThuHo(rs2.getLong("THU_HO"));
                return response;
            }
        } else {
            throw new VtException(msg);
        }
        return null;
    }

    @Transactional
    public void groupBillAssign(UserInfo info, String groupId, String phone, Long type, String content) throws Exception {
        Object[] rsArr = getListResult(sessionFactory, "VTP.PKG_VTMAN_API_V2.ASSIGN_GROUP_BILL",
                Arrays.asList(info.getUserId(), info.getPost(), groupId, phone, type, content),
                new int[]{OracleTypes.VARCHAR});
        String msg = (String) rsArr[0];
        if (!Utils.isNullOrEmpty(msg)) {
            throw new VtException(msg);
        }
    }
}
