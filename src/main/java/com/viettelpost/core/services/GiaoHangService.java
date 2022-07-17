package com.viettelpost.core.services;

import com.viettelpost.core.controller.forms.GomBillForm;
import com.viettelpost.core.controller.responses.GomBillResponse;
import com.viettelpost.core.services.domains.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiaoHangService {
    GomBillResponse gomBillTxxh(UserInfo info, GomBillForm gomBillForm) throws Exception;
}
