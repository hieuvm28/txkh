package com.viettelpost.core.services.impl;

import com.viettelpost.core.controller.forms.GomBillForm;
import com.viettelpost.core.controller.responses.GomBillResponse;
import com.viettelpost.core.services.GiaoHangService;
import com.viettelpost.core.services.daos.GiaoHangDAO;
import com.viettelpost.core.services.domains.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiaoHangImpl implements GiaoHangService {

    @Autowired
    GiaoHangDAO giaoHangDAO;

    @Override
    public GomBillResponse gomBillTxxh(UserInfo info, GomBillForm gomBillForm) throws Exception {
        return giaoHangDAO.gomBillTxxh(info, gomBillForm);
    }
}
